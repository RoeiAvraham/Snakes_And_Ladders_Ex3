/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var refreshRate = 2000; //miliseconds
var CELL_WIDTH = 86;
var boardSize;
var currPlayer;

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
            boardSize = r.boardSize;
            $(".snakesandladdersDiv").css("width", boardSize * CELL_WIDTH).css("height", boardSize * CELL_WIDTH);
            $(".boardMainDiv").css("width", boardSize * CELL_WIDTH).css("height", boardSize * CELL_WIDTH);
            drawBoard(r.boardSize, r.snakeMap, r.ladderMap);
        }
    });
    return false;
}

function drawBoard(boardSize, snakeMap, ladderMap)
{
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

    placeSnakesAndLaddersOnBoard(snakeMap, ladderMap, boardSize);
}

function refreshPlayerList(r) {
    $("#playerList").empty();
    $.each(r.playerNames || [], function(index, playerName) {
        $('<option>' + playerName + "   -   " + r.playerTypes[index] + '</option>').appendTo($("#playerList"));
    });
}

function placeSnakesAndLaddersOnBoard(snakeMap, ladderMap, boardSize)
{
    placeSnakesOnBoard(snakeMap, boardSize);
    placeLaddersOnBOard(ladderMap, boardSize);
}

function placeSnakesOnBoard(snakeMap, boardSize)
{
    $.each(snakeMap, function(index, value) {
        var topY = $("#cell" + value.from).position().top;
        var topX = $("#cell" + value.from).position().left;
        var bottomY = $("#cell" + value.to).position().top;
        var bottomX = $("#cell" + value.to).position().left;

        var width = Math.abs(topX - bottomX);
        var height = Math.abs(topY - bottomY);
        var tmp = value.to + boardSize;
        var nearestMultOfBoardSizeToBottom = tmp - (tmp % boardSize);
        if ((value.from % boardSize) == (value.to % boardSize))
        { //vertical
            $(".snakesandladdersDiv").append('<img  src="images/snakePics/snakeVertical.png" class="snakeOrLadder" id="' + index + '"/>');
            $("#" + index).css("left", topX + CELL_WIDTH / 2).css("top", topY + CELL_WIDTH / 2).css("height", height);
        } else if (value.to % boardSize == 0)
        {           //left 
            $(".snakesandladdersDiv").append('<img  src="images/snakePics/snakeLeft.png" class="snakeOrLadder" id="' + index + '"/>');
            $("#" + index).css("left", topX + CELL_WIDTH / 2).css("top", topY + CELL_WIDTH / 2).css("width", width).css("height", height);

        } else if (value.from <= nearestMultOfBoardSizeToBottom)
        {         //horizontal
            $(".snakesandladdersDiv").append('<img  src="images/snakePics/snakeHorizontal.png" class="snakeOrLadder" id="' + index + '"/>');
            $("#" + index).css("left", bottomX + CELL_WIDTH / 2).css("top", topY + CELL_WIDTH / 2 - 30).css("width", width);

        } else if (value.from % boardSize == 0)
        {              //right
            $(".snakesandladdersDiv").append('<img  src="images/snakePics/snakeRight.png" class="snakeOrLadder" id="' + index + '"/>');
            $("#" + index).css("left", bottomX + CELL_WIDTH / 2).css("top", topY + CELL_WIDTH / 2).css("width", width).css("height", height);

        } else if ((value.from % boardSize) > (value.to % boardSize))
        {
            $(".snakesandladdersDiv").append('<img  src="images/snakePics/snakeRight.png" class="snakeOrLadder" id="' + index + '"/>');
            $("#" + index).css("left", bottomX + CELL_WIDTH / 2).css("top", topY + CELL_WIDTH / 2).css("width", width).css("height", height);

        } else //((to % boardSize) < (from % boardSize))  
        {
            $(".snakesandladdersDiv").append('<img  src="images/snakePics/snakeLeft.png" class="snakeOrLadder" id="' + index + '"/>');
            $("#" + index).css("left", topX + CELL_WIDTH / 2).css("top", topY + CELL_WIDTH / 2).css("width", width).css("height", height);
        }
    });
}

