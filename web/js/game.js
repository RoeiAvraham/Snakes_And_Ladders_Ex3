/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var refreshRate = 2000; //miliseconds
var CELL_WIDTH = 86;
function getGameInfo()
{
    $.ajax({
        url: "gameinfo",
        type: "GET",
        contentType: "application/json",
        dataType: "json",
        timeout: 2000,
        success: function(r) {
            document.title = 'Snakes And Ladders - ' + r.gameName.toString();
            drawBoard(r.boardSize);
            placeSnakesAndLaddersOnBoard(r.snakeMap, r.ladderMap, r.boardSize);
        }
    });
    return false;
}

function drawBoard(boardSize)
{
//    var tableDiv = document.createElement("div");
//    $(tableDiv).attr("id","tableDiv");
//    $(tableDiv).attr("align","center");
    var table = document.createElement("table");
    $(table).attr("class", "tableDiv");
    for (var i = boardSize - 1; i >= 0; i--)
    {
        var row = document.createElement("tr");
        for (var j = 0; j < boardSize; j++)
        {
            var currCellNum = i * boardSize + j;
            var cell = document.createElement("td");
            $(cell).attr("id", "cell" + (currCellNum + 1));
            var cellText = document.createTextNode(currCellNum + 1);
            cell.appendChild(cellText);
            row.appendChild(cell);
        }
        table.appendChild(row);
    }
    $("#board").prepend(table);
}

function refreshPlayerList(r) {
    $("#playerList").empty();
    $.each(r.playerNames || [], function(index, playerName) {
        $('<option>' + playerName + "   -   " + r.playerTypes[index] + '</option>').appendTo($("#playerList"));
    });
}

//function placeSnakesAndLaddersOnBoard(numLadders)
//{
//
//}

function placeSnakesAndLaddersOnBoard(snakeMap, ladderMap, boardSize)
{
    placeSnakesOnBoard(snakeMap, boardSize);
    placeLaddersOnBOard(ladderMap, boardSize);
}

function placeSnakesOnBoard(snakeMap, boardSize)
{
    $.each(snakeMap, function(index, value) {
        var topY = $("#cell" + value.x).position().top;
        var topX = $("#cell" + value.x).position().left;
        var bottomY = $("#cell" + value.y).position().top;
        var bottomX = $("#cell" + value.y).position().left;

        var width = Math.abs(topX - bottomX);
        var height = Math.abs(topY - bottomY);
        var tmp = value.y + boardSize;
        var nearestMultOfBoardSizeToBottom = tmp - (tmp % boardSize);
        if ((value.x % boardSize) === (value.y % boardSize))
        { //vertical
            $(".snakesandladdersDiv").append('<img  src="images/snakePics/snakeVertical.png" class="snakeOrLadder" id="' + index + '"/>');
            $("#" + index).css("left", topX + CELL_WIDTH / 2).css("top", topY + CELL_WIDTH / 2).css("width", width).css("height", height);
        } else if (value.y % boardSize === 0)
        {           //left 
            $(".snakesandladdersDiv").append('<img  src="images/snakePics/snakeLeft.png" class="snakeOrLadder" id="' + index + '"/>');
            $("#" + index).css("left", topX + CELL_WIDTH / 2).css("top", topY + CELL_WIDTH / 2).css("width", width).css("height", height);
            ;
        } else if (value.x <= nearestMultOfBoardSizeToBottom)
        {         //horizontal
            $(".snakesandladdersDiv").append('<img  src="images/snakePics/snakeHorizontal.png" class="snakeOrLadder" id="' + index + '"/>');
            $("#" + index).css("left", topX + CELL_WIDTH / 2).css("top", topY + CELL_WIDTH / 2).css("width", width).css("height", height);
            ;
        } else if (value.x % boardSize === 0)
        {              //right
            $(".snakesandladdersDiv").append('<img  src="images/snakePics/snakeRight.png" class="snakeOrLadder" id="' + index + '"/>');
            $("#" + index).css("left", topX + CELL_WIDTH / 2).css("top", topY + CELL_WIDTH / 2).css("width", width).css("height", height);
            ;
        } else if ((value.x % boardSize) > (value.y % boardSize))
        {
            $(".snakesandladdersDiv").append('<img  src="images/snakePics/snakeRight.png" class="snakeOrLadder" id="' + index + '"/>');
            $("#" + index).css("left", topX + CELL_WIDTH / 2).css("top", topY + CELL_WIDTH / 2).css("width", width).css("height", height);
            ;
        } else //((to % boardSize) < (from % boardSize))  
        {
            $(".snakesandladdersDiv").append('<img  src="images/snakePics/snakeLeft.png" class="snakeOrLadder" id="' + index + '"/>');
            $("#" + index).css("left", topX + CELL_WIDTH / 2).css("top", topY + CELL_WIDTH / 2).css("width", width).css("height", height);
            ;
        }

    });
}

function ajaxJoinedPlayerList()
{
    $.ajax({
        url: "getjoinedplayers",
        success: function(r) {
            refreshPlayerList(r);
        }
    });
    return false;
}

$(function()
{
    $.ajaxSetup({cache: false});
    setInterval(ajaxJoinedPlayerList, refreshRate);
    getGameInfo();
});