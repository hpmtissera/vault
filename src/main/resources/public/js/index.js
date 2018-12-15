function getTypes() {
    let url2 = "https://fandoco-vault.herokuapp.com/types";

    //Fetch the content of the url using the XMLHttpRequest object
    let req2 = new XMLHttpRequest();
    req2.open("GET", url2);
    req2.send(null);


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

    let url1 = "https://fandoco-vault.herokuapp.com/data?type=" + type;

    //Fetch the content of the url using the XMLHttpRequest object
    let req1 = new XMLHttpRequest();
    req1.open("GET", url1);
    req1.send(null);

    //register an event handler function
    req1.onreadystatechange = function () {
        if (req1.readyState === 4 && req1.status === 200) {
            let response = req1.responseText;
            let listOfSecureDetails = JSON.parse(response);

            let list = "";

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

function showDetails() {
    let category = document.getElementById("categoryDropDown");
    let selectedOption = category.options[category.selectedIndex].value;
    clearInformation();
    const table = document.getElementById("dataTable");

    const header = table.createTHead();
    header.innerHTML = selectedOption;

    getDatabyType(selectedOption);


}

function clearInformation() {

    document.getElementById("categoryDropDown").selectedIndex = "0";
    const Table = document.getElementById("dataTable");
    Table.innerHTML = "";
}
