package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

public class GameConnection {

    private static String name = "test1";
    private static String opponentName = "test2";
    private static Socket socket = null;
    private static PrintWriter out = null;
    private static BufferedReader in = null;
    private static int[][] board = new int[10][10];

    private GameConnection() { }

    public static void connect(String _name, String mode) throws IOException {
        if (socket == null) {
            int PORT = 8100;
            String serverAddress = "127.0.0.1";
            socket = new Socket(serverAddress, PORT);
            name = _name;

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println(name + "#" + mode); // send my name and gamemode
            opponentName = in.readLine(); // receive the opponent's name
        }
    }

    public static int[][] getBoard() {
        return board;
    }

    public static void sendBoard(int[][] _board) {
        board = _board;
        out.println(Arrays.deepToString(board));
    }

    public static void disconnect() {
        socket = null;
    }

    public static boolean isActive() {
        return socket != null;
    }

    public static String getName() {
        return name;
    }

    public static String getOpponentName() {
        return opponentName;
    }

    public static PrintWriter getOutputStream() {
        return out;
    }

    public static BufferedReader getInputStream() {
        return in;
    }
}
