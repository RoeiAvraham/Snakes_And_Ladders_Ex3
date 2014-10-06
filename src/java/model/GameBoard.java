/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import exception.*;
import java.util.*;
import xmlPackage.Ladders;
import xmlPackage.Ladders.Ladder;
import xmlPackage.Snakes;
import xmlPackage.Snakes.Snake;
import xmlPackage.Snakesandladders;

/**
 *
 * @author Anat
 */
public class GameBoard
{

    private Cell[] m_cells;
    private int m_boardSize;
    private int m_numOfSnakesAndLadders;
    public static final int EMPTY = 0;

    //GameBoard c'tor
    public GameBoard(Snakesandladders gameXml) throws
            XmlIsInvalidException
    {
        m_boardSize = gameXml.getBoard().getSize();
        m_cells = new Cell[m_boardSize * m_boardSize];
        m_numOfSnakesAndLadders = gameXml.getBoard().getSnakes().getSnake().size();

        int i;
        for (i = 0; i < m_boardSize * m_boardSize; i++)
        {
            m_cells[i] = new Cell(i + 1, 0,
                    gameXml.getPlayers().getPlayer().size());
        }

        NoDuplicateCellNumbersInXmlException(gameXml);
        insertSnakesAndLaddersFromXml(gameXml.getBoard().getSnakes(), gameXml.getBoard().getLadders());
    }

    public GameBoard(final int boardSize, final int numOfSnakesAndLadders, final int numPlayers)
    {
        m_cells = new Cell[boardSize * boardSize];
        m_boardSize = boardSize;
        m_numOfSnakesAndLadders
                = numOfSnakesAndLadders;

        int i;
        for (i = 0; i < m_boardSize * m_boardSize; i++)
        {
            m_cells[i] = new Cell(i + 1, 0,
                    numPlayers);
        }
        insertSnakesAndLaddersToBoard();
    }

    public void setPlayersPosFromXml(Snakesandladders gameXml, LinkedList<Player> players,
            LinkedList<String> playerNames) throws
            XmlIsInvalidException
    {
        int[] soldiersFoundCounter = new int[players.size()];
        // soldiersFoundCount keeps for each player by index)  how many of its soldiers // where found on the xml board.

        for (xmlPackage.Cell c : gameXml.getBoard().getCells().getCell())
        {
            for (xmlPackage.Cell.Soldiers s
                    : c.getSoldiers())
            {
                String name = s.getPlayerName();
                int playerIndex = playerNames.indexOf(name) + 1;
                m_cells[c.getNumber().intValue()
                        - 1].insertSoldiers(playerIndex, s.getCount());

                for (int i = 0; i < s.getCount(); i++)
                {
                    players.get(playerIndex
                            - 1).getSoldiersPos()[soldiersFoundCounter[playerIndex
                            - 1]] = c.getNumber().intValue();
                    soldiersFoundCounter[playerIndex
                            - 1]++;
                }
            }
        }

    }

    public void NoDuplicateCellNumbersInXmlException(Snakesandladders gameXml) throws
            XmlIsInvalidException
    {
        List<xmlPackage.Cell> cellsList
                = gameXml.getBoard().getCells().getCell();

        int i = 0, j = 0;

        for (i = 0; i < cellsList.size(); i++)
        {
            for (j = i + 1; j < cellsList.size(); j++)
            {
                if (cellsList.get(i).getNumber().intValue() == cellsList.get(j).getNumber().intValue())
                {
                    throw new DuplicateCellNumbersXmlException();
                }
            }
        }

    }

    public void insertSnakesAndLaddersFromXml(Snakes snakes, Ladders ladders) throws XmlIsInvalidException
    {
        List<Snake> snakesList = snakes.getSnake();
        List<Ladder> ladderList = ladders.getLadder();

        checkSnakesAndLaddersQuantity(snakesList,
                ladderList);

        checkSnakesAndLaddersTopAndBottomAndFirstAndLastCells(snakesList, ladderList);
        checkNoSnakeAndLadderOnSameCell(snakesList,
                ladderList);
    }

    public void checkSnakesAndLaddersQuantity(List<Snake> snakesList, List<Ladder> ladderList)
            throws XmlIsInvalidException
    {
        if (snakesList.size() != ladderList.size())
        {
            throw new SnakesAndLaddersQuantityNotEqualXmlException();
        } else if (snakesList.size() > ((m_boardSize
                * m_boardSize) / 4))
        {
            throw new SnakesAndLaddersQuantityToBigXmlException();
        }
    }

