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
            placeSnakesAndLaddersOnBoard(r.snakeMap, r.ladderMap);

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

function placeSnakesAndLaddersOnBoard(snakeMap, ladderMap)
{


    placeSnakesOnBoard(snakeMap);
    placeLaddersOnBOard(ladderMap);

//        
//        for (Cell cell : model.getBoard().getCells())
//        {
//            if (cell.getDest() != Cell.NO_DEST)
//            {
//                if (cell.getCellNum() < cell.getDest())
//                {
//                    boardView.placeLadderOnBoard(cell.getCellNum(), cell.getDest());
//                } else
//                {
//                    boardView.placeSnakeOnBoard(cell.getDest(), cell.getCellNum());
//                }
//            }
//        }
}

function placeSnakesOnBoard(snakeMap, boardSize)
{
    $.each(snakeMap, function(index, value) {
         $(".snakesandladdersDiv").append('<img  src="images/ladderPics/ladderRight.png" class="snakeOrLadder id="'+index+'" />');
         $("#"+index).css("left", $("#cell"+value.x).position().left +CELL_WIDTH/2).css("top",$("#cell"+value.y).position().top + CELL_WIDTH/2);
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