/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Anat
 */
public class HumanPlayer extends Player {

    public HumanPlayer(int playerNum, GameBoard board, LoadedFrom source) {
        super(playerNum, board, source);
    }

    @Override
    public PlayerType getType() {
        return PlayerType.HUMAN;
    }

    @Override
    public int chooseSoldierToMove() {
        // No actual use in HumanPlayer.
        return 0;
    }
}
