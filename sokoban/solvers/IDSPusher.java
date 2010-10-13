package sokoban.solvers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import sokoban.Board;
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
    private static int maxDepth, remainingDepth;
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
        final LinkedList<Direction> solution;

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

        if (!visitedBoards.add(board.getZobristKey())) {
            // Duplicate state
            return SearchInfo.Failed;
        }

        // True if at least one successor tree was inconclusive.
        boolean inconclusive = false;

        final Position source = new Position(board.getPlayerRow(), board.getPlayerCol());
        remainingDepth--;

        // TODO optimize: no need for paths here
        final byte[][] cells = board.cells;
        for (int i = 0; i < board.reachableBoxCount; i++) {
            final Position boxFrom = board.reachableBoxes[i];
            for (final Direction dir : Board.Direction.values()) {
                final Position player = new Position(boxFrom, dir.reverse());
                final Position boxTo = new Position(boxFrom, dir);
                if (Board.is(cells[player.row][player.column], Board.REACHABLE)
                        && Board.is(cells[boxFrom.row][boxFrom.column], Board.BOX)
                        && !Board
                                .is(cells[boxTo.row][boxTo.column], Board.REJECT_BOX)) {
                    // The move is possible
                    
                    // Move the player and push the box
                    board.movePlayer(source, boxFrom);
                    board.moveBox(boxFrom, boxTo);

                    // Process successor states
                    final SearchInfo result = dfs();
                    if (result.status == SearchStatus.Solution) System.out.println(board);

                    // Restore changes
                    board.movePlayer(boxFrom, source);
                    board.moveBox(boxTo, boxFrom);

                    // Evaluate result
                    switch (result.status) {
                        case Solution:
                            // Found a solution. Return it now!
                            
                            board.clearFlag(Board.VISITED);
//                            System.out.println(board);
//                            System.out.println(source+"--->"+player);
                            result.solution.addFirst(dir);
                            if (remainingDepth == maxDepth-1 && !source.equals(player)) {
                                result.solution.addAll(0, board.findPath(source, player));
                            }

                            return result;
                        case Inconclusive:
                            // Make the parent inconclusive too
                            inconclusive = true;
                            continue;
                        case Failed:
                            // Mark this node as failed
                            failedBoards.add(board.getZobristKey());
                            continue;
                    }
                }
            }
        }

        remainingDepth++;

        if (inconclusive) {
            // Add all successors that failed to the failed set
            return SearchInfo.Inconclusive;
        }
        else {
            // All successors failed, so this node is failed
            failedBoards.add(board.getZobristKey());
            return SearchInfo.Failed;
        }
    }

    public String solve(final Board startBoard)
    {
        failedBoards = new HashSet<Long>();
        final int lowerBound = lowerBound(startBoard);
        System.out.println("lowerBound(): " + lowerBound);
        System.out.println("IDS depth limit (progress): ");
        for (maxDepth = lowerBound; maxDepth < DEPTH_LIMIT; maxDepth += 3) {
            System.out.print(maxDepth + ".");

            visitedBoards = new HashSet<Long>(failedBoards);
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

    private static int lowerBound(final Board board)
    {
        final ArrayList<Position> boxes = new ArrayList<Position>();
        final Queue<Position> goals = new LinkedList<Position>();
        for (int row = 0; row < board.height; row++) {
            for (int col = 0; col < board.width; col++) {
                if (Board.is(board.cells[row][col], Board.BOX)) {
                    boxes.add(new Position(row, col));
                }
                if (Board.is(board.cells[row][col], Board.GOAL)) {
                    goals.add(new Position(row, col));
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
     * TODO It might be smarter to implement a search that takes the actual
     * board into account.
     * 
     * @param a One of the positions
     * @param b The other position
     * @return The approximate distance between the two.
     */
    private static int distance(final Position a, final Position b)
    {
        return Math.abs(a.column - b.column) + Math.abs(a.row - b.row);
    }
}
