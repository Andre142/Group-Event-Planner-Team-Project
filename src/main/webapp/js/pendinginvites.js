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
var finalizedProposals = [];
class FinalizedProposal {
    constructor(name, date, time, url, genre) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.url = url;
        this.genre = genre;
    }
}


function back(){
    window.location.href = "./dashboard.html"
}

function check(){
    alert("Accepted Invite")
}

function cross(){
    alert("Declined Invite")
}

function populate(){
    var done = false;
    var currentUser = localStorage.getItem("username")
    let url = "http://localhost:8080/proposal/get?type=received&username=" + currentUser
    ajaxGet(url, (response) => {
        let json = JSON.parse(response)
        for(let i=0; i<json.data.length; i++){
            console.log(json.data[i])
            if(json.data[i].finalizedEventID != null){
                for(let j=0; j<json.data[i].events.length; j++){
                    if(json.data[i].events[j].eventID === json.data[i].finalizedEventID){
                        var name = json.data[i].events[j].name
                        var date = json.data[i].events[j].date
                        var time = json.data[i].events[j].time
                        var url = json.data[i].events[j].url
                        var genre = json.data[i].events[j].genre
                        const newFinalizedProposal = new FinalizedProposal(name, date, time, url, genre)
                        finalizedProposals.push(newFinalizedProposal)
                        console.log("i: " + i)
                        console.log("json: " + json.data.length)
                    }
                }

            }
            if (i === json.data.length-1){
                finalize()
            }
        }

    })


}

function finalize(){
    let name = finalizedProposals[0].name
    let date = finalizedProposals[0].date
    let time = finalizedProposals[0].time
    let url = finalizedProposals[0].url
    let genre = finalizedProposals[0].genre

    let str = "";
    for(let i=0; i<finalizedProposals.length; i++){
        str +=
            '        <div class="card">\n' +
            '            <img src="assets/images/invited.png" alt="" class="card_image">\n' +
            '            <div class="card_content">\n' +
                         `<a href="${url}">${name}</a>` + '<br>' +  `Date: ${date}` +  '<br>' + `Time: ${time}` + '<br>' + `Genre: ${genre}` +
            '            </div>\n' +
            '            <div class="card_info">\n' +
            '                <span class="material-icons" name="check" onclick="check()">done</span>\n' +
            '                <span class="material-icons" name="cross" onclick="cross()">close</span>\n' +
            '            </div>\n' +
            '        </div>\n';

    }
    document.getElementById("cards").innerHTML = str
}