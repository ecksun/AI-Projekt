package sokoban.solvers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import sokoban.Board;
import sokoban.Position;
import sokoban.SearchInfo;
import sokoban.SearchStatus;
import sokoban.Board.Direction;

/**
 * A solver that pushes boxes around with iterative deepening and
 * hashing to avoid duplicate states.
 */
public class IDSPusher implements Solver
{
    private final int DEPTH_LIMIT = 1000;
    /**
     * The number of generated nodes
     */
    public static int generatedNodes = 0;
    private static int remainingDepth;
    private static Board board;

    public int getIterationsCount()
    {
        return generatedNodes;
    }

    /**
     * Set of visited boards, including the player position
     */
    private HashSet<Long> visitedBoards;

    /**
     * Boards that just lead to deadlocks or already visited boards. It
     * doesn't make sense to visit these in later iterations.
     */
    private HashSet<Long> failedBoards;

    /**
     * Recursive Depth-First algorithm
     * 
     * @param maxDepth The maximum depth.
     * @return
     */
    private SearchInfo dfs()
    {
        generatedNodes++;

        if (board.getRemainingBoxes() == 0) {
            // Found a solution
            return SearchInfo.emptySolution();
        }

        if (remainingDepth <= 0) {
            return SearchInfo.Inconclusive;
        }

        // True if at least one successor tree was inconclusive.
        boolean inconclusive = false;

        final long hash = board.getZobristKey();

        final Position source = board.positions[board.getPlayerRow()][board
                .getPlayerCol()];
        remainingDepth--;

        final byte[][] cells = board.cells;
        for (final Position player : board.findReachableBoxSquares()) {
            for (final Direction dir : Board.Direction.values()) {
                final Position boxFrom = board.getPosition(player, Board.moves[dir
                        .ordinal()]);
                Position boxTo = board.getPosition(boxFrom, Board.moves[dir
                        .ordinal()]);

                // Check if the move is possible
                if (Board.is(cells[boxFrom.row][boxFrom.column], Board.BOX)
                        && !Board.is(cells[boxTo.row][boxTo.column],
                                Board.REJECT_BOX)) {

                    final int move[] = Board.moves[dir.ordinal()];
                    if (inTunnel(dir, boxTo)
                            && !Board.is(
                                    cells[boxTo.row + move[0]][boxTo.column
                                            + move[1]],
                                    (byte) (Board.REJECT_BOX | Board.GOAL))) {
                    }

                    // Tunnel detection:
                    // If found, push as many steps in same direction as
                    // possible.
                    int numberOfTunnelMoves = 0;
                    while (inTunnel(dir, boxTo)
                            && !Board.is(
                                    cells[boxTo.row + move[0]][boxTo.column
                                            + move[1]],
                                    (byte) (Board.REJECT_BOX | Board.GOAL))) {
                        // Count tunnel moves.
                        numberOfTunnelMoves++;
                        // Update boxTo position one step.
                        boxTo = board.getPosition(boxTo, move);
                    }

                    final Position playerTo = board.getPosition(boxTo,
                            Board.moves[dir.reverse().ordinal()]);

                    // Move the player and push the box
                    board.moveBox(boxFrom, boxTo);
                    board.movePlayer(source, playerTo);

                    SearchInfo result = SearchInfo.Failed;
                    // Check if we got a freeze deadlock
                    if (!freezeDeadlock(boxTo, DEADLOCK_BOTH, new HashSet<Position>())) {
                        if (visitedBoards.add(board.getZobristKey())) {
                            result = dfs();
                        }
                    }

                    // Restore changes
                    board.moveBox(boxTo, boxFrom);
                    board.movePlayer(playerTo, source);

                    // Evaluate result
                    switch (result.status) {
                        case Solution:
                            // We have found a solution. Find the path of
                            // the move and add it to the solution.
                            board.clearFlag(Board.VISITED);

                            // Add tunnel path directions, if any.
                            for (int i = 0; i < numberOfTunnelMoves; i++) {
                                // We always walk in the same direction in a
                                // tunnel.
                                result.solution.addFirst(dir);
                            }

                            // Add standard direction for this state.
                            result.solution.addFirst(dir);

                            // Add path from previous player position to
                            // reachable position.
                            result.solution.addAll(0, board.findPath(source,
                                    player));
                            return result;
                        case Inconclusive:
                            // Make the parent inconclusive too
                            inconclusive = true;
                            continue;
                        case Failed:
                            // Mark this node as failed
                            continue;
                    }
                }
            }
        }

        remainingDepth++;

        if (inconclusive) {
            // Add all successors that failed to the failed set
            return SearchInfo.Inconclusive;
        }
        else {
            // All successors failed, so this node is failed
            failedBoards.add(hash);
            return SearchInfo.Failed;
        }
    }

