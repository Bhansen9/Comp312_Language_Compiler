
//Used Mongooese library to interact with MongoDB database
const mongoose = require("mongoose");

// creates a layout for how the user data will be stored in mongoDB
const userSchema = new mongoose.Schema({
//Username section
  username: {
    type: String,
    required: true,
    unique: true,
    trim: true
  },
  //Email Section
  email: {
    type: String,
    required: true,
    unique: true,
    trim: true,
    lowercase: true
  },
  //Password Section
  password: {
    type: String,
    required: true
  }
});

module.exports = mongoose.model("User", userSchema);