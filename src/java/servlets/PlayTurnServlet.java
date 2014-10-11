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
import model.Player.PlayerType;
import static model.Player.PlayerType.HUMAN;
import model.TurnData;
import utilities.Constants;
import utilities.ServletUtils;
import utilities.SessionUtils;

/**
 *
 * @author Anat
 */
@WebServlet(name = "PlayTurnServlet", urlPatterns = {"/playturn"})
public class PlayTurnServlet extends HttpServlet {

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
            int soldierNum;
            if (game.getCurrPlayer().getType() == HUMAN)
            {
              soldierNum = Integer.parseInt(request.getParameter(Constants.SOLDIER_ID));  
            }
            else
            {
                soldierNum = game.getCurrPlayer().chooseSoldierToMove();
            }
             
            int diceRes = Integer.parseInt(request.getParameter(Constants.DICE_RES));
            TurnData data = game.getCurrPlayer().playTurn(soldierNum, diceRes);
            boolean isWinner = game.isWinner(game.getCurrPlayer());
            game.advanceTurnToNextPlayer();
            String newCurrPlayerName = game.getCurrPlayer().getPlayerName();
            int newCurrPlayerID = game.getCurrPlayer().getPlayerNum();
            PlayerType newCurrPlayerType = game.getCurrPlayer().getType();

            TurnInfo ti = new TurnInfo(data, newCurrPlayerName, newCurrPlayerID, newCurrPlayerType, isWinner);

            Gson gson = new Gson();
            String jsonResponse = gson.toJson(ti);
            out.print(jsonResponse);
            out.flush();
        }
    }

    class TurnInfo {

        TurnData turnData;
        String newCurrPlayerName;
        int newCurrPlayerID;
        PlayerType newCurrPlayerType;
        boolean isThereWinner;

        public TurnInfo(TurnData td, String newCurrPlayerName, int newCurrPlayerID, PlayerType newCurrPlayerType, boolean isWinner) {
            turnData = td;
            this.newCurrPlayerName = newCurrPlayerName;
            this.newCurrPlayerID = newCurrPlayerID;
            this.newCurrPlayerType = newCurrPlayerType;
            isThereWinner = isWinner;
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
