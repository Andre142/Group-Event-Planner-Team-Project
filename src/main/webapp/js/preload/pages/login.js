function login(username, password) {
    if(username.length == 0 || password.length == 0) {
        if (username.length == 0) {
            document.querySelector("#input-username + .error-msg").classList.remove("hidden");
        }
        if (password.length == 0) {
            document.querySelector("#input-password + .error-msg").classList.remove("hidden");
        }
        document.querySelector(".error-msg").classList.add("hidden");
        return;
    }
    document.querySelector("#input-password + .error-msg").classList.add("hidden");
    document.querySelector("#input-username + .error-msg").classList.add("hidden");

    ajaxGet(ENDPOINT_URL + "/auth/login?username=" + username + "&psw=" + password, function(response) {
        let json = JSON.parse(response);

        if (!json.status) {
            document.querySelector(".error-msg > p").innerText = json.message;
            document.querySelector(".error-msg").classList.remove("hidden");
        } else {
            document.querySelector(".error-msg").classList.add("hidden");
            localStorage.setItem("uuid", json.uuid)
            window.location.href = "./dashboard.html"
        }
    });
}
