import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
            // Get the request from the input stream: client → server
            BufferedReader in = new BufferedReader(new InputStreamReader(player1.getSocket().getInputStream()));
            //PrintWriter out = new PrintWriter(socket.getOutputStream());


        }
        catch (IOException e)
        {
            System.err.println("Communication error... " + e);
        }
    }

}
