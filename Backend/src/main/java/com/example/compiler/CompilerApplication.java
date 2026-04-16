package com.example.compiler;
//Import spring boot classes need to start the app
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



// marks this as the main spring boot app, enable auto-configuration, scans for components like the controller and services
@SpringBootApplication
public class CompilerApplication {
// main method that starts the spring boot
    public static void main(String[] args) {
        SpringApplication.run(CompilerApplication.class, args);
    }
}