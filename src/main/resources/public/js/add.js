//onclick Add button
function addData() {
    sessionStorage.setItem("source", "addData");
    if (localStorage.getItem('token') == null) {
        document.getElementById("details").innerHTML = document.getElementById("loginform").innerHTML;


    } else {
        document.getElementById("details").innerHTML = document.getElementById("addtypeform").innerHTML;
        document.getElementById("typesDropdownAddDiv").innerHTML = document.getElementById("typesDropdownCommonComponent").innerHTML;

        populateTypesDropdown("typesDropDownCommon");

    }
}

function addEntry() {
    {
        let newKey = document.getElementById("addKey").value;
        let newVal = document.getElementById("addVal").value;
        let type = document.getElementById("typesDropDownCommon");
        let selectedType = type.options[type.selectedIndex].value;
        let url = domainName + "/data";
        let body = "{\"type\" : \"" + selectedType + "\",\"key\" : \"" + newKey + "\",\"value\" : \"" + newVal + "\"}";

        let postreq = new XMLHttpRequest();
        postreq.open('POST', url, true);
        postreq.setRequestHeader("Authorization", localStorage.getItem('token'));
        postreq.setRequestHeader('Content-type', 'application/json');
        postreq.send(body);

        postreq.onreadystatechange = function () {//Call a function when the state changes.
            if (postreq.readyState === 4 && postreq.status === 200) {
                document.getElementById("typesDropDownCommon").innerHTML = "";

                populateTypesDropdown("typesDropDownCommon");
            }
        };
    }

}

function onAddType() {
    let newField = document.getElementById("addType").value;
    let url = domainName + "/types";
    let body = "{\"name\" : \"" + newField + "\"}";

    let postreq = new XMLHttpRequest();
    postreq.open('POST', url, true);
    postreq.setRequestHeader("Authorization", localStorage.getItem('token'));
    postreq.setRequestHeader('Content-type', 'application/json');
    postreq.send(body);

    postreq.onreadystatechange = function () {//Call a function when the state changes.
        if (postreq.readyState === 4 && postreq.status === 200) {
            document.getElementById("typesDropDownCommon").innerHTML = "";

            populateTypesDropdown("typesDropDownCommon");
        }
    };
}