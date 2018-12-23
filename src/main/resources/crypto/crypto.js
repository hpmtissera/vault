function supportsCrypto() {
    document.getElementById("supportCrypto").innerText = "Support Crypto : " + Boolean(window.crypto && crypto.subtle && window.TextEncoder).toString();

    if (!window.crypto || !window.crypto.subtle) {
        alert("Your current browser does not support the Web Cryptography API! This page will not work.");
        return;
    }
    if (!window.indexedDB) {
        alert("Your current browser does not support IndexedDB. This page will not work.");
    }

}


function encryptText() {
    const textToEncrypt = document.getElementById("textToEncrypt").value;
    encryptString(textToEncrypt);
}

function decryptText() {
    const textToDecrypt = document.getElementById("textToDecrypt").value;
    decryptString(textToDecrypt);
}

function encryptString(text) { // base64InitVector.base64CipherText
    const mode = 'AES-GCM',
        length = 256,
        ivLength = 12;

    encrypt(text, mode, length, ivLength, encrypted => {
        console.log(encrypted); // { cipherText: ArrayBuffer, iv: Uint8Array }

        const cipherText = encrypted.cipherText;
        const initVector = encrypted.iv;

        const initVectorStr = uint8ToBase64(initVector);
        const cipherTextString = arrayBufferToBase64(cipherText);

        document.getElementById("encryptedText").innerText = initVectorStr + "." + cipherTextString;
    });

}

function decryptString(text) {
    const mode = 'AES-GCM',
        length = 256,
        ivLength = 12;

    const initVectorAndCipherText = text.split(".");
    const initVectorStr = initVectorAndCipherText[0];
    const cipherTextString = initVectorAndCipherText[1];

    decrypt(cipherTextString, initVectorStr, mode, length, decrypted => {
        console.log(decrypted);
        document.getElementById("decryptedText").innerText = decrypted;
    });
}

function ab2str(buf) {
    return String.fromCharCode.apply(null, new Uint8Array(buf));
}

function str2ab(str) {
    return Uint8Array.from([...str].map(ch => ch.charCodeAt())).buffer;
}

function base64ToArrayBuffer(base64) {
    return base64ToUint8(base64).buffer;
}

function base64ToUint8(base64) {
    const raw = atob(base64);
    const rawLength = raw.length;
    const array = new Uint8Array(new ArrayBuffer(rawLength));

    for (i = 0; i < rawLength; i++) {
        array[i] = raw.charCodeAt(i);
    }
    return array;
}

function arrayBufferToBase64(buf) {
    return uint8ToBase64(new Uint8Array(buf));
}

function uint8ToBase64(uint8) {
    return btoa(uint8.reduce((s, b) => s + String.fromCharCode(b), ''));
}

// Generate key from password
async function genEncryptionKey(password, mode, length) {
    const algo = {
        name: 'PBKDF2',
        hash: 'SHA-256',
        salt: new TextEncoder().encode('a-unique-salt'),
        iterations: 1000
    };
    const derived = {name: mode, length: length};
    const encoded = new TextEncoder().encode(password);
    const key = await crypto.subtle.importKey('raw', encoded, {name: 'PBKDF2'}, false, ['deriveKey']);
    return crypto.subtle.deriveKey(algo, key, derived, true, ['encrypt', 'decrypt']);
}

function generateKey() {
    const algo = {
        name: 'AES-GCM',
        length: 256,
        iv: crypto.getRandomValues(new Uint8Array(12))
    };

    const password = new TextEncoder().encode(crypto.getRandomValues(new Int8Array(128)));

    genEncryptionKey(password, algo.name, algo.length).then(key => {
        window.crypto.subtle.exportKey(
            "raw", //can be "jwk" or "raw"
            key //extractable must be true
        ).then(function (keydata) {
            //returns the exported key data
            console.log(keydata);

            let keyString = arrayBufferToBase64(keydata);
            console.log("Key string : ", keyString);

            document.getElementById("privateKey").innerText = keyString;

            console.log("array buf : ", base64ToArrayBuffer(keyString));

            importKey(keyString);

        }).catch(function (err) {
            console.error(err);
        });
    });

}

function importKeyString() {
    const keyStr = document.getElementById("keyToUse").value;
    importKey(keyStr);
}

function importKey(keyString) {
    let userName = "admin";
    const keydata = base64ToArrayBuffer(keyString);
    crypto.subtle.importKey("raw", keydata, {name: 'AES-GCM'}, false, ['encrypt', 'decrypt']).then(result => {
        console.log("result", result);
        removeKey(userName);
        addKey(userName, result);
        getKey(userName, result => {
            console.log("callback called", result);
        });

    });
}

// Encrypt function
function encrypt(text, mode, length, ivLength, callback) {
    const algo = {
        name: mode,
        length: length,
        iv: crypto.getRandomValues(new Uint8Array(ivLength))
    };

    const encoded = new TextEncoder().encode(text);

    getKey("admin", key => {
        crypto.subtle.encrypt(algo, key, encoded).then(cipherText => {
            callback({
                cipherText: cipherText,
                iv: algo.iv
            })
        })
    });
}


// Decrypt function
function decrypt(encryptedCipherText, initVector, mode, length, callback) {
    const algo = {
        name: mode,
        length: length,
        iv: base64ToUint8(initVector)
    };

    getKey("admin", key => {
        crypto.subtle.decrypt(algo, key, base64ToArrayBuffer(encryptedCipherText)).then(decrypted => {
            let decoded = new TextDecoder().decode(decrypted);
            callback(decoded)
        })
    });

}