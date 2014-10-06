/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author Anat
 */
public class DuplicateCellNumbersXmlException extends XmlIsInvalidException
{
    String msg = "Chosen XML has errors! There are two or more duplicate cell numbers!";
    
    @Override
    public String getMessage()
    {
        return msg;
    }
}
