package manager;
import frames.*;

public class WindowManager {

    private Menu menu;
    private Welcome welcome;
    private Waiting waiting;
    private ShipPlacement shipPlacement;

    private String name;

    public WindowManager() {
        welcome = new Welcome(this);
        welcome.setVisible(true);
    }

    public void setShipPlacement(ShipPlacement placement) {
        this.shipPlacement = placement;
    }

    public void setShipPlacementVisibility(boolean visible) {
        shipPlacement.setVisible(visible);
    }

    public void setWaiting(Waiting waiting) {
        this.waiting = waiting;
    }

    public Waiting getWaiting() {
        return this.waiting;
    }

    public void setWaitingVisibility(boolean visible) {
        waiting.setVisible(visible);
    }

    public Menu getMenu() {
        return menu;
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
