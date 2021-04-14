package EA.Components;


import Graph.Edge;
import Graph.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Individual {
    private String chromosome;

    public Individual(int height, int width) {
        this.initializeChromosome(height, width);
    }


    private void initializeChromosome(int height, int width) {
        // Prims algorithm //
        List<String> names = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q"));
        // Make graph structure
        List<Node> nodes = new ArrayList<>();
        int counter = 0;
        for (int row = 0; row < height; row++ ) {
            for (int col = 0; col < width; col++ ) {
                Node node = new Node(names.get(counter));
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
        List<Edge> edges = new ArrayList<>();
        for (Node n : nodes) {
            Edge northEdge = n.getEdgeNorth();
            if (northEdge != null) {
                edges.add(northEdge);
            }
            Edge westEdge = n.getEdgeWest();
            if (westEdge != null) {
                edges.add(westEdge);
            }
        }
        for (Edge e : edges) {
            System.out.println(e);
        }
    }
}
