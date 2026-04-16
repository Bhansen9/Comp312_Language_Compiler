
// gets the elements from HTML pages
const runBtn = document.getElementById("runBtn");
const clearBtn = document.getElementById("clearBtn");
const codeBox = document.getElementById("code");
const outputBox = document.getElementById("output");

//RUN button functionality

// only runs the code if the elements exist
if (runBtn && codeBox && outputBox) {
  // When the run button is clicked
  runBtn.addEventListener("click", async () => {
    //gets the code from the code box
    const code = codeBox.value;
    //send the user a message the code is running
    outputBox.textContent = "Running...";

    try {
      //sends code to Spring boot backend to be compiled
      const response = await fetch("http://127.0.0.1:8080/run", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({ code })
      });
      //converts the response into JSON
      const result = await response.json();
      // if there was an error durin the process it lets the user know
      if (result.error) {
        outputBox.textContent = result.error;
      } else {
        // Shows the output from the code
        outputBox.textContent =
          result.output || "Program finished with no output.";
      }
      // if the backend is down it lets the user know
    } catch (error) {
      outputBox.textContent = "Could not connect to backend.";
      console.error(error);
    }
  });
}


// Clear button functionality



if (clearBtn && outputBox) {
  // Resets the output box when "Clear" button is clicked
  clearBtn.addEventListener("click", () => {
    outputBox.textContent = "Your output will appear here...";
  });
}


// Check Login auth

async function checkLogin() {
  try {
    //call Node.js backend to check session
    const response = await fetch("http://127.0.0.1:5000/auth/me", {
      method: "GET",
      credentials: "include"
    });
    // if the user is not logged in it redirects them to login page
    if (!response.ok) {
      window.location.href = "Login_Page.html";
      return;
    }
    // if logged in it gets user data
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