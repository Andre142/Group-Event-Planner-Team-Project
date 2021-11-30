const logout = () => {
  localStorage.removeItem("uuid")
  window.location.href = "./index.html"
}
const account = () => {
  window.location.href = "./account.html"
}
const getProposals = (username, errMsg, resultsContainer) => {
    if (username.value.length < 1) {
        errMsg.innerHTML = "Username cannot be empty"
        errMsg.classList.remove("hidden")
        genResults(resultsContainer)
        return;
      }
      else {
        errMsg.innerHTML = ""
        errMsg.classList.add("hidden")
      }
      $("#display-button").addClass("loading")
      console.log("arrived");
       ajaxGet(ENDPOINT_URL + "/proposal/get?type=sent&username=" + encodeURIComponent(username.value.trim()), (response) => {
               let json = JSON.parse(response)
               console.log(response);
               genResults(json, resultsContainer)
               $("#display-button").removeClass("loading")
             })
}

const genResults = (json = {}, container) => {
  container.innerHTML = ""
  if (JSON.stringify(json) !== "{}") {
    if (!json.status) {
      let msg = document.createElement("p")
      msg.className = "error"
      msg.innerHTML = "No results"
      container.appendChild(msg)
    }
    else {
      let msg = document.createElement("p")
      msg.innerHTML = (json.data.events.length.toString() + " result(s)").trim()
      container.appendChild(msg)
      for (const event of json.data.events) {
        let result = document.createElement("div")
        result.className = "result"
        let p = document.createElement("p")
        p.className = "subtitle"
        result.appendChild(p)
        container.appendChild(result)
      }
    }
  }
}