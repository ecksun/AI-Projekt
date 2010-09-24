package sokoban.solvers;

import java.util.LinkedList;
import java.util.List;

import sokoban.Board;

public class IDS implements Solver
{

    private final Board board;
    private final int DEPTH_LIMIT = 1000;
    
    public IDS(Board startBoard)
    {
        this.board = startBoard;
    }
    
    /**
     * Recursive Depth-First algorithm
     * @param maxDepth  The maximum depth.
     * @return
     */
    private List<Character> dfs(int maxDepth)
    {
        if (board.getRemainingBoxes() == 0) {
            // Found a solution
            return new LinkedList<Character>();
        }
        
        if (maxDepth == 0) return null;
        
        // TODO implement DFS here
        return null;
    }
    
    /**
     * 
     * @param solution  An array from dfs()
     * @return The solution as a string
     */
    private String solutionToString(List<Character> solution) {
        StringBuilder sb = new StringBuilder(2*solution.size());
        for (Character move : solution) {
            sb.append(move);
            sb.append(' ');
        }
        return sb.toString();
    }

    @Override
    public String solve()
    {
        System.out.println(board);
        
        for (int maxDepth = 1; maxDepth < DEPTH_LIMIT; maxDepth++) {
            List<Character> solution = dfs(maxDepth);
            if (solution != null) {
                return solutionToString(solution); 
            }
        }
        
        return null;
    }

}
