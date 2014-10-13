/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package utilities;

import model.Player;
import model.TurnData;

/**
 *
 * @author Anat
 */
   public class TurnInfo {

        Integer versionID;
        String currPlayerName;
        Player.PlayerType currPlayerType;
        Integer currPlayerID;
        TurnData turnData;
        String newCurrPlayerName;
        Integer newCurrPlayerID;
        Player.PlayerType newCurrPlayerType;
        private boolean isThereWinner;
        boolean isItPlayerSessionTurn;
        Integer nextFreeID;
        private boolean hasAnyPlayerLeft;
        String playerLeftName;
        Integer playerLeftID;
        

        public TurnInfo(String currPlayerName, Player.PlayerType currPlayerType, Integer currPlayerID,
                TurnData td, String newCurrPlayerName, Integer newCurrPlayerID, Player.PlayerType newCurrPlayerType,
                boolean isWinner, Integer nextFreeID) {
            this.currPlayerName = currPlayerName;
            this.currPlayerType = currPlayerType;
            this.currPlayerID = currPlayerID;
            turnData = td;
            this.newCurrPlayerName = newCurrPlayerName;
            this.newCurrPlayerID = newCurrPlayerID;
            this.newCurrPlayerType = newCurrPlayerType;
            isThereWinner = isWinner;
            this.nextFreeID=nextFreeID;
        }

        public int getVersionId() {
            return versionID;
        }

        public void setVersionId(int version) {
            versionID=version;
        }

        public Player.PlayerType getNextPlayerType() {
            return newCurrPlayerType;
        }

        public void setIsPlayerSessionTurn(boolean val) {
            this.isItPlayerSessionTurn = val;
        }

        public String getNextPlayerName() {
            return this.newCurrPlayerName;
        }
        
        public void setHasAnyPlayerLeft(boolean hasAnyPlayerLeft)
        {
            this.hasAnyPlayerLeft = hasAnyPlayerLeft;
        }
        
        public void setPlayerLeftName(String playerLeftName)
        {
            this.playerLeftName = playerLeftName;
        }

        public void setPlayerLeftID(Integer playerLeftID) {
            this.playerLeftID = playerLeftID;
        }

    /**
     * @param isThereWinner the isThereWinner to set
     */
    public void setIsThereWinner(boolean isThereWinner) {
        this.isThereWinner = isThereWinner;
    }

    /**
     * @return the hasAnyPlayerLeft
     */
    public boolean isHasAnyPlayerLeft() {
        return hasAnyPlayerLeft;
    }
       
    }
