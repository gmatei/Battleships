import java.net.Socket;

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
}
