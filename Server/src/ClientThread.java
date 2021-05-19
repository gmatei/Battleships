import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientThread extends Thread {
    private final Socket socket;
    private final MultiplayerManager manager = MultiplayerManager.getInstance();

    public ClientThread(Socket socket) {
        this.socket = socket;
    }

    public void run()
    {

        try
        {
            // Get the request from the input stream: client → server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //PrintWriter out = new PrintWriter(socket.getOutputStream());

            String clientMsg = in.readLine();   //read name and playing mode separated by '#'
            var tokens = clientMsg.split("#", 2);

            String name = tokens[0];
            String mode = tokens[1];
            HumanPlayer player = new HumanPlayer(socket, name, mode);

            if (mode.equals("true"))
            {
                //start single player
                new SingleGameThread(player).start();
            }
            else
            {
                //start multi player
                manager.addPlayer(player); //add player to the waiting list
            }


        }
        catch (IOException e)
        {
            System.err.println("Communication error... " + e);
        }
    }

}
