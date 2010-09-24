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

    @Test
    public void parseBoard1() {
        Board board1 = BoardParser.parse(board1In);
        
        assertEquals("Top left cell in board 1 is a wall.", board1.cells[0][0] & Board.INPUT_CELL_MASK, Board.WALL);
        assertEquals("Player is at row index 4", board1.playerRow, 4);
        assertEquals("Player is at col index 4", board1.playerCol, 4);
    }
    
}
