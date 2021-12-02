window.addEventListener('load', (event) => {
   event.preventDefault()
   const container = document.getElementById("results")
   getProposals(container);
});