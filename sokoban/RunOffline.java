package sokoban;

import java.io.File;
import java.util.List;

import sokoban.solvers.Puller;
import sokoban.solvers.Solver;

public class RunOffline {

    public static void main(String[] args)
    {
        if (args.length < 1) {
            System.err.println("You need to supply the board number as an argument!\n");
            System.exit(2);
        }
        
        int boardNumber = Integer.parseInt(args[0]);
        
        File slc = new File("custom.slc");
        List<String> boards = BoardParser.getBoardStrings(slc);
        
        Board board = BoardParser.parse(boards.get(boardNumber-1).getBytes());
        
        System.out.println(board);
        
        long beforeSolve = System.currentTimeMillis();
        Solver solver = new Puller();
        String solution = solver.solve(board);
        long solveTime = System.currentTimeMillis() - beforeSolve;

        System.out.println(solution);
        System.out.println("Solve time (ms): " + solveTime);

    }
    
}
