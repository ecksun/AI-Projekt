package sokoban;

import java.io.File;
import java.util.*;

import sokoban.Board.*;
import sokoban.solvers.*;

public class RunOffline {

    public static void main(String[] args)
    {
        if (args.length < 3) {
            System.err.println("Usage: java sokoban.RunOffline  solver  level_file  level_number\n");
            System.exit(2);
        }
        
        boolean validate = false;
        if (args.length == 4 && args[3].equals("--validate")) {
            validate = true;
        }        
        
        int exitCode = 1;
        int boardNumber = Integer.parseInt(args[2]);
        Solver solver = SolverFactory.loadSolver(args[0]);
        
        File slc = new File(args[1]);
        List<String> boards = BoardParser.getBoardStrings(slc);
        
        Board board = new Board(boards.get(boardNumber-1).getBytes());
        
        System.out.println(board);
        
        long beforeSolve = System.currentTimeMillis();
        String solution = solver.solve(board);
        long solveTime = System.currentTimeMillis() - beforeSolve;

        if (solution != null) {
            exitCode = 0;
        }
        
        System.out.println("Solve time (ms): " + solveTime);
        System.out.println("Expanded nodes: " + solver.getIterationsCount());
        System.out.println("Solution: " + solution);

        
        if (validate) {
            Board boardToValidate = new Board(boards.get(boardNumber-1).getBytes());
            validate(boardToValidate, solution);
        }
        
        System.exit(exitCode);
    }

    /**
     * Validates the specified board and solution.
     * 
     * @param board
     * @param solution
     */
    private static void validate(Board board, String solutionString)
    {
//        System.out.println("Initial state:\n" + board);
        
        
//        IDSPuller ip2 = new IDSPuller();
//        ip2.reverseBoard(board);
//        
//        board.reverseMove(byteToDir((byte)'D'));
//        System.out.println("Reverting move D results in:");
//        System.out.println(board);
//        
//        board.reverseMove(byteToDir((byte)'D'));
//        System.out.println("Reverting move D results in:");
//        System.out.println(board);
//        
//        board.reverseMove(byteToDir((byte)'L'));
//        System.out.println("Reverting move L results in:");
//        System.out.println(board);
//        
//        board.reverseMove(byteToDir((byte)'R'));
//        System.out.println("Reverting move R results in:\n" + board);
//        
//        board.reverseMove(byteToDir((byte)'R'));
//        System.out.println("Reverting move R results in:\n" + board);
//       
//        board.reverseMove(byteToDir((byte)'R'));
//        System.out.println("Reverting move R results in:\n" + board);
//        
//        board.reverseMove(byteToDir((byte)'R'));
//        System.out.println("Reverting move R results in:\n" + board);
//        
//        board.reverseMove(byteToDir((byte)'D'));
//        System.out.println("Reverting move D results in:\n" + board);
//        
//        board.reverseMove(byteToDir((byte)'L'));
//        System.out.println("Reverting move L results in:\n" + board);
//        
//        board.reverseMove(byteToDir((byte)'L'));
//        System.out.println("Reverting move L results in:\n" + board);
//        
//        board.reverseMove(byteToDir((byte)'U'));
//        System.out.println("Reverting move U results in:\n" + board);
//        
//        board.reverseMove(byteToDir((byte)'L'));
//        System.out.println("Reverting move L results in:\n" + board);
//        
//        System.exit(1);
        
        
//        System.out.println("board should look like\n" + board);
        
//        board.movePlayer(null, new Position(2,5));
        
        LinkedList<Board.Direction> solution = new LinkedList<Direction>();
        
        int bajskebab = 1;
        
        for (byte direction : solutionString.getBytes()) {
            Board.Direction dir = byteToDir(direction);
            if (dir != null || bajskebab++ > 0) {
                solution.addLast(dir);
            }
        }
        
//        IDSPuller ip = new IDSPuller();
//        ip.reverseBoard(board);

        System.out.println("Initial state:\n" + board);
        
//        Iterator<Board.Direction> it = solution.descendingIterator();
        Iterator<Board.Direction> it = solution.iterator();
        while (it.hasNext()) {
            Board.Direction dir = it.next();
//            board.reverseMove(dir);
//            System.out.println("Reversing move " + dir + " results in:");
            try {
                board.move(dir);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Outside board.");
                return;
            }
            System.out.println("Moving " + dir + " results in:\n" + board);
        }
       
    }

    private static Direction byteToDir(byte direction)
    {
        if (direction == 'U') {
            return Board.Direction.UP;
        } else if (direction == 'R') {
            return Board.Direction.RIGHT;
        } else if (direction == 'D') {
            return Board.Direction.DOWN;
        } else if (direction == 'L') {
            return Board.Direction.LEFT;
        } 
        
        return null;
    }
    
    
    
    
}
