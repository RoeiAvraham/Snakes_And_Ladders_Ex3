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
import model.GameManager;
import model.Player;
import utilities.ServletUtils;
import utilities.SessionUtils;

/**
 *
 * @author roei.avraham
 */
@WebServlet(name = "GetSoldierMapOfJoinedPlayer", urlPatterns = {"/getsoldiermap"})
public class GetSoldierMap extends HttpServlet {

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

        HashMap<Integer, HashMap<Integer, SoldierData>> soldierMap = new HashMap<>();

        for (Player p : currGame.getPlayerList()) {
            soldierMap.put(p.getPlayerNum(), createSoldierMap(p.getPlayerNum(), gameManager.getGames().get(gameNameFromSession)));
        }

        sendDataToClient(response, true, soldierMap);
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

    private void sendDataToClient(HttpServletResponse response, boolean areThereNewPlayers,
            HashMap<Integer, HashMap<Integer, SoldierData>> soldierMap) throws IOException {

        try (PrintWriter out = response.getWriter()) {

            NewPlayersSoldierMap npsm = new NewPlayersSoldierMap(soldierMap);

            Gson gson = new Gson();
            String jsonResponse = gson.toJson(npsm);
            out.print(jsonResponse);
            out.flush();
        }
    }

    private HashMap<Integer, SoldierData> createSoldierMap(int playerNum, Game currGame) {

        int[] soldierPos = currGame.getPlayerByNum(playerNum).getSoldiersPos();
        HashMap<Integer, SoldierData> res = new HashMap<>();

        for (int i = 0; i < soldierPos.length; i++) {
            if (res.containsKey(soldierPos[i])) {
                res.get(soldierPos[i]).incrementSoldierAmount();
            } else {
                res.put(soldierPos[i], new SoldierData(currGame.getPlayerByNum(playerNum).getType(), (i + 1), 1));
            }
        }

        return res;
    }

    class SoldierData {

        Player.PlayerType playerType;
        int soldierNum;
        int soldierAmount;

        public SoldierData(Player.PlayerType playerType, int soldierNum, int soldierAmount) {
            this.playerType = playerType;
            this.soldierNum = soldierNum;
            this.soldierAmount = soldierAmount;
        }

        public void incrementSoldierAmount() {
            this.soldierAmount++;
        }
    }

    class NewPlayersSoldierMap {

        HashMap<Integer, HashMap<Integer, SoldierData>> soldierMap;

        public NewPlayersSoldierMap(HashMap<Integer, HashMap<Integer, SoldierData>> soldierMap) {
            this.soldierMap = soldierMap;
        }
    }
}
