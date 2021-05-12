import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.StringTokenizer;

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
            System.out.println("What's your name?");
            String name = commandLineInput.readLine();
            System.out.println("Singleplayer/Multiplayer? (true/false)");
            String mode = commandLineInput.readLine();
            out.println(name + " " + mode);

            System.out.println("Waiting for opponent to finish registration...");
            String opponentName = in.readLine();
            System.out.println("Your opponent is " + opponentName);

            System.out.println("Position your ships onto the board!");
            /// TODO - implement board placement mode
            out.println(Arrays.deepToString(new int[10][10]));

            System.out.println("Waiting for opponent to finish placing their ships on the board...");
            System.out.println(in.readLine()); // Starting game...

            int[][] board = new int[10][10];
            int[][] opponentBoard = new int[10][10];
            boolean running = true;

            while (running) {
                String serverMsg = in.readLine();
                var tokens = serverMsg.split("_", 2);
                String command = tokens[0];
                String argument = tokens[1];

                switch (command) {
                    case "MOVE":
                        // move -> choose a new place to move into
                        System.out.println(argument);
                        String move = commandLineInput.readLine();
                        opponentBoard[move.charAt(1) - '0'][move.charAt(0) - 'A'] = 1;
                        out.println(move);
                        break;
                    case "UPDATE":
                        //update -> update your board with the specified position (it was not your turn last)
                        board[argument.charAt(1) - '0'][argument.charAt(0) - 'A'] = 1;
                        break;
                    case "HIT":
                        // hit -> update your board with the specified position (it was your turn last)
                        System.out.println("You hit!");
                        opponentBoard[argument.charAt(1) - '0'][argument.charAt(0) - 'A'] = 2;
                        break;
                    case "MISS":
                        // miss -> update your board with the specified position (it was your turn last)
                        System.out.println("You missed...");
                        break;
                    case "STOP":
                        // stop -> display a custom message depending on whether you won or lost & send YES/NO back to the server
                        if (argument.equals(name)) {
                            System.out.println("Congrats, you won! Care for a rematch? (YES/NO)");
                        } else if (argument.equals(opponentName)) {
                            System.out.println("Sorry, you lost! Care for a rematch? (YES/NO)");
                        }
                        out.println(commandLineInput.readLine());
                        break;
                    case "REMATCH":
                        // rematch -> a new match will begin
                        System.out.println("Prepare for a new match to begin shortly...");
                        break;
                    case "END":
                        // end -> the game will end; display the message from the server
                        System.out.println(argument);
                        running = false;
                        break;
                    default:
                        // not good if we arrived here - no command matches our switch case
                        // TODO: implement custom exception or sth
                        break;
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("No server listening... " + e);
        }
    }
}
