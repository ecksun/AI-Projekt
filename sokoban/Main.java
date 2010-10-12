package sokoban;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import sokoban.solvers.Solver;
import sokoban.solvers.SolverFactory;

/**
 * The main class that handles the execution of the program
 */
public class Main
{
    /**
     * The main method that is run when the program is executed
     * 
     * @param args The CLI argument list
     */
    public static void main(String[] args)
    {
        if (args.length < 2) {
            System.err
                    .println("You need to supply solver and board number as argument");
            return;
        }
        try {
            byte[] boardbytes = new byte[1024];
            Solver solver = SolverFactory.loadSolver(args[0]);
            int boardNumber = Integer.parseInt(args[1]);

            Socket socket = new Socket("cvap103.nada.kth.se", 5555);
            InputStream inRaw = socket.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(inRaw));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            out.println(boardNumber);

            inRaw.read(boardbytes);
            long beforeParse = System.currentTimeMillis();
            Board board = BoardParser.parse(boardbytes);
            long parseTime = System.currentTimeMillis() - beforeParse;
            System.out.println(board);
            System.out.println("Parse time (ms): " + parseTime);

            long beforeSolve = System.currentTimeMillis();

            String solution = solver.solve(board);
            long solveTime = System.currentTimeMillis() - beforeSolve;

            out.println(solution);
            System.out.println("Solve time (ms): " + solveTime);
            System.out.println("Solution: " + solution);

            String result = in.readLine();

            System.out.println(result);
            out.close();
            in.close();
            socket.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
