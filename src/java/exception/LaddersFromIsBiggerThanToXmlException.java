/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author Anat
 */
public class LaddersFromIsBiggerThanToXmlException extends XmlIsInvalidException
{
    String msg = "Chosen XML has errors! One ladder's top is lower than it's bottom!";
    
    @Override
    public String getMessage()
    {
        return msg;
    }
}
