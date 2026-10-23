import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;

public class Controller implements Runnable{
    private Game game;
    private Screen screen;
    private boolean finish;

    public Controller(Game game, Screen screen){
        this.game = game;
        this.screen = screen;
        finish = false;

        System.out.println("cont");
    }

    @Override
    public void run() {
        while (!finish) {
            KeyStroke key = null;
            try {
                key = screen.readInput();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            game.processKey(key);
        }

        System.out.println("Cont f.");
    }

    public void finish(){
        finish = true;
    }
}

