package sokoban.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import sokoban.Board;

public class GUI
{

    JFrame frame;
    JPanel boardPanel;
    Board board;
    boolean keepRunning;

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Create the GUI and show it. For thread safety, this method should be
     * invoked from the event-dispatching thread.
     */
    private void createAndShowGUI()
    {
        frame = new JFrame("Sokoban solver");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        boardPanel = new JPanel(new GridLayout(board.getWidth(), board
                .getHeight(), 1, 1));

        for (int i = 0; i < board.getWidth() * board.getHeight(); ++i) {
            JPanel cell = new JPanel();
            cell.setBackground(new Color(100, 200, 120));
            boardPanel.add(cell);
        }

        frame.getContentPane().add(boardPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

    public GUI()
    {
        board = new Board(12, 12, 1, 1);
        keepRunning = true;

        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run()
            {
                createAndShowGUI();
            }
        });

        while (keepRunning) {

            updateBoardPanel(board);

            try {
                Thread.sleep(500);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateBoardPanel(Board board)
    {
        for (int x = 0; x < board.getWidth(); ++x) {
            for (int y = 0; y < board.getHeight(); ++y) {
                System.out.println(x + " " + y);
                boardPanel.getComponentAt(x, y).setBackground(
                        getCellColor(board.cells[x][y]));
            }
        }
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

    public static void main(String[] args)
    {
        new GUI();
    }

}
