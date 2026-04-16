// gets the signup formm elements
const signupForm = document.getElementById("signupForm");

// Get the message element to display feedback to the user
const message = document.getElementById("message");


// Only runs the code if the form exists to prevent error on other pages
if (signupForm) {
  //checks for when the user sumbits the form
  signupForm.addEventListener("submit", async (e) => {
    // Prevents the page from refreshing when the form is submitted
    e.preventDefault();
    // Get the values from input fields and then cleans it
    const username = document.getElementById("username").value.trim();
    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value.trim();

    try {
      // sends a request to backend signup route with the users inputted data
      const response = await fetch("http://127.0.0.1:5000/auth/signup", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        // allows cookies and sessions to be used for authentication
        credentials: "include",
        // sends the data as a JSON string in the request body
        body: JSON.stringify({ username, email, password })
      });
      // convert response into JSON
      const data = await response.json();
      // if signup was successful displays successful message
      if (response.ok) {
        message.textContent = data.message || "Account created successfully!";
        // Redirects to login page after an account is made
        setTimeout(() => {
          window.location.href = "Login_Page.html";
        }, 1000);
      } else {
        // Otherwise displays an error message if the signup failed
        message.textContent = data.message || "Signup failed.";
      }
    } catch (error) {
      // If the fetch request fails on the server side it will display a message
      message.textContent = "Could not connect to server.";
      console.error(error);
    }
  });
}