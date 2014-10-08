/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Game;
import model.GameData;
import model.GameManager;
import utilities.ServletUtils;
import utilities.SessionUtils;

/**
 *
 * @author ANAT-EL
 */
@WebServlet(name = "GetGameInfoServlet", urlPatterns = {"/gameinfo"})
public class GetGameInfoServlet extends HttpServlet {

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
            GameManager gm = ServletUtils.getGameManager(getServletContext());
            Game currGame = gm.getGames().get(gameNameFromSession);
            GameData gd = new GameData(currGame.getBoard().getBoardSize(), currGame.getBoard().getNumberOfSnakesAndLadders(),
            currGame.getNumSoldiersToWin(), currGame.getPlayerList().size(), ServletUtils.getJoinedPlayerTypes(currGame));
            HashMap<String, SnakeOrLadder> ladderMap = new HashMap<>();
            HashMap<String, SnakeOrLadder> snakeMap = new HashMap<>();
            ServletUtils.buildLocationMapOfLadders(this,currGame, ladderMap, snakeMap);
            gd.setSnakeAndLadderMaps(ladderMap, snakeMap);
            
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(gd);
            jsonResponse = jsonResponse.substring(0, jsonResponse.length() - 1);
            jsonResponse = jsonResponse.concat(",\"gameName\":\"" + gameNameFromSession + "\"}");

            out.print(jsonResponse);
            out.flush();
        }
    }
    public class SnakeOrLadder
    {
        int from;
        int to;
        
        public SnakeOrLadder(int from, int to)
        {
            this.from = from;
            this.to = to;
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
