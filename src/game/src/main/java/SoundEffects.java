public class SoundEffects {

    //SE = Sound Effect.
    private Sound jumpSE;
    private Sound scoreSE;
    private Sound hitSE;
    private Sound startingSE;
    private Sound diesSE;

    public SoundEffects() {

        jumpSE = new Sound();
        jumpSE.setFile("sounds/wing.wav");

        scoreSE = new Sound();
        scoreSE.setFile("sounds/point.wav");

        hitSE = new Sound();
        hitSE.setFile("sounds/hit.wav");

        startingSE = new Sound();
        startingSE.setFile("sounds/swoosh.wav");

        diesSE = new Sound();
        diesSE.setFile("sounds/die.wav");
    }

    public void playJump() {
        try {
            jumpSE.play();
        } catch (Exception e) {
        }
    }

    public void playScore() {
        try {
            scoreSE.play();
        } catch (Exception e) {
        }
    }

    public void playHit() {
        try {
            hitSE.play();
        } catch (Exception e) {
        }
    }

    public void playStarting() {
        try {
            startingSE.play();
        } catch (Exception e) {
        }
    }

    public void playDies() {
        try {
            diesSE.play();
        } catch (Exception e) {
        }
    }
}

