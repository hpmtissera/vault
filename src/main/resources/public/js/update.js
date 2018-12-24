function displayUpdate() {

    sessionStorage.setItem("source", "update");

    if (localStorage.getItem('token') == null) {
        document.getElementById("details").innerHTML = document.getElementById("loginform").innerHTML;

        return;
    }
    document.getElementById("details").innerHTML = document.getElementById("updateform").innerHTML;
    document.getElementById("typesDropdownUpdateDiv").innerHTML = document.getElementById("typesDropdownCommonComponent").innerHTML;
    document.getElementById("keysDropdownUpdateDiv").innerHTML = document.getElementById("keysDropdownCommonComponent").innerHTML;
    document.getElementById("typesDropDownCommon").onchange = function () {
        getDataToUpdate()
    };


    populateTypesDropdown("typesDropDownCommon");
}

function getDataToUpdate() {

    let category = document.getElementById("typesDropDownCommon");
    let selectedOption = category.options[category.selectedIndex].value;

    getDatabyType(selectedOption, "update", createDropdownKeys);
}

function createDropdownKeys(dataArray) {
    document.getElementById("keysDropDownCommon").innerHTML = "";
    createSelectDropdown("---", "keysDropDownCommon");
    let dataMap = new Map();
    for (let i = 0; i < dataArray.length; i++) {

        let key = dataArray[i].key;
        let value = dataArray[i].value;
        dataMap.set(key, value);
        createSelectDropdown(key, "keysDropDownCommon");
    }

    localStorage.dataMap = JSON.stringify(Array.from(dataMap.entries()));

}

function showValue() {
    let dataMap = new Map(JSON.parse(localStorage.dataMap));
    let key = document.getElementById("keysDropDownCommon");
    let selectedKey = key.options[key.selectedIndex].value;
    let value = dataMap.get(selectedKey);
    document.getElementById("updateVal").value = value;
    console.log(value);
}