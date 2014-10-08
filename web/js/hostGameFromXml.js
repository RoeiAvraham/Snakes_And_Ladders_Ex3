/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$(document).ready(function () {
    setUploadButtonBinding();
    setUploadListener();
});

function validate()
{
    if ($("#error").length)
    {
        $('#error').fadeOut("slow", function () {
            $(this).remove();
        });
    }
}
function setUploadButtonBinding()
{
    $('#file').change(
            function () {
                if ($(this).val()) {
                    $('#upload').attr('disabled', false);
                }
            });
}

function setUploadListener()
{
    $("#loadGameForm").submit(function (e) {
        e.preventDefault();
        $.ajax({
            url: 'newxmlgame',
            data: new FormData($(this)[0]),
            cache: false,
            contentType: false,
            processData: false,
            type: 'POST',
            timeout: 2000,
            error: function() {
                console.error("Server unavailable or timeout");
            },
            success: function (r) {
                if (r.isXmlGameAndIsReady)
                {
                    createDropDownPlayerName(r);
                    createSubmitButton();
                    $("#upload").attr("disabled", "disabled");
                    $("#file").attr("disabled", "disabled");
                    validate();
                    ajaxSubmitStartButton();
                }
                else
                {
                    if (r.data === "game.html") // redirect
                    {
                        window.location.href = r.data;
                    }
                    displayErrorMessage($("#error").length, r.data);
                }
            }
        }
        );
    });
    return false;
}

function ajaxSubmitStartButton() {
    //add a function to the submit event
    $("#playerNameForm").submit(function() {
        jQuery.ajax({
            data: $(this).serialize(),
            url: this.action,
            timeout: 2000,
            error: function() {
                console.error("Server unavailable or timeout");
            },
            success: function(r) {
                window.location.href = r;
            }
        });
        return false;
    });
}

function displayErrorMessage(isErrorPresent, data)
{
    if (!isErrorPresent)
    {
        var errorMessage = $('<div></div>').attr("id", "error").attr("class", "alert alert-danger text-center center-block").attr("role", "alert");
        if (data === "gameName_error")
        {
            errorMessage.append("Game name provided in XML File already exists, choose another file or change game name.");
        }
        else if (data === "xmlFileError")
        {
            errorMessage.append("There is a problem with parsing the XML File, Try again later.");
        }
        else if (data === "xmlInvalidError")
        {
            errorMessage.append("XML File provided is invalid. Correct it or choose another XML File.");
        }
        errorMessage.hide().fadeIn("slow");
        $("body").append(errorMessage);
    }
    else
    {
        if (data === "gameName_error")
        {
            $("#error").html("Game name provided in XML File already exists, choose another file or change game name.");
        }
        else if (data === "xmlFileError")
        {
            $("#error").html("There is a problem with parsing the XML File, Try again later.");
        }
        else if (data === "xmlInvalidError")
        {
            $("#error").html("XML File provided is invalid. Correct it or choose another XML File.");
        }
        $("#error").hide().fadeIn("slow");
    }
}