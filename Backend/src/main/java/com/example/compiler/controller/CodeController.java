
// Defines the package for the controller
package com.example.compiler.controller;
//immport request/response and models
import com.example.compiler.model.CodeRequest;
import com.example.compiler.model.CodeResponse;

//import the service that actually executes the code
import com.example.compiler.service.CodeExecutionService;
// Srping annotations for REST controller and COR configuration
import org.springframework.web.bind.annotation.*;
// marks this class as a REST controller and handels HTTP request and returns JSON responses
@RestController

// Allows frontend to call to the backend from local development enviroment and * allows all origins without restrictions
@CrossOrigin(origins = "*")
public class CodeController {
    // Service layer that handles the logic of executing the code
    private final CodeExecutionService codeExecutionService;
    // a Constructor injection 
    public CodeController(CodeExecutionService codeExecutionService) {
        this.codeExecutionService = codeExecutionService;
    }
    // Defines a post endpoint at /run and is called from the frontend when user clicks the "Run" button
    @PostMapping("/run")
    public CodeResponse runCode(@RequestBody CodeRequest request) {

        // the request.getCode() retrives the code sent from the frontend
        //Calls the service to execute the java code and reuturn its output
        return codeExecutionService.executeJavaCode(request.getCode());
    }
}