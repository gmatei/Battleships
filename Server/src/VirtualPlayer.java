import java.util.Arrays;

public class VirtualPlayer {

    private String name = "Battle BOT";
    private int[][] board = new int[10][10];
    private int shipsNr = 5;

    public String getName() {
        return name;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard() {

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
        return null;
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
