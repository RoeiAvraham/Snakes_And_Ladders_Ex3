package utilities;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import model.Cell;
import model.Game;
import model.GameManager;
import model.Player;
import model.Player.PlayerType;
import servlets.GetGameInfoServlet;
import servlets.GetGameInfoServlet.SnakeOrLadder;

/**
 *
 * @author blecherl
 */
public class ServletUtils {

    private static final String GAME_MANAGER_ATTRIBUTE_NAME = "gameManager";
    private static final String GAME_XML_ATTRIBUTE_NAME = "xmlGame";

    public static GameManager getGameManager(ServletContext servletContext) {
        if (servletContext.getAttribute(GAME_MANAGER_ATTRIBUTE_NAME) == null) {
            servletContext.setAttribute(GAME_MANAGER_ATTRIBUTE_NAME, new GameManager());
        }
        return (GameManager) servletContext.getAttribute(GAME_MANAGER_ATTRIBUTE_NAME);
    }

    public static void setXmlGameInServletContext(ServletContext servletContext, Game xmlGame) {
        servletContext.setAttribute(GAME_XML_ATTRIBUTE_NAME, xmlGame);
    }

    public static Game getXmlGameFromServletContext(ServletContext servletContext) {
        return (Game) servletContext.getAttribute(GAME_XML_ATTRIBUTE_NAME);
    }

    public static Player.PlayerType[] createPlayerTypesFromRequest(HttpServletRequest request, int numPlayers) {
        int numHumanPlayers = Integer.parseInt(request.getParameter(Constants.NUM_OF_HUMAN_PLAYERS));
        int numCompPlayers = numPlayers - numHumanPlayers;
        Player.PlayerType[] res = new Player.PlayerType[numCompPlayers + 1];
        res[0] = Player.PlayerType.HUMAN;
        int i;
        for (i = 1; i <= numCompPlayers; i++) {
            res[i] = Player.PlayerType.COMP;
        }
        return res;
    }

    public static Player.PlayerType[] createPlayerTypesFromGame(Game game) {
        Player.PlayerType[] res = new Player.PlayerType[game.getPlayerList().size()];
        int i = 0;
        for (Player p : game.getPlayerList()) {
            res[i] = p.getType();
            i++;
        }

        return res;
    }

    public static ArrayList<String> createPlayerNamesFromRequest(HttpServletRequest request, int numPlayers) {
        ArrayList<String> names = new ArrayList<>();
        int numHumanPlayers = Integer.parseInt(request.getParameter(Constants.NUM_OF_HUMAN_PLAYERS));
        int numCompPlayers = numPlayers - numHumanPlayers;
        String playerName = request.getParameter(Constants.PLAYER_NAME).trim();
        names.add(playerName);
        int i;
        for (i = 0; i < numCompPlayers; i++) {
            names.add("Comp" + (i + 1));
        }
        return names;
    }

    public static ArrayList<String> createPlayerNamesFromGame(Game game) {
        ArrayList<String> names = new ArrayList<>();

        int i = 0;

        for (Player p : game.getPlayerList()) {
            names.add(p.getPlayerName());
        }
        return names;
    }

    public static ArrayList<String> createHumanPlayerNamesList(Game game) {
        ArrayList<String> names = new ArrayList<>();

        int i = 0;

        for (Player p : game.getPlayerList()) {
            if (p.getType() == PlayerType.HUMAN) {
                names.add(p.getPlayerName());
            }
        }
        return names;
    }

    public static void buildLocationMapOfLadders(GetGameInfoServlet ggis, Game game, HashMap<String, SnakeOrLadder> ladderMap, HashMap<String, SnakeOrLadder> snakeMap) {
        int snakeCounter = 1;
        int ladderCounter = 1;

        for (Cell cell : game.getBoard().getCells()) {
            if (cell.getDest() != Cell.NO_DEST) {
                if (cell.getCellNum() < cell.getDest()) {
                //    ladderMap.put("ladder" + ladderCounter, new Point(cell.getCellNum(),cell.getDest()));
                    ladderMap.put("ladder" + ladderCounter, ggis. new SnakeOrLadder(cell.getCellNum(), cell.getDest()));
                    ladderCounter++;
                } else {
                    snakeMap.put("snake" + snakeCounter, ggis. new  SnakeOrLadder(cell.getCellNum(),cell.getDest()));
                    snakeCounter++;
                }
            }
        }
    }
    
    
}
