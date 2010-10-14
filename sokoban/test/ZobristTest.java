package sokoban.test;

import static org.junit.Assert.assertEquals;

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
        b = new Board("#####" + "\n" + "#@  #" + "\n" + "# $ #" + "\n"
                + "#  .#" + "\n" + "#####");
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
    }

}
