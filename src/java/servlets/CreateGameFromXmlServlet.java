/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.google.gson.Gson;
import exception.DuplicateGameNameException;
import exception.XmlIsInvalidException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.JAXBException;
import model.Game;
import model.GameManager;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.xml.sax.SAXException;
import utilities.Constants;
import utilities.ServletUtils;
import utilities.SessionUtils;
import utilities.XmlUtils;
import xmlPackage.Snakesandladders;

/**
 *
 * @author Anat
 */
@WebServlet(name = "CreateGameFromXmlServlet", urlPatterns = {"/newxmlgame"})
@MultipartConfig
public class CreateGameFromXmlServlet extends HttpServlet {

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
            try {
                Snakesandladders xmlGame = XmlUtils.loadGameFromXml(saveRequestBodyToXml(request), getServletContext());
                Game game = gameManager.createNewGame(xmlGame.getName(), xmlGame);
                ServletUtils.setXmlGameInServletContext(getServletContext(), game);
                getServletContext().getRequestDispatcher("/getnamesxml?" + Constants.GAME_NAME + "=" + xmlGame.getName()).forward(request, response);
            } catch (DuplicateGameNameException ex) {
                sendDataToClient(response, false, Constants.GAME_NAME_ERROR);
            } catch (JAXBException ex) {
                sendDataToClient(response, false, Constants.XML_FILE_ERROR);
            } catch (SAXException ex) {
                sendDataToClient(response, false, Constants.XML_FILE_ERROR);
            } catch (XmlIsInvalidException ex) {
                sendDataToClient(response, false, Constants.XML_INVALID_ERROR);
            }
        } else {
            sendDataToClient(response, false, Constants.GAME_HTML);
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

    private File saveRequestBodyToXml(HttpServletRequest request) throws ServletException, IOException {
        final Part filePart = request.getPart("file");
        InputStream filecontent = filePart.getInputStream();
        String fileUUIDName = UUID.randomUUID().toString();
        File xmlfile = new File(fileUUIDName + ".xml");
        try (OutputStream outputStream = new FileOutputStream(xmlfile)) {
            IOUtils.copy(filecontent, outputStream);
            filecontent.close();
            outputStream.close();
        }
        return xmlfile;
    }

    private void sendDataToClient(HttpServletResponse response, boolean isXmlGameAndIsReady, String data) throws IOException {

        try (PrintWriter out = response.getWriter()) {

            CreateXmlGameError cxge = new CreateXmlGameError(isXmlGameAndIsReady, data);
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(cxge);
            out.print(jsonResponse);
            out.flush();
        }
    }

    class CreateXmlGameError {

        boolean isXmlGameAndIsReady;
        String data;

        public CreateXmlGameError(boolean isXmlGameAndIsReady, String data) {
            this.isXmlGameAndIsReady = isXmlGameAndIsReady;
            this.data = data;
        }
    }
}
