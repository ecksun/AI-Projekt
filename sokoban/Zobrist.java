/**
 * 
 */
package sokoban;

import java.util.Random;

/**
 * Our application of the Zobrist hash algorithm considers empty cells, boxes
 * and the player.
 */
public final class Zobrist
{
    public final static byte EMPTY = 0;
    public final static byte BOX = 1;
    public final static byte PLAYER = 2;

    /**
     * TODO document the different indices here
     */
    public static long[][][] hash;

    private static Random rand;

    public static long calculateHashTable(Board board)
    {
        // hash = rand.nextLong();
        rand = new Random(0);

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
                if (board.getPlayerCol() == col && board.getPlayerRow() == row) {
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

    /**
     * Updates the given hash key by XOR:ing the specified entity on the
     * position specified by the row and column.
     * 
     * @param key The hash key to update.
     * @param entity The entity.
     * @param row The row.
     * @param col The column.
     * @return The new hash key.
     */
    private static long update(long key, byte entity, int row, int col)
    {
        return key ^= hash[entity][row][col];
    }
    
    /**
     * Alias for update().
     */
    public static long add(long key, byte entity, int row, int col) {
        return update(key, entity, row, col);
    }

    /**
     * Alias for update().
     */
    public static long remove(long key, byte entity, int row, int col) {
        return update(key, entity, row, col);
    }

}
