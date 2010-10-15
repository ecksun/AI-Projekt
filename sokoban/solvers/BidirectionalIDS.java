package sokoban.solvers;

import java.util.HashSet;

import sokoban.Board;
import sokoban.Position;

/**
 * This solver performs a bidirectional (TODO iterative deepening DFS?) search.
 */
public class BidirectionalIDS extends IDSCommon implements Solver
{
    // Puller specific members.
    private static Board reversedBoard;
    private static int depth, maxDepth;
    private int boxesNotInStart, initialBoxesNotInStart;
    private boolean[][] boxStart;
    private Position playerStart;
    
    @Override
    public String solve(final Board startBoard)
    {
        final Board pullerStartBoard = (Board) startBoard.clone();
        
        failedBoards = new HashSet<Long>();
        final int lowerBound = lowerBound(startBoard);

        reverseBoard(pullerStartBoard);
        
        // IDS loop

        
        return null;
    }

    
    private void reverseBoard(final Board board) {
        // Store starting positions
        playerStart = board.positions[board.getPlayerRow()][board.getPlayerCol()];
        boxStart = new boolean[board.height][board.width];
        initialBoxesNotInStart = board.boxCount;
        for (int row = 0; row < board.height; row++) {
            for (int column = 0; column < board.width; column++) {
                if (Board.is(board.cells[row][column], Board.BOX)) {
                    if (Board.is(board.cells[row][column], Board.GOAL)) {
                        initialBoxesNotInStart--;
                    }
                    board.cells[row][column] &= ~Board.BOX;
                    boxStart[row][column] = true;
                }
            }
        }
        
        // Put the boxes in the goals
        for (int row = 0; row < board.height; row++) {
            for (int column = 0; column < board.width; column++) {
                if (Board.is(board.cells[row][column], Board.GOAL)) {
                    board.cells[row][column] |= Board.BOX;
                }
            }
        }
        
        board.forceReachabilityUpdate();
    }
    
    
}
