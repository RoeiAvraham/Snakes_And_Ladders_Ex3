/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author roei.avraham
 */
public class OnlyComputerPlayersXmlException extends XmlIsInvalidException {
    
    String msg = "Chosen XML has errors! All the players are computer players!";
    
    @Override
    public String getMessage()
    {
        return msg;
    }
    
}
