const loginForm = document.getElementById("loginForm");
const message = document.getElementById("message");

if (loginForm) {
  loginForm.addEventListener("submit", async (e) => {
    e.preventDefault();

    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value.trim();

    try {
      const response = await fetch("http://127.0.0.1:5000/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        credentials: "include",
        body: JSON.stringify({ email, password })
      });

      const data = await response.json();

      if (response.ok) {
        message.textContent = data.message || "Login successful!";

        setTimeout(() => {
          window.location.href = "Index.html";
        }, 800);
      } else {
        message.textContent = data.message || "Login failed.";
      }
    } catch (error) {
      console.error(error);
      message.textContent = "Could not connect to server.";
    }
  });
}