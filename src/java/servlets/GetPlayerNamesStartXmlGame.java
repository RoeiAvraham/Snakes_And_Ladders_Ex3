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
@WebServlet(name = "GetPlayerNamesStartXmlGame", urlPatterns = {"/getnamesxml"})
public class GetPlayerNamesStartXmlGame extends HttpServlet {

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
        Game game;
        if (gameNameFromSession == null) {
            String gameNameFromParameter = request.getParameter(Constants.GAME_NAME).trim();
            try (PrintWriter out = response.getWriter()) {
                if (gameManager.getGames().get(gameNameFromParameter) == null) {
                    game = ServletUtils.getXmlGameFromServletContext(getServletContext());
                } else {
                    game = gameManager.getGames().get(gameNameFromParameter);
                }
                ArrayList<String> playerNames = ServletUtils.getHumanFreeNamesList(game);
                
                PlayerNamesGameName pngn = new PlayerNamesGameName(playerNames, gameNameFromParameter, true);
                Gson gson = new Gson();
                String jsonResponse = gson.toJson(pngn);
                out.print(jsonResponse);
                out.flush();
            }
        } else {
            response.sendRedirect(Constants.GAME_HTML);
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
    
    class PlayerNamesGameName
    {
        ArrayList<String> playerNames;
        String gameName;
        boolean isXmlGameAndIsReady;
        
        public PlayerNamesGameName(ArrayList<String> playerNames, String gameName, boolean isXmlGameAndIsReady)
        {
            this.playerNames = playerNames;
            this.gameName = gameName;
            this.isXmlGameAndIsReady = isXmlGameAndIsReady;
        }
    }

}
