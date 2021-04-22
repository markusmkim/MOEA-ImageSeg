package EA;

import javafx.scene.paint.Color;

public class Metrics {
    /*
    Returns "Euclidean" distance (in RGB space) between two Color objects
     */
    public static double distance(Color c1, Color c2) {
        double deltaRed = c1.getRed() - c2.getRed();
        double deltaGreen = c1.getGreen() - c2.getGreen();
        double deltaBlue = c1.getBlue() - c2.getBlue();
        return Math.sqrt(Math.pow(deltaRed, 2) + Math.pow(deltaGreen, 2) + Math.pow(deltaBlue, 2));
    }
}