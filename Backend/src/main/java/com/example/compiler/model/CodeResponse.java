
package com.example.compiler.model;

// Represents the response sent back to the frontend after code execution from backend
public class CodeResponse {
    //Stores the output of the executed code
    private String output;
    // Store any error messages
    private String error;
// Constructor for Spring to convert java object to JSON response
    public CodeResponse() {
    }
// Constructor with parameter for easier object creation if needed
    public CodeResponse(String output, String error) {
        this.output = output;
        this.error = error;
    }
// Getter for output to read the output value for frontend use
    public String getOutput() {
        return output;
    }
//setter for output for backend use
    public void setOutput(String output) {
        this.output = output;
    }
// Getter for error to read the error valuse for frontend use
    public String getError() {
        return error;
    }
// Setter for error for backend use
    public void setError(String error) {
        this.error = error;
    }
}