package Graph;

import javafx.scene.image.PixelReader;

import java.util.ArrayList;
import java.util.List;


public class Builder {
    /**
     * Builds a rectangular grid-like graph/lattice of nodes (Graph.Node) and edges (Graph.Edge),
     * where every node is connected to 4 others: East, West, North and South.
     * The nodes at the edges of the grid will naturally only have 3 connected nodes,
     * and the corner nodes will have only 2 connected nodes.
     * @param height height of grid = number of nodes in the y-direction
     * @param width width of grid = number of nodes in the x-direction
     * @param pixelReader a PixelReader object to access Color-objects from the Image objective to segmentation
     * @return List of nodes (with edges) comprising the grid
     */
    public static List<Node> buildGrid(int height, int width, PixelReader pixelReader) {
        List<Node> nodes = new ArrayList<>();
        int counter = 0;
        for (int row = 0; row < height; row++ ) {
            for (int col = 0; col < width; col++ ) {
                Node node = new Node("" + counter, pixelReader.getColor(col, row));
                if (row > 0) {
                    Node neighbourNorth = nodes.get(counter - width);
                    node.setEdgeNorth(neighbourNorth);
                }
                if (col > 0) {
                    Node neighbourWest = nodes.get(counter - 1);
                    node.setEdgeWest(neighbourWest);
                }
                nodes.add(node);
                counter ++;
            }
        }
        return nodes;
    }
}
