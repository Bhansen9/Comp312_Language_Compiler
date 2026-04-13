const runBtn = document.getElementById("runBtn");
const clearBtn = document.getElementById("clearBtn");
const codeBox = document.getElementById("code");
const outputBox = document.getElementById("output");

if (runBtn && codeBox && outputBox) {
  runBtn.addEventListener("click", async () => {
    const code = codeBox.value;

    outputBox.textContent = "Running...";

    try {
      const response = await fetch("http://127.0.0.1:8080/run", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({ code })
      });

      const result = await response.json();

      if (result.error) {
        outputBox.textContent = result.error;
      } else {
        outputBox.textContent =
          result.output || "Program finished with no output.";
      }
    } catch (error) {
      outputBox.textContent = "Could not connect to backend.";
      console.error(error);
    }
  });
}

if (clearBtn && outputBox) {
  clearBtn.addEventListener("click", () => {
    outputBox.textContent = "Your output will appear here...";
  });
}

async function checkLogin() {
  try {
    const response = await fetch("http://127.0.0.1:5000/auth/me", {
      method: "GET",
      credentials: "include"
    });

    if (!response.ok) {
      window.location.href = "Login_Page.html";
      return;
    }

    const data = await response.json();
    console.log("Logged in as:", data.user.username);
  } catch (error) {
    console.error("Session check failed:", error);
    window.location.href = "Login_Page.html";
  }
}

// only run session check on compiler page
if (runBtn) {
  checkLogin();
}