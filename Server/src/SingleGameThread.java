import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class SingleGameThread extends Thread{

    private Player player;
    private VirtualPlayer botPlayer;

    public SingleGameThread(Player player) {
        this.player = player;
    }

    public void run()
    {

        try
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(player.getSocket().getInputStream()));
            PrintWriter out = new PrintWriter(player.getSocket().getOutputStream());

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
                        String move = botPlayer.makeMove();
                        boolean boatHit = player.recordOpponentMove(move);

                        out.println("UPDATE_" + move);
                        if (!boatHit) {
                            player1ToMove = true;
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

    private int[][] stringToMatrixBoard(String clientBoard) { //fu

        int[][] board = new int[10][10];
        int col = 0;
        int line = 0;

        for (int i = 0; i < clientBoard.length(); i++) {
            if (clientBoard.charAt(i) >= '0' && clientBoard.charAt(i) <= '9') {
                board[line][col] = clientBoard.charAt(i) - '0';
                col++;

                if(col == 9)
                {
                    col = 0;
                    line++;
                }
            }
        }

        return board;
    }

}
