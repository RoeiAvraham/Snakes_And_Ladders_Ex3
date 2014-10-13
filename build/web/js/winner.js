/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(function() {
    var audio = document.getElementById("diceSound");
    audio.play();
    $.ajax({
        url: "winner",
        type: "GET",
        timeout: 2000,
        success: function(r) {
            $(".winnerName").html(r.winnerName + " won!!!");
            rotateWinner(r.winnerID, r.type);
        }
    });
});

function rotateWinner(winnerID, type)
{
    if (type == "HUMAN")
    {
        $(".winner").attr("src", "images/humanPlayerPics/human" + winnerID + "big.png");
    }
    else
    {
        $(".winner").attr("src", "images/computerPlayerPics/comp" + winnerID + ".png");
    }

    var $elie = $(".winner"), degree = 0, timer;
    rotate();
    function rotate() {
        $elie.css({WebkitTransform: 'rotate(' + degree + 'deg)'});
        $elie.css({'-moz-transform': 'rotate(' + degree + 'deg)'});
        timer = setTimeout(function() {
            ++degree;
            rotate();
        }, 10);
    }
}