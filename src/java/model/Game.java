/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import exception.*;
import java.math.BigInteger;
import java.util.*;
import model.Player.LoadedFrom;
import static xmlPackage.PlayerType.*;
import xmlPackage.Snakesandladders;

/**
 *
 * @author Roei
 */
public class Game {

    private String gameName;
    private int m_numSoldiersToWin;
    private int m_numPlayers;
    private GameBoard m_board;
    private LinkedList<Player> playerList;
    private Player currPlayer;
    public static final Integer NUM_SOLDIERS_FOR_EACH_PLAYER = 4;
    private ListIterator<Player> playerItr;
    private Player winner = null;
    private LoadedFrom gameSrc;
    boolean isStarted = false;

    public Game(Snakesandladders gameXml) throws XmlIsInvalidException {
        gameName = gameXml.getName();
        LinkedList<String> playerNames = new LinkedList<String>();
        m_numSoldiersToWin = gameXml.getNumberOfSoldiers();
        m_numPlayers = gameXml.getPlayers().getPlayer().size();
        playerList = new LinkedList<Player>();
        gameSrc = LoadedFrom.XML;

        m_board = new GameBoard(gameXml);
        int i = 0;

        checkIfXmlGameAlreadyFinished(m_numSoldiersToWin, gameXml.getBoard().getCells().getCell(), gameXml.getBoard().getSize());
        checkNumOfSoldiersXml(gameXml.getBoard().getCells().getCell(), gameXml.getPlayers().getPlayer());

        for (xmlPackage.Players.Player p : gameXml.getPlayers().getPlayer()) {
            if (playerNames.contains(p.getName())) {
                throw new DuplicatePlayerNamesXmlException();
            }
            playerNames.add(i, p.getName());
            if (p.getType() == HUMAN) {
                playerList.add(new HumanPlayer(++i, p.getName(), m_board, gameSrc));
            } else if (p.getType() == COMPUTER) {
                playerList.add(new CompPlayer(++i, p.getName(), m_board, gameSrc));
            }
        }

        m_board.setPlayersPosFromXml(gameXml, playerList, playerNames);
        getCurrentPlayerFromXml(gameXml, playerNames);
    }

    public void addNewPlayer(String playerName) throws DuplicatePlayerNamesException {
        if (doesPlayerNameAlreadyExist(playerName)) {
            throw new DuplicatePlayerNamesException();
        }
        HumanPlayer newPlayer = new HumanPlayer(playerList.size() + 1, playerName, m_board, LoadedFrom.REG);
        playerList.add(newPlayer);
        m_numPlayers++;
    }

