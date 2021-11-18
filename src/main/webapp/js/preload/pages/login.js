const login = (username, password) => {
  document.querySelector(".error-msg").classList.add("hidden")
  if (username.length == 0 || password.length == 0) {
    if (username.length == 0) {
      document.querySelector(".error-msg > p").innerText = "Username cannot be empty"
    }
    else {
      if (password.length == 0) {
        document.querySelector(".error-msg > p").innerText = "Password cannot be empty"
      }
    }
    document.querySelector(".error-msg").classList.remove("hidden")
    return;
  }
  ajaxGet(ENDPOINT_URL + "/auth/login?username=" + username + "&psw=" + password, (response) => {
    let json = JSON.parse(response)
    if (!json.status) {
      document.querySelector(".error-msg > p").innerText = json.message
      document.querySelector(".error-msg").classList.remove("hidden")
    }
    else {
      localStorage.setItem("uuid", json.data.uuid)
      localStorage.setItem("username", json.data.username)
      window.location.href = "./dashboard.html"
    }
  })
};