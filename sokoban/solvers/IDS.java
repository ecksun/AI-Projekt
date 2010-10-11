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
     * Recursive Depth-First algorithm
     * 
     * @param maxDepth The maximum depth.
     * @return
     */
    private LinkedList<Board.Direction> dfs(Board board, int maxDepth)
    {
        iterationsCount++;
        
        if (board.getRemainingBoxes() == 0) {
            // Found a solution
            return new LinkedList<Board.Direction>();
        }

        if (maxDepth <= 0)
            return null;

        for (Board.Direction dir : Board.Direction.values()) {
            // Check that the move is possible
            if (!board.canMove(dir))
                continue;

            // The move is possible

            // Make the move on a copy of the board
            Board successor = (Board) board.clone();
            successor.move(dir);
            
            // Penalty (in moves): Used for "soft" duplicate avoiding and heuristics 
            int penalty = 1;
            
            // Avoid identical box-player setups
            if (board.isBoxAhead(dir)) {
                // We just moved a box
                
                // Check for identical state with cells and player
                if (visitedBoards.contains(successor)) {
                    return null;
                } else {
                    visitedBoards.add(successor);
                }
            }

            // Recurse
            LinkedList<Board.Direction> sol = dfs(successor, maxDepth - penalty);
            if (sol != null) {
                sol.addFirst(dir);
                return sol;
            }
        }

        return null;
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
        
        System.out.println("IDS depth limit (progress): ");
        for (int maxDepth = 1; maxDepth < DEPTH_LIMIT; maxDepth += 3) {
            System.out.print(maxDepth + ".");
            
            visitedBoards = new HashSet<Board>();
            
            LinkedList<Board.Direction> solution = dfs(startBoard, maxDepth);
            if (solution != null) {
                System.out.println();
                return Board.solutionToString(solution);
            }
        }

        System.out.println("no solution!");
        return null;
    }

    @Override
    public int getIterationsCount()
    {
        return iterationsCount;
    }

}
