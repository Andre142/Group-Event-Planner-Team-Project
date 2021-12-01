var list = document.getElementById("eventList");

function removeChildren(node)
{
    while (node.children.length > 0)
    {
        node.removeChild(node.firstChild);
    }
}

function addEventToList(event)
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

    eventItem.setAttribute("datetime", event.date + "T" + event.time);

    list.append(eventItem);
}

function sortList()
{
    var oldestFirst = document.getElementById("listSortSelector").value == "oldestFirst";
    var sortFunc;
    if (oldestFirst)
    {
        sortFunc = function(event1, event2) {
            let event1Time = moment(event1.getAttribute("datetime"));
            let event2Time = moment(event2.getAttribute("datetime"));
            if (event1Time.isBefore(event2Time))
            {
                return -1;
            }
            else if (event1Time.isSame(event2Time))
            {
                return 0;
            }
            else
            {
                return 1;
            }
        }
    }
    else
    {
        sortFunc = function(event1, event2) {
            let event1Time = moment(event1.getAttribute("datetime"));
            let event2Time = moment(event2.getAttribute("datetime"));
            if (event1Time.isBefore(event2Time))
            {
                return 1;
            }
            else if (event1Time.isSame(event2Time))
            {
                return 0;
            }
            else
            {
                return -1;
            }
        }
    }

    var eventArray = Array.from(list.children);
    eventArray.sort(sortFunc);

    removeChildren(list);
    for (let i = 0; i < eventArray.length; i++)
    {
        list.append(eventArray[i]);
    }
}

function addEventToCalendar(event)
{

}

ajaxGet(ENDPOINT_URL + "/proposal/get?type=received&username=" + localStorage.getItem("username"), (response) => {
      let json = JSON.parse(response);
      for (let i = 0; i < json.data.length; i++)
      {
        if (json.data[i].finalizedEventID == null)
        {
            for (let j = 0; j < json.data[i].events.length; j++)
            {
                addEventToList(json.data[i].events[j]);
                addEventToCalendar(json.data[i].events[j]);
            }
        }
        else
        {
            for (let j = 0; j < json.data[i].events.length; j++)
            {
                if (json.data[i].events[j].eventID == json.data[i].finalizedEventID)
                {
                    addEventToList(json.data[i].events[j]);
                    addEventToCalendar(json.data[i].events[j]);
                }
            }
        }
      }
      sortList();
    })

document.getElementById("switchButton").onclick = (e) => {
    if (document.getElementById('switchButton').innerText.includes("List"))
    {
        document.getElementById("switchButton").innerText = document.getElementById("switchButton").innerText.replace("List", "Calendar");
        document.getElementById("eventList").hidden = false;
        document.getElementById("listSort").hidden = false;
        document.getElementById("eventCalendar").hidden = true;
    }
    else
    {
        document.getElementById("switchButton").innerText = document.getElementById("switchButton").innerText.replace("Calendar", "List");
        document.getElementById("eventList").hidden = true;
        document.getElementById("listSort").hidden = true;
        document.getElementById("eventCalendar").hidden = false;
    }
}

document.getElementById("listSortSelector").onchange = (e) => {
    sortList();
}