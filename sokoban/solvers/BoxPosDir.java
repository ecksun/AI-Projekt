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

    public BoxPosDir(Direction dir, Position box, Position player) {
        this.dir = dir;
        this.box = box;
        this.player = player;
    }
}
