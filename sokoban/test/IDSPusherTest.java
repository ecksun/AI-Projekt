package sokoban.test;

import java.io.File;
import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import sokoban.Board;
import sokoban.BoardParser;
import sokoban.solvers.IDSPusher;
import sokoban.solvers.Solver;

public class IDSPusherTest
{
    static File levelsFile;
    static ArrayList<String> levels;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        levelsFile = new File(new File("."), "all.slc");
        levels = BoardParser.getBoardStrings(levelsFile);
    }

    @Test
    public void level1()
    {
        byte[] bytes = levels.get(0).getBytes();
        Board board1 = new Board(bytes);
        Solver ids = new IDSPusher();
        String solution = ids.solve(board1);
        
        // TODO perform the moves and make sure it looks good
        
    }

}
