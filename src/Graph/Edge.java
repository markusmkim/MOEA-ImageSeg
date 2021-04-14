package Graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Edge {

    private final List<Node> nodes;
    private final double distance;

    public Edge(Node node1, Node node2) {
        this.nodes = new ArrayList<>(Arrays.asList(node1, node2));
        this.distance = 1;
    }

    public double getDistance()  { return distance; }
    public List<Node> getNodes() { return nodes; }

    @Override
    public String toString() {
        return "" + nodes.get(0).getId() + " - " + nodes.get(1).getId();
    }
}
