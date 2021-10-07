let ENDPOINT_URL = "http://localhost:8080"
function ajaxPost(endpointUrl, postData, returnFunction){
    var xhr = new XMLHttpRequest();
    xhr.open('POST', endpointUrl, true);
    xhr.setRequestHeader('Content-type', 'application/json');
    xhr.onreadystatechange = function(){
        if (xhr.readyState == XMLHttpRequest.DONE) {
            if (xhr.status == 200) {
                returnFunction( xhr.responseText );
            } else {
                alert('AJAX Error.');
                console.log(xhr.status);
            }
        }
    }
    xhr.send(JSON.stringify(postData));
};
function ajaxGet(endpointUrl, returnFunction){
    var xhr = new XMLHttpRequest();
    xhr.open('GET', endpointUrl, true);
    xhr.onreadystatechange = function(){
        if (xhr.readyState == XMLHttpRequest.DONE) {
            if (xhr.status == 200) {
                // When ajax call is complete, call this function, pass a string with the response
                returnFunction( xhr.responseText );
            } else {
                alert('AJAX Error.');
                console.log(xhr.status);
            }
        }
    }
    xhr.send();
};