    public void checkSnakesAndLaddersTopAndBottomAndFirstAndLastCells(List<Snake> snakesList, List<Ladder> ladderList)
            throws XmlIsInvalidException
    {
        for (Snake s : snakesList)
        {
            if (s.getFrom().intValue() <= s.getTo().intValue())
            {
                throw new SnakesFromIsSmallerThanToXmlException();
            }
            if ((s.getFrom().intValue() == 1)
                    || (s.getFrom().intValue() == getLastCellNum()))
            {
                throw new SnakeHeadInIllegalCellXmlException();
            }
            if (s.getFrom().intValue()
                    > getLastCellNum() || s.getTo().intValue() < 1)
            {
                throw new SnakeEndOutOfRangeXmlException();
            }
            m_cells[s.getFrom().intValue()
                    - 1].setDest(s.getTo().intValue());
        }

        for (Ladder l : ladderList)
        {
            if (l.getFrom().intValue() >= l.getTo().intValue())
            {
                throw new LaddersFromIsBiggerThanToXmlException();
            }
            if ((l.getFrom().intValue() == 1)
                    || (l.getFrom().intValue() == getLastCellNum()))
            {
                throw new LadderBottomInIllegalCellXmlException();
            }
            if (l.getFrom().intValue() < 1 || l.getTo().intValue() > getLastCellNum())
            {
                throw new LadderEndOutOfRangeXmlException();
            }
            m_cells[l.getFrom().intValue()
                    - 1].setDest(l.getTo().intValue());
        }
    }

    public void checkNoSnakeAndLadderOnSameCell(List<Snake> snakesList, List<Ladder> ladderList)
            throws XmlIsInvalidException
    {
        for (Snake s : snakesList)
        {
            for (Ladder l : ladderList)
            {
                if (s.getFrom().intValue()
                        == l.getFrom().intValue()
                        || s.getFrom().intValue()
                        == l.getTo().intValue()
                        || s.getTo().intValue()
                        == l.getFrom().intValue()
                        || s.getTo().intValue()
                        == l.getTo().intValue())
                {
                    throw new SnakesAndLaddersOnSameCellXmlException();
                }
            }
        }
    }

    public final Cell[] getCells()
    {
        return m_cells;
    }

    public Cell getFirstCell()
    {
        return m_cells[0];
    }

    private void insertSnakesAndLaddersToBoard()
    {
        Random r = new Random();
        int i, x, y, max;
        ArrayList<Integer> cells = new ArrayList<>();

        int numCells = m_boardSize * m_boardSize;
        // Numbers to choose ladders from:
        for (i = 2; i <= numCells; i++)
        {
            cells.add(i);
        }

        int maxSnakesAndLadders = m_boardSize
                * m_boardSize / 4;
        if (numCells % 2 == 0
                && m_numOfSnakesAndLadders == maxSnakesAndLadders)
        {
            max = m_numOfSnakesAndLadders - 1;
            cells.remove((Integer) numCells);
            x = cells.remove(r.nextInt(cells.size()));
            m_cells[x - 1].setDest(numCells);
        } else
        {
            max = m_numOfSnakesAndLadders;
        }

        //Add Ladders:
        for (i = 0; i < max; i++)
        {
            x = cells.remove(r.nextInt(cells.size()));
            y = cells.remove(r.nextInt(cells.size()));
            if (x > y)
            {
                m_cells[y - 1].setDest(x);
            } else
            {
                m_cells[x - 1].setDest(y);
            }
        }

        cells.add(1); //cell number one now allowed
        if (cells.contains(numCells))
        {
            cells.remove((Integer) numCells);
        }

        //Add Snakes:
        for (i = 0; i < m_numOfSnakesAndLadders; i++)
        {
            x = cells.remove(r.nextInt(cells.size()));
            y = cells.remove(r.nextInt(cells.size()));
            if (x > y)
            {
                m_cells[x - 1].setDest(y);
            } else
            {
                m_cells[y - 1].setDest(x);
            }
        }
    }

    public int getBoardSize()
    {
        return m_boardSize;
    }

    public int getLastCellNum()
    {
        return m_boardSize * m_boardSize;
    }

    public Cell getCell(final int cellNum)
    {
        return m_cells[cellNum - 1];
    }

    /**
     * @return the m_numOfSnakesAndLadders
     */
    public int getNumberOfSnakesAndLadders()
    {
        return m_numOfSnakesAndLadders;
    }
}
