package sokoban;

import java.util.List;

/**
 * Contains a position of a box and a path to it.
 */
public class ReachableBox
{
    public Position position;
    public List<Board.Direction> path;
    
    public ReachableBox(Position position, List<Board.Direction> path)
    {
        this.position = position;
        this.path = path;
    }
}

