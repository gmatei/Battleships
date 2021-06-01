package manager;
import frames.*;

public class WindowManager {

    private Menu menu;
    private Welcome welcome;
    public ShipPlacement shipPlacement;
    public HowToPlay howToPlay;
    private Game game;

    private String name;

    public WindowManager() {
        welcome = new Welcome(this);
        welcome.setVisible(true);
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setGameVisibility(boolean visible) {
        game.setVisible(visible);
    }

    public void setShipPlacement(ShipPlacement placement) {
        this.shipPlacement = placement;
    }

    public void setShipPlacementVisibility(boolean visible) {
        shipPlacement.setVisible(visible);
    }

    public void setHowToPlay(HowToPlay howToPlay) {
        this.howToPlay = howToPlay;
    }

    public void setHowToPlayVisibility(boolean visible) {
        howToPlay.setVisible(visible);
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public void setMenuVisibility(boolean visible) {
        menu.setVisible(visible);
    }

    public void setWelcomeVisibility(boolean visible) {
        welcome.setVisible(visible);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
