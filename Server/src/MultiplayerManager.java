public class MultiplayerManager {

    private static MultiplayerManager manager = null;
    private boolean gameWaitingSecondPlayer = false;
    private Player playerWaiting;

    private MultiplayerManager() {

    }

    // static method to create instance of Singleton class
    public static MultiplayerManager getInstance()
    {
        if (manager == null)
            manager = new MultiplayerManager();
        return manager;
    }

    public void addPlayer(Player playerReceived) {

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