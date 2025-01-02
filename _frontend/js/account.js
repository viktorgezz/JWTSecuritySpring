document.addEventListener("DOMContentLoaded", () => {
    const apiUrl = "http://localhost:8080/account"; // URL контроллера

    const accountIdElement = document.getElementById("account-id");
    const accountLoginElement = document.getElementById("account-login");

    // Функция для загрузки данных об аккаунте
    async function fetchAccount() {
        const token = localStorage.getItem("jwt"); // Получаем токен из localStorage
        try {
            const response = await fetch(apiUrl, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`, // Передача токена
                },
            });

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const account = await response.json();

            // Заполнение элементов данными об аккаунте
            accountIdElement.textContent = account.id;
            accountLoginElement.textContent = account.login;
        } catch (error) {
            console.error("Error fetching account:", error);
            alert("Не удалось загрузить данные об аккаунте.");
        }
    }

    // Загрузка данных об аккаунте при загрузке страницы
    fetchAccount();
});
