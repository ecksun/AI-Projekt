/**
 *
 */
public class Board
{
    byte cells[][];
    int playerX;
    int playerY;
    int remainingBoxes;
    Board(int x, int y, int playerX, int playerY) {
        cells = new byte[x][y];
        this.playerX = playerX;
        this.playerY = playerY;
    }
}
