package Players;

import java.net.Socket;
import java.util.Arrays;

public class HumanPlayer {

    private Socket socket;
    private String name;
    private String mode;
    private int[][] board = new int[10][10];
    private int shipsNr = 5;

    public HumanPlayer(Socket socket, String name, String mode) {
        this.socket = socket;
        this.name = name;
        this.mode = mode;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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