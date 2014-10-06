/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.text.Format;

/**
 *
 * @author Anat
 */
public class Cell {

    private int m_cellNum;
    private int m_dest;
    private int[] m_numOfSoldiers;
    public static final int NO_DEST = 0;

    public Cell(final int cellNum, final int cellDest, final int numPlayers) {
        m_cellNum = cellNum;
        m_dest = cellDest;
        m_numOfSoldiers = new int[numPlayers];

    }

    public boolean isThereSoldiers() {
        boolean isThereSoldiers = false;
        int i = 0;
        while (!isThereSoldiers && i < m_numOfSoldiers.length) {
            if (m_numOfSoldiers[i] != 0) {
                isThereSoldiers = true;
            }
            i++;
        }
        return isThereSoldiers;
    }

    public void insertSoldier(final int playerNum) {
        m_numOfSoldiers[playerNum - 1]++;
    }

    public void insertSoldiers(int playerNum, int numOfSoldiers) {
        m_numOfSoldiers[playerNum - 1] += numOfSoldiers;
    }

    public void removeSoldier(final int playerNum) {
        m_numOfSoldiers[playerNum - 1]--;
    }

    public final int getDest() {
        return m_dest;
    }

    /**
     * @param m_dest the m_dest to set
     */
    public void setDest(int m_dest) {
        this.m_dest = m_dest;
    }

    public final int getCellNum() {
        return m_cellNum;
    }

    public final int[] getSoldiersInCell() {
        return m_numOfSoldiers;
    }
}
