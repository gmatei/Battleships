package shapes;

import java.awt.*;

public class ContentRectangle extends Rectangle {

    public enum CellType {
        BLANK,
        LETTER,
        DIGIT,
        CELL
    }
    private CellType type;
    private int content;

    public ContentRectangle(int x, int y, int width, int height, CellType type, int content) {
        super(x, y, width, height);
        this.type = type;
        this.content = content;
    }

    public CellType getType() {
        return type;
    }

    public int getContent() {
        return content;
    }

    public void setContent(int content) {
        this.content = content;
    }
}
