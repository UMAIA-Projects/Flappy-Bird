import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BirdTest {

    private Bird bird;

    @BeforeEach
    public void setUp() {
        bird = new Bird(20, 30);
    }

    @AfterEach
    public void tearDown() {
        bird = null;
    }

    //Teste de Estados: getState
    @Test
    public void getState_Birdy() {

        SpritesBird state = bird.getState();

        assertEquals(SpritesBird.BIRDY, state);
    }

    //setState
    @Test
    public void setState_DeadBirdy() {

        assertEquals(SpritesBird.BIRDY, bird.getState());   //BIRDY = bird.state?

        bird.setState(SpritesBird.DEADBIRDY);               //marca state = DEADBIRDY

        assertEquals(SpritesBird.DEADBIRDY, bird.getState());   //verifica se mudança foi feita corretamente
    }

    //Testes de posição: getX
    @Test
    public void getX() {

        int x = bird.getX();

        assertEquals(20, x);
    }

    //getY
    @Test
    public void getY() {

        int y = bird.getY();

        assertEquals(30, y);
    }

    //Atualização das coordenadas do bird
    @Test
    public void setPosition() {

        Position newPos = new Position(50, 60);

        bird.setPosition(newPos);

        assertEquals(50, bird.getX());
        assertEquals(60, bird.getY());
    }

    //Teste do Bird "Sprite"                                        bird = new Bird(20, 30);
    @Test
    public void getLeftX() {

        int leftX = bird.getLeftX();

        assertEquals(16, leftX);  // 20 - 8/2 = 16         return getX() - (bird_width / 2);
    }

    @Test
    public void getRightX() {

        int rightX = bird.getRightX();

        assertEquals(23, rightX);  // 16 + 8 - 1 = 23       return getLeftX() + bird_width - 1;
    }

    @Test
    public void getHeight() {

        int bird_height = bird.getHeight();

        assertEquals(4, bird_height);
    }

    @Test
    public void getStartY() {

        int startY = bird.getStartY();

        assertEquals(30, startY); //                            return getY();
    }

    @Test
    public void getEndY() {

        int endY = bird.getEndY();

        assertEquals(33, endY);  // 30 + 4 - 1 = 33             return getY() + bird_height - 1;
    }

}