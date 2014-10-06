/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import exception.DuplicateGameNameException;
import exception.XmlIsInvalidException;
import java.util.HashMap;
import xmlPackage.Snakesandladders;

/**
 *
 * @author Roei
 */
public class GameManager
{
    private final HashMap<String, Game> games;

    public GameManager()
    {
        games = new HashMap<>();
    }
        
    public void addGame(String gameName, Game game)
    {        
        games.put(gameName, game);
    }
    
    public Game createNewGame(String gameName, GameData gameData) throws DuplicateGameNameException
    {
        if (games.containsKey(gameName))
        {
            throw new DuplicateGameNameException();
        }
        return (new Game(gameData.getBoardSize(),
                gameData.getNumOfLadders(),
                gameData.getNumSoldiersToWin(),
                gameData.getNumPlayers(),
                gameData.getPlayerNames(),
                gameData.getPlayerTypes(), gameName));
    }
    
    public Game createNewGame(String gameName, Snakesandladders gameXml) throws XmlIsInvalidException, DuplicateGameNameException
    {
        if (games.containsKey(gameName))
        {
            throw new DuplicateGameNameException();
        }
        
        Game res = new Game(gameXml);
        return res;
    }
    
    public void removeGame(String gameName)
    {
        games.remove(gameName);
    }
    
    public HashMap<String, Game> getGames()
    {
        return games;
    }
    
    public boolean isGameStarted(String gameName)
    {
        return games.get(gameName).isGameStarted();
    }
}
