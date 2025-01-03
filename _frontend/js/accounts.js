document.addEventListener("DOMContentLoaded", () => {
    const apiUrl = "http://localhost:8080/admin/accounts"; // URL для получения списка аккаунтов
    const accountsTable = document.getElementById("accounts-table");
    const token = localStorage.getItem("jwt"); // Извлекаем токен из localStorage

    // Функция для загрузки данных с API
    async function fetchAccounts() {
        try {
            const response = await fetch(apiUrl, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`,
                },
            });

            if (response.status === 403) {
                // Перенаправляем на страницу ошибки 403, которая не создана)
                window.location.href = "/403.html";
                return;
            }

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const accounts = await response.json();
            populateTable(accounts);
        } catch (error) {
            console.error("Error fetching accounts:", error);
        }
    }

    // Функция для заполнения таблицы
    function populateTable(accounts) {
        accountsTable.innerHTML = ""; // Очистка таблицы перед заполнением

        accounts.forEach(account => {
            const row = document.createElement("tr");
            row.innerHTML = `
                <td>${account.id}</td>
                <td>${account.login}</td>
            `;
            accountsTable.appendChild(row);
        });
    }

    // Загрузка аккаунтов при загрузке страницы
    fetchAccounts();
});
