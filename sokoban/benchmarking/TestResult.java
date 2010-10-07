package sokoban.benchmarking;

/**
 * Holds a test result.
 */
public class TestResult
{
    private long runTime;

    private int  iterations;

    /**
     * Constructs a new test result with the given values.
     * 
     * @param runTime
     *            The number of milliseconds the program ran.
     * @param iterations
     *            The number of iterations the algorithm used.
     */
    public TestResult(long runTime, int iterations)
    {
        this.runTime = runTime;
        this.iterations = iterations;
    }
    
    /**
     * Returns the run time of this test result.
     * 
     * @return The run time.
     */
    public long getRunTime() {
        return runTime;
    }
    
    /**
     * Returns the number of iterations required to solve the board.
     * 
     * @return The number of iterations.
     */
    public int getIterationsCount() {
        return iterations;
    }
    
}
