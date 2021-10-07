function signup(username, password) {
    if (username.length == 0) {
        document.querySelector("#input-username + .error-msg").classList.remove("hidden");
        return;
    }
    if (password.length == 0) {
        document.querySelector("#input-password + .error-msg").classList.remove("hidden");
        return;
    }
    document.querySelector("#input-password + .error-msg").classList.add("hidden");
    document.querySelector("#input-username + .error-msg").classList.add("hidden");

    ajaxPost(ENDPOINT_URL + "/auth/signUp", {"username": username, "password": password}, function (response) {
        let json = JSON.parse(response);
        console.log("Reached");
        console.log(json.status);
        if (!json.status) {
            document.querySelector(".error-msg").value = json.message;
            document.querySelector(".error-msg").classList.remove("hidden");
        } else {
            document.querySelector(".error-msg").classList.add("hidden");
        }
    });
}
