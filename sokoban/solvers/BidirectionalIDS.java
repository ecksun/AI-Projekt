package sokoban.solvers;

import java.util.HashMap;
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

    @Override
    public String solve(final Board startBoard)
    {
        HashSet<Long> failedBoardsPuller = new HashSet<Long>();
        HashSet<Long> failedBoardsPusher = new HashSet<Long>();
        HashMap<Long, BoxPosDir> pullerStatesMap = new HashMap<Long, BoxPosDir>(); 
        HashMap<Long, BoxPosDir> pusherStatesMap = new HashMap<Long, BoxPosDir>();

        pusher = new IDSPusher(startBoard, failedBoardsPuller, pusherStatesMap, pullerStatesMap);
        puller = new IDSPuller(startBoard, failedBoardsPusher, pullerStatesMap, pusherStatesMap);

        boolean runPuller = true;
        int lowerBound = IDSCommon.lowerBound(startBoard);
        // TODO implement collision check in pusher and update accordingly here (remove line)
//        SearchInfo result;
        SearchInfo result = pusher.dfs(lowerBound);
                
        // IDS loop
        for (int maxDepth = lowerBound; maxDepth < IDSCommon.DEPTH_LIMIT; maxDepth++) {

            // Puller
            runPuller = true;
            if (runPuller) {
                result = puller.dfs(maxDepth);
            }
            // Pusher
            else {
                result = pusher.dfs(maxDepth);
            }
            
            if (result.solution != null) {
                System.out.println();
                return Board.solutionToString(result.solution);
            }
            else if (result.status == SearchStatus.Failed) {
                System.out.println("no solution!");
                return null;    
            }

            // TODO: implement collision check in pusher and activate this line
            runPuller = !runPuller;
        }

        System.out.println("Maximum depth reached!");
        return null;
    }

    @Override
    public int getIterationsCount()
    {
        // TODO
        return pusher.getIterationsCount() + puller.getIterationsCount();
    }

}
