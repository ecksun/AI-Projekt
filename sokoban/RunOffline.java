package sokoban;

import java.io.File;
import java.util.List;

import sokoban.solvers.Solver;
import sokoban.solvers.SolverFactory;

public class RunOffline {

    public static void main(String[] args)
    {
        if (args.length < 2) {
            System.err.println("You need to supply the solver name and the board number as arguments!\n");
            System.exit(2);
        }
        
        int boardNumber = Integer.parseInt(args[1]);
        Solver solver = SolverFactory.loadSolver(args[0]);
        
        File slc = new File("custom.slc");
        List<String> boards = BoardParser.getBoardStrings(slc);
        
        Board board = BoardParser.parse(boards.get(boardNumber-1).getBytes());
        
        System.out.println(board);
        
        long beforeSolve = System.currentTimeMillis();
        String solution = solver.solve(board);
        long solveTime = System.currentTimeMillis() - beforeSolve;

        System.out.println(solution);
        System.out.println("Solve time (ms): " + solveTime);

    }
    
}
