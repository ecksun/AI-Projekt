package sokoban.benchmarking;

import java.io.File;
import java.util.ArrayList;

import sokoban.Board;
import sokoban.BoardParser;
import sokoban.solvers.Solver;
import sokoban.solvers.SolverFactory;

/**
 * Benchmarker uses a Sokoban solver to solve different boards several times
 * while benchmarking performance.
 */
public class Benchmarker
{

    public static final String levelsFilename = "custom.slc";

    /**
     * The number of times to run tests on a board.
     */
    public static final int TEST_RUNS = 10;

    /**
     * The solver to be benchmarked.
     */
    private Solver solver;

    /**
     * Boards to test.
     */
    private ArrayList<Board> boards;

    /**
     * A list of results for each solved board.
     */
    private ArrayList<ResultsList> boardResults;

    /**
     * Constructs a new benchmarker of the specified solver.
     */
    public Benchmarker(Solver solver)
    {
        this.solver = solver;
        this.boards = new ArrayList<Board>();
        this.boardResults = new ArrayList<ResultsList>();
    }

    /**
     * Sets up the benchmarking environment.
     */
    public void setup()
    {
        File levelsFile = new File(new File("."), levelsFilename);
        ArrayList<String> boardStrings = BoardParser
                .getBoardStrings(levelsFile);

        for (String boardString : boardStrings) {
            Board board = new Board(boardString);
            boards.add(board);
        }
    }

    /**
     * Runs the tests and collects the benchmarking data.
     */
    public void runTests()
    {
        ResultsList results;
        long startTime, runTime;

        for (Board board : boards) {
            results = new ResultsList();
            System.out.println("Running tests on board:");
            System.out.println(board);

            for (int i = 0; i < TEST_RUNS; ++i) {
                startTime = System.currentTimeMillis();
                solver.solve(board);
                runTime = System.currentTimeMillis() - startTime;

                results
                        .add(new TestResult(runTime, solver
                                .getIterationsCount()));

            }

            boardResults.add(results);
        }
    }

    /**
     * Prints the collected performance values.
     */
    public void printBenchmarks()
    {
        System.out.println("--------------------");
        System.out.println("BENCHMARKING SECTION");
        System.out.println("--------------------");

        for (int i = 0; i < boardResults.size(); ++i) {
            ResultsList results = boardResults.get(i);
            System.out.printf("Board %d:", i);
            System.out.println();
            System.out.printf("Average run time:   %d", results
                    .averageRunTime());
            System.out.println();
            System.out.printf("Average iterations: %d", results
                    .averageIterationsCount());
            System.out.println();
            System.out.println();
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args)
    {

        Benchmarker benchmarker = new Benchmarker(SolverFactory
                .loadSolver(args[0]));

        benchmarker.setup();
        benchmarker.runTests();
        benchmarker.printBenchmarks();

    }

}
