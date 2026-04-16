
// Load enviroment varibles from .env file
require("dotenv").config();


// import libraries for the server, database connections, and session management
const express = require("express");
const mongoose = require("mongoose");
const cors = require("cors");
const session = require("express-session");


//Importing auth routes
const authRoutes = require("./routes/auth");

// Create express app
const app = express();

// Middleware

// Allows server to read JSON data from requests
app.use(express.json());

// CORS configuration for backend and frontend communication
app.use(cors({
  origin: ["http://127.0.0.1:5500", "http://localhost:5500"],
  credentials: true
}));

// Session Configuration 


app.use(session({
// Secret key for signing session ID cookies
  secret: process.env.SESSION_SECRET(KEY) || "ONLINE_COMPILER_LOYOLA_COMP312E",
  // dont save session if nothing is changed
  resave: false,
  // dont create session that are empty
  saveUninitialized: false,
  cookie: {
    // secure: false becuase it just using localhost
    secure: false,
    //prevent client side from accesing the cookie 
    httpOnly: true,
    // Session only last 1 day
    maxAge: 1000 * 60 * 60 * 24
  }
}));

// Test route - route to test if server is running
app.get("/", (req, res) => {
  res.send("Auth backend is running");
});

// Auth routes to prefix all auth related routes with /auth
app.use("/auth", authRoutes);

// MongoDB connection + server startup
const PORT = process.env.PORT || 5000;
// connect to MongoDB
mongoose.connect(process.env.MONGO_URI)
  .then(() => {
    console.log("MongoDB connected");
// Start the server after successful database connection
    app.listen(PORT, () => {
      console.log(`Server running on port ${PORT}`);
    });
  })
  .catch((err) => {
    // display error if there is an issue connecting
    console.error("MongoDB connection error:", err);
  });