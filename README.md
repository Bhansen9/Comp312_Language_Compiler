Mission Statement:
The goal of the project was to develop an easy to use java language compiler that allows the user to execute java code with a core compiler concept. Building this we aimed to strengthen our process of full stack development, and allows experiment with scalable features such as a login auth concept. This project was something that was built from scratch with concepts that we were already familiar with such as different coding languages. We also used this opportunity to work as a dev team, Ben being on the frontend side and login auth with his previous projects and Victor working on the backend with his java experience. The testing procedure worked with testing how the app would run and what parts of the frontend and backend were not communicating. We did this by pushing both our products to the main and launching the app in a live review session to point out the errors in the code and what features were not working.


Work Divided up:
Victor: Model language interpreter/compiler, parsing, Lexer, Call Stack, Language interpreter fundamentals
Ben: Frontend UI, Mongo DB auth, Login/Sign Up fundamentals (password encryption ), Fetch API routes/JSON communication frontend to backend



Steps to run (Have Maven env installed to system)
-Backend (spring boot) - For compiler system
mvn spring-boot:run
-Backend (Node.js) - For auth system and login
Node server.js

Launch Frontend from the VS code local host tool



A full-stack web application that allows users to sign up, log in, and execute Java code directly in the browser.

Features
User authentication (signup, login)
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
