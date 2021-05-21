import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class VirtualPlayer {

    private final int[][] board = new int[10][10];
    private int shipsNr = 5;

    public String getName() {
        return "Battle BOT";
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard() {
        Map<String, Integer> ships = new HashMap<>();
        ships.put("Carrier", 5);
        ships.put("Battleship", 4);
        ships.put("Cruiser", 3);
        ships.put("Submarine", 3);
        ships.put("Destroyer", 2);

        for (var ship : ships.entrySet())
        {
            boolean currentBoatPositioned = false;
            while (!currentBoatPositioned)
            {
                int line;
                int col;
                int positioning;
                do {
                    line = (int) (Math.random() * 10);
                    col = (int) (Math.random() * 10);
                    positioning = (int) (Math.random() * 10) % 2;
                } while (board[line][col] != 0);    //find an unoccupied spot

                int boatSize = ship.getValue();

                if (positioning == 1)   // position the boat vertically
                {
                    if (line + boatSize > 9)
                        continue;

                    currentBoatPositioned = true;

                    for (int i = line; boatSize > 0; boatSize--, i++)
                    {
                        if (i < 9)
                            if (board[i+1][col] == 1)    {currentBoatPositioned = false; break;}
                        if (i > 0)
                            if (board[i-1][col] == 1)    {currentBoatPositioned = false; break;}
                        if (col < 9)
                            if (board[i][col+1] == 1)    {currentBoatPositioned = false; break;}
                        if (col > 0)
                            if (board[i][col-1] == 1)    {currentBoatPositioned = false; break;}
                    }

                    if(currentBoatPositioned)
                        for (int i = line; i < line + ship.getValue(); i++)
                            board[i][col] = 1;
                }
                else                    // position the boat horizontally
                {
                    if (col + boatSize > 9)
                        continue;

                    currentBoatPositioned = true;

                    for (int j = col; boatSize > 0; boatSize--, j++)
                    {
                        if (line < 9)
                            if (board[line+1][j] == 1)    {currentBoatPositioned = false; break;}
                        if (line > 0)
                            if (board[line-1][j] == 1)    {currentBoatPositioned = false; break;}
                        if (j < 9)
                            if (board[line][j+1] == 1)    {currentBoatPositioned = false; break;}
                        if (j > 0)
                            if (board[line][j-1] == 1)    {currentBoatPositioned = false; break;}
                    }

                    if(currentBoatPositioned)
                        for (int j = col; j < col + ship.getValue(); j++)
                            board[line][j] = 1;
                }
            }
        }
    }

    public int getShipsNr() {
        return shipsNr;
    }

    public void setShipsNr(int shipsNr) {
        this.shipsNr = shipsNr;
    }

    public void newGameReset() {
        shipsNr = 5;
        Arrays.stream(board).forEach(a -> Arrays.fill(a, 0));
    }

    public String makeMove() {

        char line;
        char col;

        col = (char) ((int) (Math.random() * 10) + 'A');
        line = (char) ((int) (Math.random() * 10) + '0');

        return "" + col + line;
    }

    public boolean recordOpponentMove(String move) {
        int col = move.charAt(0) - 'A';
        int line = move.charAt(1) - '0';
        if(board[line][col] == 1)
        {
            board[line][col] = 2;
            if(shipDestroyed(line, col))
                shipsNr--;
            return true;
        }
        board[line][col] = 3;
        return false;
    }

    private boolean shipDestroyed(int line, int col) {
        for (int i = line; i <= 9; i++)
        {
            if (board[i][col] == 1)
                return false;
            if (board[i][col] == 0 || board[i][col] == 3)
                break;
        }

        for (int i = line; i >= 0; i--)
        {
            if (board[i][col] == 1)
                return false;
            if (board[i][col] == 0 || board[i][col] == 3)
                break;
        }

        for (int j = col; j <= 9; j++)
        {
            if (board[line][j] == 1)
                return false;
            if (board[line][j] == 0 || board[line][j] == 3)
                break;
        }

        for (int j = col; j >= 0; j--)
        {
            if (board[line][j] == 1)
                return false;
            if (board[line][j] == 0 || board[line][j] == 3)
                break;
        }

        return true;

    }

}
