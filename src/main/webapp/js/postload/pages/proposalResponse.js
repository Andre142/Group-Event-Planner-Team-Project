const logout = () => {
  localStorage.removeItem("uuid")
  window.location.href = "./index.html"
}

function back(){
    window.location.href = "./dashboard.html"
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
           for (let j=0;j<json.data[i].events.length;j++){
             let str = `<li class="list-group-item">
                                        <div class="row">
                                            <div class="col-12">
                                                <strong><a href="${json.data[i].events[j].url}">${json.data[i].proposalTitle}</a></strong>
                                                <br>
                                                Event Name: ${json.data[i].events[j].name}
                                                <br>
                                                Date: ${json.data[i].events[j].date}
                                                <br>
                                                Time: ${json.data[i].events[j].time}
                                                <br>
                                                Genre: ${json.data[i].events[j].genre}
                                        </div>
                                            <div class="col-3">
                                                <input type="radio" class="btn-check" name="yes-no${i}${j}" id="yes${i}${j}" autocomplete="off">
                                            </div>
                                            <div class="col-1">
                                                <label class="btn btn-outline-success" name="yes-no${i}${j}" for="yes${i}${j}">Yes</label>
                                            </div>
                                            <div class="col-3">
                                                <input type="radio" class="btn-check" name="yes-no${i}${j}" id="maybe${i}${j}" autocomplete="off">
                                            </div>
                                            <div class="col-1">
                                                <label class="btn btn-outline-success" name="yes-no${i}${j}" for="maybe${i}${j}">Maybe</label>
                                                <br><br><br>
                                                  Excitement:
                                                  <select id="${i}${j}">
                                                  <option selected disabled hidden style='display: none' value=''></option>
                                                  <option value="1">1</option>
                                                  <option value="2">2</option>
                                                  <option value="3">3</option>
                                                  <option value="4">4</option>
                                                  <option value="5">5</option>
                                                  </select>
    </div>
                                            <div class="col-3">
                                            <input type="radio" class="btn-check" name="yes-no${i}${j}" id="no${i}${j}" autocomplete="off">
                                        </div>
                                            <div class="col-1">
                                                <label class="btn btn-outline-success" name="yes-no${i}${j}" for="no${i}${j}">No</label>

                                            </div>
      
                                        </div>
                                    </li>`;
           container.append(str);
           }
         }
         }
       }
       }
const search = (username, resultsContainer) => {
 ajaxGet(ENDPOINT_URL + "/proposal/get?type=received&username=" + username, (response) => {
       let json = JSON.parse(response)
       genResults(json, resultsContainer)
     

})
};
 search(username,c);

 $("#send-button").on("click",function() {
var xhReq = new XMLHttpRequest();
xhReq.open("GET", ENDPOINT_URL + "/proposal/get?type=received&username=" + username, false);
xhReq.send(null);
var json = JSON.parse(xhReq.responseText);
for (let i=0;i<json.data.length;i++) {
for (let j=0;j<json.data[i].events.length;j++){
 if (!($('input[name=yes-no'+i+j+']:checked').length > 0 && $("#"+i+j+" option:selected").length > 0)) {
 alert("Please fill out all entries.");
 return;
 }
 }
 }
 for (let i=0;i<json.data.length;i++) {
for (let j=0;j<json.data[i].events.length;j++){
 if ($('input[name=yes-no'+i+j+']:checked').length > 0 && $("#"+i+j+" option:selected").length > 0) {

 let ele = document.getElementsByName('yes-no'+i+j);
 let availabilityVal;
             for(let k = 0; k < ele.length; k+=2) {
                 if(ele[k].checked && k == 0) {
                    availabilityVal = 1;
                 }
                 else
                 {
                    availabilityVal = 0;
                 }
             }
   let excitementVal = document.getElementById(""+i+j).value;
$.ajax({
    type: "POST",
    url: "http://localhost:8080/response/send",
    data: JSON.stringify({ "eventID": ""+json.data[i].events[j].eventID,
                                  "availability": availabilityVal,
                                  "excitement": excitementVal,
                                  "receiverUsername": ""+username
                                  }),
    contentType: "application/json; charset=utf-8",
    dataType: "json",
    success: function(data){},
    error: function(errMsg) {
        alert(errMsg);
    }
});
}
     }
     }
     });
