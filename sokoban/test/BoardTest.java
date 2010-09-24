package sokoban.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sokoban.Board;

public class BoardTest
{

    Board b1;

    @Before
    public void setUp() throws Exception
    {
        b1 = new Board(4, 5, 1, 1);
        b1.cells[0][0] = Board.WALL;
        b1.cells[0][1] = Board.WALL;
        b1.cells[0][2] = Board.WALL;
        b1.cells[0][3] = Board.WALL;

        b1.cells[1][0] = Board.WALL;
        b1.cells[1][1] = 0;
        b1.cells[1][2] = 0;
        b1.cells[1][3] = Board.WALL;

        b1.cells[2][0] = Board.WALL;
        b1.cells[2][1] = 0;
        b1.cells[2][2] = Board.BOX;
        b1.cells[2][3] = Board.WALL;

        b1.cells[3][0] = Board.WALL;
        b1.cells[3][1] = 0;
        b1.cells[3][2] = Board.GOAL;
        b1.cells[3][3] = Board.WALL;

        b1.cells[4][0] = Board.WALL;
        b1.cells[4][1] = Board.WALL;
        b1.cells[4][2] = Board.WALL;
        b1.cells[4][3] = Board.WALL;
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void cloneBoard()
    {
        System.out.println(b1);
        
        Board b2 = (Board) b1.clone();

        for (int row = 0; row < b1.height; ++row) {
            for (int col = 0; col < b1.width; ++col) {
                assertEquals("Value at row index " + row + " and col index "
                        + col + " in clone and original equals.",
                        b1.cells[row][col], b2.cells[row][col]);
            }
        }

        b1.cells[3][1] = Board.GOAL;
        b1.cells[2][1] = Board.BOX;
        
        b2.cells[3][1] = Board.WALL;
        
        assertEquals("", b2.cells[3][1], Board.BOX);
        
    }

}
