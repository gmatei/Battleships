import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SimpleClient {
    public static void main(String[] args) throws IOException {
        String serverAddress = "127.0.0.1";
        int PORT = 8100;
        try (
                Socket socket = new Socket(serverAddress, PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedReader commandLineInput = new BufferedReader(new InputStreamReader(System.in));
        ) {
            boolean running = true;

            while (socket.isConnected() && running) {
                String request = commandLineInput.readLine();
                out.println(request);

                String response = in.readLine();
                System.out.println(response);

                if (response.equals("The server has stopped.")) {
                    running = false;
                }

            }

        } catch (UnknownHostException e) {
            System.err.println("No server listening... " + e);
        }
    }
}
