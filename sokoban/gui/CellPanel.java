package sokoban.gui;
import java.awt.Color;

import javax.swing.JPanel;

import sokoban.Board;

public class CellPanel extends JPanel
{

    private static final long serialVersionUID = 613987193281337337L;

    public CellPanel(byte boardCell)
    {
        update(boardCell);
    }

    /**
     * Updates this cell panel according to the specified board cell.
     * 
     * @param boardCell The new board cell.
     */
    public void update(byte boardCell) {
        this.setBackground(getCellColor(boardCell));
    }
    
    /**
     * Returns the color that should represent the specified cell.
     * 
     * @param cell
     *            A cell in the board.
     * @return A color.
     */
    private Color getCellColor(byte cell)
    {
        switch (cell) {
            case Board.WALL:
                return new Color(20, 20, 20);
            case Board.GOAL:
                return new Color(20, 100, 20);
            case Board.BOX:
                return new Color(120, 90, 0);
            default:
                return new Color(0, 0, 0);
        }
    }

}
