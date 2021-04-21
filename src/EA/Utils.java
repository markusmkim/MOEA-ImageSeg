package EA;


public class Utils {

    public static int[] convertIndexToCoordinates(int index, int width) {  // [x, y] coordinate
        int row = index / width;
        int col = index % width;
        return new int[]{col, row};
    }

}
