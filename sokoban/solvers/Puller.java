/**
 * 
 */
package sokoban.solvers;

import java.util.Random;

import sokoban.Board;
import sokoban.PlayerPosDir;
import sokoban.Position;

/**
 * A Solover that pulls the boxes instead of the usual pushing
 */
public class Puller implements Solver
{
    private Board startBoard;
    private Board board;
    private int numBoxes;
    private Random rand;
    private int iterationsCount = 0;

    private Position[] boxes;

    /**
     * Resets the board to the starting board.
     */
    private void reset()
    {
        board = (Board) startBoard.clone();

        // Store all boxes
        int b = 0;
        for (int row = 0; row < board.height; ++row) {
            for (int column = 0; column < board.width; ++column) {
                if (Board.is(board.cells[row][column], Board.BOX)) {
                    boxes[b] = new Position(row, column);
                    b++;
                }
            }
        }
    }

    @Override
    public String solve(Board inputBoard)
    {
        rand = new Random(500);
        startBoard = (Board) inputBoard.clone();
        numBoxes = startBoard.getRemainingBoxes();
        startBoard.reverse();
        boxes = new Position[numBoxes];

        return solverAlgorithm();
    }

    public String solverAlgorithm()
    {
        finished: while (true) {
            reset();

            PlayerPosDir playerPosDir;
            do {
                playerPosDir = choosePosition();
                // No moves are possible
                if (playerPosDir == null)
                    break;

                while (moveBox(playerPosDir)) {
                    if (solved())
                        break finished;
                    // TODO choose Condition X and exit from
                    // loop if Condition X fails
                }
            }
            while (!deadlock(playerPosDir));
        }

        return null;
    }

    private boolean solved()
    {
        return board.getBoxesInStart() == numBoxes;
    }

    private boolean deadlock(PlayerPosDir pos)
    {
        return !(Board.is(board.cells[pos.row - 1][pos.column],
                Board.REJECT_PULL)
                && Board.is(board.cells[pos.row + 1][pos.column],
                        Board.REJECT_PULL)
                && Board.is(board.cells[pos.row][pos.column - 1],
                        Board.REJECT_PULL) && Board.is(
                board.cells[pos.row][pos.column + 1], Board.REJECT_PULL));
    }

    private boolean moveBox(PlayerPosDir pos)
    {
        int newRow = pos.row - pos.boxRow;
        int newColumn = pos.column - pos.boxColumn;

        // System.err.println(pos.x+", "+pos.y+"  :  "+pos.bx+", "+pos.by+" ---> "+newRow+", "+newColumn);

        iterationsCount++;

        // See if there next square is empty
        if (Board.is(board.cells[newColumn][newRow], Board.REJECT_PULL))
            return false;

        // Move the box
        board.pull(pos.row, pos.column, pos.boxRow, pos.boxColumn);
        pos.row += newRow;
        pos.column += newColumn;

        return true;
    }

    private PlayerPosDir choosePosition()
    {
        final int boxCount = boxes.length;
        final int max = 4 * boxCount;
        final int[] stepRow = { -1, 0, 1, 0 };
        final int[] stepColumn = { 0, 1, 0, -1 };
        int triesLeft = max;
        int p = rand.nextInt(max);

        while (triesLeft > 0) {
            Position box = boxes[p / 4];
            int dir = p % 4;
            int row = box.row + stepRow[dir];
            int column = box.column + stepColumn[dir];
            if (column > 0 && column < board.width - 1 && row > 0
                    && row < board.height - 1
                    && !Board.is(board.cells[row][column], Board.REJECT_PULL)) {
                return new PlayerPosDir(row, column, -stepRow[dir],
                        -stepColumn[dir]);
            }
            p = (p + 1) % max;
            triesLeft--;
        }
        return null;
    }

    @Override
    public int getIterationsCount()
    {
        return iterationsCount;
    }

}
