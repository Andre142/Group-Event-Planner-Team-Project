document.querySelector("#switchButton").onclick = (e) => {
    e.preventDefault()

    if (document.getElementById('switchButton').innerText.includes("List"))
    {
        document.getElementById("switchButton").innerText = document.getElementById("switchButton").innerText.replace("List", "Calendar");
        document.getElementById("eventList").hidden = true;
        document.getElementById("eventCalendar").hidden = false;
    }
    else
    {
        document.getElementById("switchButton").innerText = document.getElementById("switchButton").innerText.replace("Calendar", "List");
        document.getElementById("eventList").hidden = false;
        document.getElementById("eventCalendar").hidden = true;
    }
}