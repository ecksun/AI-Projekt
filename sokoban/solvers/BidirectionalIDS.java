package sokoban.solvers;

import java.util.HashSet;

import sokoban.Board;
import sokoban.SearchInfo;
import sokoban.SearchStatus;

/**
 * This solver performs a bidirectional (TODO iterative deepening DFS?) search.
 */
public class BidirectionalIDS implements Solver
{
    private IDSPuller puller;
    private IDSPusher pusher;

    private HashSet<Long> failedBoards;

    @Override
    public String solve(final Board startBoard)
    {
        failedBoards = new HashSet<Long>();

        puller = new IDSPuller(failedBoards);
        pusher = new IDSPusher(failedBoards);

        boolean runPuller = true;

        // IDS loop
        for (int maxDepth = IDSCommon.lowerBound(startBoard); maxDepth < IDSCommon.DEPTH_LIMIT; maxDepth++) {

            final SearchInfo result;

            // TODO: Interlace visitedBoards in puller and pusher.
            // Maybe by supplying a common such data structure to the dfs()
            // method of each one?

            // TODO: Give maxDepth to the two dfs()'s.

            // Puller
            if (runPuller) {
                result = puller.dfs();
                runPuller = !runPuller;
            }
            // Pusher
            else {
                result = pusher.dfs();
            }

            if (result.solution != null) {
                System.out.println();
                return Board.solutionToString(result.solution);
            }
            else if (result.status == SearchStatus.Failed) {
                System.out.println("no solution!");
                return null;
            }
            // else if (result.status == SearchStatus.Collision) {
            // Backtrack the solution.
            // }

        }

        System.out.println("Maximum depth reached!");
        return null;
    }

    @Override
    public int getIterationsCount()
    {
        // TODO
        return 0;
    }

}
