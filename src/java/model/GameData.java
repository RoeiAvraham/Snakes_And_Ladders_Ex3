/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.HashMap;
import servlets.GetGameInfoServlet.SnakeOrLadder;

/**
 *
 * @author Roei
 */
public class GameData {

    private int boardSize;
    private int numOfLadders;
    private int numSoldiersToWin;
    private int numPlayers;
    private Player.PlayerType[] playerTypes;
    private HashMap<String, SnakeOrLadder> ladderMap;
    private HashMap<String, SnakeOrLadder> snakeMap;

    public GameData(int boardSize, int numOfLadders, int numSoldiersToWin, int numPlayers, Player.PlayerType[] playerTypes) {
        this.boardSize = boardSize;
        this.numOfLadders = numOfLadders;
        this.numSoldiersToWin = numSoldiersToWin;
        this.numPlayers = numPlayers;
        this.playerTypes = playerTypes;
    }

    /**
     * @return the boardSize
     */
    public int getBoardSize() {
        return boardSize;
    }

    /**
     * @param boardSize the boardSize to set
     */
    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    /**
     * @return the numOfLadders
     */
    public int getNumOfLadders() {
        return numOfLadders;
    }

    /**
     * @param numOfLadders the numOfLadders to set
     */
    public void setNumOfLadders(int numOfLadders) {
        this.numOfLadders = numOfLadders;
    }

    /**
     * @return the numSoldiersToWin
     */
    public int getNumSoldiersToWin() {
        return numSoldiersToWin;
    }

    /**
     * @param numSoldiersToWin the numSoldiersToWin to set
     */
    public void setNumSoldiersToWin(int numSoldiersToWin) {
        this.numSoldiersToWin = numSoldiersToWin;
    }

    /**
     * @return the numPlayers
     */
    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     * @param numPlayers the numPlayers to set
     */
    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    /**
     * @return the playerTypes
     */
    public Player.PlayerType[] getPlayerTypes()
    {
        return playerTypes;
    }
    /**
     * @param playerTypes the playerTypes to set
     */
    public void setPlayerTypes(Player.PlayerType[] playerTypes)
    {
        this.playerTypes = playerTypes;
    }
    
    public void setSnakeAndLadderMaps(HashMap<String, SnakeOrLadder> ladderMap, HashMap<String, SnakeOrLadder> snakeMap) {
        this.ladderMap = ladderMap;
        this.snakeMap = snakeMap;
    }
}
