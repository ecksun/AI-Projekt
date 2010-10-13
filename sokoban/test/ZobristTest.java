package sokoban.test;


import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sokoban.Board;
import sokoban.Position;
import sokoban.Zobrist;

/**
 * Tests the Zobrist hashing algorithm.
 */
public class ZobristTest
{

    Board b;
    
    @Before
    public void setUp() throws Exception
    {
        b = getBoard();
    }

    @After
    public void tearDown() throws Exception
    {
    }

    
    @Test
    public void testHash1() 
    {
        long key = Zobrist.calculateHashTable(b);

        // Move box one step right
        b.moveBox(new Position(2, 2), new Position(2, 3));

        key = Zobrist.remove(key, Zobrist.BOX, 2, 2);
        key = Zobrist.add(key, Zobrist.EMPTY, 2, 2);
        
        key = Zobrist.remove(key, Zobrist.EMPTY, 2, 3);
        key = Zobrist.add(key, Zobrist.BOX, 2, 3);
        
        assertEquals(Zobrist.calculateHashTable(b), key);
        
        // Update player position
        key = Zobrist.remove(key, Zobrist.PLAYER, b.playerRow, b.playerCol);
        key = Zobrist.add(key, Zobrist.EMPTY, b.playerRow, b.playerCol);
        b.playerRow = 2;
        b.playerCol = 2;
        key = Zobrist.remove(key, Zobrist.EMPTY, b.playerRow, b.playerCol);
        key = Zobrist.add(key, Zobrist.PLAYER, b.playerRow, b.playerCol);
        
        assertEquals(Zobrist.calculateHashTable(b), key);
        
    }
    
    private Board getBoard() {
        Board board;
        
        board = new Board(5, 5, 1, 1);

        // Row 1
        board.cells[0][0] = Board.WALL;
        board.cells[0][1] = Board.WALL;
        board.cells[0][2] = Board.WALL;
        board.cells[0][3] = Board.WALL;
        board.cells[0][4] = Board.WALL;
        // Row 2
        board.cells[1][0] = Board.WALL;
        board.cells[1][1] = Board.PLAYER_START;
        board.cells[1][2] = 0;
        board.cells[1][3] = 0;
        board.cells[1][4] = Board.WALL;
        // Row 3
        board.cells[2][0] = Board.WALL;
        board.cells[2][1] = 0;
        board.cells[2][2] = Board.BOX;
        board.cells[2][3] = 0;
        board.cells[2][4] = Board.WALL;
        // Row 4
        board.cells[3][0] = Board.WALL;
        board.cells[3][1] = 0;
        board.cells[3][2] = 0;
        board.cells[3][3] = Board.GOAL;
        board.cells[3][4] = Board.WALL;        
        // Row 5
        board.cells[4][0] = Board.WALL;
        board.cells[4][1] = Board.WALL;
        board.cells[4][2] = Board.WALL;
        board.cells[4][3] = Board.WALL;
        board.cells[4][4] = Board.WALL;
        
        return board;
    }
    
}
