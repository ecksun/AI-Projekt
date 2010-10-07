package sokoban.solvers;

import sokoban.Board;

/**
 * An interface for the different solver.
 */
public interface Solver
{

    /**
     * Solves the specified board.
     * 
     * @param board
     *            The board to solve.
     * @return A string corresponding to the movements of the player
     */
    String solve(Board board);

    /**
     * Returns the number of iterations used for solving the board.
     * 
     * @return Number of iterations
     */
    int getIterationsCount();
}
