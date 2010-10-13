package sokoban.solvers;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import sokoban.Board;
import sokoban.PlayerPosDir;
import sokoban.Position;
import sokoban.ReachableBox;
import sokoban.Board.Direction;

/**
 * A solver that pushes boxes around with iterative deepening and
 * a bloom filter to avoid duplicate states.
 */
public class IDSPusher implements Solver
{
    private final int DEPTH_LIMIT = 1000;
    /**
     * The number of generated nodes
     */
    public static int generatedNodes = 0;
    private static int remainingDepth;
    private static Board board;

    public int getIterationsCount()
    {
        return generatedNodes;
    }

    /**
     * Set of visited boards, including the player position
     */
    private HashSet<Long> visitedBoards;

    /**
     * Boards that just lead to deadlocks or already visited boards. It
     * doesn't make sense to visit these in later iterations.
     */
    private HashSet<Long> failedBoards;

    enum SearchStatus {
        /**
         * The search reached the maximum depth, and no solution was found,
         * so it's inconclusive (a solution could follow, but we don't know).
         */
        Inconclusive,

        /**
         * This search resulted in a solution.
         */
        Solution,

        /**
         * This search failed without reached the maximum depth, so there's
         * no point in trying it again with a greater search depth.
         */
        Failed,
    };

    /**
     * Contains information about a search, whether it is failed, reached a
     * solution or is inconclusive.
     */
    final static class SearchInfo
    {
        final SearchStatus status;
        final LinkedList<Board.Direction> solution;

        static SearchInfo Inconclusive = new SearchInfo(
                SearchStatus.Inconclusive);
        static SearchInfo Failed = new SearchInfo(SearchStatus.Inconclusive);

        public SearchInfo(final SearchStatus status)
        {
            this.status = status;
            solution = null;
        }

        private SearchInfo()
        {
            status = SearchStatus.Solution;
            solution = new LinkedList<Board.Direction>();
        }

        public static SearchInfo emptySolution()
        {
            return new SearchInfo();
        }
    }

    /**
     * Recursive Depth-First algorithm
     * 
     * @param maxDepth The maximum depth.
     * @return
     */
    private SearchInfo dfs()
    {
        generatedNodes++;

        if (board.getRemainingBoxes() == 0) {
            // Found a solution
            return SearchInfo.emptySolution();
        }

        if (remainingDepth <= 0) {
            return SearchInfo.Inconclusive;
        }

        // True if at least one successor tree was inconclusive.
        boolean inconclusive = false;

        // Keep track of failed successors. If all successors fail we can
        // fail ourselves (reduces the search tree).
        int numFailed = 0;

        final Position source = new Position(board.playerRow, board.playerCol);
        remainingDepth--;

        // TODO optimize: no need for paths here
        final byte[][] cells = board.cells;
        for (final ReachableBox reachable : board.findReachableBoxSquares()) {
            for (final Direction dir : Board.Direction.values()) {
                final Position from = new Position(reachable.position,
                        Board.moves[dir.ordinal()]);
                final Position to = new Position(from, Board.moves[dir
                        .ordinal()]);
                if (Board.is(cells[from.row][from.column], Board.BOX)
                        && !Board
                                .is(cells[to.row][to.column], Board.REJECT_BOX)) {
                    // The move is possible

                    // Move the player and push the box
                    board.moveBox(from, to);
                    board.playerRow = from.row;
                    board.playerCol = from.column;

                    // Process successor states
                    final SearchInfo result = dfs();

                    // Restore changes
                    board.moveBox(to, from);
                    board.playerRow = source.row;
                    board.playerCol = source.column;

                    // Evaluate result
                    switch (result.status) {
                        case Solution:
                            // Found a solution. Return it now!

                            // Add the last movement first
                            result.solution.addFirst(dir);
                            // So we can put the rest in front of it
                            result.solution.addAll(0, reachable.path);

                            return result;
                        case Inconclusive:
                            // Make the parent inconclusive too
                            inconclusive = true;
                            continue;
                        case Failed:
                            // Keep the failed board for now
                            // failed[numFailed++] = successor;
                            numFailed++;
                            continue;
                    }
                }
            }
        }

        remainingDepth++;

        if (inconclusive) {
            // Add all successors that failed to the failed set
            // TODO add failed nodes? (is this needed?)
            return SearchInfo.Inconclusive;
        }
        else {
            // All successors failed, so this node is failed
            // TODO
            // failedBoards.add(board.);
            return SearchInfo.Failed;
        }
    }

    // TODO make the hashCode() function in Board use Ze.... hashes?

    public String solve(final Board startBoard)
    {
        failedBoards = new HashSet<Long>();
        System.out.println("IDS depth limit (progress): ");
        for (int maxDepth = 10; maxDepth < DEPTH_LIMIT; maxDepth += 3) {
            System.out.print(maxDepth + ".");

            visitedBoards = new HashSet<Long>();
            remainingDepth = maxDepth;
            board = (Board) startBoard.clone();

            final SearchInfo result = dfs();
            if (result.solution != null) {
                System.out.println();
                return Board.solutionToString(result.solution);
            }
            else if (result.status == SearchStatus.Failed) {
                System.out.println("no solution!");
                return null;
            }
        }

        System.out.println("maximum depth reached!");
        return null;
    }
}