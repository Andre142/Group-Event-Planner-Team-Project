const logout = () => {
  localStorage.removeItem("uuid")
  window.location.href = "./index.html"
}

const search = (keywords, country, startDate, endDate, errMsg, code, resultsContainer) => {
  if (keywords.value.length < 1) {
    errMsg.innerHTML = "Keywords cannot be empty"
    errMsg.classList.remove("hidden")
    genResults(resultsContainer)
    return;
  }
  else {
    errMsg.innerHTML = ""
    errMsg.classList.add("hidden")
  }
  let add = ""
  if (COUNTRIES[code]) {
    add = ("&countryCode=" + code)
  }
  $("#search-button").addClass("loading")
  if (startDate.value.length && endDate.value.length) {
    ajaxGet(ENDPOINT_URL + "/search/event?keyword=" + encodeURIComponent(keywords.value.trim()) + "&startDate=" + startDate.value + "&endDate=" + endDate.value + add, (response) => {
      let json = JSON.parse(response)
      genResults(json, resultsContainer)
      $("#search-button").removeClass("loading")
    })
  }
  else {
    ajaxGet(ENDPOINT_URL + "/search/event?keyword=" + encodeURIComponent(keywords.value.trim()) + add, (response) => {
      let json = JSON.parse(response)
      genResults(json, resultsContainer)
      $("#search-button").removeClass("loading")
    })
  }
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
        let resultContent = document.createElement("div")
        resultContent.className = "result-content"
        let p = document.createElement("p")
        p.className = "subtitle"
        p.innerHTML = moment(event.date).format("MMMM Do YYYY")
        let a = document.createElement("a")
        a.href = event.url
        a.innerHTML = event.name
        var box = document.createElement("input");
        box.type = "checkbox"
        resultContent.appendChild(p)
        resultContent.appendChild(a)
        result.appendChild(box);
        result.appendChild(resultContent)
        container.appendChild(result)
      }
    }
  }
}

const country = (element) => {
  element.value = element.value.trim().toUpperCase()
  const code = element.value
  if (COUNTRIES[code]) {
    element.style.backgroundImage = ("url('https://flagcdn.com/h60/" + code.toLowerCase() + ".png')")
  }
  else {
    element.style.backgroundImage = "url('../../assets/images/dummy-flag.png')"
  }
}
