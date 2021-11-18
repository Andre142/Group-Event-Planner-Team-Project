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
    console.log("Hi")

    let url = "http://localhost:8080/proposal/get?type=received&username=jessie"
    ajaxGet(url, (response) => {
        let json = JSON.parse(response)
        console.log(json);

        let finalizedProposals = [];
        for(let i=0; i<json.data.length; i++){
            if(json.data[i].finalizedEventID != "null"){
                finalizedProposals.push(json.data[i])
                console.log(json.data[i])
            }
        }

    })

    let str = "";
    for(let i=0; i<5; i++){
        str +=
            '        <div class="card">\n' +
            '            <img src="assets/images/invited.png" alt="" class="card_image">\n' +
            '            <div class="card_content">\n' +
            '                <p>Dummy Name</p>\n' +
            '            </div>\n' +
            '            <div class="card_info">\n' +
            '                <span class="material-icons" name="check" onclick="check()">done</span>\n' +
            '                <span class="material-icons" name="cross" onclick="cross()">close</span>\n' +
            '            </div>\n' +
            '        </div>\n';

    }
    document.getElementById("cards").innerHTML = str
}