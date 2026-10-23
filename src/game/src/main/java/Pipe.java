import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;

public class Pipe extends GameComplements implements Scoring{

    private int gapStart;
    private int gap;
    private final int width; //largura do pipe
    private final int arenaHeight; //altura da arena pa ter referencia no desenho dos pipes

    private boolean scored; //inic. false

    public Pipe(int x, int gapStart, int width, int gap, int arenaHeight) {
        super(x, gapStart);
        this.gapStart = gapStart;
        this.width = width;
        this.gap = gap;
        this.arenaHeight = arenaHeight;
        this.scored = false;
    }

    @Override
    public void draw(TextGraphics graphics, int StartOfGameScreenX, int StartOfGameScreenY) {
        graphics.setForegroundColor(TextColor.Factory.fromString("#008000")); //verde pipe
        graphics.setBackgroundColor(TextColor.Factory.fromString("#4FC3F7")); // AZUL em vez de preto!

        //pipe de cima_ (do topo até ao gap)
        for (int y = 1; y < gapStart; y++) {
            for (int c = 0; c < width; c++) {
                int Draw = StartOfGameScreenX + getX() + c;
                if (Draw >= StartOfGameScreenX) {
                    graphics.putString(new TerminalPosition(Draw, y + StartOfGameScreenY), "█");
                }
            }
        }

        // pipe de baixo (fim do gap inicio ate ao fim da height da arena)
        for (int y = gapStart + gap; y < arenaHeight - 1; y++) {
            for (int c = 0; c < width; c++) {
                int Draw = StartOfGameScreenX + getX() + c;
                if (Draw >= StartOfGameScreenX) {
                    graphics.putString(new TerminalPosition(Draw, y + StartOfGameScreenY), "█");
                }
            }
        }
    }

    public void moveLeft() { //metodo chamado na classe arena pa mover pra esquerda os pipes no array
        position = new Position(position.getX() - 1, position.getY());
    };

    public int getGapStart() {
        return gapStart;
    }

    public int getGap() {
        return gap;
    }

    public boolean collidePipe(Bird bird) {

        int birdLeft   = bird.getLeftX();
        int birdRight  = bird.getRightX();
        int birdStart    = bird.getStartY();
        int birdEnd = bird.getEndY();

        if (birdRight < getX() || birdLeft >= getX() + width) { //compara o x dos lados do brid com a posicao do pipe
            //se birdRight antes de pipe ou birdLeft depois do fim pipe, n ha colisao
            return false;
        }

        if (birdStart < gapStart || birdEnd >= gapStart + gap) { //parte vertical, se topo do bird bate no pipe de cima
            //ou se o fim dele bate no pipe de baixo (isto quando passa no mei)
            return true;
        }

        return false;
    }

    @Override
    public boolean isScored() {
        return scored;
    }

    @Override
    public void setAsScored(boolean scored) {
        this.scored = scored;
    }

    @Override
    public int getScoreValue() {
        return 1;  //return 1 = 1 ponto cada pipe
    }
}

