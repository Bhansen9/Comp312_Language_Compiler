
// Get the login form from the HTML
const loginForm = document.getElementById("loginForm");


// Gets the message area to display feedback to the user
const message = document.getElementById("message");

// only runs if the login form exists
if (loginForm) {
  // Checks for form submissions
  loginForm.addEventListener("submit", async (e) => {
    e.preventDefault();

    //  Gets the user input and cleans it
    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value.trim();

    try {
      // sends a request to backend for the login route
      const response = await fetch("http://127.0.0.1:5000/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        credentials: "include",
        body: JSON.stringify({ email, password })
      });
      // converts the response into JSON
      const data = await response.json();


      // if the login was succesfull it displays a message and redirects to the index page
      if (response.ok) {
        message.textContent = data.message || "Login successful!";

        setTimeout(() => {
          window.location.href = "Index.html";
        }, 800);
      } else {
        message.textContent = data.message || "Login failed.";
      }
    } catch (error) {
      // if the fetch request fails on the server side it will display an error
      console.error(error);
      message.textContent = "Could not connect to server.";
    }
  });
}