    private boolean doesPlayerNameAlreadyExist(String name) {
        for (Player p : playerList) {
            if (p.getPlayerName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public Game(int boardSize, int numOfLadders, int numSoldiersToWin, int numPlayers,
            ArrayList<String> playerNames, Player.PlayerType[] playerTypes, String gameName) {

        this.gameName = gameName;
        m_board = new GameBoard(boardSize, numOfLadders, numPlayers);
        m_numSoldiersToWin = numSoldiersToWin;
        m_numPlayers = playerNames.size();
        playerList = new LinkedList<>();
        gameSrc = LoadedFrom.REG;
        int i;
        for (i = 0; i < m_numPlayers; i++) {
            if (playerTypes[i] == Player.PlayerType.COMP) {
                playerList.add(new CompPlayer(i + 1, playerNames.get(i), m_board, gameSrc));
            } else {
                playerList.add(new HumanPlayer(i + 1, playerNames.get(i), m_board, gameSrc));
            }
        }

        int j;
        for (i = 0; i < numPlayers; i++) {
            for (j = 0; j < Player.NUM_SOLDIERS; j++) {
                m_board.getFirstCell().insertSoldier(i + 1);
            }
        }
        currPlayer = playerList.getFirst();
        playerItr = playerList.listIterator();
        setIteratorOnFirstPlayer(playerItr, currPlayer);
    }

    private void setIteratorOnFirstPlayer(Iterator<Player> itr, Player first) {
        Player tmpPlayer = null;
        while (itr.hasNext() && first != tmpPlayer) {
            tmpPlayer = itr.next();
        }
    }

    public Player getWinner() {
        return winner;
    }

    public boolean isWinner(Player player) {
        int lastCell = m_board.getLastCellNum();
        if (m_board.getCell(lastCell).getSoldiersInCell()[player.getPlayerNum() - 1] == m_numSoldiersToWin) {
            winner = player;
            return true;
        } else if (playerList.size() == 1) {
            winner = player;
            return true;
        } else {
            return false;
        }
    }

    public void advanceTurnToNextPlayer() {
        if (playerItr.hasNext()) {
            setCurrPlayer(playerItr.next());
        } else {
            setCurrPlayer(playerList.getFirst());
            playerItr = playerList.listIterator();
            if (playerItr.hasNext()) {
                playerItr.next();
            }
        }

    }

    public void setCurrPlayer(Player curr) {
        this.currPlayer = curr;
    }

    public void checkNumOfSoldiersXml(List<xmlPackage.Cell> xmlCells, List<xmlPackage.Players.Player> playerListXml) throws XmlIsInvalidException {
        HashMap<String, Integer> numSoldiers = new HashMap();
        for (xmlPackage.Players.Player p : playerListXml) {
            numSoldiers.put(p.getName(), m_board.EMPTY);
        }

        for (xmlPackage.Cell c : xmlCells) {
            for (xmlPackage.Cell.Soldiers s : c.getSoldiers()) {
                numSoldiers.put(s.getPlayerName(), numSoldiers.get(s.getPlayerName()) + s.getCount());
            }
        }

        for (Map.Entry<String, Integer> entry
                : numSoldiers.entrySet()) {
            if (entry.getValue() != Player.NUM_SOLDIERS) {
                throw new PlayerHasNoFourSoldiersXmlException();
            }
        }

    }

    public void parseBoardToXml(xmlPackage.Snakesandladders gameToSave) {
        xmlPackage.Board xmlBoard = new xmlPackage.Board();
        gameToSave.setBoard(xmlBoard);
        gameToSave.getBoard().setSize(m_board.getBoardSize());
        xmlPackage.Cells xmlCells = new xmlPackage.Cells();
        xmlBoard.setCells(xmlCells);
        xmlPackage.Snakes xmlSnakes = new xmlPackage.Snakes();
        xmlPackage.Ladders xmlLadders = new xmlPackage.Ladders();
        xmlBoard.setLadders(xmlLadders);
        xmlBoard.setSnakes(xmlSnakes);
        for (Cell c : m_board.getCells()) {
            if (c.isThereSoldiers()) {
                xmlPackage.Cell xmlCell = new xmlPackage.Cell();
                xmlCell.setNumber(BigInteger.valueOf(c.getCellNum()));

                int i;
                for (i = 0; i < c.getSoldiersInCell().length; i++) {
                    if (c.getSoldiersInCell()[i] != GameBoard.EMPTY) {
                        xmlPackage.Cell.Soldiers xmlCellSoldier = new xmlPackage.Cell.Soldiers();
                        xmlCellSoldier.setPlayerName(getPlayerByNum(i + 1).getPlayerName());
                        xmlCellSoldier.setCount(c.getSoldiersInCell()[i]);
                        xmlCell.getSoldiers().add(xmlCellSoldier);
                    }
                }
                xmlCells.getCell().add(xmlCell);
            }

            if (c.getDest() != Cell.NO_DEST) {
                if (c.getCellNum() < c.getDest()) {
                    xmlPackage.Ladders.Ladder xmlLadder = new xmlPackage.Ladders.Ladder();
                    xmlLadder.setFrom(BigInteger.valueOf(c.getCellNum()));
                    xmlLadder.setTo(BigInteger.valueOf(c.getDest()));
                    xmlLadders.getLadder().add(xmlLadder);
                } else {
                    xmlPackage.Snakes.Snake xmlSnake = new xmlPackage.Snakes.Snake();
                    xmlSnake.setFrom(BigInteger.valueOf(c.getCellNum()));
                    xmlSnake.setTo(BigInteger.valueOf(c.getDest()));
                    xmlSnakes.getSnake().add(xmlSnake);
                }
            }
        }
    }

    private void getCurrentPlayerFromXml(Snakesandladders gameXml, LinkedList<String> playerNames) throws XmlIsInvalidException {
        if (!playerNames.contains(gameXml.getCurrentPlayer())) {
            throw new CurrentPlayerIsNotInPlayerListXml();
        }
        currPlayer = playerList.get(playerNames.indexOf(gameXml.getCurrentPlayer()));
        playerItr = playerList.listIterator();
        setIteratorOnFirstPlayer(playerItr, currPlayer);
    }

    public Player getPlayerByNum(int playerNum) {
        int i;
        Player res = null;
        boolean isFound = false;
        for (i = 0; i < playerList.size() && !isFound; i++) {
            if (playerList.get(i).getPlayerNum() == playerNum) {
                isFound = true;
                res = playerList.get(i);
            }
        }
        return res;
    }

    public void checkIfXmlGameAlreadyFinished(int numSoldiersToWin, List<xmlPackage.Cell> cellsXml, int xmlBoardSize)
            throws XmlIsInvalidException {
        for (xmlPackage.Cell c : cellsXml) {
            if (c.getNumber().intValue() == xmlBoardSize * xmlBoardSize) {
                for (xmlPackage.Cell.Soldiers s : c.getSoldiers()) {
                    if (s.getCount() == numSoldiersToWin) {
                        throw new GameAlreadyFinishedXmlException();
                    }
                }
            }
        }
    }

    public xmlPackage.Players parsePlayersToXml(xmlPackage.Snakesandladders gameToSave) {
        xmlPackage.Players xmlPlayers = new xmlPackage.Players();
        for (Player p : playerList) {
            xmlPackage.Players.Player xmlPlayer = new xmlPackage.Players.Player();
            xmlPlayer.setName(p.getPlayerName());
            if (p.getType() == Player.PlayerType.COMP) {
                xmlPlayer.setType(xmlPackage.PlayerType.COMPUTER);
            } else {
                xmlPlayer.setType(xmlPackage.PlayerType.HUMAN);
            }
            xmlPlayers.getPlayer().add(xmlPlayer);

        }
        return xmlPlayers;
    }
    
    public boolean isXMLGame()
    {
        return gameSrc.equals(LoadedFrom.XML);
    }

    public boolean isGameStarted() {
        if (playerList.size() == getM_numPlayers()) {
            isStarted = true;
        }
        return isStarted;
    }

    public Player getCurrPlayer() {
        return currPlayer;
    }

    public GameBoard getBoard() {
        return m_board;
    }

    public LinkedList<Player> getPlayerList() {
        return playerList;
    }

    public void removePlayerFromGame(Player player) {
        player.removePlayerSoldiersFromGame();
        playerItr.remove();
    }

    public int getNumSoldiersToWin() {
        return m_numSoldiersToWin;
    }

    /**
     * @return the m_numPlayers
     */
    public int getM_numPlayers() {
        return m_numPlayers;
    }

    /**
     * @return the gameName
     */
    public String getGameName() {
        return gameName;
    }
}
