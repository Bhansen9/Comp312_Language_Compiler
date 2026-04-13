const signupForm = document.getElementById("signupForm");
const message = document.getElementById("message");

if (signupForm) {
  signupForm.addEventListener("submit", async (e) => {
    e.preventDefault();

    const username = document.getElementById("username").value.trim();
    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value.trim();

    try {
      const response = await fetch("http://127.0.0.1:5000/auth/signup", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        credentials: "include",
        body: JSON.stringify({ username, email, password })
      });

      const data = await response.json();

      if (response.ok) {
        message.textContent = data.message || "Account created successfully!";

        setTimeout(() => {
          window.location.href = "Login_Page.html";
        }, 1000);
      } else {
        message.textContent = data.message || "Signup failed.";
      }
    } catch (error) {
      message.textContent = "Could not connect to server.";
      console.error(error);
    }
  });
}