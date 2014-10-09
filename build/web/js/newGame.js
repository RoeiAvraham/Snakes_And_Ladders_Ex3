

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function() {
    $('#gameName, #playerName').keyup(validate);
    bindSelects();
    ajaxSubmitGameParams();
});

function ajaxSubmitGameParams() {
    //add a function to the submit event
    $("#gameForm").submit(function() {
        jQuery.ajax({
            data: $(this).serialize(),
            url: this.action,
            timeout: 2000,
            error: function() {
                console.error("Server unavailable or timeout");
            },
            success: function(r) {
                if (r.wasGameCreated)
                {
                    window.location.href = r.data;
                } else {
                    if (!$("#error").length) {
                        $("#mainDiv").append($('<div id="error" class="alert alert-danger text-center center-block" role="alert">\n\
                        There is already a game with this name, please try another one. </div>').hide().fadeIn("slow"));
                    }
                }
            }
        });
        return false;
    });
}

function validate()
{
    if ($('#gameName').val().length > 0 &&
            $('#playerName').val().length > 0)
    {
        $("#createBtn").prop("disabled", false);
    }
    else {
        $("#createBtn").prop("disabled", true);
    }

    if ($("#error").length)
    {
        $('#error').fadeOut("slow",function() {
            $(this).remove();
        });
    }
}

  
function bindSelects()
{
    bindNumberOfSnakesSelect();
    bindNumberOfHumanPlayers();
}

function bindNumberOfSnakesSelect()
{
    $("#boardSize").change(function() {
        var boardSize = $(this).val();
        var maxNumSnakes = boardSize * boardSize / 4;

        $("#numSnakes").empty();
        var list = '';
        for (var i = 0; i <= maxNumSnakes; i++)
        {
            list += "<option value='" + i + "'>" + i + "</option>";
        }
        $("#numSnakes").html(list);
    });
}

function bindNumberOfHumanPlayers()
{
    $("#numTotal").change(function() {
        var numTotal = $(this).val();

        $("#numHuman").empty();
        var list = '';
        for (var i = 1; i <= numTotal; i++)
        {
            list += "<option value='" + i + "'>" + i + "</option>";
        }
        $("#numHuman").html(list);
    });
}