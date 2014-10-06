/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author Anat
 */
public class GameAlreadyFinishedXmlException extends XmlIsInvalidException
{
    String msg = "Chosen XML has errors! Game provided already has a winner!";
    
    @Override
    public String getMessage()
    {
        return msg;
    }
}
