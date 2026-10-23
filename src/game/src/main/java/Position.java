public class Position {
    private double x;
    private double y;

    public Position(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }

    @SuppressWarnings("EqualsHashCode")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position))return false;

        Position position = (Position) o;

        if (Double.compare(position.x, x) != 0) return false;
        return Double.compare(position.y, y) == 0;
    }

    @Override
    public  String toString(){
        return x + ", " + y;
    }
}
