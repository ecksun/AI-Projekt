package sokoban.benchmarking;

import java.util.ArrayList;

/**
 * This is a container for results for a solver solving a specific board.
 */
public class ResultsList extends ArrayList<TestResult>
{

    /**
     * Calculates and returns the average run times of the current results.
     * 
     * @return Average run times (milliseconds).
     */
    public long averageRunTime()
    {
        return totalRunTime() / this.size();
    }

    /**
     * Returns the total (sum) run time of all test runs.
     */
    private long totalRunTime()
    {
        long totalRunTime = 0;

        for (TestResult result : this) {
            totalRunTime += result.getRunTime();
        }

        return totalRunTime;
    }

    /**
     * Returns the average iterations count.
     * 
     * @return
     */
    public int averageIterationsCount()
    {
        return totalIterationsCount() / this.size();
    }

    /**
     * Returns the total number of iterations used for solving the board.
     * 
     * @return Total number of iterations.
     */
    private int totalIterationsCount()
    {
        int totalIterationsCount = 0;

        for (TestResult result : this) {
            totalIterationsCount += result.getIterationsCount();
        }

        return totalIterationsCount;
    }
}
