/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author Anat
 */
public class DuplicatePlayerNamesXmlException extends XmlIsInvalidException
{
    String msg = "Chosen XML has errors! There are two or more duplicate players names!";
    
    @Override
    public String getMessage()
    {
        return msg;
    }
}
