package sokoban.solvers;

import sokoban.Board;

/**
 * 
 */

/**
 * An interface for the different solver.
 */
public interface Solver
{
    
    /**
     * Solve the board
     * 
     * @param board The board to solve.
     * @return A string corresponding to the movements of the player
     */
    String solve();
}
