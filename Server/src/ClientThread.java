import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {
    private Socket socket = null;
    private GameManager manager = GameManager.getInstance();

    public ClientThread(Socket socket) {
        this.socket = socket;
        manager.addPlayer(socket);
    }

    public void run() {
        try {
            // Get the request from the input stream: client → server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            boolean running = true;

            while (socket.isConnected() && running) {
                String request = in.readLine();

                if (request.equals("stop")) {
                    out.println("The server has stopped.");
                    running = false;
                }
                else {
                    out.println("The server has received the command " + request);
                }
                out.flush();
            }

            socket.close();

        } catch (IOException e) {
            System.err.println("Communication error... " + e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }
}
