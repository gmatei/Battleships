package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class SimpleServer {
    public static final int PORT = 8100;

    public SimpleServer() throws IOException {


        try (ServerSocket serverSocket = new ServerSocket(PORT))
        {
            while (true)
            {
                System.out.println("Waiting for client ...");
                Socket socket = serverSocket.accept();
                System.out.println("Client accepted!");

                // Execute the client's request in a new thread
                new ClientThread(socket).start();
            }
        }
        catch (IOException e)
        {
            System.err.println("Oops... " + e);
        }
    }


}
