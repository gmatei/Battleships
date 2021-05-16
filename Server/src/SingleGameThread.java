import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class SingleGameThread extends Thread{

    Player player;
    VirtualPlayer botPlayer;

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
                String clientBoard = in.readLine();   //read board received as String created in client by using deepToString method
                player.setBoard(stringToMatrixBoard(clientBoard)); //set board as int matrix

                botPlayer.setBoard();   //the AI places its ships on the board

                out.println("Starting game...");

                boolean player1ToMove = true;
                boolean gameOver = false;
                while(!gameOver)
                {
                    if (player1ToMove)
                    {
                        out.println("MOVE_Make your move!")     //Server -> Client1 : "MOVE_Make your move"
                        String move = in.readLine();            //Client1 -> Server : <<LetterDigit>>
                    }

                    if (VirtualPlayer)

//                    if (boat_hit)
//                    {Server -> Client2 : "UPDATE_<<LetterDigit>>"
//                        Server -> Client1 : "HIT_<<LetterDigit>>"
//                    }
//                    else
//                    {
//                        Server -> Client2 : "UPDATE_<<LetterDigit>>"
//                        Server -> Client1 : "MISS_<<LetterDigit>>"
//                        changeCurrentPlayer();
//                    }
//                    gameOver = isGameOver();
                }

//                Server -> ClientWinner : "STOP_Winner"
//                Server -> ClientLoser  : "STOP_Loser"
//
//                Client1 & Client2 -> Server : "YES / NO"
//
//                if(bothYes)
//                    Server -> Client1 & Client2 : "Starting new game"
//	                goto LogicaMutari
//	            else
//                Server -> Client1 & Client2 : "Ending game"

            }

        }
        catch (IOException e)
        {
            System.err.println("Communication error... " + e);
        }
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