function placeLaddersOnBOard(ladderMap, boardSize)
{
    $.each(ladderMap, function(index, value) {
        var topY = $("#cell" + value.to).position().top;
        var topX = $("#cell" + value.to).position().left;
        var bottomY = $("#cell" + value.from).position().top;
        var bottomX = $("#cell" + value.from).position().left;
        var width = Math.abs(topX - bottomX);
        var height = Math.abs(topY - bottomY);
        var tmp = value.from + boardSize;
        var nearestMultOfBoardSizeToBottom = tmp - (tmp % boardSize);

        if ((value.to % boardSize) == (value.from % boardSize))
        { //vertical
            $(".snakesandladdersDiv").append('<img  src="images/ladderPics/ladderVertical.png" class="snakeOrLadder" id="' + index + '"/>');
            $("#" + index).css("left", topX + CELL_WIDTH / 2 - 30).css("top", topY + CELL_WIDTH / 2).css("height", height);

        } else if (value.from % boardSize == 0)
        {           //left 
            $(".snakesandladdersDiv").append('<img  src="images/ladderPics/ladderLeft.png" class="snakeOrLadder" id="' + index + '"/>');
            $("#" + index).css("left", topX + CELL_WIDTH / 2).css("top", topY + CELL_WIDTH / 2).css("width", width).css("height", height);
        } else if (value.to <= nearestMultOfBoardSizeToBottom)
        {         //horizontal
            $(".snakesandladdersDiv").append('<img  src="images/ladderPics/ladderHorizontal.png" class="snakeOrLadder" id="' + index + '"/>');
            $("#" + index).css("left", bottomX + CELL_WIDTH / 2).css("top", topY + CELL_WIDTH / 2 - 30).css("width", width);
        } else if (value.to % boardSize == 0)
        {              //right
            $(".snakesandladdersDiv").append('<img  src="images/ladderPics/ladderRight.png" class="snakeOrLadder" id="' + index + '"/>');
            $("#" + index).css("left", bottomX + CELL_WIDTH / 2).css("top", topY + CELL_WIDTH / 2).css("width", width).css("height", height);
        } else if ((value.to % boardSize) > (value.from % boardSize))
        {
            $(".snakesandladdersDiv").append('<img  src="images/ladderPics/ladderRight.png" class="snakeOrLadder" id="' + index + '"/>');
            $("#" + index).css("left", bottomX + CELL_WIDTH / 2).css("top", topY + CELL_WIDTH / 2).css("width", width).css("height", height);
        } else //((to % boardSize) < (from % boardSize))  
        {
            $(".snakesandladdersDiv").append('<img  src="images/ladderPics/ladderLeft.png" class="snakeOrLadder" id="' + index + '"/>');
            $("#" + index).css("left", topX + CELL_WIDTH / 2).css("top", topY + CELL_WIDTH / 2).css("width", width).css("height", height);
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
    $("#arrow").hide();
    initComponentsForNewTurn();
});

function setDiceAction() {
    $(".dice").css("cursor", "pointer");
    $('.dice').click(function() {
        $("#arrow").hide();
        var audio = document.getElementById("diceSound");
        audio.play();
        getDiceResFromServer();
        $(this).off();
        $(this).css('background-image', 'url(\'images/dicePics/rolling_dice.gif\')');
        $(this).css('cursor', 'arrow');
        return false;
    });
}

function getDiceResFromServer()
{
    $.ajax({
        url: "diceRes",
        type: "GET",
        contentType: "application/json",
        dataType: "json",
        timeout: 2000,
        success: function(r) {
            setTimeout(function() {
                $(".dice").css('background-image', 'url(\'images/dicePics/die' + r + '.png\')');
                setDiceAction();
                
                setSoldiersAction();
                
            }, 1000);
        }
    });
    return false;
}

function setSoldiersAction()
{   $("[class='soldier'][data-owner=1]").css("cursor","pointer");
    $("[class='soldier'][data-owner=1]").click(function() {
        //ajax request to play turn then move the soldier to the right cell..
        var left, top;
        var movingSoldier = this;
        var currSoldierNumSoldiers = +$(this).find(".numSoldiersLabel").text();
        if (currSoldierNumSoldiers > 1)
        {
            $(this).find(".numSoldiersLabel").text(+currSoldierNumSoldiers - 1);
            var splittedSoldier = $(this).clone();
//            var nextFreeSoldierId = ...

            movingSoldier = splittedSoldier;
            $(movingSoldier).attr("data-id",2);
            $(movingSoldier).find(".numSoldiersLabel").text(1);
            $("#board").append(movingSoldier);
        }
        var soldierAlreadyInDestCell = $("[class='soldier'][data-cell='7'][data-owner='1']");
        var isThereAlreadySoldierInDest = soldierAlreadyInDestCell.length;
        
        $(movingSoldier).attr("data-cell", 7);        
        
        $(movingSoldier).animate({
            left: '86px',
            top: '-=86px'
        });

        if (isThereAlreadySoldierInDest)
        {
            var numSoldiersAtDestCell = +soldierAlreadyInDestCell.text().trim();
            var numSoldiersInMovingSoldier = +$(movingSoldier).text().trim();
            $(movingSoldier).find(".numSoldiersLabel").text(numSoldiersAtDestCell + numSoldiersInMovingSoldier);
            soldierAlreadyInDestCell.remove();
        }

    });
}

function initComponentsForNewTurn()
{
    //set player pic
    $("#arrow").show();
    setDiceAction();
    
    //make dice clickable- until then disabled
    //all soldiers not clickable.
    //add quit button ?
    
    
}
//$('.soldier').click( function() {
//    
//    if (!$(this).attr('data-owner') == currentPlayerId)
//        return;
//
//});


//function Soldier() {
//    var myPrivateVar, myPrivateMethod;
//    // A private counter variable
//    myPrivateVar = 0;
//    // A private function which logs any arguments
//    myPrivateMethod = function( foo ) {
//        console.log( foo );
//    };
//    
//    return {
//        // A public variable
//        myPublicVar: "foo",
//        // A public function utilizing privates
//        myPublicFunction: function( bar ) {
//        // Increment our private counter
//        myPrivateVar++;
//        // Call our private method using bar
//        myPrivateMethod( bar );
//    }
//  };



//x= new Soldier;
//x.myPublicFunction();

//function Soldier() {
//    this. 
//    this.owner;
//}
//var z = new MyClass();
//console.log(z.y);
