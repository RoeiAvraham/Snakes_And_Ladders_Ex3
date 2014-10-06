/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author Anat
 */
public class PlayerHasNoFourSoldiersXmlException extends XmlIsInvalidException
{
    String msg = "Chosen XML has errors! At least one player doesn't have 4 soldiers!";
    
    @Override
    public String getMessage()
    {
        return msg;
    }
}
