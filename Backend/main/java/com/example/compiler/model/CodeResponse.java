package com.example.compiler.model;

public class CodeResponse {
    private String output;
    private String error;

    public CodeResponse() {
    }

    public CodeResponse(String output, String error) {
        this.output = output;
        this.error = error;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}