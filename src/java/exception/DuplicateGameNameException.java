/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package exception;

/**
 *
 * @author Roei
 */
public class DuplicateGameNameException extends Exception
{
    String msg = "There are two or more duplicate names!";
    
    @Override
    public String getMessage()
    {
        return msg;
    }
}
