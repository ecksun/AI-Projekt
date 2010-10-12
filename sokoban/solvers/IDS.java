package sokoban.solvers;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

import sokoban.Board;
import sokoban.Board.Direction;

public class IDS implements Solver
{

    private final int DEPTH_LIMIT = 1000;
    private Random rand = new Random();
    private int iterationsCount = 0;
    
    /**
     * Set of visited boards, including the player position
     */ 
    private Set<Board> visitedBoards;
    
    /**
     * Boards that just lead to deadlocks or already visited boards. It
     * doesn't make sense to visit these in later iterations.
     */ 
    private Set<Board> failedBoards;
    
    
    enum SearchStatus
    {
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
        
        static SearchInfo Inconclusive = new SearchInfo(SearchStatus.Inconclusive);
        static SearchInfo Failed = new SearchInfo(SearchStatus.Inconclusive);
        
        public SearchInfo(SearchStatus status)
        {
            this.status = status;
            this.solution = null;
        }
        
        private SearchInfo()
        {
            this.status = SearchStatus.Solution;
            this.solution = new LinkedList<Board.Direction>();
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
    private SearchInfo dfs(Board board, int maxDepth)
    {
        iterationsCount++;
        
        if (board.getRemainingBoxes() == 0) {
            // Found a solution
            return SearchInfo.emptySolution();
        }

        if (maxDepth <= 0)
            return SearchInfo.Inconclusive;

        // True if at least one successor tree was inconclusive.
        boolean inconclusive = false;
        
        // Keep track of failed successors. If all successors fail we can
        // fail ourselves (reduces the search tree). 
        Board[] failed = new Board[4];
        int numFailed = 0;
        
        for (Board.Direction dir : Board.Direction.values()) {
            // Check that the move is possible
            if (!board.canMove(dir))
                continue;

            // The move is possible

            // Make the move on a copy of the board
            // TODO keep track of this move (and any box that gets moved)
            //      and restore it instead of making a copy. If the node
            //      has to be stored in failedBoards or visitedBoards, then
            //      make a copy "on demand".
            Board successor = (Board) board.clone();
            successor.move(dir);
            
            // Skip boards that are known to not have solutions in their subtree
            if (failedBoards.contains(successor)) {
                continue;
            }
            
            // Penalty (in moves): Used for "soft" duplicate avoiding and heuristics 
            int penalty = 1;
            
            // Avoid identical box-player setups
            // (duplicate player moves are avoided by marking cells as
            // visited in Board.move)
            if (successor.isBoxNearby()) {
                // Check for identical state with cells and player
                if (visitedBoards.contains(successor)) {
                    continue;
                } else {
                    visitedBoards.add(successor);
                }
            }

            // Recurse
            SearchInfo result = dfs(successor, maxDepth - penalty);
            
            switch (result.status) {
                case Solution:
                    // Found a solution. Return it now!
                    result.solution.addFirst(dir);
                    return result;
                case Inconclusive:
                    // Make the parent inconclusive too
                    inconclusive = true;
                    continue;
                case Failed:
                    // Keep the failed board for now
                    failed[numFailed++] = successor;
                    continue;
            }
        }
        
        if (inconclusive) {
            // Add all successors that failed to the failed set
            for (int i = 0; i < numFailed; i++) {
                failedBoards.add(failed[i]);
            }
            return SearchInfo.Inconclusive;
        } else {
            // All successors failed, so this node is failed
            failedBoards.add(board);
            return SearchInfo.Failed;
        }
    }

    /**
     * Returns an array of Direction enums in random order.
     * 
     * @return An array of all Direction enums.
     */
    private Direction[] getDirectionsRandomly()
    {
        Direction[] directions = Board.Direction.values();

        // Shuffle array
        for (int i = 0; i < directions.length; i++) {
            int randomPosition = rand.nextInt(directions.length);
            Direction temp = directions[i];
            directions[i] = directions[randomPosition];
            directions[randomPosition] = temp;
        }
        
        return directions;
    }


    public String solve(Board board)
    {
        final Board startBoard = board;
        failedBoards = new HashSet<Board>();
        
        System.out.println("IDS depth limit (progress): ");
        for (int maxDepth = 1; maxDepth < DEPTH_LIMIT; maxDepth += 3) {
            System.out.print(maxDepth + ".");
            
            visitedBoards = new HashSet<Board>();
            
            SearchInfo result = dfs(startBoard, maxDepth);
            if (result.solution != null) {
                System.out.println();
                return Board.solutionToString(result.solution);
            } else if (result.status == SearchStatus.Failed) {
                System.out.println("no solution!");
                return null;
            }
        }

        System.out.println("maximum depth reached!");
        return null;
    }

    public int getIterationsCount()
    {
        return iterationsCount;
    }

}
