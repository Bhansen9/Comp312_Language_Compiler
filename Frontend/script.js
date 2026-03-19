const runBtn = document.getElementById("runBtn");
const clearBtn = document.getElementById("clearBtn");
const codeBox = document.getElementById("code");
const outputBox = document.getElementById("output");

runBtn.addEventListener("click", async () => {
  const code = codeBox.value;

  outputBox.textContent = "Running...";

  try {
    const response = await fetch("http://localhost:8080/run", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ code: code })
    });

    const result = await response.json();

    if (result.error) {
      outputBox.textContent = result.error;
    } else {
      outputBox.textContent = result.output || "Program finished with no output.";
    }
  } catch (error) {
    outputBox.textContent = "Could not connect to backend.";
  }
});

clearBtn.addEventListener("click", () => {
  outputBox.textContent = "Your output will appear here...";
});