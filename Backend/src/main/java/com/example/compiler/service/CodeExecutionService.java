package com.example.compiler.service;

import com.example.compiler.model.CodeResponse;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

@Service
public class CodeExecutionService {

    public CodeResponse executeJavaCode(String code) {
        Path tempDir = null;

        try {
            if (code == null || code.trim().isEmpty()) {
                return new CodeResponse(null, "Code cannot be empty.");
            }

            tempDir = Files.createTempDirectory("java-mvp-");
            Path javaFile = tempDir.resolve("Main.java");

            Files.writeString(javaFile, code);

            Process compileProcess = new ProcessBuilder("javac", javaFile.toString())
                    .directory(tempDir.toFile())
                    .redirectErrorStream(true)
                    .start();

            boolean compileFinished = compileProcess.waitFor(5, TimeUnit.SECONDS);
            if (!compileFinished) {
                compileProcess.destroyForcibly();
                return new CodeResponse(null, "Compilation timed out.");
            }

            String compileOutput = readProcessOutput(compileProcess);

            if (compileProcess.exitValue() != 0) {
                return new CodeResponse(null, compileOutput);
            }

            Process runProcess = new ProcessBuilder("java", "-cp", tempDir.toString(), "Main")
                    .directory(tempDir.toFile())
                    .redirectErrorStream(true)
                    .start();

            boolean runFinished = runProcess.waitFor(5, TimeUnit.SECONDS);
            if (!runFinished) {
                runProcess.destroyForcibly();
                return new CodeResponse(null, "Execution timed out.");
            }

            String runOutput = readProcessOutput(runProcess);

            if (runProcess.exitValue() != 0) {
                return new CodeResponse(null, runOutput);
            }

            return new CodeResponse(runOutput, null);

        } catch (Exception e) {
            return new CodeResponse(null, "Server error: " + e.getMessage());
        } finally {
            if (tempDir != null) {
                deleteDirectory(tempDir);
            }
        }
    }

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