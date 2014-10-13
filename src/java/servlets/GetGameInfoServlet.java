/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
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
import utilities.TurnInfo;
import utilities.TurnInfoMap;

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
        
            String gameNameFromSession = SessionUtils.getGameName(request);
            GameManager gm = ServletUtils.getGameManager(getServletContext());
            Game currGame = gm.getGames().get(gameNameFromSession);     
            
            HashMap<String, SnakeOrLadder> ladderMap = new HashMap<>();
            HashMap<String, SnakeOrLadder> snakeMap = new HashMap<>();
            ServletUtils.buildLocationMapOfLadders(this,currGame, ladderMap, snakeMap);
            
            TurnInfoMap turnInfoMap = ServletUtils.getTurnInfoMap(getServletContext());
            if (turnInfoMap.getTurnInfo(gameNameFromSession) == null)
            {
                ServletUtils su = new ServletUtils();
                turnInfoMap.putTurnInfo(gameNameFromSession, new TurnInfo(null,null,null,null,null,null,null,false,null));
            }
            turnInfoMap.getTurnInfo(gameNameFromSession).setVersionId(0);
            
            gameInfoForUi gifu = new gameInfoForUi(currGame.getBoard().getBoardSize(), ladderMap, snakeMap,
                                                   currGame.getCurrPlayer().getPlayerNum(),
                                                   currGame.getCurrPlayer().getPlayerName(),
                                                   currGame.getCurrPlayer().getType());
            
            currGame.setLastPlayTime(new Date());
            
            try (PrintWriter out = response.getWriter()) {
            
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(gifu);
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
    
    public class gameInfoForUi
    {   
        int boardSize;
        HashMap<String, SnakeOrLadder> ladderMap;
        HashMap<String, SnakeOrLadder> snakeMap;
        int currPlayerId;
        String currPlayerName;
        Player.PlayerType currPlayerType;
        
        public gameInfoForUi(int boardSize, HashMap<String, SnakeOrLadder> ladderMap, HashMap<String, SnakeOrLadder> snakeMap,
                             int currPlayerId, String currPlayerName, Player.PlayerType currPlayerType)
        {
            this.boardSize = boardSize;
            this.ladderMap = ladderMap;
            this.snakeMap = snakeMap;
            this.currPlayerId = currPlayerId;
            this.currPlayerName = currPlayerName;
            this.currPlayerType = currPlayerType;
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
