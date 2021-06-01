package Utils;

import GameThreads.MultiGameThread;
import Players.HumanPlayer;

public class MultiplayerManager {

    private static MultiplayerManager manager = null;
    private boolean gameWaitingSecondPlayer = false;
    private HumanPlayer playerWaiting;

    private MultiplayerManager() {

    }

    // static method to create instance of Singleton class
    public static MultiplayerManager getInstance()
    {
        if (manager == null)
            manager = new MultiplayerManager();
        return manager;
    }

    public void addPlayer(HumanPlayer playerReceived) {

        if (!gameWaitingSecondPlayer)
            {
                playerWaiting = playerReceived;
                gameWaitingSecondPlayer = true;
            }
            else
            {
                new MultiGameThread(playerWaiting, playerReceived).start();
                gameWaitingSecondPlayer = false;
            }
    }
}
