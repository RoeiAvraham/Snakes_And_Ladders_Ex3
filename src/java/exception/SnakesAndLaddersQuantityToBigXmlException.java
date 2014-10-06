/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author Anat
 */
public class SnakesAndLaddersQuantityToBigXmlException extends XmlIsInvalidException
{
    String msg = "Chosen XML has errors! Too many snakes and ladders to handle in this board size!";
    
    @Override
    public String getMessage()
    {
        return msg;
    }
}
