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
}
