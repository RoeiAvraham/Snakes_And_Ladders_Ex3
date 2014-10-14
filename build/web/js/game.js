/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var refreshRate = 2000; //miliseconds
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
var isFirstCall = true;
var isFirstHumanPlayed = false;
var quitVersionID =0;

function getGameInfo()
{
    $.ajax({
        url: "gameinfo",
        type: "GET",
        contentType: "application/json",
        dataType: "json",
        timeout: 2000,
        success: function (r) {
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
    $.each(r.playerNames || [], function (index, playerName) {
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
    $.each(snakeMap, function (index, value) {
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
            $("#" + index).css("left", topX + CELL_WIDTH / 2 - CELL_WIDTH / 3).css("top", topY + CELL_WIDTH / 2).css("height", height);
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
    $.each(ladderMap, function (index, value) {
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

function hasPlayerLeft()
{
    $.ajax({
        url: "hasplayerleft",
        data: {quitVersionID: quitVersionID},
        dataType: "json",
        type: "GET",
//        contentType: "application/json",
        timeout: 2000,
        success: function (r) {
            if (r.hasAnyPlayerLeft)
            {
                quitVersionID=r.quitVersionID;
                $("#gameStatus").html(r.playerLeftName + " has left the game.");
                $("#gameStatus").slideDown();
                setTimeout(function ()
                {
                    $("#gameStatus").slideUp();
                },1500);
                $("[class='soldier'][data-owner="+r.playerLeftID+"]").remove();
                currPlayerName = r.nextPlayerName;
                currPlayerID= r.nextPlayerID;
                currPlayerType = r.nextPlayerType;
            }
        }
    });
}

function ajaxJoinedPlayerList()
{
    $.ajax({
        url: "getjoinedplayers",
        success: function (r) {
            refreshPlayerList(r);
            //gameStarted:
            if (r.howManyLeftToJoin == 0 && !isGameStarted)
            {
                isGameStarted = true;
                if (!isFirstHumanPlayed)
                {
                    if (currPlayerType == "HUMAN")
                    {
                        isFirstHumanPlayed = true;
                        setTimerAtServer();
                        setInterval(hasPlayerLeft, 1000);
                    }
                }
                $("#gameStatus").html("Game Started!");
                $("#gameStatus").slideDown();
                setTimeout(function () {
                    $("#gameStatus").slideUp();

                }, 2000);
                getJoinedPlayersSoldierLocation();
                setInterval(requestLastTurnData, 1000);
                if (r.isSessionPlayerFirstPlayer)
                {
                    isItMyTurn = true;
                    initComponentsForNewTurn();
                }
                else {
                    $(".dice").css('background-image', 'url(\'images/wait.gif\')');
                    if (currPlayerType == "COMP")
                    {
                        $(".currPlayer").html('<label class="currenPlayerLabel">Current Player: <br><span class="currPlayerName">' + currPlayerName + '</span></label><img src="images/computerPlayerPics/comp' + currPlayerID + '.png" class="currPlayerPic">');
                        setTimeout(function ()
                        {
                            diceRes = 0;
                            playTurn(0);
                        }, 2500);
                    }
                    else
                    {
                        $(".currPlayer").html('<label class="currenPlayerLabel">Current Player: <br><span class="currPlayerName">' + currPlayerName + '</span></label><img src="images/humanPlayerPics/human' + currPlayerID + 'big.png" class="currPlayerPic">');
                    }
                }
            }
            else if (!isWaitingShown && !isGameStarted)
            {
                $("#gameStatus").html("Waiting for players to join...");
                isWaitingShown = true;
                $("#gameStatus").slideDown();
            }
        }
    });
    return false;
}

//fix
//function playCompTurn()
//{
//    getDiceResFromServer();
//    var soldierId = 0;
//    playTurn(soldierId);
//}

//fix - add request fir each second 
function requestLastTurnData()
{
    if (!isItMyTurn) {
        $.ajax({
            url: "lastturn",
            type: "GET",
            data: {versionID: versionID.toString()},
            contentType: "application/json",
            dataType: "json",
            timeout: 2000,
            success: function (r) {
                if (currPlayerType == "COMP")
                {
                    $(".currPlayer").html('<label class="currenPlayerLabel">Current Player: <br><span class="currPlayerName">' + currPlayerName + '</span></label><img src="images/computerPlayerPics/comp' + currPlayerID + '.png" class="currPlayerPic">');
                }
                else
                {
                    $(".currPlayer").html('<label class="currenPlayerLabel">Current Player: <br><span class="currPlayerName">' + currPlayerName + '</span></label><img src="images/humanPlayerPics/human' + currPlayerID + 'big.png" class="currPlayerPic">');
                }
                showOtherPlayerTurn(r);
                if (r.isThereWinner)
                {
                    //redirect
                    isItMyTurn = true;
                    setTimeout(function () {
                        window.location.href = "winner.html";
                    }, 2500);
                }
                currPlayerID = r.newCurrPlayerID;
                currPlayerName = r.newCurrPlayerName;
                currPlayerType = r.newCurrPlayerType;
                versionID = r.versionID;
                if (r.isItPlayerSessionTurn)
                {
                    setTimeout(function () {
                        isItMyTurn = true;
                        initComponentsForNewTurn();
                    }, 2500);
                }
                else
                {
                    isItMyTurn = false;
                }
            }
        });
    }
    return false;
}


function showOtherPlayerTurn(r)
{
    var audio = document.getElementById("diceSound");
    audio.play();
    diceRes = r.turnData.turnDiceRes;
    setTimeout(function () {
        $(".dice").css('background-image', 'url(\'images/dicePics/die' + diceRes + '.png\')');
        setTimeout(function () {
            moveSoldier(r, r.turnData.turnSoldierNum);
            if (r.newCurrPlayerType == "HUMAN")
            {
                $(".currPlayer").html('<label class="currenPlayerLabel">Current Player: <br><span class="currPlayerName">' + r.newCurrPlayerName + '</span></label><img src="images/humanPlayerPics/human' + r.newCurrPlayerID + 'big.png" class="currPlayerPic">');
                if (!isItMyTurn)
                {
                    $(".dice").css('background-image', 'url(\'images/wait.gif\')');
                }
            }
        }, 1000);
    }, 1000);
    $(".dice").css('background-image', 'url(\'images/dicePics/rolling_dice.gif\')');
}
function getJoinedPlayersSoldierLocation()
{
    $.ajax({
        url: "getsoldiermap",
        type: "GET",
        contentType: "application/json",
        dataType: "json",
        timeout: 2000,
        success: function (r) {
            $.each(r.soldierMap, function (index, player) {
                $.each(player, function (soldierIndex, soldier)
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

$(function ()
{
    $.ajaxSetup({cache: false});
    setInterval(ajaxJoinedPlayerList, refreshRate);
    getGameInfo();
    $("#arrow").hide();
    $("#gameStatus").hide();
});
function setDiceAction() {
    $(".dice").css("cursor", "pointer");
    $(".dice").css('background-image', 'url(\'images/dicePics/staticDic.png\')');
    $('.dice').click(function () {
        $("#arrow").hide();
        $(this).css('cursor', 'context-menu');
        var audio = document.getElementById("diceSound");
        audio.play();
        $(this).off();
        $(this).css('background-image', 'url(\'images/dicePics/rolling_dice.gif\')');
        $(this).css('cursor', 'arrow');
        getDiceResFromServer();
        return false;
    });
}

function setTimerAtServer()
{
    $.ajax({
        url: "starttimer",
        timeout: 2000
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
        success: function (r) {
            setTimeout(function () {
                $(".dice").css('background-image', 'url(\'images/dicePics/die' + r + '.png\')');
                diceRes = r;
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
        data: {soldierID: soldierId.toString(), diceRes: diceRes.toString(), versionID: versionID.toString()},
        timeout: 2000,
        success: function (r) {
            if (r.currPlayerType == "HUMAN")
            {
                moveSoldier(r, soldierId);
                versionID = r.versionID;
                isItMyTurn = false;
                currPlayerID = r.newCurrPlayerID;
                currPlayerType = r.newCurrPlayerType;
                currPlayerName = r.newCurrPlayerName;
                $(".dice").css('background-image', 'url(\'images/wait.gif\')');
                if (r.isThereWinner)
                {
                    //redirect
                    setTimeout(function () {
                        window.location.href = "winner.html";
                    }, 1500);
                }

                if (r.newCurrPlayerType == "HUMAN")
                {
                    $(".currPlayer").html('<label class="currenPlayerLabel">Current Player: <br><span class="currPlayerName">' + r.newCurrPlayerName + '</span></label><img src="images/humanPlayerPics/human' + r.newCurrPlayerID + 'big.png" class="currPlayerPic">');
                }


            }
            if (r.newCurrPlayerType == "COMP" && !r.isThereWinner)
            {
//                if (r.currPlayerType == "COMP") {
                setTimeout(function () {
//                    $(".currPlayer").html('<label class="currenPlayerLabel">Current Player: <br><span class="currPlayerName">' + currPlayerName + '</span></label><img src="images/computerPlayerPics/comp' + currPlayerID + '.png" class="currPlayerPic">');
                    playTurn(0, 0);
                }, 2000);
//                }
//                else
//                {
//                    playTurn(0, 0);
//                }
            }


        }
    });
    return false;
}

function moveSoldier(turnData, soldierID)
{
    //alert(turnData);
    var left, top;
    var leftOffset = 0;
    var topOffset = 0;
    var destCell = +turnData.turnData.turnDest;
    var clickedSoldier = $("[class='soldier'][data-id=" + soldierID + "][data-owner=" + turnData.currPlayerID + "]");
    //for comp player
    if (clickedSoldier.length == 0)
    {
        clickedSoldier = $("[class='soldier'][data-owner=" + turnData.currPlayerID + "][data-cell=" + turnData.turnData.sourceCell + "]");
    }
    var midDestCell = +clickedSoldier.attr('data-cell') + +turnData.turnData.turnDiceRes;
    if (midDestCell > boardSize * boardSize)
    {
        midDestCell = boardSize * boardSize;
    }
    var movingSoldier = clickedSoldier;
    var currSoldierNumSoldiers = +$(clickedSoldier).find(".numSoldiersLabel").text();
    if (currSoldierNumSoldiers > 1)
    {
        $(clickedSoldier).find(".numSoldiersLabel").text(+currSoldierNumSoldiers - 1);
        movingSoldier = $(clickedSoldier).clone();
        var nextFreeSoldierId = turnData.nextFreeID;
        $(clickedSoldier).attr("data-id", nextFreeSoldierId);
        $(movingSoldier).attr("data-id", soldierID);
        $(movingSoldier).find(".numSoldiersLabel").text(1);
        $("#board").append(movingSoldier);
    }

    if (+turnData.currPlayerID == 2 || +turnData.currPlayerID == 4)
    {
        leftOffset = 37;
    }

    if (+turnData.currPlayerID == 3 || +turnData.currPlayerID == 4)
    {
        topOffset = 46;
    }

    if (midDestCell != destCell)
    {
        var audio;
        if (midDestCell < destCell)
        {
            audio = document.getElementById("ladderSound");
            audio.play();
        }
        else
        {
            audio = document.getElementById("snakeSound");
            audio.play();
        }
        left = $("#cell" + midDestCell).position().left + +leftOffset + "px";
        top = $("#cell" + midDestCell).position().top + +topOffset + "px";
        $(movingSoldier).animate({
            left: left,
            top: top
        });
    }

    var soldierAlreadyInDestCell = $("[class='soldier'][data-cell='" + destCell + "'][data-owner=" + turnData.currPlayerID + "]");
    var isThereAlreadySoldierInDest = soldierAlreadyInDestCell.length;
    $(movingSoldier).attr("data-cell", destCell);
    left = $("#cell" + turnData.turnData.turnDest).position().left + +leftOffset + "px";
    top = $("#cell" + turnData.turnData.turnDest).position().top + +topOffset + "px";
    $(movingSoldier).animate({
        left: left,
        top: top
    });
    //merge soldier
    if (isThereAlreadySoldierInDest)
    {
        if (!$(soldierAlreadyInDestCell).is(movingSoldier))
        {
            var numSoldiersAtDestCell = +soldierAlreadyInDestCell.text().trim();
            var numSoldiersInMovingSoldier = +$(movingSoldier).text().trim();
            $(movingSoldier).find(".numSoldiersLabel").text(numSoldiersAtDestCell + numSoldiersInMovingSoldier);
            soldierAlreadyInDestCell.remove();
        }
    }

}
function setSoldiersAction()
{
    $("[class='soldier'][data-owner=" + currPlayerID + "]").not("[data-cell=" + (boardSize * boardSize) + "]").css("cursor", "pointer");
    //setInterval(blinkPlayerSoldiers,100);

    $("[class='soldier'][data-owner=" + currPlayerID + "]").not("[data-cell=" + (boardSize * boardSize) + "]").click(function () {
        // clearInterval(blinkPlayerSoldiers);
        $("[class='soldier'][data-owner=" + currPlayerID + "]").css("cursor", '');
        $("[class='soldier'][data-owner=" + currPlayerID + "]").off();
        playTurn($(this).attr('data-id'));
    });
}

var blinkPlayerSoldiers = function ()
{
    $("[class='soldier'][data-owner=" + currPlayerID + "]").not("[data-cell=" + (boardSize * boardSize) + "]").fadeOut(500).fadeIn(500);
};
function initComponentsForNewTurn()
{
    //set player pic
    $("#arrow").show();
    setDiceAction();
    if (currPlayerType == "COMP")
    {
        $(".currPlayer").html('<label class="currenPlayerLabel">Current Player: <br><span class="currPlayerName">' + currPlayerName + '</span></label><img src="images/computerPlayerPics/comp' + currPlayerID + '.png" class="currPlayerPic">');
    }
    else
    {
        $(".currPlayer").html('<label class="currenPlayerLabel">Current Player: <br><span class="currPlayerName">' + currPlayerName + '</span></label><img src="images/humanPlayerPics/human' + currPlayerID + 'big.png" class="currPlayerPic">');
    }
}
