/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author Anat
 */
public class SnakeEndOutOfRangeXmlException extends XmlIsInvalidException
{
    String msg = "Chosen XML has errors! One snake's end is out of the board range!";
    
    @Override
    public String getMessage()
    {
        return msg;
    }
}
