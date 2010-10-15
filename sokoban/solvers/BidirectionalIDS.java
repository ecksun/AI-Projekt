package sokoban.solvers;

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

    @Override
    public String solve(final Board startBoard)
    {
        puller = new IDSPuller();
        pusher = new IDSPusher();

        // IDS loop
        for (int maxDepth = IDSCommon.lowerBound(startBoard); maxDepth < IDSCommon.DEPTH_LIMIT; maxDepth++) {

            final SearchInfo result;

            // TODO: Interlace visitedBoards in puller and pusher.
            // Maybe by supplying a common such data structure to the dfs()
            // method of each one?

            // TODO: Give maxDepth to the two dfs()'s.
            
            // Puller
            if (true) {
                result = puller.dfs();
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
