import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PipeTest {

    private Pipe pipe;

    @BeforeEach
    public void setUp() {   //criação de pipe e bird aleatoriamente
        pipe = new Pipe(50, 10, 6, 13, 45);
    }

    @AfterEach
    public void tearDown() {
        pipe = null;
    }

    //Teste de posição do pipe
    @Test
    public void GetX() {

        int x = pipe.getX();

        assertEquals(50, x);

    }

    //Teste de movimentação do pipe (moveLeft())
    @Test
    public void moveLeft() {

        int initialX = pipe.getX();

        pipe.moveLeft();

        assertEquals(initialX - 1, pipe.getX());

    }

    //Teste de movimentações sucessivas do pipe
    @Test
    public void moveLeft_manyTimes() {

        int initialX = pipe.getX();

        pipe.moveLeft();
        pipe.moveLeft();
        pipe.moveLeft();

        assertEquals(initialX - 3, pipe.getX());
    }

    //Teste para marcação de pipes como scored
    @Test
    public void setAsScored() {

        assertFalse(pipe.isScored());   //pipe não marcado como "scored"

        pipe.setAsScored(true);         //marcá-lo como scored

        assertTrue(pipe.isScored(), "Pipe deve ser marcado como scored");
    }

    //Teste da criação dos ‘gaps’ (Início deste)...
    @Test
    public void getGapStart() {

        int gapStart = pipe.getGapStart();

        assertEquals(10, gapStart);
    }

    //(Fim deste)
    @Test
    public void getGapEnd() {

        int gapEnd = pipe.getGap();

        assertEquals(13, gapEnd);
    }

}

