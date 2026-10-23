import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;

public class Wall extends GameComplements {

    private String s = null;

    Wall(int x, int y, String s){
        super(x, y);
        this.s = s;
    }

    @Override
    public void draw(TextGraphics graphics, int StartOfGameScreenX, int StartOfGameScreenY) {
        graphics.setForegroundColor(TextColor.Factory.fromString("#000000")); // preto
        graphics.setBackgroundColor(TextColor.Factory.fromString("#000000")); // fundo preto também
        graphics.enableModifiers(SGR.BOLD);
        graphics.putString(new TerminalPosition(StartOfGameScreenX + (int) position.getX(), StartOfGameScreenY + (int) position.getY()), s);
    }
}
