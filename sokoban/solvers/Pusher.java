/**
 * 
 */
package sokoban.solvers;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

import sokoban.Board;
import sokoban.Position;

/**
 *
 */
public class Pusher implements Solver
{
    /**
     *
     *
     */
    private class SearchNode
    {
        /**
         * The path to this searchnode
         */
        public final String path;
        /**
         * The board at this searchnode
         */
        public final Board board;
        /**
         * The parent searchnode
         */
        public final SearchNode parent;

        /**
         * Initialize a searchnode
         * 
         * @param path The path to this searchnode
         * @param board The board at this searchnode
         * @param parent The parent searchnode
         */
        public SearchNode(String path, Board board, SearchNode parent)
        {
            this.path = path;
            this.board = board;
            this.parent = parent;
        }
    }

    @Override
    public int getIterationsCount()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String solve(Board board)
    {
        Queue<SearchNode> queue = new LinkedList<SearchNode>();
        queue.add(new SearchNode("", board, null));
        SearchNode node;
        do {
            node = queue.poll();
            queue.addAll(getAllBoxesMovement(node.board));
        }
        while (node.board.getRemainingBoxes() != 0);
        return null;
    }

    /**
     * Return all possible movements of the boxes on the current board
     * 
     * @param board The board
     * @return A list of new SearchNodes
     */
    private Collection<SearchNode> getAllBoxesMovement(Board board)
    {
        Collection<SearchNode> tmp = new LinkedList<SearchNode>();
        for (Position box : board.getBoxes()) {
            tmp.addAll(getMoves(board, box));
        }
        return tmp;
    }

    /**
     * Get all possible moves for the specified box on the specified board
     * 
     * @param board The board to check for movements on
     * @param box The box to check for movements on
     * @return A list of searchnodes
     */
    private Collection<SearchNode> getMoves(Board board, Position box)
    {
        Collection<SearchNode> tmp = new LinkedList<SearchNode>();

        return tmp;
    }
}
