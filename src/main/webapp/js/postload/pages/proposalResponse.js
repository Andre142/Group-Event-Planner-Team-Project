const logout = () => {
  localStorage.removeItem("uuid")
  window.location.href = "./index.html"
}

 let username = localStorage.getItem("username")
 let c = $(".prop-list")

const search = (username, resultsContainer) => {
 ajaxGet(ENDPOINT_URL + "/proposal/get?type=received&username=" + "inviteeNametester1", (response) => {
       let json = JSON.parse(response)
       genResults(json, resultsContainer)
     })

};
 search(username,c);
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
                                          ${json.data[i].proposalTitle}
                                          <br>
                                          Event Name: ${json.data[i].events.name}
                                          <br>
                                          Date: ${json.data[i].events.date}
                                          <br>
                                          Time: ${json.data[i].events.time}
                                          <br>
                                                                                    URL: ${json.data[i].events.url}
                                                                                    <br>
                                                                                                                              Genre: ${json.data[i].events.genre}
                                  </div>
                                      <div class="col-3">
                                          <input type="radio" class="btn-check" name="yes-no" id="yes1" autocomplete="off">
                                      </div>
                                      <div class="col-2">
                                          <label class="btn btn-outline-success" name="yes-no" for="yes1">Yes</label>
                                      </div>
                                      <div class="col-3">
                                      <input type="radio" class="btn-check" name="yes-no" id="no1" autocomplete="off">
                                  </div>
                                      <div class="col-2">
                                          <label class="btn btn-outline-success" name="yes-no" for="no1">No</label>
                                      </div>

                                  </div>
                              </li>`;
     container.append(str);
     }
   }
 }
 blockUser();
}

function blockUser() {
let blockList = document.querySelectorAll(".blocked");
    for (let i = 0; i < blockList.length; i++) {

        blockList[i].onclick = function() {
           let userName = $(this).prev().text();
           this.parentNode.parentNode.remove(document.querySelector(".list-group-item"));
           let str = `<li class="list-group-item">
                                                     <div class="row">
                                                     <div class="col-8 unblock">
                                                         ${userName}
                                                     </div>
                                                     <button class="col-3 unblock btn-sm">
                                                         Unblock
                                                     </button>
                                                     </div>
                                                 </li>`;
            $(".blocked-list").append(str);
        unblockUser();
        }
    }
}
function unblockUser() {
let unblockList = document.querySelectorAll(".unblock");
    for (let i = 0; i < unblockList.length; i++) {
        unblockList[i].onclick = function() {
           let userName = $(this).prev().text();
           this.parentNode.parentNode.remove(document.querySelector(".list-group-item"));
           let str = `<li class="list-group-item">
                                      <div class="row">
                                      <div class="col-8">
                                          ${userName}
                                      </div>
                                      <button class="col-3 blocked btn-sm">
                                          Block
                                      </button>
                                      </div>
                                  </li>`;
            $(".search-users").append(str);
        blockUser();
        }
    }
}
blockUser();
unblockUser();
