function signup(username, password) {
    document.querySelector("#input-password").classList.remove("invalid");
    document.querySelector("#input-username").classList.remove("invalid");
    if(username.length == 0 || password.length == 0) {
        if (username.length == 0) {
            document.querySelector("#input-username").classList.add("invalid");
        }
        if (password.length == 0) {
            document.querySelector("#input-password").classList.add("invalid");
        }
        document.querySelector(".error-msg").classList.add("hidden");
        return;
    }

    ajaxPost(ENDPOINT_URL + "/auth/signUp", {"username": username, "psw": password}, function (response) {
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
