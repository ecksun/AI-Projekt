package sokoban.gui;

import java.awt.BorderLayout;
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

        for (int x = 0; x < board.getWidth(); ++x) {
            for (int y = 0; y < board.getHeight(); ++y) {
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
        board = new Board(12, 12, 1, 1);
        keepRunning = true;

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
                //boardPanel.getComponentAt(x, y).update(board.cells[x][y]);
            }
        }
    }



    public static void main(String[] args)
    {
        new GUI();
    }

}
