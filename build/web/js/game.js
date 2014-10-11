/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var refreshRate = 2000; //miliseconds
var getSoldierLocationOfJoinedPlayers;
var CELL_WIDTH = 86;
var boardSize;
var currPlayerID;
var currPlayerType;
var currPlayerName;
var diceRes;
var isGameStarted = false;
var isWaitingShown = false;
var versionID = 0;
var isItMyTurn = false;

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
            currPlayerID = r.currPlayerId;
            currPlayerName = r.currPlayerName;
            currPlayerType = r.currPlayerType;
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
            if (r.howManyLeftToJoin == 0 && !isGameStarted)
            {
                isGameStarted = true;
                $("#gameStatus").html("Game Started!");
                $("#gameStatus").slideDown();
                setTimeout(function() {
                    $("#gameStatus").slideUp();
                }, 2000);
                getJoinedPlayersSoldierLocation();
                if (r.isSessionPlayerFirstPlayer)
                {
                    initComponentsForNewTurn();
                }
                else if (currPlayerType == "COMP")
                {
                    setTimeout(function() {
                        playCompTurn();
                    }, 1500);
                }
            }
            else if (!isWaitingShown)
            {
                $("#gameStatus").html("Waiting for players to join...");
                isWaitingShown = true;
                $("#gameStatus").slideDown();
            }
        }
    });
    return false;
}

function playCompTurn()
{
    getDiceResFromServer();
    var soldierId = 0;
    playTurn(soldierId);
}
function getJoinedPlayersSoldierLocation()
{
    $.ajax({
        url: "getsoldiermap",
        type: "GET",
        contentType: "application/json",
        dataType: "json",
        timeout: 2000,
        success: function(r) {
            $.each(r.soldierMap, function(index, player) {
                $.each(player, function(soldierIndex, soldier)
                {
                    var htmlSoldier;
                    if (soldier.playerType == "COMP")
                    {
                        $("#board").append('<div class="soldier" data-owner="' + index + '" data-id="' + soldier.soldierNum + '"data-cell="' + soldierIndex + '">\n\
                                            <label class="numSoldiersLabel">' + soldier.soldierAmount + '</label>\n\
                                            <img src ="images/computerPlayerPics/comp' + index + '.png" class="soldierPic">\n\
                                            </div>');
                    }
                    else
                    {
                        $("#board").append('<div class="soldier" data-owner="' + index + '" data-id="' + soldier.soldierNum + '"data-cell="' + soldierIndex + '">\n\
                                            <label class="numSoldiersLabel">' + soldier.soldierAmount + '</label>\n\
                                            <img src ="images/humanPlayerPics/human' + index + '.png" class="soldierPic">\n\
                                            </div>');
                    }
                    htmlSoldier = $("[class='soldier'][data-owner=" + index + "][data-id=" + soldier.soldierNum + "]");
                    var leftOffset = 0;
                    var topOffset = 0;
                    if (index == 2 || index == 4)
                    {
                        leftOffset = 37;
                    }

                    if (index == 3 || index == 4)
                    {
                        topOffset = 46;
                    }
                    $(htmlSoldier).attr("style", "left:" + (+$("#cell" + soldierIndex).position().left + +leftOffset) + "px ;top:" + (+$("#cell" + soldierIndex).position().top + +topOffset) + "px;");
                });
            });
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
    $("#gameStatus").hide();
    // initComponentsForNewTurn();
});

function setDiceAction() {
    $(".dice").css("cursor", "pointer");
    $('.dice').click(function() {
        var dice = this;
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
                diceRes = r;
                setDiceAction();
                if (currPlayerType == "HUMAN")
                {
                    setSoldiersAction();
                }
            }, 1000);
        }
    });
    return false;
}

