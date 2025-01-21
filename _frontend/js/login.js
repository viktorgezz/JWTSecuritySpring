document.getElementById("register-btn").addEventListener("click", () => {
    window.location.href = "../register";
});

document.getElementById("login-form").addEventListener("submit", async (e) => {
    e.preventDefault();

    const login = document.getElementById("login").value;
    const password = document.getElementById("password").value;

    const response = await fetch("http://localhost:8080/auth/signin", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ login, password }),
    });

    if (response.ok) {
        const data = await response.json();
        localStorage.setItem("jwt", data.token);
        window.location.href = "http://localhost:4200/account";
    } else {
        alert("Login failed!");
    }
});