    /**
     * Checks if the given box position is a placement that has no influence on
     * the board, i.e. if it is inside a tunnel.
     * 
     * @param dir The direction in which the box was pushed in order to get
     *            where it is.
     * @param box The position of the box after it has been pushed in the given
     *            direction.
     * @return True if the box is has gone into a tunnel, which has no influence
     *         on the board, and can therefore be pushed all the way outside.
     */
    private boolean inTunnel(final Direction dir, final Position box)
    {
        // #v#
        // 1$2
        if (dir == Direction.DOWN && isWall(box.row - 1, box.column - 1)
                && isWall(box.row - 1, box.column + 1)) {
            // 1 or 2 above is a wall (or both)
            return isWall(box.row, box.column - 1)
                    || isWall(box.row, box.column + 1);
        }

        // 1$2
        // #^#
        if (dir == Direction.UP && isWall(box.row + 1, box.column - 1)
                && isWall(box.row + 1, box.column + 1)) {
            // 1 or 2 above is a wall (or both)
            return isWall(box.row, box.column - 1)
                    || isWall(box.row, box.column + 1);
        }

        // #1
        // >$
        // #2
        if (dir == Direction.RIGHT && isWall(box.row - 1, box.column - 1)
                && isWall(box.row + 1, box.column - 1)) {
            // 1 or 2 above is a wall (or both)
            return isWall(box.row - 1, box.column)
                    || isWall(box.row + 1, box.column);
        }

        // 1#
        // $<
        // 2#
        if (dir == Direction.LEFT && isWall(box.row - 1, box.column + 1)
                && isWall(box.row + 1, box.column + 1)) {
            // 1 or 2 above is a wall (or both)
            return isWall(box.row - 1, box.column)
                    || isWall(box.row + 1, box.column);
        }

        return false;
    }

    /**
     * Checks if the position specified by the given row and column is a wall.
     * 
     * @param row The row.
     * @param col The column.
     * @return True if the given position is a wall, otherwise false.
     */
    private boolean isWall(final int row, final int col)
    {
        return Board.is(board.cells[row][col], Board.WALL);
    }

    public String solve(final Board startBoard)
    {
        failedBoards = new HashSet<Long>();
        long startTime = System.currentTimeMillis();
        final int lowerBound = lowerBound(startBoard);
        System.out.println("lowerBound(): " + lowerBound + " took "
                + (System.currentTimeMillis() - startTime) + " ms");
        System.out.println("IDS depth limit (progress): ");
        for (int maxDepth = lowerBound; maxDepth < DEPTH_LIMIT; maxDepth += 3) {
            System.out.print(maxDepth + ".");

            visitedBoards = new HashSet<Long>(failedBoards);
            remainingDepth = maxDepth;
            board = (Board) startBoard.clone();
            visitedBoards.add(board.getZobristKey());

            final SearchInfo result = dfs();
            if (result.solution != null) {
                System.out.println();
                return Board.solutionToString(result.solution);
            }
            else if (result.status == SearchStatus.Failed) {
                System.out.println("no solution!");
                return null;
            }
        }

        System.out.println("maximum depth reached!");
        return null;
    }

    private static int lowerBound(final Board board)
    {
        final ArrayList<Position> boxes = new ArrayList<Position>();
        final Queue<Position> goals = new LinkedList<Position>();
        for (int row = 0; row < board.height; row++) {
            for (int col = 0; col < board.width; col++) {
                if (Board.is(board.cells[row][col], Board.BOX)) {
                    boxes.add(board.positions[row][col]);
                }
                if (Board.is(board.cells[row][col], Board.GOAL)) {
                    goals.add(board.positions[row][col]);
                }
            }
        }
        int result = 0;
        while (!goals.isEmpty()) {
            final Position goal = goals.poll();
            Position minBox = null;
            int min = Integer.MAX_VALUE;
            for (final Position box : boxes) {
                final int tmp = distance(goal, box);
                if (tmp < min) {
                    min = tmp;
                    minBox = box;
                }
            }

            boxes.remove(minBox);
            result += min;
        }
        return result;
    }

