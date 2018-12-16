const domainName = "https://fandoco-vault.herokuapp.com";
var loggedin = false;

//function load onload
function showLogin() {

    document.getElementById("details").style.visibility = "hidden";
    document.getElementById("info").innerHTML = "Please Login";
    document.getElementById("loginDetails").style.visibility = "hidden";
    document.getElementById("addField").style.visibility = "hidden";
    document.getElementById("adddata").style.visibility = "hidden";

}

//onclick Add button
function addData() {
    clearInformation();
    document.getElementById("details").style.visibility = "hidden";
    document.getElementById("info").innerHTML = "Add a new field";
    document.getElementById("loginDetails").style.visibility = "hidden";
    document.getElementById("addField").style.visibility = "visible";
    document.getElementById("adddata").style.visibility = "visible";

}

function sendNewField() {
    let newField = document.getElementById("field").value
console.log(newField)
    var http = new XMLHttpRequest();
    var url = domainName;
    var params = 'Type=newField';
    http.open('POST', url, true);

//Send the proper header information along with the request
    http.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');

    http.onreadystatechange = function() {//Call a function when the state changes.
        if(http.readyState == 4 && http.status == 200) {
            alert(http.responseText);
        }
    }
    http.send(params);
}
//onclick Update button
function displayUpdate() {
    clearInformation();
}

//onclick login button
function displayLogin() {
    if( loggedin === true){
        document.getElementById("info").innerHTML = "You Already Logged in";

        return;
    } else
    document.getElementById("loginDetails").style.visibility = "visible";
}

//onclick login button submitting id password
function checkLogin() {
   let id = document.getElementById("id").value
    let password = document.getElementById("password").value

    if (id === "aaa" && password === "bbb"){
        document.getElementById("info").innerHTML = "Login Successful!";
        document.getElementById("loginDetails").style.visibility = "hidden";
        loggedin = true;
    }else {
        document.getElementById("info").innerHTML = "ID or Password is not valid";
        document.getElementById("id").value = ""
        document.getElementById("password").value = ""
    }

}

function getTypes() {

if( loggedin === false){
    document.getElementById("info").innerHTML = "Please Login First";

    return;
}
    let url2 = domainName + "/types";

    //Fetch the content of the url using the XMLHttpRequest object
    let req2 = new XMLHttpRequest();
    req2.open("GET", url2);
    req2.send(null);
    document.getElementById("details").style.visibility = "visible";
    document.getElementById("info").innerHTML = "Please select a category";
    //register an event handler function
    req2.onreadystatechange = function () {
        if (req2.readyState === 4 && req2.status === 200) {
            let response = req2.responseText;
            const types = JSON.parse(response);

            for (let i = 0; i < types.length; i++) {
                CreateSelectDropDown(types[i])

            }
        }
    }
}

function CreateSelectDropDown(type) {
    let dropdownOptions;
    let item;

    dropdownOptions = document.createElement("option");
    dropdownOptions.setAttribute("value", type);
    item = document.createTextNode(type);
    dropdownOptions.appendChild(item);
    document.getElementById("categoryDropDown").appendChild(dropdownOptions);
}

function getDatabyType(type) {

    let url1 = domainName + "/data?type=" + type;

    //Fetch the content of the url using the XMLHttpRequest object
    let req1 = new XMLHttpRequest();
    req1.open("GET", url1);
    req1.send(null);

    //register an event handler function
    req1.onreadystatechange = function () {
        if (req1.readyState === 4 && req1.status === 200) {
            let response = req1.responseText;
            let listOfSecureDetails = JSON.parse(response);

            for (let i = 0; i < listOfSecureDetails.length; i++) {

                let key = listOfSecureDetails[i].key;
                let value = listOfSecureDetails[i].value;
                addToDataTable(key,value);
            }
        }
    }
}

function addToDataTable(key, value) {

    const table = document.getElementById("dataTable");

    const row = table.insertRow();
    const cell1 = row.insertCell();
    const cell2 = row.insertCell();
    cell1.innerHTML = key;
    cell2.innerHTML = value;
}

function sendNewData() {
    let newKey = document.getElementById("newkey").value
    console.log(newKey)
    let newVal = document.getElementById("newvalue").value
    console.log(newVal)
    var http = new XMLHttpRequest();
    var url = domainName;
    var params = 'Key=newkey&value=newVal';
    http.open('POST', url, true);

//Send the proper header information along with the request
    http.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');

    http.onreadystatechange = function() {//Call a function when the state changes.
        if(http.readyState == 4 && http.status == 200) {
            alert(http.responseText);
        }
    }
    http.send(params);
}
function showDetails() {
    let category = document.getElementById("categoryDropDown");
    let selectedOption = category.options[category.selectedIndex].value;
    clearInformation();
    document.getElementById("infoMessage").innerHTML = "<br>";

    const table = document.getElementById("dataTable");

    const header = table.createTHead();
    header.innerHTML = selectedOption;

    getDatabyType(selectedOption);


}

function clearInformation() {

    //document.getElementById("categoryDropDown").selectedIndex = "0";
    const Table = document.getElementById("dataTable");
    Table.innerHTML = "";
}
