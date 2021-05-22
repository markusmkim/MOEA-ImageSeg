package Graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/*
An edge between two nodes
 */
public class Edge {
    private final List<Node> nodes;
    private final double distance;
    private final boolean horizontal;


    public Edge(Node node1, Node node2, boolean horizontal, double distance) {
        this.nodes = new ArrayList<>(Arrays.asList(node1, node2));
        this.distance = distance;
        this.horizontal = horizontal;
    }


    public double getDistance()  { return distance; }
    public List<Node> getNodes() { return nodes; }


    public Node traverse(Node fromNode) {
        if (nodes.get(0) == fromNode) {
            return nodes.get(1);
        }
        if (nodes.get(1) == fromNode) {
            return nodes.get(0);
        }
        return null;
    }


    public char getTraverseDirection(Node fromNode) {
        if (nodes.get(0) == fromNode) {
            if (horizontal) {
                return 'W';
            }
            return 'N';
        }
        if (nodes.get(1) == fromNode) {
            if (horizontal) {
                return 'E';
            }
            return 'S';
        }
        return '0';
    }

    @Override
    public String toString() {
        return "" + nodes.get(0).getId() + " - " + nodes.get(1).getId();
    }
}
