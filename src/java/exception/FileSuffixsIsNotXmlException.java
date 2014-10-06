/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author Anat
 */
public class FileSuffixsIsNotXmlException extends Exception
{
    public void printMessage()
    {
        System.out.println("File suffix is illegal, has to be xml.");
    }
}
