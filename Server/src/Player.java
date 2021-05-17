import java.net.Socket;
import java.util.Arrays;

public class Player {

    private Socket socket;
    private String name;
    private String mode;
    private int[][] board = new int[10][10];
    private int shipsNr = 5;

    public Player(Socket socket, String name, String mode) {
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
        int line = move.charAt(0) - 'A';
        int col = move.charAt(1) - '0';
        if(board[line][col] == 1)
        {
            if(shipDestroyed())
                shipsNr--;
            board[line][col] = 0;
            return true;
        }
        return false;
    }

    private boolean shipDestroyed() {
        return true;
    }
}
