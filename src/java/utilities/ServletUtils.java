package utilities;

import java.util.ArrayList;
import java.util.Date;
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


public class ServletUtils {

    private static final String GAME_MANAGER_ATTRIBUTE_NAME = "gameManager";
    private static final String GAME_XML_ATTRIBUTE_NAME = "xmlGame";
    private static final String TURN_INFO_MAP_ATTRIBUTE_NAME = "turnInfoMap";
    private static final String QUIT_PLAYER_MAP_ATTRIBUTE_NAME = "quitPlayerMap";

    public static GameManager getGameManager(ServletContext servletContext) {
        if (servletContext.getAttribute(GAME_MANAGER_ATTRIBUTE_NAME) == null) {
            servletContext.setAttribute(GAME_MANAGER_ATTRIBUTE_NAME, new GameManager());
        }
        return (GameManager) servletContext.getAttribute(GAME_MANAGER_ATTRIBUTE_NAME);
    }
    
    public static void removeGameFromGameManager(String gameName, ServletContext servletContext)
    {
        GameManager gameManager = getGameManager(servletContext);
        gameManager.removeGame(gameName);
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
        Player.PlayerType[] res = new Player.PlayerType[numPlayers];
        res[0] = Player.PlayerType.HUMAN;
        int i;
        for (i = 1; i <= numCompPlayers; i++) {
            res[i] = Player.PlayerType.COMP;
        }

        for (int j = i; j < numPlayers; j++) {
            res[j] = Player.PlayerType.HUMAN;
        }
        return res;
    }

    public static Player.PlayerType[] getJoinedPlayerTypes(Game game) {
        Player.PlayerType[] res = new Player.PlayerType[game.getPlayerList().size()];
        int i = 0;
        for (Player p : game.getPlayerList()) {
            if (p.isJoined()) {
                res[i] = p.getType();
                i++;
            }
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

    public static ArrayList<String> getJoinedPlayerNames(Game game) {
        ArrayList<String> names = new ArrayList<>();

        for (Player p : game.getPlayerList()) {
            if (p.isJoined()) {
                names.add(p.getPlayerName());
            }
        }
        return names;
    }

    public static ArrayList<String> getHumanFreeNamesList(Game game) {
        ArrayList<String> names = new ArrayList<>();

        for (Player p : game.getPlayerList()) {
            if (p.getType() == PlayerType.HUMAN && !p.isJoined()) {
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
                    ladderMap.put("ladder" + ladderCounter, ggis.new SnakeOrLadder(cell.getCellNum(), cell.getDest()));
                    ladderCounter++;
                } else {
                    snakeMap.put("snake" + snakeCounter, ggis.new SnakeOrLadder(cell.getCellNum(), cell.getDest()));
                    snakeCounter++;
                }
            }
        }
    }

    public static void SetTurnInfoInServletContext(String gameName, TurnInfo turnInfo, ServletContext servletContext) {
        TurnInfoMap turnInfoMap = getTurnInfoMap(servletContext);
        turnInfoMap.putTurnInfo(gameName, turnInfo);
    }

    public static TurnInfo getTurnInfoFromServletContext(String gameName, ServletContext servletContext) {
        return (TurnInfo) ((TurnInfoMap) servletContext.getAttribute(TURN_INFO_MAP_ATTRIBUTE_NAME)).getTurnInfo(gameName);
    }

    public static TurnInfoMap getTurnInfoMap(ServletContext servletContext) {
        if (servletContext.getAttribute(TURN_INFO_MAP_ATTRIBUTE_NAME) == null) {
            servletContext.setAttribute(TURN_INFO_MAP_ATTRIBUTE_NAME, new TurnInfoMap(new HashMap<String, TurnInfo>()));
        }
        return (TurnInfoMap) servletContext.getAttribute(TURN_INFO_MAP_ATTRIBUTE_NAME);
    }
    
    public static QuitPlayerMap getQuitPlayerMap(ServletContext servletContext) {
        if (servletContext.getAttribute(QUIT_PLAYER_MAP_ATTRIBUTE_NAME) == null) {
            servletContext.setAttribute(QUIT_PLAYER_MAP_ATTRIBUTE_NAME, new QuitPlayerMap(new HashMap<String, QuitPlayer>()));
        }
        return (QuitPlayerMap) servletContext.getAttribute(QUIT_PLAYER_MAP_ATTRIBUTE_NAME);
    }
    
    public static void SetQuitPlayerInServletContext(String gameName, QuitPlayer qp, ServletContext servletContext) {
        QuitPlayerMap qpMap = getQuitPlayerMap(servletContext);
        qpMap.putQuitPlayer(gameName, qp);
    }

    public static QuitPlayer getQuitPlayerFromServletContext(String gameName, ServletContext servletContext) {
        QuitPlayerMap qpMap = getQuitPlayerMap(servletContext);
        return qpMap.getQuitPlayer(gameName);
    }
    
    public static void retirePlayerFromGame(ServletContext servletContext, Game currGame, String playerLeftName, QuitPlayer qp)
    {
        currGame.setLastPlayTime(new Date());
        
        if (getQuitPlayerFromServletContext(currGame.getGameName(), servletContext) == null)
        {
            SetQuitPlayerInServletContext(currGame.getGameName(), new QuitPlayer(), servletContext);
        }
        
        if (playerLeftName.equals(currGame.getCurrPlayer().getPlayerName()))
        {
            currGame.advanceTurnToNextPlayer();
        }
        qp.setPlayerLeftID(currGame.getPlayerNumByName(playerLeftName));
        currGame.removePlayerFromGame(currGame.getPlayerByName(playerLeftName));
        qp.setHasAnyPlayerLeft(true);
        qp.setPlayerLeftName(playerLeftName);
        
        int currQuitVersion = ServletUtils.getQuitPlayerFromServletContext(currGame.getGameName(), servletContext).getQuitVersionID();
        qp.setQuitVersionID(currQuitVersion + 1);
        ServletUtils.SetQuitPlayerInServletContext(currGame.getGameName(), qp, servletContext);
    }
}
