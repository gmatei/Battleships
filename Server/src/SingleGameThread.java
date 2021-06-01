import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;

public class SingleGameThread extends Thread{

    private final HumanPlayer player;
    private final VirtualPlayer botPlayer;

    public SingleGameThread(HumanPlayer player) {
        this.player = player;
        this.botPlayer = new VirtualPlayer();
    }

    public void run()
    {
        try
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(player.getSocket().getInputStream()));
            PrintWriter out = new PrintWriter(player.getSocket().getOutputStream(), true);

            out.println(botPlayer.getName()); //send bot name to player

            boolean running = true;
            while (player.getSocket().isConnected() && running)
            {
                player.newGameReset();
                botPlayer.newGameReset();
                String clientBoard = in.readLine();   //read board received as String created in client by using deepToString method
                player.setBoard(stringToMatrixBoard(clientBoard)); //set board as int matrix

                botPlayer.setBoard();   //the AI places its ships on the board

                out.println("Starting game...");

                boolean player1ToMove = true;
                boolean gameOver = false;
                boolean previouslyHitPlayer = false;
                while(!gameOver)
                {
                    if (player1ToMove) // human player to move
                    {
                        out.println("MOVE_Make your move!");    //Server -> Client1 : "MOVE_Make your move"
                        String move = in.readLine();            //Client1 -> Server : <<LetterDigit>>
                        boolean boatHit = botPlayer.recordOpponentMove(move);

                        if (boatHit)
                        {
                            out.println("HIT_" + move);
                        }
                        else
                        {
                            out.println("MISS_" + move);
                            player1ToMove = false;
                        }
                    }
                    else    // virtual player to move
                    {
                        String move;
                        boolean triedAgain = false;
                        do
                        {
                            move = botPlayer.makeMove(previouslyHitPlayer, triedAgain);
                            triedAgain = true;
                        }
                        while (!moveValid(move));

                        boolean boatHit = player.recordOpponentMove(move);

                        out.println("UPDATE_" + move);
                        if (!boatHit) {
                            player1ToMove = true;
                            previouslyHitPlayer = false;
                        }
                        else {
                            previouslyHitPlayer = true;
                        }
                    }
                    gameOver = isGameOver();
                }

                if(player1ToMove)
                    out.println("STOP_Winner");   // human player won
                else
                    out.println("STOP_Loser");    // human player lost

                String playAgainResponse = in.readLine();

                if(playAgainResponse.equals("YES"))
                {
                    out.println("REMATCH_Play again");
                }
                else
                {
                    out.println("END_Stop game");
                    running = false;
                }
            }
            player.getSocket().close();
        }
        catch (IOException e)
        {
            System.err.println("Communication error... " + e);
        }
    }

    private boolean isGameOver()
    {
        return botPlayer.getShipsNr() == 0 || player.getShipsNr() == 0;
    }

    private int[][] stringToMatrixBoard(String clientBoard) {

        int[][] board = new int[10][10];
        int col = 0;
        int line = 0;

        for (int i = 0; i < clientBoard.length(); i++) {
            if (clientBoard.charAt(i) >= '0' && clientBoard.charAt(i) <= '9') {
                board[line][col] = clientBoard.charAt(i) - '0';
                col++;

                if(col == 10)
                {
                    col = 0;
                    line++;
                }
            }
        }
        return board;
    }

    public boolean moveValid(String move)
    {
        if(move.charAt(1) < '0' || move.charAt(1) > '9' || move.charAt(0) < 'A' || move.charAt(0) > 'J')
            return false;

        int line = move.charAt(1) - '0';
        int col = move.charAt(0) - 'A';

        if (botPlayer.getHitMode().equals("random"))
        {
            if (line < 9)
                if (player.getBoard()[line + 1][col] == 2)
                    return false;

            if (line > 0)
                if (player.getBoard()[line - 1][col] == 2)
                    return false;

            if (col < 9)
                if (player.getBoard()[line][col + 1] == 2)
                    return false;

            if (col > 0)
                if (player.getBoard()[line][col - 1] == 2)
                    return false;

        int linSpace = 0;
        int colSpace = 0;

        for (int i = line; player.getBoard()[i][col] == 0 || player.getBoard()[i][col] == 1; i++, linSpace++){ if (i == 9) break; }
        for (int i = line; player.getBoard()[i][col] == 0 || player.getBoard()[i][col] == 1; i--, linSpace++){ if (i == 0) break; }

        for (int j = col; player.getBoard()[line][j] == 0 || player.getBoard()[line][j] == 1; j++, colSpace++){ if (j == 9) break; }
        for (int j = col; player.getBoard()[line][j] == 0 || player.getBoard()[line][j] == 1; j--, colSpace++){ if (j == 0) break; }

        if (linSpace < Arrays.stream(botPlayer.getBoatSizes()).min().getAsInt() || colSpace < Arrays.stream(botPlayer.getBoatSizes()).min().getAsInt())
            return false;

        }

        return player.getBoard()[line][col] == 0 || player.getBoard()[line][col] == 1;
    }

}
