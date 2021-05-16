import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
            // -----------------------------------------------------------------------------------------
            // -------------------------------- CONNECTION TO SERVER -----------------------------------
            // -----------------------------------------------------------------------------------------
            System.out.println("What's your name?");
            String name = commandLineInput.readLine();
            System.out.println("Singleplayer/Multiplayer? (true/false)");
            String mode = commandLineInput.readLine();
            out.println(name + "#" + mode);

            System.out.println("Waiting for opponent to finish registration...");
            String opponentName = in.readLine();
            System.out.println("Your opponent is " + opponentName);

            // -----------------------------------------------------------------------------------------
            // ----------------------------------- SHIP PLACEMENT --------------------------------------
            // -----------------------------------------------------------------------------------------
            System.out.println("Position your ships onto the board! Format: <LetterDigit> <Orientation>");
            /// TODO - implement board placement mode
            Map<String, Integer> ships = new HashMap<>();
            ships.put("Carrier", 5);
            ships.put("Battleship", 4);
            ships.put("Cruiser", 3);
            ships.put("Submarine", 3);
            ships.put("Destroyer", 2);
            int[][] board = new int[10][10];

            for (var ship : ships.entrySet()) {
                System.out.println("Place your " + ship.getKey() + "! (size " + ship.getValue() + ")");

                System.out.print("  "); // colt stanga sus tabla
                for (int i = 0; i < board.length; i++) System.out.print((char)('A' + i) + " "); // literele de pe coloane
                System.out.println();
                for (int i = 0; i < board.length; i++) {
                    System.out.print(i + " "); // cifrele de pe linii
                    for (int j = 0; j < board[i].length; j++) {
                        System.out.print(board[i][j] + " ");
                    }
                    System.out.println();
                }

                while (true) {
                    var placement = commandLineInput.readLine().split(" ", 2);
                    if (placement[0].charAt(0) < 'A' || placement[0].charAt(0) > 'Z' ||
                        placement[0].charAt(1) < '0' || placement[0].charAt(1) > '9' ||
                        !(placement[1].equals("V") || placement[1].equals("H"))) {
                        System.out.println("Placement format: <LetterDigit> <Orientation>");
                        continue;
                    }

                    int line = placement[0].charAt(1) - '0';
                    int col  = placement[0].charAt(0) - 'A';
                    if (col + ship.getValue() > 9) {
                        System.out.println("Invalid placement: out of bounds");
                        continue;
                    }

                    boolean ok = true;
                    for (int j = col; j < col + ship.getValue(); j++) {
                        if (board[line][j] != 0) {
                            ok = false;
                            break;
                        }
                    }

                    if (!ok) {
                        System.out.println("Invalid placement: overlapping ships");
                        continue;
                    }

                    for (int j = col; j < col + ship.getValue(); j++) {
                        board[line][j] = 1;
                    }
                    break;
                }
            }
            out.println(Arrays.deepToString(board));

            // -----------------------------------------------------------------------------------------
            // ------------------------------------- GAME START ----------------------------------------
            // -----------------------------------------------------------------------------------------
            System.out.println("Waiting for opponent to finish placing their ships on the board...");
            System.out.println(in.readLine()); // Starting game...

            int[][] opponentBoard = new int[10][10];
            boolean running = true;

            while (running) {
                String serverMsg = in.readLine();
                var tokens = serverMsg.split("_", 2);
                String command = tokens[0];
                String argument = tokens[1];

                switch (command) {
                    case "MOVE":
                        // move -> choose a new place to strike
                        System.out.println(argument);
                        String move;
                        while (true) {
                            move = commandLineInput.readLine();
                            if (move.charAt(0) < 'A' || move.charAt(0) > 'Z' ||
                                move.charAt(1) < '0' || move.charAt(1) > '9') {
                                System.out.println("Placement format: <LetterDigit>");
                                continue;
                            }

                            int line = move.charAt(1) - '0';
                            int col  = move.charAt(0) - 'A';
                            if (opponentBoard[line][col] != 0) {
                                System.out.println("Invalid placement: position already tried");
                                continue;
                            }

                            opponentBoard[line][col] = 1;
                            break;
                        }
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

                        String response;
                        while (true) {
                            response = commandLineInput.readLine();
                            if (!response.equals("YES") && !response.equals("NO")) {
                                System.out.println("Please input YES if you want to play again or NO if you don't.");
                                continue;
                            }
                            break;
                        }
                        out.println(response);
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
