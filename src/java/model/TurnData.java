/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Anat
 */
public class TurnData {

    private int turnDiceRes;
    private int turnSoldierNum;
    private int turnDest;

    public TurnData(int diceRes, int soldierNum, int cellDest) {
        turnDiceRes = diceRes;
        turnSoldierNum = soldierNum;
        turnDest = cellDest;
    }

    /**
     * @return the turnDiceRes
     */
    public int getTurnDiceRes() {
        return turnDiceRes;
    }

    /**
     * @param turnDiceRes the turnDiceRes to set
     */
    public void setTurnDiceRes(int turnDiceRes) {
        this.turnDiceRes = turnDiceRes;
    }

    /**
     * @return the turnSoldierNum
     */
    public int getTurnSoldierNum() {
        return turnSoldierNum;
    }

    /**
     * @param turnSoldierNum the turnSoldierNum to set
     */
    public void setTurnSoldierNum(int turnSoldierNum) {
        this.turnSoldierNum = turnSoldierNum;
    }

    /**
     * @return the turnDest
     */
    public int getTurnDest() {
        return turnDest;
    }

    /**
     * @param turnDest the turnDest to set
     */
    public void setTurnDest(int turnDest) {
        this.turnDest = turnDest;
    }
}
