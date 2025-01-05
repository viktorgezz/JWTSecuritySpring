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
        alert("Регистрация прошла успешно!");
        window.location.href = "../";
        localStorage.setItem("login", login); // Сохранение логина
    } else {
        alert("Ошибка регистрации!");
    }
});

document.getElementById("login-btn").addEventListener("click", () => {
    window.location.href = "../";
});
