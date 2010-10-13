package sokoban.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import sokoban.Board;

public class GUI
{

    JFrame                    frame;
    JPanel                    boardPanel;
    Board                     board;
    boolean                   keepRunning;

    private static final long serialVersionUID = 1L;

    /**
     * Create the GUI and show it. For thread safety, this method should be
     * invoked from the event-dispatching thread.
     */
    private void createAndShowGUI()
    {
        frame = new JFrame("Sokoban solver");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        boardPanel = new JPanel(new GridLayout(board.width, board
                .height, 1, 1));

        for (int x = 0; x < board.width; ++x) {
            for (int y = 0; y < board.height; ++y) {
                CellPanel cell = new CellPanel(board.cells[x][y]);
                boardPanel.add(cell);
            }
        }

        frame.getContentPane().add(boardPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

    public GUI()
    {
        board = new Board("#####" + "\n" + "#@  #" + "\n" + "# $ #" + "\n"
                + "#  .#" + "\n" + "#####");
        keepRunning = true;

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run()
            {
                createAndShowGUI();
                board.cells[10][10] = Board.GOAL;
                updateBoardPanel(board);
                frame.repaint();
            }
        });

        try {
            Thread.sleep(500);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void updateBoardPanel(Board board)
    {
        for (int x = 0; x < board.width; ++x) {
            for (int y = 0; y < board.height; ++y) {
                // boardPanel.getComponentAt(x, y).update(board.cells[x][y]);
            }
        }
    }

    public static void main(String[] args)
    {
        new GUI();
    }

}
