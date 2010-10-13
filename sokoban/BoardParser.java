package sokoban;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A board parser
 */
public class BoardParser
{
    /**
     * Reads the given levels file and returns a list of all level boards as
     * strings.
     * 
     * @param levelsFile
     *            The file containing the levels.
     */
    public static ArrayList<String> getBoardStrings(File levelsFile)
    {
        BufferedReader in = null;
        ArrayList<String> levels = new ArrayList<String>();

        try {
            in = new BufferedReader(new FileReader(levelsFile));
            String line;
            StringBuilder partialLevel = new StringBuilder();

            while ((line = in.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                if (line.startsWith(";")) {
                    if (partialLevel.length() > 0) {
                        levels.add(partialLevel.toString());
                        partialLevel = new StringBuilder();
                    }
                }
                else if (!line.startsWith(";")) {
                    partialLevel.append(line);
                    partialLevel.append('\n');
                }
            }
            
            // Flush (add) last board.
            levels.add(partialLevel.toString());

            in.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return levels;
    }
}
