document.getElementById("register-form").addEventListener("submit", async (e) => {
    e.preventDefault();

    const login = document.getElementById("login").value;
    const password = document.getElementById("password").value;

    const response = await fetch("http://localhost:8080/auth/signup", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ login, password }),
    });

    if (response.ok) {
        alert("Registration successful!");
        window.location.href = "../html/login.html";
        localStorage.setItem("login", login); // Передача логина
    } else {
        alert("Registration failed!");
    }
});
