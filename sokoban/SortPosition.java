/**
 * 
 */
package sokoban;

import sokoban.Board.Direction;

/**
 *
 */
public class SortPosition extends Position implements Comparable
{
    public final int priority;
    
    /**
     * @param prio The priority of this position
     */
    public SortPosition(int row, int column, int prio)
    {
        super(row, column);
        priority = prio;
    }

    @Override
    public int compareTo(Object object)
    {
        return priority - ((SortPosition) object).priority;
    }
}
