import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;

import java.awt.*;
import java.io.IOException;

public class Game {
//.
    private Screen screen = null;
    private Arena arena = null;
    private Controller c;
    private Thread t;
    private boolean finish;
    private int gameState;

    public TerminalSize terminalSize; //guardar o tam do terminal com o resize
    private int StartOfGameScreenX;
    private int StartOfGameScreenY;

    private static final int gameWidth = 65;
    private static final int max_height_arena = 45;

    private SoundEffects SoundEffects;

    Game() {
        try {
            DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
            terminalFactory.setInitialTerminalSize(new TerminalSize(gameWidth, 45));

            Terminal terminal = terminalFactory.createTerminal();

            if (terminal instanceof SwingTerminalFrame) {
                SwingTerminalFrame terminalFrame = (SwingTerminalFrame) terminal;

                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();     //dimensoes da tela

                int x = (screenSize.width - terminalFrame.getWidth()) / 2;
                int y = ((screenSize.height - terminalFrame.getHeight()) / 2) - 13;
                terminalFrame.setLocation(x, y);
            }

            screen = new TerminalScreen(terminal);
            screen.setCursorPosition(null);
            screen.startScreen();

            terminalSize = screen.getTerminalSize();
            arena = new Arena(gameWidth, terminalSize.getRows(), this);

            c = new Controller(this, screen);
            t = new Thread(c);
            t.start();
            finish = false;

            gameState = 0;

            this.SoundEffects = new SoundEffects();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void draw() throws IOException {

        //resize da doc aqui
        TerminalSize newTerminalSize = screen.doResizeIfNecessary();
        if(newTerminalSize != null) {
            terminalSize = newTerminalSize;

            StartOfGameScreenX = (terminalSize.getColumns() - gameWidth) / 2; //centrar o game no terminal
            //gameWidth no tamanho do terminal respetivo -> /2 pa ficar no meio

            int gameHeight = Math.min(terminalSize.getRows(), max_height_arena);
            StartOfGameScreenY = (terminalSize.getRows() - gameHeight) / 2; // centralização vertical

            arena.updateTerminalHeight(gameHeight); //height somente, logo getRows para conseguir dar update

        }

        screen.clear();

        if(gameState == 0) { //menu
            TextGraphics graphics = screen.newTextGraphics();

            graphics.setBackgroundColor(TextColor.Factory.fromString("#000000"));
            graphics.setForegroundColor(TextColor.Factory.fromString("#FFFFFF"));
            graphics.enableModifiers(SGR.BOLD);

            //TITLE dinamico
            String[] draw = SpritesGame.TITLE.getSprite();

            int titleX = (terminalSize.getColumns() - draw[0].length()) / 2;
            int titleY = terminalSize.getRows() / 3;

            for (int y = 0; y < draw.length; y++) {
                for (int x = 0; x < draw[y].length(); x++) {
                    char c = draw[y].charAt(x);

                    if (c != ' ') {
                        graphics.putString(new TerminalPosition(titleX + x, titleY + y), String.valueOf(c));
                    }
                }
            }

            int centerY = terminalSize.getRows() / 2 + 5;
            graphics.putString(
                    new TerminalPosition((terminalSize.getColumns() - 30) / 2, centerY), "Clique no espaço para iniciar"
            );
            graphics.putString(
                    new TerminalPosition((terminalSize.getColumns() - 24) / 2, centerY + 3), "Pressione 'q' para sair"
            );
        }

        if (gameState == 1) {  //a jogar
            arena.draw(screen.newTextGraphics(), StartOfGameScreenX, StartOfGameScreenY);

        } else if (gameState == 2) {  //morre
            TextGraphics graphics = screen.newTextGraphics();

            graphics.setBackgroundColor(TextColor.Factory.fromString("#000000"));
            graphics.setForegroundColor(TextColor.Factory.fromString("#FFFFFF"));
            graphics.enableModifiers(SGR.BOLD);

            //GAME OVER dinamico
            String[] draw = SpritesGame.ENDING.getSprite();

            int endingX = (terminalSize.getColumns() - draw[0].length()) / 2;
            int endingY = terminalSize.getRows() / 4;

            for (int y = 0; y < draw.length; y++) {
                for (int x = 0; x < draw[y].length(); x++) {
                    char c = draw[y].charAt(x);

                    if (c != ' ') {
                        graphics.putString(new TerminalPosition(endingX + x, endingY + y), String.valueOf(c));
                    }
                }
            }

            int centerY = terminalSize.getRows() / 2 + 8;

            String bestScore = "Best Score: " + arena.getBestScore();

            graphics.putString(
                    new TerminalPosition((terminalSize.getColumns() - bestScore.length()) / 2, centerY), bestScore
            );
            graphics.putString(
                    new TerminalPosition((terminalSize.getColumns() - 32) / 2, centerY + 4), "Clica no espaço para recomeçar"
            );
            graphics.putString(
                    new TerminalPosition((terminalSize.getColumns() - 48) / 2, centerY + 6), "Aperta 'Q' para sair ou 'E' para voltar ao menu"
            );

        }

        screen.refresh();
    }

    public void run() {
        while (!finish) {
            try {
                draw();

                if (gameState == 1) {
                    arena.updateBird();
                    arena.updatePipes();
                    arena.checkCollision();
                }

                Thread.sleep(40);

            } catch(IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            screen.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Morreu");
    }

    public void processKey (KeyStroke key){
        try {
            if (key.getKeyType() == KeyType.Character && key.getCharacter() == 'q') {
                c.finish();
                arena.clean();
                finish = true;
                screen.close();

            } else if (key.getKeyType() == KeyType.EOF) {
                c.finish();
                arena.clean();
                finish = true;
            }
            else if (key.getKeyType() == KeyType.Character && key.getCharacter() == ' ') {
                if(gameState == 0) {
                    SoundEffects.playStarting();
                    arena.start();
                    gameState = 1;
                }
                else if (gameState == 1) {
                    arena.processKey(key);
                }
                else if (gameState == 2) {
                    SoundEffects.playStarting();
                    arena.start();
                    gameState = 1;
                }
            }
            else if (key.getKeyType() == KeyType.Character && key.getCharacter() == 'e') {
                if (gameState == 2) {
                    gameState = 0;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void died () {
        arena.clean();
        gameState = 2;
    }
}
