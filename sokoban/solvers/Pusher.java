/**
 * 
 */
package sokoban.solvers;

import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

import sokoban.Board;
import sokoban.Position;
import sokoban.Board.Direction;

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
        public final Deque<Direction> path;
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
        public SearchNode(Deque<Direction> path, Board board, SearchNode parent)
        {
            System.out.println("Generating new node");
            this.path = path;
            this.board = (Board) board.clone();
            this.parent = parent;
        }

        /**
         * Create a new SearchNode from a parent and moves the player in the
         * specified direction.
         * 
         * @param newPath The path from the parent to this node
         * @param dir The direction to move the player in.
         * @param parent The parent state.
         */
        public SearchNode(Deque<Direction> newPath, Board.Direction dir,
                Position playerStartPosition, SearchNode parent)
        {
            // TODO it is possible to generate the path from parent when needed.
            this(new LinkedList<Direction>(), parent.board, parent);
            board.playerCol = playerStartPosition.column;
            board.playerRow = playerStartPosition.row;
            path.addAll(newPath);
            path.add(dir);
            board.move(dir);
        }
    }

    public int getIterationsCount()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public String solve(Board board)
    {
        Queue<SearchNode> queue = new LinkedList<SearchNode>();
        queue.add(new SearchNode(new LinkedList<Direction>(), board, null));
        SearchNode node;
        do {
            if ((node = queue.poll()) == null) {
                return null;
            }
            queue.addAll(getAllSuccessorStates(node));
        }
        while (node.board.getRemainingBoxes() != 0);
        System.out.println(Board.solutionToString(node.path));
        return Board.solutionToString(node.path);
    }

    /**
     * Finds all possible new states, expands and returns them for a specific
     * search node.
     * 
     * @param board The board
     * @return A list of new SearchNodes
     */
    private Collection<SearchNode> getAllSuccessorStates(SearchNode node)
    {
        Collection<SearchNode> tmp = new LinkedList<SearchNode>();
        System.out.println("node:" + node);
        System.out.println("node.board:\n" + node.board);
        for (Position box : node.board.getBoxes()) {
            // if (accessible(node.board, box)

            Deque<Direction> path;
            Position player;
            // This might be a problem with array index out of bounds if the box
            // is right next to the end of the board
            if (!Board.is(node.board.cells[box.row + 1][box.column],
                    Board.REJECT_BOX)) {
                System.out.println("Board.is down");
                player = new Position(box.row - 1, box.column);
                if ((path = node.board.findPath(player)) != null) {
                    System.out.println("Baord.is down foudn path");
                    tmp.add(new SearchNode(path, Direction.DOWN, player, node));
                }
            }
            if (!Board.is(node.board.cells[box.row - 1][box.column],
                    Board.REJECT_BOX)) {
                System.out.println("board is up");
                player = new Position(box.row + 1, box.column);
                if ((path = node.board.findPath(player)) != null) {
                    System.out.println("board is up found path");
                    tmp.add(new SearchNode(path, Direction.UP, player, node));
                }
            }
            if (!Board.is(node.board.cells[box.row][box.column + 1],
                    Board.REJECT_BOX)) {
                player = new Position(box.row, box.column - 1);
                if ((path = node.board.findPath(player)) != null) {
                    tmp
                            .add(new SearchNode(path, Direction.RIGHT, player,
                                    node));

                }
            }
            if (!Board.is(node.board.cells[box.row][box.column - 1],
                    Board.REJECT_BOX)) {
                player = new Position(box.row, box.column + 1);
                if ((path = node.board.findPath(player)) != null) {
                    tmp.add(new SearchNode(path, Direction.LEFT, player, node));

                }
            }

        }
        return tmp;
    }
}
