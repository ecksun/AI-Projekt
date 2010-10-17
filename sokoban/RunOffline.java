package sokoban;

import java.io.File;
import java.util.List;

import sokoban.solvers.Solver;
import sokoban.solvers.SolverFactory;

/**
 * A class to be able to test the solvers against levels on file
 */
public class RunOffline
{

    /**
     * The main method, will do the execution
     * 
     * @param args Contains what class to run, which solver, level and level
     *            file.
     */
    public static void main(final String[] args)
    {
        if (args.length < 3) {
            System.err
                    .println("Usage: java sokoban.RunOffline  solver  level_file  level_number\n");
            System.exit(2);
        }

        int exitCode = 1;
        final int boardNumber = Integer.parseInt(args[2]);
        final Solver solver = SolverFactory.loadSolver(args[0]);

        final File slc = new File(args[1]);
        final List<String> boards = BoardParser.getBoardStrings(slc);

        final Board board = new Board(boards.get(boardNumber - 1).getBytes());

        System.out.println(board);

        final long beforeSolve = System.currentTimeMillis();
        final String solution = solver.solve(board);
        final long solveTime = System.currentTimeMillis() - beforeSolve;

        if (solution != null) {
            exitCode = 0;
        }

        System.out.println("Solve time (ms): " + solveTime);
        System.out.println("Expanded nodes: " + solver.getIterationsCount());
        System.out.println("Solution: " + solution);

        System.exit(exitCode);
    }

}
