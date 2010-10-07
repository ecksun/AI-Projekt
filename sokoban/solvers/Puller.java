/**
 * 
 */
package sokoban.solvers;

import java.util.ArrayList;
import sokoban.Board;

/**
 * A Solover that pulls the boxes instead of the usual pushing
 */
public class Puller implements Solver
{
    private final Board startBoard;
    private Board board;
    private int numBoxes;

    private ArrayList<Position> boxes;

    /**
     * Small class that stores a position
     */
    public class Position
    {
        final int x, y;

        /**
         * Create a new position
         * 
         * @param x X coordinate
         * @param y Y coordinate
         */
        public Position(int x, int y)
        {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * Small class containing the player position and its relative box position
     */
    public class PlayerPosDir extends Position
    {
        final int bx, by; // relative box position

        /**
         * Create a new player position
         * 
         * @param x TODO
         * @param y TODO
         * @param bx TODO
         * @param by TODO
         */
        public PlayerPosDir(int x, int y, int bx, int by)
        {
            super(x, y);
            this.bx = bx;
            this.by = by;
        }
    }

    /**
     * Initialize the class by copying the startBoard and reversing it and
     * setting
     * some local variables
     * 
     * @param startBoard The original board
     */
    public Puller(Board startBoard)
    {
        this.startBoard = (Board) startBoard.clone();
        numBoxes = startBoard.getRemainingBoxes();
        startBoard.reverse();
        boxes = new ArrayList<Position>(numBoxes);
    }

    /**
     * Resets the board to the starting board.
     */
    private void reset()
    {
        board = (Board) startBoard.clone();

        // Store all boxes
        int b = 0;
        for (int i = 0; i < board.height; ++i) {
            for (int j = 0; j < board.width; ++j) {
                if (board.cells[i][j] == Board.BOX) {
                    boxes.set(b, new Position(i, j));
                    b++;
                }
            }
        }
    }

    @Override
    public String solve()
    {
        do {
            reset();

            do {
                PlayerPosDir playerPosDir = choosePosition();
                while (moveBox(playerPosDir)) {
                }

            }
            while (!deadlock());
        }
        while (!solved());

        return null;
    }

    private boolean solved()
    {
        return board.getBoxesInStart() == numBoxes;
    }

    private boolean deadlock()
    {
        // TODO Auto-generated method stub
        return false;
    }

    private boolean moveBox(Position box)
    {
        // TODO Auto-generated method stub
        return false;
    }

    private PlayerPosDir choosePosition()
    {
        // TODO Auto-generated method stub
        return null;
    }

}
