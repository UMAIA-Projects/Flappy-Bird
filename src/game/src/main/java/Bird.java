import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;

import java.util.List;

public class Bird extends GameComplements {

    private boolean end;
    private static final int bird_width = 8;
    private static final int bird_height = 4;

    private SpritesBird birdState;

    Bird(int x, int y){
        super(x, y);
        birdState = SpritesBird.BIRDY;
    }

    public void setState(SpritesBird newState) {
        birdState = newState;
    }

    public SpritesBird getState() {
        return birdState;
    }

    @Override
    public void draw(TextGraphics graphics, int StartOfGameScreenX, int StartOfGameScreenY) {
        draw(graphics, StartOfGameScreenX, StartOfGameScreenY, null, 0); //necessário para o override
    }
    public void draw(TextGraphics graphics, int StartOfGameScreenX, int StartOfGameScreenY, List<Pipe> pipes, int arenaHeight) {

        if(end)
            return;

        String[] draw = birdState.getSprite();
        int midX = draw[0].length()/2;  //centro bird

        for(int y = 0; y < draw.length; y++){   //cada linha
            for(int x = 0; x < draw[y].length(); x++){  //cada coluna

                char c = draw[y].charAt(x);     //caract do bird

                int px = x - midX;
                int py = y;

                TextColor bgColor = getColorAt(position.getX() + px, position.getY() + py, pipes, arenaHeight); //background desse pixel

                if (c == '>') { //bico

                    graphics.setForegroundColor(TextColor.Factory.fromString("#FF7034"));
                    graphics.setBackgroundColor(bgColor); //background dinamico
                    graphics.enableModifiers(SGR.BOLD);

                    graphics.putString(
                            new TerminalPosition(StartOfGameScreenX + (int) (position.getX() + px), StartOfGameScreenY + (int) (position.getY() + py)),
                            String.valueOf(c)
                    );

                } else if(c == 'O') { //olho

                    graphics.setForegroundColor(TextColor.Factory.fromString("#000000"));
                    graphics.setBackgroundColor(bgColor); //background dinamico
                    graphics.enableModifiers(SGR.BOLD);

                    graphics.putString(
                            new TerminalPosition(StartOfGameScreenX + (int) (position.getX() + px), StartOfGameScreenY + (int) (position.getY() + py)),
                            String.valueOf(c)
                    );

                } else if(c != '*') { //restante corpo do bird

                    graphics.setForegroundColor(TextColor.Factory.fromString("#00008B")); //azul escuro traços
                    graphics.setBackgroundColor(bgColor); //background dinamico
                    graphics.enableModifiers(SGR.BOLD);

                    graphics.putString(
                            new TerminalPosition(StartOfGameScreenX + (int) (position.getX() + px), StartOfGameScreenY + (int) (position.getY() + py)),
                            String.valueOf(c)
                    );
                }
            }
        }
    }

    private TextColor getColorAt(double x, double y, List<Pipe> pipes, int arenaHeight) {

        if (y <= 0 || y >= arenaHeight - 1) {
            return TextColor.Factory.fromString("#000000"); // preto
        }

        for (Pipe pipe : pipes) {    // verif dos pixeis na zona dos pipes
            int pipeX = pipe.getX();

            if (x >= pipeX && x < pipeX + 6) {   //verifica se está na área horizontal do pipe

                int gapStart = pipe.getGapStart();
                int gapEnd = gapStart + pipe.getGap();

                if (y < gapStart || y >= gapEnd) { //verif se bird está exatamente onde está o pipe de cima e de baixo
                    return TextColor.Factory.fromString("#008000"); // verde, está no pipe
                }
            }
        }
        return TextColor.Factory.fromString("#4FC3F7"); // azul, não está no pipe
    }

    //para a hitbox
    public int getStartY() {
        return getY();
    }

    public int getEndY() {
        return getY() + bird_height - 1;
    }

    public int getLeftX() {
        return getX() - (bird_width / 2);
    }

    public int getRightX() {
        return getLeftX() + bird_width - 1;
    }

    public int getHeight() {    //criado especialemente para conseguir o gameover após a queda
        return bird_height;
    }
}
