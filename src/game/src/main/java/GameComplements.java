import com.googlecode.lanterna.graphics.TextGraphics;

public abstract class GameComplements {

    protected Position position;

    public GameComplements(int x, int y) {
        this.position = new Position(x, y);
    }

    public int getX() {
        return (int) position.getX();
    }

    public int getY() {
        return (int) position.getY();
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public abstract void draw(TextGraphics graphics, int StartOfGameScreenX, int StartOfGameScreenY);
}

