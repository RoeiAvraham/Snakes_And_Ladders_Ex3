/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author Anat
 */
public class LadderBottomInIllegalCellXmlException extends XmlIsInvalidException
{
    String msg = "Chosen XML has errors! Ladder bottom at first or last cell!";
    
    @Override
    public String getMessage()
    {
        return msg;
    }
}
