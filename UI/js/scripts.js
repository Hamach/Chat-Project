var author = "User";
var textBox = $('#textbox');

function myText(s) {
    var pre = document.createElement('pre');
    var text = document.createTextNode(s);
    pre.appendChild(text);
    return pre.innerHTML;
}

function send() {
    var message = '<h3 class="bubble-you text-style">' + myText($('#mess').val()) + '</h3></div>';
    var date = new Date();
    var buttons = '<a href="#" class="edit" title="Edit message"><i class="fa fa-pencil"></i></a> ' +
        '<a href="#" class="del" title="Remove message"><i class="fa fa-trash"></i></a>';
    var authorInfo = '<div class="text-style-author myText">' + author + ', ' + date.toLocaleString() + ' ' + buttons + '</div>';
    $('#mess').val("");
    $('#textbox').append(
        '<div class="message">' + authorInfo + message + '</div>'
    );
    textBox.scrollTop(textBox[0].scrollHeight);
}

function changeName(){
    var name=myText($('#user').val());
    $('#user').val("");
    author=name;
    $('#me').html(author);
}

$(function () {
    $('#message-form').submit(function (e) {
        e.preventDefault();
        send();
    });

    $('#user-form').submit(function (e) {
        e.preventDefault();
        changeName();
    });

    $(document).on('click', '.del', function () {
        var message = $(this).parent().next();
        $(this).prev().remove();
        $(this).remove();
        message.html("This message was deleted.");
        message.addClass("deleted");
    });

    $(document).on('click', '.edit', function () {
        var myMessage = $(this).parent();
        var message = myMessage.next();
        var messageText = message.html();
        swal({
                title: "Correct message",
                text: "<textarea id='messageField' style='width:100%;resize:none; background-color: #ffffff; border: 2px solid #ffffff;" +
                "border-radius: 4px;  color: #26282b; ' rows='2'>" + messageText + "</textarea>",
                html: true,
                showCancelButton: true,
                closeOnConfirm: false,
                animation: "slide-from-bottom"
            },
            function () {
                var messageValue = $('#messageField').val();
                if (messageValue === false) return false;
                if (messageValue === "") {
                    swal.showInputError("Write something please!");
                    return false;
                }
                if (messageValue === messageText) {
                    swal.close();
                    return false;
                }
                message.html(myText(messageValue));
                var messageInfo = myMessage.children();
                var editInfo = "Edited  " + new Date().toLocaleString();
                messageInfo.parent().append('<div>' + editInfo + '</div>');
                swal.close();
            }
        );
    });
});
