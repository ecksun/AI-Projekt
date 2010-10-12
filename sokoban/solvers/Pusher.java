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
 * A solver that pushes boxes around.
 */
public class Pusher implements Solver
{
    /**
     * Small little container class for a searchnode
     * Each searchnode contains the path to it, the board its handling and the
     * parent.
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
            this.board = (Board) parent.board.clone();
            this.parent = parent;
        }

        /**
         * Create a new SearchNode from a parent
         * 
         * @param newPath The path from the parent to this node
         * @param parent The parent
         */
        public SearchNode(String newPath, SearchNode parent)
        {
            // TODO it is possible to generate the path from parent when needed.
            this(parent.path + newPath, parent.board, parent);
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
            queue.addAll(getAllBoxesMovement(node));
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
    private Collection<SearchNode> getAllBoxesMovement(SearchNode node)
    {
        Collection<SearchNode> tmp = new LinkedList<SearchNode>();
        for (Position box : node.board.getBoxes()) {
            // if (accessible(node.board, box)
            
            // This might be a problem with array index out of bounds if the box
            // is right next to the end of the board
            if (!Board.is(node.board.cells[box.row + 1][box.column],
                    Board.REJECT_BOX))
                tmp.add(new SearchNode("U", node));
            if (!Board.is(node.board.cells[box.row - 1][box.column],
                    Board.REJECT_BOX))
                tmp.add(new SearchNode("D", node));
            if (!Board.is(node.board.cells[box.row][box.column + 1],
                    Board.REJECT_BOX))
                tmp.add(new SearchNode("R", node));
            if (!Board.is(node.board.cells[box.row][box.column - 1],
                    Board.REJECT_BOX))
                tmp.add(new SearchNode("L", node));
        }
        return tmp;
    }
}
