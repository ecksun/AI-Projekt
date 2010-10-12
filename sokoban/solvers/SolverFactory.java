package sokoban.solvers;

import sokoban.solvers.Solver;

public class SolverFactory
{

    /**
     * Returns a solver instance that corresponds to the given solver name.
     * 
     * @param solverName A string containing the name of the solver class.
     * @return A solver instance.
     */
    public static Solver loadSolver(String solverName)
    {
        ClassLoader classLoader = SolverFactory.class.getClassLoader();
        Solver solver = null;
        
        try {
            solver = (Solver) classLoader.loadClass(
                    "sokoban.solvers." + solverName).newInstance();
        }
        catch (InstantiationException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        return solver; 
    }
}


