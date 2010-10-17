package sokoban;

import java.util.List;

/**
 * Contains a position of a box and a path to it.
 */
public class ReachableBox
{
    /**
     * The position of the box
     */
    public Position position;

    /**
     * The path to take to the box
     */
    public List<Board.Direction> path;

    /**
     * Create a new reachablebox with the provided position and path
     * 
     * @param position The position
     * @param path The path
     */
    public ReachableBox(final Position position,
            final List<Board.Direction> path)
    {
        this.position = position;
        this.path = path;
    }
}
