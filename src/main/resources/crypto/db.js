let db;

let openRequest = indexedDB.deleteDatabase('key_db');

openRequest = indexedDB.open('key_db', 1);

openRequest.onupgradeneeded = function (e) {
    var db = e.target.result;
    console.log('running onupgradeneeded');
    if (!db.objectStoreNames.contains('keystore')) {
        var storeOS = db.createObjectStore('keystore');
    }
};
openRequest.onsuccess = function (e) {
    console.log('running onsuccess');
    db = e.target.result;
};

openRequest.onerror = function (e) {
    console.log('onerror!');
    console.dir(e);
};

function addKey(userName, key) {
    const transaction = db.transaction(['keystore'], 'readwrite');
    const store = transaction.objectStore('keystore');
    const request = store.add(key, "admin");

    request.onerror = function (e) {
        console.log('Error', e.target.error.name);
    };
    request.onsuccess = function (e) {
        console.log('Woot! Did it');
    };

}

function removeKey(userName) {
    const transaction = db.transaction(['keystore'], 'readwrite');
    const store = transaction.objectStore('keystore');
    const request = store.delete("admin");

    request.onerror = function (e) {
        console.log('Error', e.target.error.name);
    };
    request.onsuccess = function (e) {
        console.log('Deleted.');
    };
}

function getKey(userName, callback) {
    const transaction = db.transaction(['keystore'], 'readwrite');
    const store = transaction.objectStore('keystore');
    const request = store.get("admin");

    request.onerror = function (e) {
        console.log('Error', e.target.error.name);
    };
    request.onsuccess = function (e) {
        console.log("Get object", request.result);
        callback(request.result);
    };
}

