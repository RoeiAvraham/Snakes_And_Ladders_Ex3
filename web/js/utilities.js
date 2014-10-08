/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function createPlayerNameForm()
{
    var form = $("<form><form>").attr("class", "form-horizontal").attr("id", "playerNameForm").attr("role", "form").attr("action", "startgame");
    var divFormGroup = $("<div></div>").attr("id","divFormGroup").attr("class", "form-group");
    form.append(divFormGroup);
    $("body").append(form);
}

function createDropDownPlayerName(r)
{
    createPlayerNameForm();
    var label = $("<label></label>").attr("class", "col-sm-5 control-label");
    var divCombo = $("<div></div>").attr("class", "col-sm-2").attr("id","choosePlayer");
    var combo = $("<select></select>").attr("id", "playerName").attr("name", "playerName").attr("class", "form-control");
    var hiddenGameNameField = '<input type="hidden" id="gameName" name="gameName" value="'+ r.gameName + '"/>';

    label.append("Please choose a player name:");

    $.each(r.playerNames, function (i, el) {
        combo.append("<option>" + el + "</option>");
    });

    divCombo.append(combo);
    $("#divFormGroup").append(label);
    $("#divFormGroup").append(divCombo);
    $("#playerNameForm").append($("#divFormGroup"));
    $("#playerNameForm").append(hiddenGameNameField);
}

function createSubmitButton()
{
    var divFormGroup = $("<div></div>").attr("class", "form-group text-center").attr("id", "form2Div");
    var startButton = $("<button></button>").attr("id", "startBtn").attr("type", "submit").attr("class", "btn btn-warning btn-lg custom center-block");

    startButton.append("Start Game");
    $("#playerNameForm").append(startButton);
    $("body").append(divFormGroup);
}

function createPlayerNameTextField(r)
{
    createPlayerNameForm();
    var label = $("<label></label>").attr("class", "col-sm-5 control-label");
    var divField = $("<div></div>").attr("class", "col-sm-3");
    var inputField = $("<input></input>").attr("id","playerName").attr("name","playerName").attr("type","text").attr("class","form-control").attr("placeholder","Your Name");
    var hiddenGameNameField = '<input type="hidden" id="gameName" name="gameName" value="'+ r.gameName + '"/>';
    
    label.append("Your Name:");
    $("#divFormGroup").append(label);
    divField.append(inputField);
    $("#divFormGroup").append(divField);
    $("#playerNameForm").append($("#divFormGroup"));
    $("#playerNameForm").append(hiddenGameNameField);
}