package sokoban.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import sokoban.Board;
import sokoban.BoardParser;
import sokoban.solvers.IDS;
import sokoban.solvers.Solver;

public class SolverTest
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
        Board board1 = BoardParser.parse(bytes);
        Solver ids = new IDS();
        String solution = ids.solve(board1);
        
        // TODO perform the moves and make sure it looks good
        
    }

}
