package com.example.compiler.controller;

import com.example.compiler.model.CodeRequest;
import com.example.compiler.model.CodeResponse;
import com.example.compiler.service.CodeExecutionService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class CodeController {

    private final CodeExecutionService codeExecutionService;

    public CodeController(CodeExecutionService codeExecutionService) {
        this.codeExecutionService = codeExecutionService;
    }

    @PostMapping("/run")
    public CodeResponse runCode(@RequestBody CodeRequest request) {
        return codeExecutionService.executeJavaCode(request.getCode());
    }
}