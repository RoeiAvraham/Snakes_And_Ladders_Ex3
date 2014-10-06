/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author Anat
 */
public class DuplicatePlayerNamesException extends Exception
{
    String msg = "Game name already exists!";
    
    @Override
    public String getMessage()
    {
        return msg;
    }
}
