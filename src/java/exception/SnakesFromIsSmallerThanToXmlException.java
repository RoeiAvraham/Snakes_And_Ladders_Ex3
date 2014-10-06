/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author Anat
 */
public class SnakesFromIsSmallerThanToXmlException extends XmlIsInvalidException
{
    String msg = "Chosen XML has errors! One snake's head is lower than its tail!";
    
    @Override
    public String getMessage()
    {
        return msg;
    }
}
