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
        readLevelsFile();
    }

    @Test
    public void level1()
    {
        byte[] bytes = levels.get(0).getBytes();
        Board board1 = BoardParser.parse(bytes);
        Solver ids = new IDS(board1);
        String solution = ids.solve();
        
        // TODO perform the moves and make sure it looks good
        
    }

    /**
     * Reads the levels file and stores all level strings in the levels field.
     */
    private static void readLevelsFile()
    {
        BufferedReader in = null;
        levels = new ArrayList<String>();

        try {
            in = new BufferedReader(new FileReader(levelsFile));
            String line;
            StringBuilder partialLevel = new StringBuilder();

            while ((line = in.readLine()) != null) {
                if (line.startsWith(";LEVEL")) {
                    if (partialLevel.length() > 0) {
                        levels.add(partialLevel.toString());
                        partialLevel = new StringBuilder();
                    }
                }
                else {
                    partialLevel.append(line);
                    partialLevel.append('\n');
                }
            }

            in.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
