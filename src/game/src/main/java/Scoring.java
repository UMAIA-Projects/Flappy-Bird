public interface Scoring {
    boolean isScored();

    void setAsScored(boolean scored);

    int getScoreValue();

    default boolean checkAndScore(Bird bird, int pipeX, int pipeWidth) {

        if (isScored())     //se já está scored, n faz nd, return
            return false;

        int birdX = bird.getX();    //pa saber a posição do bird
        int passedPipe = pipeX + pipeWidth;     //pa ter noção se passou ou não do pipe

        if (birdX > passedPipe && !isScored()) {    //dependendo da pos do Bird, se passou pipe:
            setAsScored(true);  // marca pipe como scored (se este não foi marcado antes)
            return true;   //done
        }

        return false;
    }
}
