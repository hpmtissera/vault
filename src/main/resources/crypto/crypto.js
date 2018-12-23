function encryptText() {
    let textToEncrypt = document.getElementById("textToEncrypt").innerText;
    encryptString(textToEncrypt);

}

function decryptText() {
    let textToDecrypt = document.getElementById("textToDecrypt").innerText;
    decryptString(textToDecrypt);
}

function encryptString(text) {

    var mode = 'AES-GCM',
        length = 256,
        ivLength = 12;

    encrypt(text, 'password', mode, length, ivLength).then(encrypted => {
        console.log(encrypted); // { cipherText: ArrayBuffer, iv: Uint8Array }

        let cipherText = encrypted.cipherText;
        let initVecotr = encrypted.iv;

        let initVecotorStr = uint8ToBase64(initVecotr);
        let initVecotrUint8 = base64ToUint8(initVecotorStr);

        let ciperTextString = arrayBufferToBase64(cipherText);
        let ciperTextArrayBuf = base64ToArrayBuffer(ciperTextString);

        console.log("init vector string : ", initVecotorStr);
        console.log("init vector array from string : ", initVecotrUint8);
        console.log("cipher text string : ", ciperTextString);
        console.log("array buffer from string : ", ciperTextArrayBuf);

        document.getElementById("encryptedText").innerText = initVecotorStr + "." + ciperTextString;

    });

}

function decryptString(text) {

    var mode = 'AES-GCM',
        length = 256,
        ivLength = 12;

    let initVectorAndCiperText = text.split(".");
    console.log(initVectorAndCiperText);

    let ciperTextString = initVectorAndCiperText[0];
    let initVecotorStr = initVectorAndCiperText[1];

    mydecrypt(ciperTextString, initVecotorStr, 'password', mode, length).then(decrypted => {
        console.log(decrypted)
    });

}

function test1(text) {

    var mode = 'AES-GCM',
        length = 256,
        ivLength = 12;

    encrypt(text, 'password', mode, length, ivLength).then(encrypted => {
        console.log(encrypted); // { cipherText: ArrayBuffer, iv: Uint8Array }

        let cipherText = encrypted.cipherText;
        let initVecotr = encrypted.iv;

        let initVecotorStr = uint8ToBase64(initVecotr);
        let initVecotrUint8 = base64ToUint8(initVecotorStr);

        let ciperTextString = arrayBufferToBase64(cipherText);
        let ciperTextArrayBuf = base64ToArrayBuffer(ciperTextString);

        console.log("init vector string : ", initVecotorStr);
        console.log("init vector array from string : ", initVecotrUint8);
        console.log("cipher text string : ", ciperTextString);
        console.log("array buffer from string : ", ciperTextArrayBuf);

        mydecrypt(ciperTextString, initVecotorStr, 'password', mode, length).then(decrypted => {
            console.log(decrypted)
        });

    });

}


// function decryptText() {
//
//     let textToDecrypt = document.getElementById("encryptedText").innerText;
//     let ivBase64 = document.getElementById("iv").innerText;
//
//     let arrayBufferToDecrypt = str2ab(textToDecrypt);
//     let initVector = base64ToArrayBuffer(ivBase64);
//
//     console.log("array buffer : ", arrayBufferToDecrypt);
//     console.log("init vecotr : ", initVector);
//
//     mydecrypt(arrayBufferToDecrypt, initVector, 'password', 'AES-GCM', 256).then(decrypted => {
//         console.log("decrypted text : ", decrypted);
//     })
//
// }

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
    var raw = atob(base64);
    var rawLength = raw.length;
    var array = new Uint8Array(new ArrayBuffer(rawLength));

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

// Returns if browser supports the crypto api
function supportsCrypto() {
    document.getElementById("supportCrypto").innerText = "Support Crypto : " + Boolean(window.crypto && crypto.subtle && window.TextEncoder).toString();
}


// Generate key from password
async function genEncryptionKey(password, mode, length) {
    var algo = {
        name: 'PBKDF2',
        hash: 'SHA-256',
        salt: new TextEncoder().encode('a-unique-salt'),
        iterations: 1000
    };
    var derived = {name: mode, length: length};
    var encoded = new TextEncoder().encode(password);
    var key = await crypto.subtle.importKey('raw', encoded, {name: 'PBKDF2'}, false, ['deriveKey']);

    return crypto.subtle.deriveKey(algo, key, derived, false, ['encrypt', 'decrypt']);
}

// Encrypt function
async function encrypt(text, password, mode, length, ivLength) {
    var algo = {
        name: mode,
        length: length,
        iv: crypto.getRandomValues(new Uint8Array(ivLength))
    };
    var key = await genEncryptionKey(password, mode, length);
    var encoded = new TextEncoder().encode(text);

    return {
        cipherText: await crypto.subtle.encrypt(algo, key, encoded),
        iv: algo.iv
    };
}

async function decrypt(encrypted, password, mode, length) {
    var algo = {
        name: mode,
        length: length,
        iv: encrypted.iv
    };
    var key = await genEncryptionKey(password, mode, length);
    var decrypted = await crypto.subtle.decrypt(algo, key, encrypted.cipherText);

    return new TextDecoder().decode(decrypted);
}

// Decrypt function
async function mydecrypt(encryptedCiperText, initVector, password, mode, length) {
    var algo = {
        name: mode,
        length: length,
        iv: base64ToUint8(initVector)
    };
    var key = await genEncryptionKey(password, mode, length);
    var decrypted = await crypto.subtle.decrypt(algo, key, base64ToArrayBuffer(encryptedCiperText));

    return new TextDecoder().decode(decrypted);
}

