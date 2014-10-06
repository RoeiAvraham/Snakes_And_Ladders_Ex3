/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author Anat
 */
public class SnakesAndLaddersOnSameCellXmlException extends XmlIsInvalidException
{
    String msg = "Chosen XML has errors! Ladder and Snake on the same cell!";
    
    @Override
    public String getMessage()
    {
        return msg;
    }
}
