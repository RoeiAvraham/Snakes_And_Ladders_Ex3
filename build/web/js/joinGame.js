/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var refreshRate = 2000; //miliseconds

function ajaxGameList() {
    $.ajax({
        url: "gamelist",
        success: function (games) {
            refreshGamesList(games.fullGames);
        }
    });
}

function refreshGamesList(fullGames) {
    var selected_item = $("#gameslist").val();

    $("#gameslist").empty();
    $.each(fullGames /*|| []*/, function (index, game) {
        if (!(game)) // Game is available to join
        {
            $('<option>' + index + '</option>').appendTo($("#gameslist"));
        }
        else
        {
            $('<option>' + index + '    -   Full game</option>').attr('disabled','disabled').appendTo($("#gameslist"));
        }
    });
    $("#gameslist").val(selected_item);
}

function bindSelectedGame() {

    $("#gameslist").dblclick(function () {
        $.ajax({
            url: "joingame",
            data: "gameName=" + $('#gameslist :selected')[0].value,
            timeout: 2000,
            error: function () {
                console.error("Server unavailable or timeout");
            },
            success: function (r) {
                if (r.isXmlGameAndIsReady) // Game selected is a XML Game.
                {
                    $("body").append($("<br></br>"));
                    createDropDownPlayerName(r);
                    createSubmitButton();
                }
                else // Game selected is a regular game.
                {
                    $("body").append($("<br></br>"));
                    createPlayerNameTextField(r);
                    createSubmitButton();
                }
            }
        });

    });
    return false;
}

$(function () 
{
    $.ajaxSetup({cache: false});
    setInterval(ajaxGameList, refreshRate);
    bindSelectedGame();
});