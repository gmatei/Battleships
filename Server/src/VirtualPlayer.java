import java.util.*;
import java.util.stream.Collectors;

public class VirtualPlayer {

    private final int[][] board = new int[10][10];
    private int shipsNr = 5;
    private String prevHitPosition;
    private String currentTry;
    private String firstHit;
    private int targetDirection;
    private String hitMode;
    private int hitCounter;
    private int[] boatSizes = new int[]{2, 3, 3, 4, 5};

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
        prevHitPosition = "none";
        firstHit = "none";
        hitMode = "random";
        targetDirection = 1; // 1 - up, 2 - down, 3 - left, 4 - right
        hitCounter = 0;
    }

    public String makeMove(boolean boatHit, boolean triedAgain) {

        char line;
        char col;
        String move;
        boolean changedDirection = false;

        if (boatHit)
            {
                prevHitPosition = currentTry;
                if(hitCounter == 0)
                    firstHit = prevHitPosition;
                hitMode = "targeted";
                hitCounter++;
            }
        if (triedAgain && hitMode.equals("targeted"))
            {
                targetDirection++;
                changedDirection = true;
            }

        if(hitCounter >= Arrays.stream(boatSizes).max().getAsInt() || targetDirection == 5 || (hitCounter > 1 && targetDirection == 3))
            {
                hitMode = "random";
                hitCounter = 0;
                targetDirection = 1;
                prevHitPosition = "none";
                firstHit = "none";
            }

        if(hitMode.equals("random"))
        {
            col = (char) ((int) (Math.random() * 10) + 'A');
            line = (char) ((int) (Math.random() * 10) + '0');
            move = col + line + "";
        }
        else
        {
            if(changedDirection)
                {
                    move = getDirectedMove(targetDirection, firstHit);
                }
            move = getDirectedMove(targetDirection, prevHitPosition);
        }

        currentTry = move;
        return move;
    }

    public String getDirectedMove(int targetDirection, String prevHitPosition)
    {
        char line = '0';
        char col = 'A';
        switch (targetDirection)
        {
            case 1 -> {
                line = (char) (prevHitPosition.charAt(1) - 1);
                col = prevHitPosition.charAt(0);
            }
            case 2 -> {
                line = (char) (prevHitPosition.charAt(1) + 1);
                col = prevHitPosition.charAt(0);
            }
            case 3 -> {
                line = (char) (prevHitPosition.charAt(1));
                col = (char) (prevHitPosition.charAt(0) - 1);
            }
            case 4 -> {
                line = (char) (prevHitPosition.charAt(1));
                col = (char) (prevHitPosition.charAt(0) + 1);
            }
        }

        return "" + col + line;
    }

    public boolean recordOpponentMove(String move) {
        int col = move.charAt(0) - 'A';
        int line = move.charAt(1) - '0';
        if(board[line][col] == 1)
        {
            board[line][col] = 2;
            int shipDestroyedSize = shipDestroyed(line, col);
            if(shipDestroyedSize > 0)
                {
                    shipsNr--;
                    for (int i = 0; i < boatSizes.length; i++)
                    {
                        if (boatSizes[i] == shipDestroyedSize)
                            {
                            boatSizes[i] = 0;
                            break;
                            }
                    }
                }
            return true;
        }
        board[line][col] = 3;
        return false;
    }

    private int shipDestroyed(int line, int col) {

        int boatSize = 0;

        for (int i = line; i <= 9; i++)
        {
            if (board[i][col] == 1)
                return 0;
            if (board[i][col] == 0 || board[i][col] == 3)
                break;
            boatSize++;
        }

        for (int i = line; i >= 0; i--)
        {
            if (board[i][col] == 1)
                return 0;
            if (board[i][col] == 0 || board[i][col] == 3)
                break;
            boatSize++;
        }

        for (int j = col; j <= 9; j++)
        {
            if (board[line][j] == 1)
                return 0;
            if (board[line][j] == 0 || board[line][j] == 3)
                break;
            boatSize++;
        }

        for (int j = col; j >= 0; j--)
        {
            if (board[line][j] == 1)
                return 0;
            if (board[line][j] == 0 || board[line][j] == 3)
                break;
            boatSize++;
        }

        return boatSize;

    }

}
