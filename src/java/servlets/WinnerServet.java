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
import model.Player;
import model.Player.PlayerType;
import utilities.ServletUtils;
import utilities.SessionUtils;

/**
 *
 * @author Anat
 */
@WebServlet(name = "WinnerServet", urlPatterns = {"/winner"})
public class WinnerServet extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            String gameNameFromSession = SessionUtils.getGameName(request);
            GameManager gameManager = ServletUtils.getGameManager(getServletContext());
            Game game = gameManager.getGames().get(gameNameFromSession);
            Player winner = game.getWinner();
            String name = winner.getPlayerName();
            int id = winner.getPlayerNum();
            PlayerType type = winner.getType();
            WinnerData wd = new WinnerData(id, name, type);
            
            //delete game and sessions
            ServletUtils.removeGameFromGameManager(gameNameFromSession, getServletContext());
            SessionUtils.clearSession(request);
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(wd);
            out.print(jsonResponse);
            out.flush();
        }
    }

    private class WinnerData {

        int winnerID;
        String winnerName;
        PlayerType type;

        public WinnerData(int id, String name, PlayerType type) {
            winnerID = id;
            winnerName = name;
            this.type = type;
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

}
