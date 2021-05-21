import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class MultiGameThread extends Thread{

    HumanPlayer player1;
    HumanPlayer player2;

    public MultiGameThread(HumanPlayer player1, HumanPlayer player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public void run()
    {
        try
        {
            BufferedReader in1 = new BufferedReader(new InputStreamReader(player1.getSocket().getInputStream()));
            PrintWriter out1 = new PrintWriter(player1.getSocket().getOutputStream(), true);
            BufferedReader in2 = new BufferedReader(new InputStreamReader(player2.getSocket().getInputStream()));
            PrintWriter out2 = new PrintWriter(player2.getSocket().getOutputStream(), true);


            out1.println(player2.getName()); //send bot name to player
            out2.println(player1.getName());

            boolean running = true;
            while (player1.getSocket().isConnected() && player2.getSocket().isConnected() && running)
            {
                player1.newGameReset();
                player2.newGameReset();

                String clientBoard1 = in1.readLine();   //read board received as String created in client by using deepToString method
                player1.setBoard(stringToMatrixBoard(clientBoard1)); //set board as int matrix

                String clientBoard2 = in2.readLine();   //read board received as String created in client by using deepToString method
                player2.setBoard(stringToMatrixBoard(clientBoard2)); //set board as int matrix

                out1.println("Starting game...");
                out2.println("Starting game...");

                boolean player1ToMove = true;
                boolean gameOver = false;
                while(!gameOver)
                {
                    if (player1ToMove)
                    {
                        out1.println("MOVE_Make your move!");    //Server -> Client1 : "MOVE_Make your move"
                        String move = in1.readLine();            //Client1 -> Server : <<LetterDigit>>
                        boolean boatHit = player2.recordOpponentMove(move);

                        out2.println("UPDATE_" + move);
                        if (boatHit)
                        {
                            out1.println("HIT_" + move);
                        }
                        else
                        {
                            out1.println("MISS_" + move);
                            player1ToMove = false;
                        }
                    }
                    else
                    {
                        out2.println("MOVE_Make your move!");    //Server -> Client2 : "MOVE_Make your move"
                        String move = in2.readLine();            //Client2 -> Server : <<LetterDigit>>
                        boolean boatHit = player1.recordOpponentMove(move);

                        out1.println("UPDATE_" + move);
                        if (boatHit)
                        {
                            out2.println("HIT_" + move);
                        }
                        else
                        {
                            out2.println("MISS_" + move);
                            player1ToMove = true;
                        }
                    }
                    gameOver = isGameOver();
                }

                if(player1ToMove)
                    {
                        out1.println("STOP_Winner");
                        out2.println("STOP_Loser");
                    }
                else
                {
                    out2.println("STOP_Winner");
                    out1.println("STOP_Loser");
                }

                String playAgainResponse1 = in1.readLine();
                String playAgainResponse2 = in2.readLine();

                if(playAgainResponse1.equals("YES") && playAgainResponse2.equals("YES"))
                {
                    out1.println("REMATCH_Play again");
                    out2.println("REMATCH_Play again");
                }
                else
                {
                    out1.println("END_Stop game");
                    out2.println("END_Stop game");
                    running = false;
                }
            }
            player1.getSocket().close();
            player2.getSocket().close();
        }
        catch (IOException e)
        {
            System.err.println("Communication error... " + e);
        }
    }

    private boolean isGameOver()
    {
        return player1.getShipsNr() == 0 || player2.getShipsNr() == 0;
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

}
