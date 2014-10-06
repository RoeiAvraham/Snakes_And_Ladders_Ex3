/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Game;
import model.GameManager;
import model.Player;
import utilities.ServletUtils;
import utilities.SessionUtils;

/**
 *
 * @author roei.avraham
 */
@WebServlet(name = "GetJoinedPlayersListServlet", urlPatterns = {"/getjoinedplayers"})
public class GetJoinedPlayersListServlet extends HttpServlet {

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
        ArrayList<String> playerNames;
        Player.PlayerType[] playerTypes;
        
        
        try (PrintWriter out = response.getWriter()) 
        {
            String gameNameFromSession = SessionUtils.getGameName(request);
            GameManager gm = ServletUtils.getGameManager(getServletContext());
            Game currGame = gm.getGames().get(gameNameFromSession);
            
            playerNames = ServletUtils.createPlayerNamesFromGame(currGame);
            playerTypes = ServletUtils.createPlayerTypesFromGame(currGame);
            
            int howManyLeftToJoin = (currGame.getM_numPlayers()) - (playerNames.size());
            
            JoinedPlayerNamesTypes jpnt = new JoinedPlayerNamesTypes(playerNames,playerTypes,howManyLeftToJoin);
            
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(jpnt);
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
    
    class JoinedPlayerNamesTypes
    {
        ArrayList<String> playerNames;
        Player.PlayerType[] playerTypes;
        int howManyLeftToJoin;
        
        public JoinedPlayerNamesTypes(ArrayList<String> playerNames, Player.PlayerType[] playerTypes, int howManyLeftToJoin)
        {
            this.playerNames = playerNames;
            this.playerTypes = playerTypes;
            this.howManyLeftToJoin = howManyLeftToJoin;
        }
    }
}
