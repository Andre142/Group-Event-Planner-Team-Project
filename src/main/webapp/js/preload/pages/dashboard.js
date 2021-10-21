const logout = () => {
  localStorage.removeItem("uuid")
  window.location.href = "./index.html"
}

const search = () => {
  const keywords = document.getElementById("keywords")
  const startDate = document.getElementById("start-date")
  const endDate = document.getElementById("end-date")
  if (keywords.value.length < 1) {
    $(keywords).addClass("invalid")
    genResults()
    return;
  }
  else {
    $(keywords).removeClass("invalid")
  }
  if (startDate.value.length && endDate.value.length) {
    ajaxGet(ENDPOINT_URL + "/search/event?keyword=" + encodeURIComponent(keywords.value.trim()) + "&startDate=" + startDate.value + "&endDate=" + endDate.value, (response) => {
      let json = JSON.parse(response)
      genResults(json)
    })
  }
  else {
    ajaxGet(ENDPOINT_URL + "/search/event?keyword=" + encodeURIComponent(keywords.value.trim()), (response) => {
      let json = JSON.parse(response)
      genResults(json)
    })
  }
}


const genResults = (json = {}) => {
  const container = document.getElementById("results")
  container.innerHTML = ""
  if (JSON.stringify(json) === "{}" || !json.status) {
    let p = document.createElement("p")
    p.className = "error"
    p.innerHTML = "No results."
    container.appendChild(p)
  }
  else {
    for (const event of json.data.events) {
      let result = document.createElement("div")
      result.className = "result"
      let p = document.createElement("p")
      p.className = "subtitle"
      p.innerHTML = moment(event.date).format("MMMM Do YYYY")
      let a = document.createElement("a")
      a.href = event.url
      a.innerHTML = event.name
      result.appendChild(p)
      result.appendChild(a)
      container.appendChild(result)
    }
  }
}
