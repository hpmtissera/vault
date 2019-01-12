function uploadImage() {
    var file = document.querySelector('input[type=file]').files[0]; //sames as here
    var reader = new FileReader();

    var canvas = document.getElementById('imgCanvas');
    var context = canvas.getContext('2d');

    reader.onloadend = function () {
        var img = new Image();
        img.onload = function () {
            canvas.width = 200;
            canvas.height = 200;
            context.drawImage(img, 0, 0, img.width, img.height,     // source rectangle
                0, 0, canvas.width, canvas.height); // destination rectangle
        };
        img.src = event.target.result;
    };

    if (file) {
        reader.readAsDataURL(file); //reads the data as a URL
    } else {
    }

}

function encodeImage() {
    var canvas = document.getElementById('imgCanvas');
    document.getElementById("output").innerText = canvas.toDataURL();
}

function decodeImage() {
    let canvas = document.getElementById('imgCanvas');
    let context = canvas.getContext('2d');

    let encodedImage = document.getElementById("encodedImage").value;

    let image = new Image();
    image.onload = function () {
        canvas.width = 200;
        canvas.height = 200;
        context.drawImage(image, 0, 0, image.width, image.height,     // source rectangle
            0, 0, canvas.width, canvas.height); // destination rectangle
    };
    image.src = encodedImage;
}