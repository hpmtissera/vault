const domainName = "https://fandoco-vault.herokuapp.com";
var loggedin = false;
var source;

//onclick Add button
function addData() {
    source = "addData";
    if (loggedin === false) {
        document.getElementById("details").innerHTML = document.getElementById("loginform").innerHTML;

        return;
    } else {
        document.getElementById("details").innerHTML = document.getElementById("addtypeform").innerHTML;
        populateTypesDropdown("categoryDropDownAdd");

    }
}

function addEntry() {
    {
        let newKey = document.getElementById("addKey").value;
        let newVal = document.getElementById("addVal").value;
        let type = document.getElementById("categoryDropDownAdd");
        let selectedType = type.options[type.selectedIndex].value;
        let url = domainName + "/data";
        let body = "{\"type\" : \"" +  selectedType + "\",\"key\" : \"" +  newKey + "\",\"value\" : \"" +  newVal + "\"}";

        let postreq = new XMLHttpRequest();
        postreq.open('POST', url, true);
        postreq.setRequestHeader("Authorization", "Basic YWRtaW46cGFzc3dvcmQ=");
        postreq.setRequestHeader('Content-type', 'application/json');
        postreq.send(body);

        postreq.onreadystatechange = function () {//Call a function when the state changes.
            if (postreq.readyState === 4 && postreq.status === 200) {
                document.getElementById("categoryDropDownAdd").innerHTML = "";

                populateTypesDropdown("categoryDropDownAdd");
            }
        };
    }

}

function onAddType() {
    let newField = document.getElementById("addType").value;
    let url = domainName + "/types";
    let body = "{\"name\" : \"" +  newField + "\"}";

    let postreq = new XMLHttpRequest();
    postreq.open('POST', url, true);
    postreq.setRequestHeader("Authorization", "Basic YWRtaW46cGFzc3dvcmQ=");
    postreq.setRequestHeader('Content-type', 'application/json');
    postreq.send(body);

    postreq.onreadystatechange = function () {//Call a function when the state changes.
        if (postreq.readyState === 4 && postreq.status === 200) {
            document.getElementById("categoryDropDownAdd").innerHTML = "";

            populateTypesDropdown("categoryDropDownAdd");
        }
    };
}

//onclick Update button
function displayUpdate() {

}

//onclick login button
function displayLogin() {
    if (loggedin === true) {
        return;
    } else {
        document.getElementById("details").innerHTML = document.getElementById("loginform").innerHTML;
    }

}

function displayLogout() {
    loggedin = false;
    document.getElementById("details").innerHTML = document.getElementById("loginform").innerHTML;
    document.getElementById("nav").innerHTML = "";

}

//onclick login button submitting id password
function checkLogin() {

    let id = document.getElementById("id").value;
    let password = document.getElementById("password").value;

    if (id === "aaa" && password === "bbb") {
        document.getElementById("details").innerHTML = "";
        loggedin = true;

        if (source === "getTypes") {
            getTypes()
        }
        if (source === "addData") {
            addData()
        }
        document.getElementById("nav").innerHTML = document.getElementById("sidemenuArea").innerHTML;
    } else {
        document.getElementById("info").innerHTML = "ID or Password is not valid";
        document.getElementById("id").value = "";
        document.getElementById("password").value = "";
    }

}

function populateTypesDropdown(id) {
    let url2 = domainName + "/types";

    //Fetch the content of the url using the XMLHttpRequest object
    let req2 = new XMLHttpRequest();
    req2.open("GET", url2);
    req2.setRequestHeader("Authorization", "Basic YWRtaW46cGFzc3dvcmQ=");
    req2.send(null);

    //register an event handler function
    req2.onreadystatechange = function () {
        if (req2.readyState === 4 && req2.status === 200) {
            let response = req2.responseText;
            const types = JSON.parse(response);

            for (let i = 0; i < types.length; i++) {
                CreateSelectDropDown(types[i], id)

            }
        }
    }
}

function getTypes() {
    source = "getTypes";
    if (loggedin === false) {
        document.getElementById("details").innerHTML = document.getElementById("loginform").innerHTML;

        return;
    }
    document.getElementById("details").innerHTML = document.getElementById("typesDropdown").innerHTML;

    populateTypesDropdown("categoryDropDown");
}

function CreateSelectDropDown(type,id) {
    let dropdownOptions;
    let item;

    dropdownOptions = document.createElement("option");
    dropdownOptions.setAttribute("value", type);
    item = document.createTextNode(type);
    dropdownOptions.appendChild(item);
    document.getElementById(id).appendChild(dropdownOptions);
}

function getDatabyType(type) {
    if (loggedin === false) {
        document.getElementById("details").innerHTML = document.getElementById("loginform").innerHTML;

        return;
    }
    let url1 = domainName + "/data?type=" + type;

    //Fetch the content of the url using the XMLHttpRequest object
    let req1 = new XMLHttpRequest();
    req1.open("GET", url1);
    req1.setRequestHeader("Authorization", "Basic YWRtaW46cGFzc3dvcmQ=");

    req1.send(null);

    //register an event handler function
    req1.onreadystatechange = function () {
        if (req1.readyState === 4 && req1.status === 200) {
            let response = req1.responseText;
            let listOfSecureDetails = JSON.parse(response);

            for (let i = 0; i < listOfSecureDetails.length; i++) {

                let key = listOfSecureDetails[i].key;
                let value = listOfSecureDetails[i].value;
                addToDataTable(key, value);
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

function showDetails() {
    let category = document.getElementById("categoryDropDown");
    let selectedOption = category.options[category.selectedIndex].value;

    let Table = document.getElementById("dataTable");
    Table.innerHTML = "";
    Table = document.getElementById("dataTable");

    const header = Table.createTHead();
    header.innerHTML = selectedOption;

    getDatabyType(selectedOption);


}

