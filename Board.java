/**
 *
 */
public class Board
{
    // Input values 
    final byte WALL = 0x01;
    final byte BOX =  0x02;
    final byte GOAL = 0x04;
    // Generated values
    final byte NO_BOX = 0x08; 
    // Bitmasks 
    final byte OCCUPIED = 0x03;
    
    byte cells[][];
    int  playerX;
    int  playerY;
    int  remainingBoxes;

    Board(int x, int y, int playerX, int playerY)
    {
        cells = new byte[x][y];
        this.playerX = playerX;
        this.playerY = playerY;
    }
}
