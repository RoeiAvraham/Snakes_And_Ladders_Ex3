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
import utilities.TurnInfo;

/**
 *
 * @author roei.avraham
 */
@WebServlet(name = "HasAnyPlayerLeftServlet", urlPatterns = {"/hasplayerleft"})
public class HasAnyPlayerLeftServlet extends HttpServlet {

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

            Date currDate = new Date();
            String gameNameFromSession = SessionUtils.getGameName(request);
            GameManager gameManager = ServletUtils.getGameManager(getServletContext());
            Game currGame = gameManager.getGames().get(gameNameFromSession);
            TurnInfo ti = ServletUtils.getTurnInfoFromServletContext(gameNameFromSession, getServletContext());

            if (ti.isHasAnyPlayerLeft()) {
                Gson gson = new Gson();
                String jsonResponse = gson.toJson(ti);
                out.print(jsonResponse);
                out.flush();
            } else {
                if (((Integer.parseInt(currDate.toString()) - Integer.parseInt(currGame.getLastPlayTime().toString())) / 1000) > Constants.SERVER_TIMEOUT) {
                    ServletUtils.retirePlayerFromGame(getServletContext(), currGame, currGame.getCurrPlayer().getPlayerName(), ti);
                }
            }
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
