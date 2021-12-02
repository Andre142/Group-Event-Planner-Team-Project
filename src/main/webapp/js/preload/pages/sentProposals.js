const logout = () => {
  localStorage.removeItem("uuid")
  window.location.href = "./index.html"
}
const account = () => {
  window.location.href = "./account.html"
}
const pendingInvites = () => {
  window.location.href = "./pendinginvites.html"
}
const proposalResponse = () => {
  window.location.href = "./proposalResponse.html"
}
const sentProposals = () => {
window.location.href = "./sentProposals.html"
}
const calendar = () => {
window.location.href = "./calendar.html"
}
const closeMe = (d, b) => {
    d.style.display = "none";
    b.style.display = "none";
}

const finalizeProp = (eventId, proposalId) =>{
    ajaxPost(ENDPOINT_URL + "/proposal/finalized?finalized_event_id=" + eventId + "&proposal_id=" + proposalId,
    {EVENT_ID: eventId, PROPOSAL_ID: proposalId}, (response) => {alert(JSON.parse(response).status)})
}
const getProposals = (resultsContainer) => {
    var senderUsername = localStorage.getItem("username");
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
      console.log(json)
      for (const event of json.data) {
        console.log(event)
        let result = document.createElement("div")
        result.className = "result"
        let p = document.createElement("p")
        p.className = "subtitle"
        console.log(event.events)
        for (let i = 0; i < event.events.length; i++){
        let divE = document.createElement('div')
        let divText = document.createTextNode("Proposal Title: " + event.proposalTitle)
        let receive = document.createTextNode("Receiver: " + event.receiverUsernames)
        let ev = document.createTextNode("Event: " + event.events[i].name)
        let ti = document.createTextNode("Time: " + event.events[i].time)
        let da = document.createTextNode("Date: " + event.events[i].date)
        var a = document.createElement('a')
        a.href = event.events[i].url
        a.title = event.events[i].url
        a.appendChild(document.createTextNode("Click here for TicketMaster link"))
                console.log(event.events[i].date)
                divE.appendChild(divText)
                divE.appendChild(document.createElement('br'))
                divE.appendChild(receive)
                divE.appendChild(document.createElement('br'))
                divE.appendChild(ev)
                divE.appendChild(document.createElement('br'))
                divE.appendChild(ti)
                divE.appendChild(document.createElement('br'))
                divE.appendChild(da)
                divE.appendChild(document.createElement('br'))
                divE.appendChild(a)
                result.appendChild(divE)
                let b = document.createElement("button")
                b.textContent = "Finalize Proposal"
                b.onclick = function(){finalizeProp(event.events[i].eventID, event.proposalID)};
//                b.onclick = function(){closeMe(divE, b)};
                result.appendChild(b)
        }
        container.appendChild(result)

      }
    }
  }
}