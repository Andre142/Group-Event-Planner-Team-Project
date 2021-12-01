document.querySelector("#switchButton").onclick = (e) => {
    e.preventDefault()

    if (document.getElementById('switchButton').innerText.includes("List"))
    {
        document.getElementById("switchButton").innerText = document.getElementById("switchButton").innerText.replace("List", "Calendar");
        document.getElementById("eventList").hidden = false;
        document.getElementById("eventCalendar").hidden = true;
    }
    else
    {
        document.getElementById("switchButton").innerText = document.getElementById("switchButton").innerText.replace("Calendar", "List");
        document.getElementById("eventList").hidden = true;
        document.getElementById("eventCalendar").hidden = false;
    }
}

function addEventToList(list, event)
{
    let eventItem = document.createElement("div");
    eventItem.className = "listItem";

    let eventDate = document.createElement("p");
    eventDate.className = "subtitle";
    eventDate.innerHTML = moment(event.date).format("MMMM Do YYYY")
    eventItem.append(eventDate);

    let eventName = document.createElement("a");
    eventName.href = event.url;
    eventName.innerHTML = event.name;
    eventItem.append(eventName);

    let eventTime = document.createElement("b");
    eventTime.innerHTML = event.time;
    eventItem.append(eventTime);

    list.append(eventItem);
}

function addEventToCalendar(event)
{
}

ajaxGet(ENDPOINT_URL + "/proposal/get?type=received&username=" + localStorage.getItem("username"), (response) => {
      let json = JSON.parse(response);
      var list = document.getElementById("eventList");
      for (let i = 0; i < json.data.length; i++)
      {
        if (json.data[i].finalizedEventID == null)
        {
            for (let j = 0; j < json.data[i].events.length; j++)
            {
                addEventToList(list, json.data[i].events[j]);
                addEventToCalendar(json.data[i].events[j]);
            }
        }
        else
        {
            for (let j = 0; j < json.data[i].events.length; j++)
            {
                if (json.data[i].events[j].eventID == json.data[i].finalizedEventID)
                {
                    addEventToList(list, json.data[i].events[j]);
                    addEventToCalendar(json.data[i].events[j]);
                }
            }
        }
      }
    })