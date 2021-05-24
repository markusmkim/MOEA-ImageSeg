package ea;

import java.util.Locale;


public class Utils {

    public static int[] convertIndexToCoordinates(int index, int width) {  // [x, y] coordinate
        int row = index / width;
        int col = index % width;
        return new int[]{col, row};
    }


    public static int[] getNeighbourIndexes(int index, int height, int width, boolean diagonals) {
        int[] coordinates = Utils.convertIndexToCoordinates(index, width);
        int east = coordinates[0] < width - 1 ? index + 1 : -1;
        int west = coordinates[0] > 0 ? index - 1 : -1;
        int north = coordinates[1] > 0 ? index - width : -1;
        int south = coordinates[1] < height - 1 ? index + width : -1;

        if (diagonals) {
            int northEast = north > -1 && east > -1 ? north + 1 : -1;
            int southEast = south > -1 && east > -1 ? south + 1 : -1;
            int northWest = north > -1 && west > -1 ? north - 1 : -1;
            int southWest = south > -1 && west > -1 ? south - 1 : -1;

            return new int[]{east, west, north, south, northEast, southEast, northWest, southWest};
        }

        return new int[]{east, west, north, south};
    }


    public static String formatValue(double value) {
        String outputValue = String.format(Locale.ROOT, "%.2f", value);
        String space = "       ".substring(0, 8 - outputValue.length());
        return space + outputValue;
    }

}
