package sokoban.solvers;


/**
 * Create new solvers
 */
public class SolverFactory
{

    /**
     * Returns a solver instance that corresponds to the given solver name.
     * 
     * @param solverName A string containing the name of the solver class.
     * @return A solver instance.
     */
    public static Solver loadSolver(final String solverName)
    {
        final ClassLoader classLoader = SolverFactory.class.getClassLoader();
        Solver solver = null;

        try {
            solver = (Solver) classLoader.loadClass(
                    "sokoban.solvers." + solverName).newInstance();
        }
        catch (final InstantiationException e) {
            e.printStackTrace();
        }
        catch (final IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (final ClassNotFoundException e) {
            e.printStackTrace();
        }

        return solver;
    }
}