function playTurn(soldierId) {
    $.ajax({
        url: "playturn",
        type: "GET",
        data: {soldierID: soldierId.toString(), diceRes: diceRes.toString()},
        timeout: 2000,
        success: function(r) {
            moveSoldier(r, soldierId);
            if (r.isThereWinner)
            {
                //redirect
                alert(currPlayerName + " won!");
            }
            else
            {
                currPlayerID = r.newCurrPlayerID;
                currPlayerType = r.newCurrPlayerType;
                currPlayerName = r.newCurrPlayerName;
            }
        }
    });
    return false;
}

function moveSoldier(turnData, soldierID)
{
    //alert(turnData);
    var left, top;
    var destCell = +turnData.turnData.turnDest;
    var clickedSoldier = $("[class='soldier'][data-id=" + soldierID + "][data-owner=" + currPlayerID + "]");
    var midDestCell = +clickedSoldier.attr('data-cell') + +turnData.turnData.turnDiceRes;
    var movingSoldier = clickedSoldier;
    var currSoldierNumSoldiers = +$(clickedSoldier).find(".numSoldiersLabel").text();
    if (currSoldierNumSoldiers > 1)
    {
        $(clickedSoldier).find(".numSoldiersLabel").text(+currSoldierNumSoldiers - 1);
        movingSoldier = $(clickedSoldier).clone();
        var nextFreeSoldierId = $("[class='soldier'][data-cell=" + clickedSoldier.attr('data-cell') + "] [data-owner=" + currPlayerID + "]").not("[data-id=" + soldierID + "]").attr('data-id');
        if (nextFreeSoldierId == undefined)
        {
            if ($(clickedSoldier).attr("data-id") == 4)
            {
                nextFreeSoldierId = 1;
            }
            else
            {
                nextFreeSoldierId = +$(clickedSoldier).attr("data-id") + 1;
            }
        }
        $(clickedSoldier).attr("data-id", nextFreeSoldierId);
        $(movingSoldier).attr("data-id", soldierID);
        $(movingSoldier).find(".numSoldiersLabel").text(1);
        $("#board").append(movingSoldier);
    }
    if (midDestCell != destCell)
    {
        left = $("#cell" + midDestCell).position().left + "px";
        top = $("#cell" + midDestCell).position().top + "px";
        $(movingSoldier).animate({
            left: left,
            top: top
        });
    }

    var soldierAlreadyInDestCell = $("[class='soldier'][data-cell='" + destCell + "'][data-owner=" + currPlayerID + "]");
    var isThereAlreadySoldierInDest = soldierAlreadyInDestCell.length;

    $(movingSoldier).attr("data-cell", destCell);
    left = $("#cell" + turnData.turnData.turnDest).position().left + "px";
    top = $("#cell" + turnData.turnData.turnDest).position().top + "px";
    $(movingSoldier).animate({
        left: left,
        top: top
    });

    if (isThereAlreadySoldierInDest)
    {
        var numSoldiersAtDestCell = +soldierAlreadyInDestCell.text().trim();
        var numSoldiersInMovingSoldier = +$(movingSoldier).text().trim();
        $(movingSoldier).find(".numSoldiersLabel").text(numSoldiersAtDestCell + numSoldiersInMovingSoldier);
        soldierAlreadyInDestCell.remove();
    }

}
function setSoldiersAction()
{
    $("[class='soldier'][data-owner=" + currPlayerID + "]").css("cursor", "pointer");
    // $("[class='soldier'][data-owner=1]").fadeOut(100).fadeIn(100);
    $("[class='soldier'][data-owner=" + currPlayerID + "]").click(function() {
        //ajax request to play turn then move the soldier to the right cell..
        $("[class='soldier'][data-owner=" + currPlayerID + "]").css("cursor", '');
        $("[class='soldier'][data-owner=" + currPlayerID + "]").off();
        playTurn($(this).attr('data-id'));
    });
}

function initComponentsForNewTurn()
{
    //set player pic
    $("#arrow").show();
    setDiceAction();
}