    /**
     * Approximate the distance between two positions
     * 
     * The distance will be the absolute minimum and are guaranteed to be equal
     * to or greater then the real distance.
     * 
     * TODO It might be smarter to implement a search that takes the actual
     * board into account.
     * 
     * @param a One of the positions
     * @param b The other position
     * @return The approximate distance between the two.
     */
    private static int distance(final Position a, final Position b)
    {
        return Math.abs(a.column - b.column) + Math.abs(a.row - b.row);
    }

    /**
     * Check if the move resulted in a freeze deadlock
     * (two boxes between each other next to a wall)
     * 
     * This method assumes the move is valid.
     * 
     * @param from The previous position
     * @param to The new position
     * @return True if there is a freeze deadlock
     */
    private final static byte DEADLOCK_BOTH = 0;
    private final static byte DEADLOCK_HORIZONTAL = 1;
    private final static byte DEADLOCK_VERTICAL = 2;
    private boolean freezeDeadlock(Position box, byte type, HashSet<Position> visited)
    {

        visited.add(box);

        if (Board.is(board.cells[box.row][box.column], Board.GOAL))
            return false;
        // TODO: Do not move the box before checking freeze deadlock, creates deadlock with it self.
        if (type == DEADLOCK_BOTH) {
            boolean blockedVertical = false;
            boolean blockedHorizontal = false;
            
//            System.out.println("Looking for deadlock at position: " + box);
            
            // If there is a wall to the left or right
            if (Board.is(board.cells[box.row][box.column+1], Board.WALL) || Board.is(board.cells[box.row][box.column-1], Board.WALL)) {
                blockedHorizontal = true;
//                System.out.println(" - Wall to the left or right");
            }
            
            // If there is a wall to the top or bottom
            if (Board.is(board.cells[box.row+1][box.column], Board.WALL) || Board.is(board.cells[box.row-1][box.column], Board.WALL)) {
                blockedVertical = true;
//                System.out.println(" - Wall above or below");
            }
            
            // If there is a box_trap (simple deadlock check) to the left and right
            if (Board.is(board.cells[box.row][box.column+1], Board.BOX_TRAP) && Board.is(board.cells[box.row][box.column-1], Board.BOX_TRAP)) {
                blockedHorizontal = true;
//                System.out.println(" - Box trap to the left and right");
            }
            
            // If there is a box_trap (simple deadlock check) to the top and bottom
            if (Board.is(board.cells[box.row+1][box.column], Board.BOX_TRAP) && Board.is(board.cells[box.row-1][box.column], Board.BOX_TRAP)) {
                blockedVertical = true;
//                System.out.println(" - Box trap above and below");
            }
            
            // If we are both blocked horizontal and vertical, return deadlock
            if (blockedVertical && blockedHorizontal) {
//                System.out.println(" - Both top/bottom and left/right are blocked");
                return true;
            // Only horizontal
            } else if (!blockedVertical && blockedHorizontal) {
//                System.out.println(" - Only horizontal (left/right) is blocked.");
                if (Board.is(board.cells[box.row+1][box.column], Board.BOX)) {
//                    System.out.println("  - Box below, check it");
                    Position tempPos = board.positions[box.row+1][box.column];
                    if (!visited.contains(tempPos)) {
                        return freezeDeadlock(tempPos, DEADLOCK_HORIZONTAL, visited);
                    }
                    //return freezeDeadlock(new Position(box.row+1, box.column), DEADLOCK_HORIZONTAL, visited); // TODO not updated with new positions
                }
                
                if (Board.is(board.cells[box.row-1][box.column], Board.BOX)) {
//                    System.out.println("  - Box above, check it");
                    Position tempPos = board.positions[box.row-1][box.column];
                    if (!visited.contains(tempPos)) {
                        return freezeDeadlock(tempPos, DEADLOCK_HORIZONTAL, visited);
                    }
                    //return freezeDeadlock(new Position(box.row-1, box.column), DEADLOCK_HORIZONTAL, visited); // TODO not updated with new positions
                }
            // Only vertical
            } else if (!blockedHorizontal && blockedVertical) {
//                System.out.println(" - Only vertical (top/bottom) is blocked.");
                if (Board.is(board.cells[box.row][box.column+1], Board.BOX)) {
//                    System.out.println("  - Box to the right, check it");
                    Position tempPos = board.positions[box.row][box.column+1];
                    if (!visited.contains(tempPos)) {
                        return freezeDeadlock(tempPos, DEADLOCK_VERTICAL, visited);
                    }
                    //return freezeDeadlock(new Position(box.row, box.column+1), DEADLOCK_VERTICAL, visited); // TODO not updated with new positions
                }
                
                if (Board.is(board.cells[box.row][box.column-1], Board.BOX)) {
//                    System.out.println("  - Box to the left, check it");
                    Position tempPos = board.positions[box.row][box.column-1];
                    if (!visited.contains(tempPos)) {
                        return freezeDeadlock(tempPos, DEADLOCK_VERTICAL, visited);
                    }
                    //return freezeDeadlock(new Position(box.row, box.column-1), DEADLOCK_VERTICAL, visited); // TODO not updated with new positions
                }
            // No deadlock
            } else {
//                System.out.println(" - No deadlock found");
                return false;
            }
        // HORIZONTAL CHECK
        } else if (type == DEADLOCK_HORIZONTAL) {
//            System.out.println("   - Check horizontal");
            
            // Check goal
            if (Board.is(board.cells[box.row][box.column], (byte) (Board.GOAL & Board.BOX))) {
//                System.out.println("    - Box in goal, no deadlock");
                return false;
            }

            // If there is a wall to the left or right
            if (Board.is(board.cells[box.row][box.column+1], Board.WALL) || Board.is(board.cells[box.row][box.column-1], Board.WALL)) {
//                System.out.println("    - Wall to the left or right, deadlock");
                return true;
            }
            
            // If there is a box_trap (simple deadlock check) to the left and right
            if (Board.is(board.cells[box.row][box.column+1], Board.BOX_TRAP) && Board.is(board.cells[box.row][box.column-1], Board.BOX_TRAP)) {
//                System.out.println("    - Box trap to the left and right, deadlock");
                return true;
            }
            
            if (Board.is(board.cells[box.row][box.column+1], Board.BOX)) {
//                System.out.println("    - Box to the right, check it");
                Position tempPos = board.positions[box.row][box.column+1];
                if (!visited.contains(tempPos)) {
                    return freezeDeadlock(tempPos, DEADLOCK_VERTICAL, visited);
                }
                //return freezeDeadlock(new Position(box.row, box.column+1), DEADLOCK_VERTICAL, visited); // TODO not updated with new positions
            }
            
            if (Board.is(board.cells[box.row][box.column-1], Board.BOX)) {
//                System.out.println("    - Box to the left, check it");
                Position tempPos = board.positions[box.row][box.column-1];
                if (!visited.contains(tempPos)) {
                    return freezeDeadlock(tempPos, DEADLOCK_VERTICAL, visited);
                }
                //return freezeDeadlock(new Position(box.row, box.column-1), DEADLOCK_VERTICAL, visited); // TODO not updated with new positions
            }
//            System.out.println("   - No deadlock"); 
            return false;
        // VERTICAL CHECK
        } else if (type == DEADLOCK_VERTICAL) {
//            System.out.println("   - Check vertical");

            // Check goal
            if (Board.is(board.cells[box.row][box.column], (byte) (Board.GOAL & Board.BOX))) {
//                System.out.println("    - Box in goal, no deadlock");
                return false;
            }

            // If there is a wall to the top or bottom
            if (Board.is(board.cells[box.row+1][box.column], Board.WALL) || Board.is(board.cells[box.row-1][box.column], Board.WALL)) {
//                System.out.println("    - Wall above or below, deadlock");
                return true;
            }
            
            // If there is a box_trap (simple deadlock check) to the top and bottom
            if (Board.is(board.cells[box.row+1][box.column], Board.BOX_TRAP) && Board.is(board.cells[box.row-1][box.column], Board.BOX_TRAP)) {
//                System.out.println("    - Box above or below, deadlock");
                return true;
            }
            
            if (Board.is(board.cells[box.row+1][box.column], Board.BOX)) {
//                System.out.println("    - Box below, check it");
                Position tempPos = board.positions[box.row+1][box.column];
                if (!visited.contains(tempPos)) {
                    return freezeDeadlock(tempPos, DEADLOCK_HORIZONTAL, visited);
                }
                //return freezeDeadlock(new Position(box.row+1, box.column), DEADLOCK_HORIZONTAL, visited); // TODO not updated with new positions
            }
            
            if (Board.is(board.cells[box.row-1][box.column], Board.BOX)) {
//                System.out.println("    - Box above, check it");
                Position tempPos = board.positions[box.row-1][box.column];
                if (!visited.contains(tempPos)) {
                    return freezeDeadlock(tempPos, DEADLOCK_HORIZONTAL, visited);
                }
                //return freezeDeadlock(new Position(box.row-1, box.column), DEADLOCK_HORIZONTAL, visited); // TODO not updated with new positions
            }
//            System.out.println("   - No deadlock"); 
            return false;
        }
//        System.out.println("No deadlock");
        return false;
    }
}
