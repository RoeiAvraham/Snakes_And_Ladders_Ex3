/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

import java.io.IOException;

/**
 *
 * @author Roei
 */
public class FxmlException extends IOException
{

    String msg = "Problem with FXML of next screen.";

    @Override
    public String getMessage()
    {
        return msg;
    }
}
