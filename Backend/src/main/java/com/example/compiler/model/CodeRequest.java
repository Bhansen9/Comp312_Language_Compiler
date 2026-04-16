
package com.example.compiler.model;
//Represents the data coming from the frontend
public class CodeRequest {
    //stores the code sent from the user
    private String code;
    // constructor for Spring to convert JSON to java object
    public CodeRequest() {
    }
// constructor wiht parameter for easier object creation if needed
    public CodeRequest(String code) {
        this.code = code;
    }
// Getter method to read the code value
    public String getCode() {
        return code;
    }
// setter method to set the code value this is used by spring boot to set the code value from JSO request
    public void setCode(String code) {
        this.code = code;
    }
}