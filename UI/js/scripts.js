var author = "User";
var textBox = $('#textbox');

function myText(s) {
    var pre = document.createElement('pre');
    var text = document.createTextNode(s);
    pre.appendChild(text);
    return pre.innerHTML;
}

function newMessage(text, author, edit, time, del, me) {
    return {
        message: text,
        auth: author,
        isEdit: !!edit,
        timestamp: time,
        id: newId(),
        isDelited: del,
        isMine: !!me
    };
}

function formatDate(date) {
    var minutes = date.getMinutes();
    if (minutes < 10) minutes = '0' + minutes;

    var hh = date.getHours();
    if (hh < 10) hh = '0' + hh;

    var dd = date.getDate();
    if (dd < 10) dd = '0' + dd;

    var mm = date.getMonth() + 1;
    if (mm < 10) mm = '0' + mm;

    var yy = date.getFullYear();
    return dd + '.' + mm + '.' + yy + ' ' + hh + ':' + minutes;
}

messageContainer = [];

function newId() {
    var date = Date.now();
    var random = Math.random() * Math.random();
    return Math.floor(date * random);
}

function send() {
    var m = myText($('#mess').val());
    var date = new Date();
    $('#mess').val("");
    var mess = newMessage(m, author, false, formatDate(date), false, true);
    renderMessage(mess);
    messageContainer.push(mess);
    textBox.scrollTop(textBox[0].scrollHeight);
}

function changeName() {
    var name = myText($('#user').val());
    $('#user').val("");
    if (name.length > 24) {
        swal({
            title: "Error! Enter another name ",
            html: true,
            animation: "pop",
            type: "error"
        })
    }
    else {
        author = name;
        $('#me').html(author);
        saveAuthor();
    }
}

function render(container) {
    for (var i = 0; i < container.length; i++) {
        renderMessage(container[i]);
    }
}

function renderMessage(mess) {
    var container = document.getElementsByClassName('textbox')[0];
    if (mess.isMine) {
        var element = myElementFromTemplate();
        renderMyMessageAtributs(element, mess);
    }
    else {
        var element = elementFromTemplate();
        renderMessageAtributes(element, mess);
    }
    container.appendChild(element);
}

function myElementFromTemplate() {
    var template = document.getElementById("my-message-template");
    return template.firstElementChild.cloneNode(true);
}

function elementFromTemplate() {
    var template = document.getElementById("others-message-template");
    return template.firstElementChild.cloneNode(true);
}

function renderMessageAtributes(element, mess) {
    element.setAttribute('data-message-id', mess.id);
    element.lastChild.previousSibling.textContent = mess.message;
    if (mess.isEdit) {
        element.firstChild.nextSibling.textContent = mess.auth + ", " + mess.timestamp + ", Edited";
    }
    else element.firstChild.nextSibling.textContent = mess.auth + ", " + mess.timestamp;
    if (mess.isDelited) {
        element.lastChild.previousSibling.textContent = "This message was deleted.";
        element.lastChild.previousSibling.style.backgroundColor = "#4f324b";
    }
}

function renderMyMessageAtributs(element, mess) {
    element.setAttribute('data-message-id', mess.id);
    element.lastChild.previousSibling.textContent = mess.message;
    if (mess.isEdit) {
        element.firstChild.nextSibling.firstChild.nextSibling.textContent = mess.auth + ", " + mess.timestamp + ", Edited";
    }
    else  element.firstChild.nextSibling.firstChild.nextSibling.textContent = mess.auth + ", " + mess.timestamp;
    if (mess.isDelited) {
        element.lastChild.previousSibling.style.backgroundColor = "#ff6582";
        element.firstChild.nextSibling.lastChild.previousSibling.remove();
        element.lastChild.previousSibling.textContent = "This message was deleted.";
    }
}

function renderAuthor(){
    $('#me').html(author);
}

function saveMessage(listToSave) {
    if (typeof(Storage) == "undefined") {
        swal('localStorage is not accessible');
        return;
    }

    localStorage.setItem("Message history", JSON.stringify(listToSave));
}

function loadMessages() {
    if (typeof(Storage) == "undefined") {
        swal('localStorage is not accessible');
        return;
    }

    var item = localStorage.getItem("Message history");

    return item && JSON.parse(item);
}

function saveAuthor(){
    if (typeof(Storage) == "undefined") {
        swal('localStorage is not accessible');
        return;
    }
    localStorage.setItem("Author",author);
}

function loadAuthor(){
    if (typeof(Storage) == "undefined") {
        swal('localStorage is not accessible');
        return;
    }
    var item = localStorage.getItem("Author");
    return item;
}

$(function () {

    messageContainer = loadMessages() || [
            newMessage("Hi,Welcome to the Oracle Help Center. Whether you are new to Oracle or anadvanced user, you can find useful information about our products and services, ranging from gettingstarted guides to advanced features.", "Anton", false, "21: 16: 12", false, false),
            newMessage("Hello", "User", false, "21: 32: 15", false, true),
            newMessage("Ok?", "Anton", true, "22: 35: 12", true, false)
        ];

    author = loadAuthor() || "User";
    renderAuthor();

    for (var i = 0; i < messageContainer.length; i++) {
        renderMessage(messageContainer[i]);
    }

    $('#message-form').submit(function (e) {
        e.preventDefault();
        send();
        saveMessage(messageContainer);
    });

    $('#user-form').submit(function (e) {
        e.preventDefault();
        changeName();
    });

    $(document).on('click', '.del', function () {
        var messageId = $(this).parent().parent().parent().attr('data-message-id');
        for (var i = 0; i < messageContainer.length; i++) {
            if (messageContainer[i].id == messageId) {
                messageContainer[i].isDelited = !messageContainer[i].isDelited;
            }
        }
        saveMessage(messageContainer);
        var message = $(this).parent().parent().next();
        $(this).prev().remove();
        $(this).remove();
        message.html("This message was deleted.");
        message.css('background', ' #ff6582');
    });

    $(document).on('click', '.edit', function () {
        var myMessage = $(this).parent().parent();
        var messageId = $(this).parent().parent().parent().attr('data-message-id');
        var messgageInfo = $(this).parent().prev();
        var message = myMessage.next();
        var messageText = message.html();
        swal({
                title: "Correct message",
                text: "<textarea id='messageField' style='width:100%;resize:none; background-color: #ffffff; border: 2px solid #ffffff;" +
                "border-radius: 4px;  color: #26282b; ' rows='2'>" + messageText + "</textarea>",
                html: true,
                showCancelButton: true,
                closeOnConfirm: false,
                animation: "pop"
            },
            function () {
                var messageValue = $('#messageField').val();
                if (messageValue === false) return false;
                if (messageValue === "") {
                    swal.showInputError("Empty message!");
                    return false;
                }
                if (messageValue === messageText) {
                    swal.close();
                    return false;
                }
                for (var i = 0; i < messageContainer.length; i++) {
                    if (messageContainer[i].id == messageId) {
                        if (!messageContainer[i].isEdit) {
                            messageContainer[i].isEdit = !messageContainer[i].isEdit;
                            messageContainer[i].message = messageValue;
                            messgageInfo.append(", Edited");
                        }
                    }
                }
                message.html(myText(messageValue));
                saveMessage(messageContainer);
                swal.close();
            }
        );
    });
});
