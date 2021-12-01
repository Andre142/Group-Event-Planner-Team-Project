const logout = () => {
  localStorage.removeItem("uuid")
  window.location.href = "./index.html"
}
const account = () => {
  window.location.href = "./account.html"
}
const getProposals = (resultsContainer) => {
      console.log("arrived");
      var senderUsername = localStorage.getItem("uuid");
      ajaxGet(ENDPOINT_URL + "/proposal/get?type=sent&username=" + senderUsername, (response) => {
               let json = JSON.parse(response)
               genResults(json, resultsContainer)
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
      msg.innerHTML = (json.data.length.toString() + " result(s)").trim()
      container.appendChild(msg)
      for (const event of json.data) {
        let result = document.createElement("div")
        result.className = "result"
        let p = document.createElement("p")
        p.className = "subtitle"
        p.innerHTML = event.proposalTitle
        result.appendChild(p)
        container.appendChild(result)
      }
    }
  }
}