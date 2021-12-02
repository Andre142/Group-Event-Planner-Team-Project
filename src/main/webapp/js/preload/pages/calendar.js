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