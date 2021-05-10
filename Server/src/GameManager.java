import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameManager {

    private static GameManager manager = null;
    private List<Game> gameList = new ArrayList<Game>();
    private boolean gameWaitingSecondPlayer = false;

    private GameManager() {

    }

    // static method to create instance of Singleton class
    public static GameManager getInstance()
    {
        if (manager == null)
            manager = new GameManager();
        return manager;
    }

    public List<Game> getGameList() {
        return gameList;
    }

    public void setGameList(List<Game> gameList) {
        this.gameList = gameList;
    }

    public void addPlayer(Socket player) {

        if (!gameWaitingSecondPlayer)
            {
                gameList.add(new Game(player, null));
                gameWaitingSecondPlayer = true;
            }
            else
            {
                gameList.get(gameList.size() - 1).setPlayer2(player);
                gameWaitingSecondPlayer = false;
            }
    }
}
