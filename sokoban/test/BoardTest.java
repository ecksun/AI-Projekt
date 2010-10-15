package sokoban.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sokoban.Board;
import sokoban.BoardParser;
import sokoban.PlayerPosDir;
import sokoban.Position;
import sokoban.Board.Direction;

public class BoardTest
{

    Board b1, b2, b3;

    @Before
    public void setUp() throws Exception
    {
        b1 = new Board("####" + "\n" + "#@ #" + "\n" + "# $#" + "\n" + "# .#"
                + "\n" + "####");

        b2 = new Board("####" + "\n" + "#@ #" + "\n" + "# *#" + "\n" + "#  #"
                + "\n" + "####" + "\n");

        b3 = new Board("########\n" + "#---#-.#\n" + "#-  $$.#\n"
                + "####  -#\n" + "####@-##\n" + "########\n");

    }

    @After
    public void tearDown() throws Exception
    {
    }
    
    @Test
    public void testRemainingBoxes() 
    {
        assertEquals(1, b1.getRemainingBoxes());
        assertEquals(0, b2.getRemainingBoxes());
        assertEquals(2, b3.getRemainingBoxes());
    }

    @Test
    public void cloneBoard()
    {
        Board clone = (Board) b1.clone();

        for (int row = 0; row < b1.height; ++row) {
            for (int col = 0; col < b1.width; ++col) {
                assertEquals("Value at row index " + row + " and col index "
                        + col + " in clone and original equals.",
                        b1.cells[row][col], clone.cells[row][col]);
            }
        }

        b1.cells[3][1] = Board.GOAL;
        b1.cells[2][1] = Board.BOX;

        clone.cells[3][1] = Board.WALL;

        assertNotSame("Changing clone does not change original.", Board.WALL,
                b1.cells[3][1]);

    }

    @Test
    public void moveToGoal()
    {
        b1.move(Board.Direction.RIGHT);
        assertEquals(1, b1.getPlayerRow());
        assertEquals(2, b1.getPlayerCol());
        assertEquals(1, b1.getRemainingBoxes());

        b1.move(Board.Direction.DOWN);
        assertEquals(2, b1.getPlayerRow());
        assertEquals(2, b1.getPlayerCol());
        assertEquals(0, b1.getRemainingBoxes());

        b1.move(Board.Direction.LEFT);
        assertEquals(2, b1.getPlayerRow());
        assertEquals(1, b1.getPlayerCol());
        assertEquals(0, b1.getRemainingBoxes());

        b1.move(Board.Direction.DOWN);
        assertEquals(3, b1.getPlayerRow());
        assertEquals(1, b1.getPlayerCol());
        assertEquals(0, b1.getRemainingBoxes());
    }

    @Test
    public void moveFromGoal()
    {
        b2.move(Board.Direction.DOWN);
        assertEquals(2, b2.getPlayerRow());
        assertEquals(1, b2.getPlayerCol());
        assertEquals(0, b2.getRemainingBoxes());

        b2.move(Board.Direction.DOWN);
        assertEquals(3, b2.getPlayerRow());
        assertEquals(1, b2.getPlayerCol());
        assertEquals(0, b2.getRemainingBoxes());

        b2.move(Board.Direction.RIGHT);
        assertEquals(3, b2.getPlayerRow());
        assertEquals(2, b2.getPlayerCol());
        assertEquals(0, b2.getRemainingBoxes());

        b2.move(Board.Direction.UP);
        assertEquals(2, b2.getPlayerRow());
        assertEquals(2, b2.getPlayerCol());
        assertEquals(1, b2.getRemainingBoxes());
    }

    @Test
    public void findPath1()
    {
        System.out.println(b1);

        Deque<Direction> path = b1.findPath(new Position(3, 2));

        Deque<Direction> onlyPossiblePath = new LinkedList<Direction>();
        onlyPossiblePath.add(Direction.DOWN);
        onlyPossiblePath.add(Direction.DOWN);
        onlyPossiblePath.add(Direction.RIGHT);

        Iterator<Direction> pathIterator = path.iterator();
        Iterator<Direction> realPathIterator = onlyPossiblePath.iterator();

        while (pathIterator.hasNext()) {
            assertEquals(
                    "Real path has more elements while found path has it.",
                    true, realPathIterator.hasNext());
            assertEquals("Path element (directions) equals.", pathIterator
                    .next(), realPathIterator.next());
        }
    }

    @Test
    public void findPath2()
    {
        System.out.println("board 3");
        System.out.println(b3);

        Position goal = new Position(1, 5);
        Deque<Direction> path = b3.findPath(goal);

        assertNotSame("Path should be found for this board.", null, path);

        Position player = new Position(b3.getPlayerRow(), b3.getPlayerCol());

        for (Direction dir : path) {
            player = b3.getPosition(player, Board.moves[dir.ordinal()]);
            assertEquals(true, b3.contains(player));
            assertEquals(false, Board.is(b3.cells[player.row][player.column],
                    Board.WALL));
            assertEquals(false, Board.is(b3.cells[player.row][player.column],
                    Board.BOX));
        }

        assertEquals(goal, player);

    }

}
