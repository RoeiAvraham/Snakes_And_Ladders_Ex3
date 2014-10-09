/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.google.gson.Gson;
import exception.DuplicateGameNameException;
import exception.DuplicatePlayerNamesException;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Game;
import model.GameData;
import model.GameManager;
import model.Player.PlayerType;
import utilities.Constants;
import utilities.ServletUtils;
import static utilities.ServletUtils.*;
import utilities.SessionUtils;

/**
 *
 * @author Roei
 */
@WebServlet(name = "CreateNewGame", urlPatterns = {"/newgame"})
public class CreateNewGameServlet extends HttpServlet {

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
            //user has not joined\created a game yet
            String gameNameFromParameter = request.getParameter(Constants.GAME_NAME).trim();
            GameData gData = createGameDataFromRequest(request);
            
            try {
                Game game = gameManager.createNewGame(gameNameFromParameter, gData);
                gameManager.addGame(gameNameFromParameter, game);
                request.getSession(true).setAttribute(Constants.GAME_NAME, gameNameFromParameter);
                String playerNameFromParameter = request.getParameter(Constants.PLAYER_NAME).trim();
                request.getSession().setAttribute(Constants.PLAYER_NAME, playerNameFromParameter);
                game.joinPlayer(playerNameFromParameter);
                ServletUtils.addToNewlyJoinedPlayersMap(gameNameFromSession, game.getPlayerNumByName(playerNameFromParameter), getServletContext());
                sendDataToClient(response, true, gameNameFromParameter);
            } catch (DuplicateGameNameException | DuplicatePlayerNamesException ex) {
                sendDataToClient(response, false, gameNameFromParameter);
            }
        } else {
            sendDataToClient(response, true, gameNameFromSession);
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

    private GameData createGameDataFromRequest(HttpServletRequest request) {
        int boardSizeFromParameter = Integer.parseInt(request.getParameter(Constants.BOARD_SIZE));
        int numSnakesFromParameter = Integer.parseInt(request.getParameter(Constants.NUM_OF_SNAKES));
        int numSoldiersToWinFromParameter = Integer.parseInt(request.getParameter(Constants.NUM_SOLDIERS_TO_WIN));
        int numPlayersFromParameter = Integer.parseInt(request.getParameter(Constants.NUM_OF_PLAYERS));
        PlayerType[] playerTypes = createPlayerTypesFromRequest(request, numPlayersFromParameter);

        return new GameData(boardSizeFromParameter, numSnakesFromParameter, numSoldiersToWinFromParameter, numPlayersFromParameter, playerTypes);
    }

    private void sendDataToClient(HttpServletResponse response, boolean wasGameCreated, String gameName) throws IOException {

        String data;

        try (PrintWriter out = response.getWriter()) {

            if (wasGameCreated) {
                data = Constants.GAME_HTML;
            } else {
                data = Constants.GAME_NAME_ERROR;
            }

            CreateGameResult cgr = new CreateGameResult(wasGameCreated, data, gameName);

            Gson gson = new Gson();
            String jsonResponse = gson.toJson(cgr);
            out.print(jsonResponse);
            out.flush();
        }
    }

    class CreateGameResult {

        boolean wasGameCreated;
        String data;
        String gameName;

        public CreateGameResult(boolean wasGameCreated, String data, String gameName) {
            this.wasGameCreated = wasGameCreated;
            this.data = data;
            this.gameName = gameName;
        }
    }
}
