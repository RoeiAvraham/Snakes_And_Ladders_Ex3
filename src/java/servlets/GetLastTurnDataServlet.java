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
import static model.Player.PlayerType.HUMAN;
import utilities.Constants;
import utilities.ServletUtils;
import utilities.SessionUtils;
import utilities.TurnInfo;

/**
 *
 * @author Anat
 */
@WebServlet(name = "GetLastTurnDataServlet", urlPatterns = {"/lastturn"})
public class GetLastTurnDataServlet extends HttpServlet {

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

            Integer serverVersionID = ServletUtils.getTurnInfoFromServletContext(gameNameFromSession, getServletContext()).getVersionId();
            Integer clientVersionID = Integer.parseInt(request.getParameter(Constants.VERSION_ID));
            String playerNameFromSession = (String) request.getSession().getAttribute(Constants.PLAYER_NAME);
            Gson gson = new Gson();
            String jsonResponse;
            if (clientVersionID < serverVersionID) {
                TurnInfo ti = ServletUtils.getTurnInfoFromServletContext(gameNameFromSession, getServletContext());

                if (ti.getNextPlayerType() == HUMAN) {
                    if (playerNameFromSession.equals(ti.getNextPlayerName())) {
                        ti.setIsPlayerSessionTurn(true);
                    } else {
                        ti.setIsPlayerSessionTurn(false);
                    }
                    jsonResponse = gson.toJson(ti);

                } else {
                    jsonResponse = gson.toJson(serverVersionID);
                }
                out.print(jsonResponse);
                out.flush();
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
