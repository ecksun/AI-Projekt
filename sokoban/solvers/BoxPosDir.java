package sokoban.solvers;

import sokoban.Position;
import sokoban.Board.Direction;

/**
 * BoxPosDir is used as the value part in a hash map and if mapped by the hash
 * of a board state. It contains the necessary information in order to backtrack
 * a solution by repeatedly generating the previous/next state and note the
 * player move directions.
 */
public class BoxPosDir
{
    final Direction dir;
    final Position box;
    final Position player;

    /**
     * Create a new BoxPosDir
     * 
     * @param dir The directoin
     * @param box The box
     * @param player The player
     */
    public BoxPosDir(final Direction dir, final Position box,
            final Position player)
    {
        this.dir = dir;
        this.box = box;
        this.player = player;
    }
}
