/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;


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
import static model.Player.PlayerType.HUMAN;
import model.TurnData;
import utilities.Constants;
import utilities.ServletUtils;
import utilities.SessionUtils;
import utilities.TurnInfo;

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
            int soldierNum, diceRes;

            if (game.getCurrPlayer().getType() == HUMAN) {
                soldierNum = Integer.parseInt(request.getParameter(Constants.SOLDIER_ID));
                diceRes = Integer.parseInt(request.getParameter(Constants.DICE_RES));
            } else//computer
            {
                diceRes = game.getCurrPlayer().throwDice();
                soldierNum = game.getCurrPlayer().chooseSoldierToMove();
            }

            String playerName = game.getCurrPlayer().getPlayerName();
            PlayerType playerType = game.getCurrPlayer().getType();
            int playerID = game.getCurrPlayer().getPlayerNum();
            Integer nextFreeID = getNextFreeIdForSplitting(soldierNum, game.getCurrPlayer().getSoldiersPos()[soldierNum-1], game);
            TurnData data = game.getCurrPlayer().playTurn(soldierNum, diceRes);
            boolean isWinner = game.isWinner(game.getCurrPlayer());
            

            game.advanceTurnToNextPlayer();

            String newCurrPlayerName = game.getCurrPlayer().getPlayerName();
            int newCurrPlayerID = game.getCurrPlayer().getPlayerNum();
            PlayerType newCurrPlayerType = game.getCurrPlayer().getType();
            

            int currVersion = ServletUtils.getTurnInfoFromServletContext(gameNameFromSession, getServletContext()).getVersionId();
            
            TurnInfo ti = new TurnInfo(playerName, playerType, playerID, data, newCurrPlayerName, newCurrPlayerID, newCurrPlayerType, isWinner, nextFreeID);

            ti.setVersionId(currVersion + 1);
            int clientVersionID = Integer.parseInt(request.getParameter("versionID"));
            ServletUtils.SetTurnInfoInServletContext(gameNameFromSession, ti, getServletContext());
            getServletContext().getRequestDispatcher("/lastturn?" + "versionID" + "=" + clientVersionID).forward(request, response);
        }
    }

    private Integer getNextFreeIdForSplitting(int soldierNum, int sourceCell, Game game)
    {
        int res =0;
        boolean isFound = false;
        int[] soldierPosition =game.getCurrPlayer().getSoldiersPos();
        int i;
        for (i=0;i<Player.NUM_SOLDIERS&&!isFound;i++)
        {
            if (soldierPosition[i] == sourceCell && soldierNum!=(i+1))
            {
                res = i+1;
                isFound = true;
            }
        }
        return res;
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
