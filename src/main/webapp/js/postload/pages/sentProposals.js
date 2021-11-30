document.querySelector("#display-button").onclick = (e) => {
    e.preventDefault()
    const username = document.getElementById("username")
    const errMsg = document.querySelector(".error-msg")
    const container = document.getElementById("results")

    getProposals(username, errMsg, container);
}