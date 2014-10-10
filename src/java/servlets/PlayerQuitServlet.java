/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(name = "PlayerQuitServlet", urlPatterns = {"/quit"})
public class PlayerQuitServlet extends HttpServlet {

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
        Game currGame = gameManager.getGames().get(gameNameFromSession);
        String playerNameFromSession = (String) request.getSession().getAttribute(Constants.PLAYER_NAME);
        
        if (currGame.isGameStarted())
        {
            currGame.removePlayerFromGame(currGame.getPlayerByName(playerNameFromSession));
        }
        else
        {
            currGame.getPlayerByName(playerNameFromSession).setIsJoined(false);
            currGame.getPlayerByName(playerNameFromSession).setPlayerName(null);
        }
      
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            String jsonResponse;
            
            WinnerPlayer wp;
            
            if (currGame.isWinner(currGame.getPlayerList().getFirst()))
            {
                wp = new WinnerPlayer(true, currGame.getPlayerList().getFirst().getPlayerName());
            }
            else
            {
                wp = new WinnerPlayer(false, null);
            }
            
            jsonResponse = gson.toJson(wp);
            out.print(jsonResponse);
            out.flush();
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
    
    class WinnerPlayer
    {
        boolean isWinner;
        String winnerName;
        
        public WinnerPlayer(boolean isWinner, String winnerName)
        {
            this.isWinner = isWinner;
            this.winnerName = winnerName;
        }
    }
}
