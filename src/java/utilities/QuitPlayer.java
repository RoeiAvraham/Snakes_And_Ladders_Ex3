/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import model.Player;

/**
 *
 * @author roei.avraham
 */
public class QuitPlayer {

        private Integer quitVersionID;
        private boolean hasAnyPlayerLeft;
        private String playerLeftName;
        private int playerLeftID;
        private String nextPlayerName;
        private Integer nextPlayerID;
        private Player.PlayerType nextPlayerType;
        private boolean isThereWinner;
        

        public QuitPlayer() {
            this.quitVersionID = 0;
        }
        
        public QuitPlayer(boolean hasAnyPlayerLeft, String playerLeftName, int playerLeftID)
        {
            this.hasAnyPlayerLeft = hasAnyPlayerLeft;
            this.playerLeftName = playerLeftName;
            this.playerLeftID = playerLeftID;
        }

    /**
     * @return the nextPlayerName
     */
    public String getNextPlayerName() {
        return nextPlayerName;
    }

    /**
     * @param nextPlayerName the nextPlayerName to set
     */
    public void setNextPlayerName(String nextPlayerName) {
        this.nextPlayerName = nextPlayerName;
    }

    /**
     * @return the nextPlayerID
     */
    public Integer getNextPlayerID() {
        return nextPlayerID;
    }

    /**
     * @param nextPlayerID the nextPlayerID to set
     */
    public void setNextPlayerID(Integer nextPlayerID) {
        this.nextPlayerID = nextPlayerID;
    }

    /**
     * @return the nextPlayerType
     */
    public Player.PlayerType getNextPlayerType() {
        return nextPlayerType;
    }

    /**
     * @param nextPlayerType the nextPlayerType to set
     */
    public void setNextPlayerType(Player.PlayerType nextPlayerType) {
        this.nextPlayerType = nextPlayerType;
    }

    /**
     * @param hasAnyPlayerLeft the hasAnyPlayerLeft to set
     */
    public void setHasAnyPlayerLeft(boolean hasAnyPlayerLeft) {
        this.hasAnyPlayerLeft = hasAnyPlayerLeft;
    }

    /**
     * @param playerLeftName the playerLeftName to set
     */
    public void setPlayerLeftName(String playerLeftName) {
        this.playerLeftName = playerLeftName;
    }

    /**
     * @param playerLeftID the playerLeftID to set
     */
    public void setPlayerLeftID(int playerLeftID) {
        this.playerLeftID = playerLeftID;
    }

    /**
     * @return the quitVersionID
     */
    public Integer getQuitVersionID() {
        return quitVersionID;
    }

    /**
     * @param quitVersionID the quitVersionID to set
     */
    public void setQuitVersionID(Integer quitVersionID) {
        this.quitVersionID = quitVersionID;
    }

    /**
     * @return the isThereWinner
     */
    public boolean isIsThereWinner() {
        return isThereWinner;
    }

    /**
     * @param isThereWinner the isThereWinner to set
     */
    public void setIsThereWinner(boolean isThereWinner) {
        this.isThereWinner = isThereWinner;
    }
        
        
    
}
