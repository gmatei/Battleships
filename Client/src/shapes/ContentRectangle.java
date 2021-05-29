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

    /**
     * Constructs a new {@code Rectangle} whose upper-left corner is
     * specified as
     * {@code (x,y)} and whose width and height
     * are specified by the arguments of the same name.
     *
     * @param x      the specified X coordinate
     * @param y      the specified Y coordinate
     * @param width  the width of the {@code Rectangle}
     * @param height the height of the {@code Rectangle}
     * @since 1.0
     */
    public ContentRectangle(int x, int y, int width, int height, CellType type, int content) {
        super(x, y, width, height);
        this.type = type;
        this.content = content;
    }

    public CellType getType() {
        return type;
    }

    public void setType(CellType type) {
        this.type = type;
    }

    public int getContent() {
        return content;
    }

    public void setContent(int content) {
        this.content = content;
    }
}
