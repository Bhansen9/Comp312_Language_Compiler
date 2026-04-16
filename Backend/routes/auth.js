//Connects the comunication between the frontend and the backend for authentification, receiving fetch() data from front-end and sending it to the database
const express = require("express");
//Used this library to hash the password before storing in the database
const bcrypt = require("bcrypt");
//Import the User data from MongoDB databse
const User = require("../models/User");
//Creates a router to handle the routes for authentification
const router = express.Router();

// SIGNUP route to create a new user account
router.post("/signup", async (req, res) => {
  try {
    //Gets data from a request body
    let { username, email, password } = req.body;
    //Cleans up the input data by trimming the white spaces, lowercasing the email
    username = username?.trim();
    email = email?.trim().toLowerCase();
    password = password?.trim();
// Validates the input data, checking if all field are filled
    if (!username || !email || !password) {
      return res.status(400).json({
        message: "All fields are required."
      });
    }
// This enforces the password length
    if (password.length < 6) {
      return res.status(400).json({
        message: "Password must be at least 6 characters long."
      });
    }
// checks if a user already is in database with the same email or same username
    const existingUser = await User.findOne({
      $or: [{ email }, { username }]
    });

    if (existingUser) {
      return res.status(409).json({
        message: "A user with that email or username already exists."
      });
    }
// hashes the password using the bcrypt library before storing it in mongoDB
    const hashedPassword = await bcrypt.hash(password, 10);
// Creates a new user layout to be stored in the database
    const newUser = new User({
      username,
      email,
      password: hashedPassword
    });
// saves the user data
    await newUser.save();
// creates a session for the user to keep them logged in after they sign up
    req.session.user = {
      id: newUser._id,
      username: newUser.username,
      email: newUser.email
    };
// Lets user know that the account was created successfully
    return res.status(201).json({
      message: "Account created successfully.",
      user: req.session.user
    });
  } catch (error) {
    console.error("Signup error:", error);
    // If an error does occur during the signup process it lets the user know
    return res.status(500).json({
      message: "Server error during signup."
    });
  }
});

// LOGIN route to authenticate an existing user
router.post("/login", async (req, res) => {
  try {
    let { email, password } = req.body;
    // cleans up data by trimming the input
    email = email?.trim().toLowerCase();
    password = password?.trim();
// Makes sure all fields are filled
    if (!email || !password) {
      return res.status(400).json({
        message: "Email and password are required."
      });
    }
// Finds the user in the database via the email they provided
    const user = await User.findOne({ email });
// If the user can not be found in the data base it lets user know that the email was wrong or the password was wrong or there are just not in the database
    if (!user) {
      return res.status(401).json({
        message: "Invalid email or password."
      });
    }
// Compares the provided password with the hashed password stored in the mongoDB database using the bcrypt libary
    const passwordMatch = await bcrypt.compare(password, user.password);
// If passwords don't match it lets the user know that input was wrong
    if (!passwordMatch) {
      return res.status(401).json({
        message: "Invalid email or password."
      });
    }
// Creates a session for the user to keep them loggin in after they are login in
    req.session.user = {
      id: user._id,
      username: user.username,
      email: user.email
    };
// lets user know that they have successfully been loggin in
    return res.status(200).json({
      message: "Login successful.",
      user: req.session.user
    });
  } catch (error) {
    console.error("Login error:", error);
    // if an error does occur on the server side it lets the user know
    return res.status(500).json({
      message: "Server error during login."
    });
  }
});

// LOGOUT route to destroy the user session
router.post("/logout", (req, res) => {
  // Destroys the session to log the user out
  req.session.destroy((err) => {
    if (err) {
      console.error("Logout error:", err);
      // if an error does occur while loggin out it lets the user know
      return res.status(500).json({
        message: "Could not log out."
      });
    }
// Clear session cookie on the client slide
    res.clearCookie("connect.sid");
//Lets user know that they have successfully been logged out
    return res.status(200).json({
      message: "Logged out successfully."
    });
  });
});

// CHECK CURRENT SESSION
router.get("/me", (req, res) => {
  try {
    // if no session exists it lets the user know that they are not logged in
    if (!req.session.user) {
      return res.status(401).json({
        message: "Not logged in."
      });
    }
// Return current loggin in user data if session exists
    return res.status(200).json({
      user: req.session.user
    });
  } catch (error) {
  //if an error does occur it lets the user know
    console.error("Session check error:", error);
    return res.status(500).json({
      message: "Server error checking session."
    });
  }
});

module.exports = router;