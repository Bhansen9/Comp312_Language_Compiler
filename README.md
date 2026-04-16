Steps to run (Have Maven env installed to system)
-Backend (spring boot) - For compiler system
mvn spring-boot:run
-Backend (Node.js) - For auth system and login
Node server.js

Launch Frontend from the VS code local host tool



A full-stack web application that allows users to sign up, log in, and execute Java code directly in the browser.

* Features
User authentication (signup, login, logout)
Secure password storage using hashing
Session-based authentication
Run Java code in real-time
Compile + runtime error handling
Dynamic frontend updates (no page refresh)


*Tech Stack
Frontend
HTML, CSS, JavaScript
Fetch API
DOM Manipulation
Backend (Authentication)
Node.js
Express.js
MongoDB (Mongoose)
express-session
bcrypt
Backend (Code Execution)
Java
Spring Boot
ProcessBuilder (for compiling + running code)
