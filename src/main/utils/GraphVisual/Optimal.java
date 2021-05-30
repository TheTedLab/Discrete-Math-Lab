package main.utils.GraphVisual;

//Класс оптимального сдвига графа
public class Optimal {
    private final double diffX;
    private final double diffY;
    private final boolean difference;
    private final double maxCordX;
    private final double maxCordY;

    public Optimal(double diffX, double diffY, boolean difference,
                   double maxCordX,
                   double maxCordY) {
        this.diffX = diffX;
        this.diffY = diffY;
        this.difference = difference;
        this.maxCordX = maxCordX;
        this.maxCordY = maxCordY;
    }

    public double getDiffX() {
        return diffX;
    }

    public double getDiffY() {
        return diffY;
    }

    public boolean isDifference() {
        return difference;
    }

    public double getMaxCordX() {
        return maxCordX;
    }

    public double getMaxCordY() {
        return maxCordY;
    }
}
