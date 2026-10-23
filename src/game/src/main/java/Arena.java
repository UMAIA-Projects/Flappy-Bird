import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Arena {

    //arena
    private int width;
    private int height;
    private static final int maxArenaHeight = 45;
    private int viewMaxHeight; //altura final usada no jogo, dep do tamanho do terminal

    //inicializações
    private Bird bird;
    private Game game = null;
    private List<Wall> walls;
    private List<Pipe> pipes;

    //pipes
    private final int pipe_width = 6; //largura do pipe
    private final int pipe_distance = 25; //ditância entre cada pipe na geração destes
    private final int pipe_gap = 13;
    private int nextPipeX; //pos pa gerar o proximo pipe
    private final Random random = new Random();

    //mecanicas - bird
    private double bird_vel = 0.0; //velocidade do passaro pa cima/baixo
    private final double gravity = 0.3; //gravidade a cada run/loop, sla valor random
    private final double jump = -1.4; //velocidade após spacebar, quanto menos mais salta
    private final double maxFallSpeed = 2.5; //referencia de max para nao ter velocidade mt alta

    private boolean birdDied = false;  // indica se o bird bateu
    private double deadVel = 0; //inicialização da var para queda quando morre

    //scoring
    private int score = 0;
    private int bestScore = 0;

    //sons
    private SoundEffects SoundEffects;

    Arena(int width, int height, Game game){
        this.width = width;
        this.viewMaxHeight = Math.min(height, maxArenaHeight); //max será 50, mas se getRows < 50, escolhe esse
        this.height = this.viewMaxHeight;
        this.game = game;

        bird = new Bird(width/3, this.height/2); //iniciar no meio da tela

        this.walls = createWalls();
        this.pipes = createPipes();
        this.nextPipeX = (width - 6) - pipe_distance;
        this.score = 0;
        this.SoundEffects = new SoundEffects();
    }

    public void updateTerminalHeight(int newTerminalHeight) {
        this.viewMaxHeight = Math.min(newTerminalHeight, maxArenaHeight);
        this.height = viewMaxHeight;
        this.walls = createWalls();

        if (bird.getY() >= height - 1) {
            bird.setPosition(new Position(bird.getX(), height / 2));
        }
    }

    //jogo - funcionalidades
    private List<Wall> createWalls() {          //paredes do jogo/arena
        List<Wall> walls = new ArrayList<>();

        for (int c = 0; c < width; c++) {
            walls.add(new Wall(c, 0, "-"));
            walls.add(new Wall(c, height - 1, "-"));
        }

        for (int r = 1; r < height - 1; r++) {
            walls.add(new Wall(0, r, "|"));
            walls.add(new Wall(width - 1, r, "|"));
        }

        return walls;
    }

    private List<Pipe> createPipes() {              //tem de ter uma funcao random pa o pipe de cima e debaixo terem tamanhos dif (de altura)
        List<Pipe> pipes = new ArrayList<>();

        int gapStart = random.nextInt(height - pipe_gap - 5) + 3; //dist da margem de baixo e margem de cima respetivamente (começa no 0), esta a gerar entre 3 e 13
        //decide o inicio do gap, vai dar um numero random entre 1 e a altura max (alt da arena - o tam do gap = 5), + 1 que é pa nao comec em 0
        pipes.add(new Pipe(width - 6, gapStart, pipe_width, pipe_gap, height)); //adiciona novo pipe à lista de pipes a aparecer

        return pipes;
    }

    public void draw(TextGraphics graphics, int StartOfGameScreenX, int StartOfGameScreenY) {
        graphics.setBackgroundColor(TextColor.Factory.fromString("#4FC3F7")); //azul fundo
        graphics.fillRectangle(new TerminalPosition(StartOfGameScreenX, StartOfGameScreenY), new TerminalSize(width, height), ' ');

        for (Pipe pipe : pipes) {
            pipe.draw(graphics, StartOfGameScreenX, StartOfGameScreenY);
        }

        for (Wall wall : walls) {
            wall.draw(graphics, StartOfGameScreenX, StartOfGameScreenY);
        }

        bird.draw(graphics, StartOfGameScreenX, StartOfGameScreenY, pipes, height);  // adiciona height

        graphics.setBackgroundColor(TextColor.Factory.fromString("#FFFFFF"));
        graphics.setForegroundColor(TextColor.Factory.fromString("#000000"));
        graphics.putString(
                new com.googlecode.lanterna.TerminalPosition(StartOfGameScreenX + 2, StartOfGameScreenY + 2), "Score: " + score);
    }

    public void processKey(KeyStroke key) { //metodo pro passaro saltar apos spacebar
        if (key.getKeyType() == KeyType.Character && key.getCharacter() == ' ') {
            bird_vel = jump;
            SoundEffects.playJump();
        }
    }

    public void updatePipes() { //alternativa, meti todos os que se vai remover num array proprio

        List<Pipe> PipeRemover = new ArrayList<>();

        for (Pipe p : pipes) {
            p.moveLeft();   //movimentação pa esq dos pipes

            giveScore(p);

            if (p.getX() + pipe_width < 0) {
                PipeRemover.add(p);   //add no array para remover se o pipe passar o limite da tela do lado esq
            }
        }

        pipes.removeAll(PipeRemover);  //remover todos os pipes que entram no array

        //se a lista de pipes ficar vazia add outro OU se o ult pipe que foi gerado esta a X distancia (entre x_limite_tela e pipe_distance)
        if (pipes.isEmpty() || pipes.get(pipes.size() - 1).getX() <= nextPipeX) {

            int gapStart = random.nextInt(height - pipe_gap - 5) + 3;
            pipes.add(new Pipe(width - 6, gapStart, pipe_width, pipe_gap, height));

        }
    }

    public void updateBird() { //metodo que atualiza a pos do bird

        if (birdDied) { //se o bird morreu, começa processo de queda

            deadVel += gravity; // aumenta velocidade da queda

            if (deadVel > maxFallSpeed) {
                deadVel = maxFallSpeed; //n ultrapassa max
            }

            int novoY = (int)(bird.getY() + deadVel); //deadVel é double

            int birdEnd = novoY + bird.getHeight() - 1; //

            if (birdEnd >= height - 1) { // somente quando o bird bate no chão éq da gameover,
                updateBestScore();                                    // tem de usar novoY para contar a pos atualizada
                game.died();        // gameover
            }

            bird.setPosition(new Position(bird.getX(), novoY));

        } else {

            bird_vel += gravity; //gravidade na velocidade do passaro

            if (bird_vel > maxFallSpeed) {
                bird_vel = maxFallSpeed; //no max velocidade maxima de queda
            }

            int novoY = (int) (bird.getY() + bird_vel); //pos nova após o jump da spacebar
            bird.setPosition(new Position(bird.getX(), novoY));
        }
    }

    //colisões
    public void checkCollision() { //colisao com as paredes e teto

        if (!birdDied) {
            if (bird.getY() <= 0 || bird.getY() >= height - 1) { //limites pro bird ir
                bird.setState(SpritesBird.DEADBIRDY); //quando bate muda de sprite
                SoundEffects.playHit();
                startFall(); //inicia o metodo que o faz cair
                SoundEffects.playDies();
                return;
            }

            for (Pipe p : pipes) {
                if (p.collidePipe(bird)) {
                    bird.setState(SpritesBird.DEADBIRDY);
                    SoundEffects.playHit();
                    startFall();
                    SoundEffects.playDies();
                    return;
                }
            }
        }
    }

    //score
    public int getScore() {
        return score;
    }

    public int getBestScore() {
        return bestScore;
    }

    private void giveScore(Pipe pipe) {

        if (pipe.checkAndScore(bird, pipe.getX(), pipe_width) && !birdDied) {

            score += pipe.getScoreValue(); //soma ao score a cada vez que passa um pipe
            SoundEffects.playScore();
        }
    }

    private void updateBestScore() {
        if (score > bestScore) {
            bestScore = score;
        }
    }

    //dead
    private void startFall() {
        birdDied = true; //vai dar update (updateBird dá as instruções para a queda
        deadVel = 0; // reinicia velocidade da queda
    }

    //start/restart
    public void clean() {
        pipes.clear();
    }

    public void start() {  //corre quando da space (inicia) -> tbm faz reset de quando morre (restart)

        bird.setPosition(new Position(width/3, height/2));
        bird_vel = 0.0;

        bird.setState(SpritesBird.BIRDY);

        birdDied = false;
        deadVel = 0;

        pipes.clear();

        score = 0;
    }

    //Adicionado para comprovar nos testes
    public Bird getBird() {
        return bird;
    }
    public List<Pipe> getPipes() {
        return new ArrayList<>(pipes); // retorna cópia para segurança
    }
    public boolean isBirdDead() {
        return birdDied;
    }
}
