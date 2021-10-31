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