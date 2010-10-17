package sokoban.solvers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import sokoban.Board;
import sokoban.Position;
import sokoban.SearchInfo;

/**
 * A class containing the common parts of the solvers.
 */
public abstract class IDSCommon implements Solver
{

    /**
     * The maximum depth of the solver
     */
    public static final int DEPTH_LIMIT = 1000;
    /**
     * The number of generated nodes
     */
    public int generatedNodes = 0;

    protected Board board;
    protected Board startBoard;

    /**
     * In bidirectional search, this map is shared between the pushing solver
     * and the pulling solver, in order to check for collisions.
     */
    protected HashMap<Long, BoxPosDir> ourStatesMap;
    protected HashMap<Long, BoxPosDir> otherStatesMap;

    @Override
    public int getIterationsCount()
    {
        return generatedNodes;
    }

    /**
     * Set of visited boards, including the player position
     */
    protected HashSet<Long> visitedBoards;

    /**
     * Boards that just lead to deadlocks or already visited boards. It
     * doesn't make sense to visit these in later iterations.
     */
    protected HashSet<Long> failedBoards;

    /**
     * Common constructor.
     * 
     * @param startBoard The start board
     * @param failedBoards The set of failed boards to use.
     * @param ours A map containing all states we have been in
     * @param others A map containing all states the other solver has been in
     */
    public IDSCommon(final Board startBoard, final HashSet<Long> failedBoards,
            final HashMap<Long, BoxPosDir> ours,
            final HashMap<Long, BoxPosDir> others)
    {
        this.startBoard = startBoard;
        this.failedBoards = failedBoards;
        ourStatesMap = ours;
        otherStatesMap = others;
    }

    /**
     * Empty common constructor.
     */
    public IDSCommon()
    {
        // Not used
    }

    /**
     * This is where the actual solving goes
     * 
     * @param maxDepth The maximum depth to search to.
     * @return A SearchInfo detailing the result of the search.
     */
    public abstract SearchInfo dfs(int maxDepth);

    protected static int lowerBound(final Board board)
    {
        final ArrayList<Position> boxes = new ArrayList<Position>();
        final Queue<Position> goals = new LinkedList<Position>();
        for (int row = 0; row < board.height; row++) {
            for (int col = 0; col < board.width; col++) {
                if (Board.is(board.cells[row][col], Board.BOX)) {
                    boxes.add(board.positions[row][col]);
                }
                if (Board.is(board.cells[row][col], Board.GOAL)) {
                    goals.add(board.positions[row][col]);
                }
            }
        }
        int result = 0;
        while (!goals.isEmpty()) {
            final Position goal = goals.poll();
            Position minBox = null;
            int min = Integer.MAX_VALUE;
            for (final Position box : boxes) {
                final int tmp = distance(goal, box);
                if (tmp < min) {
                    min = tmp;
                    minBox = box;
                }
            }

            boxes.remove(minBox);
            result += min;
        }
        return result;
    }

    /**
     * Approximate the distance between two positions
     * 
     * The distance will be the absolute minimum and are guaranteed to be equal
     * to or greater then the real distance.
     * 
     * @param a One of the positions
     * @param b The other position
     * @return The approximate distance between the two.
     */
    protected static int distance(final Position a, final Position b)
    {
        return Math.abs(a.column - b.column) + Math.abs(a.row - b.row);
    }

}
