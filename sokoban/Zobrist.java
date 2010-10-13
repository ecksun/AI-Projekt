/**
 * 
 */
package sokoban;

import java.util.Random;

/**
 *
 */
public final class Zobrist
{
    public final static byte EMPTY = 0;
    public final static byte BOX = 1;
    public final static byte PLAYER = 2;
    
    public static long[][][] hash;
    
    public static long calculateHashTable(Board board)
    {
        Random rand = new Random();
        //hash = rand.nextLong();
        
        // TODO might be faster to change the indexes here
        hash = new long[3][board.height][board.width];
        
        // Fill them with random stuff
        for (int i = 0; i < hash.length; ++i) {
            for (int j = 0; j < board.height; ++j) {
                for (int k = 0; k < board.width; ++k)
                hash[i][j][k] = rand.nextLong();
            }
        }

        long key = 0;
        
        for (int row = 0; row < board.height; ++row) {
            for (int col = 0; col < board.width; ++col) {
                if (board.playerCol == col && board.playerRow == row) {
                    key ^= hash[PLAYER][row][col];
                }
                else if (board.cells[row][col] == Board.BOX) {
                    key ^= hash[BOX][row][col];
                }
                else {
                    key ^= hash[EMPTY][row][col];
                }
            }
        }
        
        return key;   
    }
}
