package sokoban.test;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import sokoban.Board;
import sokoban.BoardParser;

public class BoardParserTest
{

    private final byte[] board1In = {
            '#', '#', '#', '#', '#', '#', '#', '#', '\n', 
            '#', ' ', ' ', ' ', '#', ' ', '.', '#', '\n', 
            '#', ' ', ' ', ' ', '$', '$', '.', '#', '\n', 
            '#', '#', '#', '#', ' ', ' ', ' ', '#', '\n', 
            '#', '#', '#', '#', '@', ' ', '#', '#', '\n', 
            '#', '#', '#', '#', '#', '#', '#', '#', '\n'
    };
    
    private final byte[] board3In = {
            '#', '#', '#', '#', '#', '#', '#', '\n',
            '#', '#', '#', ' ', ' ', '#', '#', '\n',
            '#', '#', '#', ' ', ' ', '#', '#', '\n',
            '#', ' ', ' ', ' ', '@', '#', '#', '\n',
            '#', ' ', '*', '$', '.', ' ', '#', '\n',
            '#', '#', '#', ' ', ' ', ' ', '#', '\n',
            '#', '#', '#', '#', '#', '#', '#', '\n'
    };

    @Test
    public void parseBoard1() {
        Board board1 = new Board(board1In);
        
        assertEquals("Top left cell in board 1 is a wall.", board1.cells[0][0] & Board.INPUT_CELL_MASK, Board.WALL);
        assertEquals("Player is at row index 4", board1.getPlayerRow(), 4);
        assertEquals("Player is at col index 4", board1.getPlayerCol(), 4);
    }
    
    @Test
    public void parseBoard3() {
        Board board = new Board(board3In);
        
        assertEquals("Top left cell in board 3 is a wall.", Board.WALL, board.cells[0][0] & Board.INPUT_CELL_MASK);
        assertEquals("Box-on-floor ($) is parsed correctly.", Board.BOX, board.cells[4][3] & Board.INPUT_CELL_MASK);
        assertEquals("Box-on-goal (*) is parsed correctly.", Board.BOX | Board.GOAL, board.cells[4][2] & Board.INPUT_CELL_MASK);
        assertEquals("Goal floor (.) is parsed correctly.", Board.GOAL, board.cells[4][4] & Board.INPUT_CELL_MASK);
        assertEquals("Player is at row index 3", board.getPlayerRow(), 3);
        assertEquals("Player is at col index 4", board.getPlayerCol(), 4);
    }
    
}
