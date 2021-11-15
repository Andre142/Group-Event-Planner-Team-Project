const logout = () => {
  localStorage.removeItem("uuid")
  window.location.href = "./index.html"
}