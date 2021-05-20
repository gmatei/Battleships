import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            new SimpleClient().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
