/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

<<<<<<< HEAD
import exception.DuplicatePlayerNamesException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
=======
import com.google.gson.Gson;
import exception.DuplicatePlayerNamesException;
import java.io.IOException;
import java.io.PrintWriter;
>>>>>>> JoinGameRewrite
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Game;
import model.GameManager;
import utilities.Constants;
import utilities.ServletUtils;
import utilities.SessionUtils;

/**
 *
 * @author roei.avraham
 */
@WebServlet(name = "StartGameServlet", urlPatterns = {"/startgame"})
public class StartGameServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");

        String gameNameFromSession = SessionUtils.getGameName(request);
        GameManager gameManager = ServletUtils.getGameManager(getServletContext());
        if (gameNameFromSession == null) {
            
            try {
                String gameNameFromParameter = request.getParameter(Constants.GAME_NAME).trim();
                String playerNameFromParameter = request.getParameter(Constants.PLAYER_NAME).trim();
                Game currGame = gameManager.getGames().get(gameNameFromParameter);
                
                //check if request came from xml creation:
                if (currGame == null) {
                    currGame = ServletUtils.getXmlGameFromServletContext(getServletContext());
                }
                currGame.joinPlayer(playerNameFromParameter);
              
                gameManager.addGame(gameNameFromParameter, currGame);
                request.getSession(true).setAttribute(Constants.GAME_NAME, gameNameFromParameter);
                request.getSession().setAttribute(Constants.PLAYER_NAME, playerNameFromParameter);
                                
                sendDataToClient(response, Constants.GAME_HTML);
            } catch (DuplicatePlayerNamesException ex) {
                sendDataToClient(response, Constants.PLAYER_EXISTS);
            }
        }
        else
        {
<<<<<<< HEAD
             String gameNameFromParameter = request.getParameter(Constants.GAME_NAME).trim();
             request.getSession(true).setAttribute(Constants.GAME_NAME, gameNameFromParameter);
             String playerNameFromParameter = request.getParameter(Constants.PLAYER_NAME).trim();
             request.getSession().setAttribute(Constants.PLAYER_NAME, playerNameFromParameter);
             
             if (gameManager.getGames().get(gameNameFromParameter) == null) {
                 gameManager.addGame(gameNameFromParameter, ServletUtils.getXmlGameFromServletContext(getServletContext()));
             }
             
             Game currGame = gameManager.getGames().get(gameNameFromParameter);
            try {
                currGame.joinPlayer(playerNameFromParameter);
            } catch (DuplicatePlayerNamesException ex) {
                Logger.getLogger(StartGameServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
=======
           sendDataToClient(response, Constants.GAME_HTML);
>>>>>>> JoinGameRewrite
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
        private void sendDataToClient(HttpServletResponse response, String data) throws IOException {

        try (PrintWriter out = response.getWriter()) 
        {   
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(data);
            out.print(jsonResponse);
            out.flush();
        }
    }

}
