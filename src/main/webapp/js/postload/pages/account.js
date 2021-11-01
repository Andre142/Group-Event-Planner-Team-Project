document.querySelector("#search-button").onclick = (e) => {
  e.preventDefault()
  console.log("hi")
  let username = document.querySelector("#username").value
  console.log(username);
  const container = document.getElementById("search-users")
  search(username,container);
}

const search = (username, resultsContainer) => {
  ajaxGet(ENDPOINT_URL + "/search/user?q=" + encodeURIComponent(username.trim()), (response) => {
        console.log(response)
        let json = JSON.parse(response)
        genResults(json, resultsContainer)
      })
};

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
      for (const name of json.data) {
        let str = `<li class="list-group-item">
                                       <div class="row">
                                       <div class="col-8">
                                           ${name}
                                       </div>
                                       <button class="col-3 blocked btn-sm">
                                           Block
                                       </button>
                                       </div>
                                   </li>`;
        container.append(str);
      }
    }
  }
}

function blockUser() {
let blockList = document.querySelectorAll(".blocked");
for (let i = 0; i < blockList.length; i++)
{

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
for (let i = 0; i < unblockList.length; i++)
{
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