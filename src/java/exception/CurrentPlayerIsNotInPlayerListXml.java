/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author Anat
 */
public class CurrentPlayerIsNotInPlayerListXml extends XmlIsInvalidException
{
    String msg = "Chosen XML has errors! Current player name is invalid!";
    
    @Override
    public String getMessage()
    {
        return msg;
    }
}
