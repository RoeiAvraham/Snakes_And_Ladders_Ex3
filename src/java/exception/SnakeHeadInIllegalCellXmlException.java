/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author Anat
 */
public class SnakeHeadInIllegalCellXmlException extends XmlIsInvalidException
{
    String msg = "Chosen XML has errors! Snake head at first or last cell!";
    
    @Override
    public String getMessage()
    {
        return msg;
    }
}
