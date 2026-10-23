import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class ArenaTest {

    //!!! Com o run há prints de erro, mas é somente em relação aos sons (não acha). Os testes passam todos, arena funcional.

    private Arena arena;
    private Game game;
    private static final int width = 65;
    private static final int height = 40;

    @BeforeEach
    public void setUp() {
        arena = new Arena(width, height, game);
    }

    @AfterEach
    public void tearDown() {
        arena = null;
        game = null;
    }

    //Testes de colisão
    //Wall de baixo
    @Test
    void checkCollision_WithLowerWall() {
        arena.start();
        Bird bird = arena.getBird();

        bird.setPosition(new Position(20, 39));  // Posiciona bird no chão (height - 1), assumindo height = 40

        arena.checkCollision();

        assertTrue(arena.isBirdDead());
        assertEquals(SpritesBird.DEADBIRDY, bird.getState());
    }

    //Com um pipe
    @Test
    void checkCollision_WithPipe() {
        arena.start();
        arena.updatePipes();
        Bird bird = arena.getBird();
        List<Pipe> pipes = arena.getPipes();

        assertFalse(pipes.isEmpty());       //pipes na arena

        Pipe firstPipe = pipes.get(0);      //primeiro pipe da lista de pipes

        int pipeX = firstPipe.getX();              //pipeX: posição horizontal do pipe (ex: x=59 se width=65)
        int pipeY = 2;                             //pipeY: 2 (topo da arena, antes do ‘gap’ começar)
        bird.setPosition(new Position(pipeX + 2, pipeY));  // pipeX + 2 = coloca bird 2 pixels à direita do início do pipe

        arena.checkCollision();

        assertTrue(arena.isBirdDead());
        assertEquals(SpritesBird.DEADBIRDY, bird.getState());
    }

    //Sem colisão
    @Test
    void checkCollision_NoCollision() {
        arena.start();
        Bird bird = arena.getBird();

        bird.setPosition(new Position(20, 20));         //bird no meio

        arena.checkCollision();

        assertFalse(arena.isBirdDead());
        assertEquals(SpritesBird.BIRDY, bird.getState());
    }

    //Teste de aumento de score após jogos
    @Test
    void getScore() {
        arena.start();

        int initialScore = arena.getScore(); // 0

        for (int i = 0; i < 100; i++) {     // Simula passar pipes
            arena.updatePipes();
            arena.updateBird();
        }

        int finalScore = arena.getScore();   //dps da simulação por muitos pipes

        assertTrue(finalScore > initialScore);
    }
}