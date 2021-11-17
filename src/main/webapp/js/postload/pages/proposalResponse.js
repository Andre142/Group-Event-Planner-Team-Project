const logout = () => {
  localStorage.removeItem("uuid")
  window.location.href = "./index.html"
}

 let username = localStorage.getItem("username")
 let c = $(".prop-list")

      const genResults = (json = {}, container) => {
       container.empty();
       if (JSON.stringify(json) !== "{}") {
         if (!json.status) {
           let msg = document.createElement("p")
           msg.className = "error"
           msg.innerHTML = "No results found"
           container.appendChild(msg)
         }
         else {
           for (let i=0;i<json.data.length;i++) {
             let str = `<li class="list-group-item">
                                        <div class="row">
                                            <div class="col-2">
                                                <strong>${json.data[i].proposalTitle}</strong>
                                                <br>
                                                Event Name: ${json.data[i].events[0].name}
                                                <br>
                                                Date: ${json.data[i].events[0].date}
                                                <br>
                                                Time: ${json.data[i].events[0].time}
                                                <br>
                                                                                          URL: ${json.data[i].events[0].url}
                                                                                          <br>
                                                                                                                                    Genre: ${json.data[i].events[0].genre}
                                        </div>
                                            <div class="col-3">
                                                <input type="radio" class="btn-check" name="yes-no${i}" id="yes1${i}" autocomplete="off">
                                            </div>
                                            <div class="col-2">
                                                <label class="btn btn-outline-success" name="yes-no${i}" for="yes${i}">Yes</label>
                                            </div>
                                            <div class="col-3">
                                            <input type="radio" class="btn-check" name="yes-no${i}" id="no${i}" autocomplete="off">
                                        </div>
                                            <div class="col-2">
                                                <label class="btn btn-outline-success" name="yes-no${i}" for="no${i}">No</label>
                                                <br><br><br>
                                                Excitement:
                                                <select id="${i}">
                                                <option selected disabled hidden style='display: none' value=''></option>
                                                <option value="1">1</option>
                                                <option value="2">2</option>
                                                <option value="3">3</option>
                                                <option value="4">4</option>
                                                <option value="5">5</option>
                                                </select>
                                            </div>
      
                                        </div>
                                    </li>`;
           container.append(str);
            console.log("HI");
           }
         }
       }
       }
const search = (username, resultsContainer) => {
 ajaxGet(ENDPOINT_URL + "/proposal/get?type=received&username=" + "inviteeNametester1", (response) => {
       let json = JSON.parse(response)
       genResults(json, resultsContainer)
     

})
};
 search(username,c);

 $("#send-button").on("click",function() {
 console.log("HI");
var xhReq = new XMLHttpRequest();
xhReq.open("GET", ENDPOINT_URL + "/proposal/get?type=received&username=" + "inviteeNametester1", false);
xhReq.send(null);
var json = JSON.parse(xhReq.responseText);  
 for (let i=0;i<json.data.length;i++) {
 console.log(i);

 if ($('input[name=yes-no'+i+']:checked').length > 0 && $("#"+i+" option:selected").length > 0) {

 let ele = document.getElementsByName('yes-no'+i);

             for(i = 0; i < ele.length; i++) {
                 if(ele[i].checked) {
                 let availabilityVal = ele[i].value;
                 }
             }
   let excitementVal = document.getElementByID(""+i).value;

     $.post("http://localhost:8080/response/send",
     {
       eventID: json.events[0].eventID,
       availability: availabilityVal,
       excitement: excitementVal,
       receiverUsername: username
     })
     .done(function(results) {
           let ev=results.data[0].availability;
           
           console.log(ev);
           
       })
       .fail(function(results)
       {
           alert("Request failed");
       })
     }
     }
     });
