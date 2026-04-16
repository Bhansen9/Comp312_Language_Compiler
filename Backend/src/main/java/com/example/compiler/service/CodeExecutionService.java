package com.example.compiler.service;
// Import the response model to send back the ouput and error messages to the frontend
import com.example.compiler.model.CodeResponse;
//Spring service
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;


// Tells Spring this class is a service component and will be managed by Spring's injected controller
@Service
public class CodeExecutionService {
    //Main method that compiles and runs java code
    public CodeResponse executeJavaCode(String code) {
        // Temp folder to safely store the users code
        Path tempDir = null;

        try {
            //Checks if the code is empty
            if (code == null || code.trim().isEmpty()) {
                return new CodeResponse(null, "Code cannot be empty.");
            }
            //Creates a temp directory and a java file to store the users code
            tempDir = Files.createTempDirectory("java-mvp-");
            Path javaFile = tempDir.resolve("Main.java");

            Files.writeString(javaFile, code);



            // this is the compile set up that runs the javac command to compile the java code
            Process compileProcess = new ProcessBuilder("javac", javaFile.toString())
                    .directory(tempDir.toFile())
                    .redirectErrorStream(true)
                    .start();
            // waits for the compile process to finish and check in 5 min to avoid infinte loops if it sirpases the time limit it kills it
            boolean compileFinished = compileProcess.waitFor(5, TimeUnit.SECONDS);
            if (!compileFinished) {
                compileProcess.destroyForcibly();
                return new CodeResponse(null, "Compilation timed out.");
            }
            // then read out to the user the output of the compile process, errors or sucess
            String compileOutput = readProcessOutput(compileProcess);
            // if compilation fails it will return an error message to the user
            if (compileProcess.exitValue() != 0) {
                return new CodeResponse(null, compileOutput);
            }


            //Run Set up
            // this is the run set up that runs the java command to execute the compiled code
            Process runProcess = new ProcessBuilder("java", "-cp", tempDir.toString(), "Main")
                    .directory(tempDir.toFile())
                    .redirectErrorStream(true)
                    .start();

            // waits for the run process to finish if it sirpases the time limit of 5 min it kills it
            boolean runFinished = runProcess.waitFor(5, TimeUnit.SECONDS);
            if (!runFinished) {
                runProcess.destroyForcibly();
                return new CodeResponse(null, "Execution timed out.");
            }
            // then reads out the oupt of the process
            String runOutput = readProcessOutput(runProcess);
            // if there is a runtime error it will let the user know
            if (runProcess.exitValue() != 0) {
                return new CodeResponse(null, runOutput);
            }
            // if everything is successful it will return the output
            return new CodeResponse(runOutput, null);

        } catch (Exception e) {

            // if there is unexpected sever error it will return the error message
            return new CodeResponse(null, "Server error: " + e.getMessage());
        } finally {
            if (tempDir != null) {
                deleteDirectory(tempDir);
            }
        }
    }




    // helper methods

    // Reads the output from javac and java and return it as a string to the user
    private String readProcessOutput(Process process) throws IOException {
        StringBuilder output = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append(System.lineSeparator());
            }
        }

        return output.toString();
    }



    // deletes the temp directory and all its contents after the code has been executed so there is no leftover files on the server
    private void deleteDirectory(Path dir) {
        try {
            Files.walk(dir)
                    .sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException ignored) {
                        }
                    });
        } catch (IOException ignored) {
        }
    }
}