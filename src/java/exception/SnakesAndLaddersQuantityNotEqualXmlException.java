/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author Anat
 */
public class SnakesAndLaddersQuantityNotEqualXmlException extends XmlIsInvalidException
{
    String msg = "Chosen XML has errors! Snakes and Ladders quantity is not equal!";
    
    @Override
    public String getMessage()
    {
        return msg;
    }
}
