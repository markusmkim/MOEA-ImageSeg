package Graph;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Node implements Comparable<Node> {
    // A node in a rectangular grid. All nodes have 2 - 4 neighours (North, South, West, East)
    private final String id;

    private Edge edgeEast;
    private Edge edgeWest;
    private Edge edgeNorth;
    private Edge edgeSouth;

    // Used by MST algorithm
    private double key = 1000000;
    private Node parent;
    private char direction = '0';


    // test
    private final double value;

    public Node(String id) {
        this.id = id;
        if (id.equals("A") || id.equals("C") || id.equals("G")) {
            this.value = 100;
        }
        else if (id.equals("D") || id.equals("J") || id.equals("L")) {
            this.value = 50;
        }
        else {
            this.value = 4;
        }
    }


    // Getters
    public String getId()        { return id; }
    public double getKey()       { return key; }
    public Node getParent()      { return parent; }
    public char getDirection()   { return direction; }
    public Edge getEdgeEast()    { return edgeEast; }
    public Edge getEdgeWest()    { return edgeWest; }
    public Edge getEdgeNorth()   { return edgeNorth; }
    public Edge getEdgeSouth()   { return edgeSouth; }

    public List<Edge> getEdges() {
        List<Edge> realEdges = new ArrayList<>();
        Edge[] edges = new Edge[]{edgeEast, edgeWest, edgeNorth, edgeSouth};
        for (Edge e : edges) {
            if (e != null) {
                realEdges.add(e);
            }
        }
        return realEdges;
    }


    // Setters
    public void setKey(double key)            { this.key = key; }
    public void setParent(Node parent)        { this.parent = parent; }
    public void setDirection(char direction)  { this.direction = direction; }
    private void setEdgeEast(Edge edgeEast)   { this.edgeEast = edgeEast; }
    private void setEdgeWest(Edge edgeWest)   { this.edgeWest = edgeWest; }
    private void setEdgeNorth(Edge edgeNorth) { this.edgeNorth = edgeNorth; }
    private void setEdgeSouth(Edge edgeSouth) { this.edgeSouth = edgeSouth; }


    public void setEdgeEast(Node neighbourEast) {
        if (this.edgeEast == null) {
            Edge edge = new Edge(this, neighbourEast, true, this.value);
            this.edgeEast = edge;
            neighbourEast.setEdgeWest(edge);
        }
    }

    public void setEdgeWest(Node neighbourWest) {
        if (this.edgeWest == null) {
            Edge edge = new Edge(this, neighbourWest, true, value);
            this.edgeWest = edge;
            neighbourWest.setEdgeEast(edge);
        }
    }

    public void setEdgeNorth(Node neighbourNorth) {
        if (this.edgeNorth == null) {
            Edge edge = new Edge(this, neighbourNorth, false, value);
            this.edgeNorth = edge;
            neighbourNorth.setEdgeSouth(edge);
        }
    }

    public void setEdgeSouth(Node neighbourSouth) {
        if (this.edgeSouth == null) {
            Edge edge = new Edge(this, neighbourSouth, false, value);
            this.edgeSouth = edge;
            neighbourSouth.setEdgeNorth(edge);
        }
    }


    @Override
    public String toString() {
        return "" + this.id;
    }

    @Override
    public int compareTo(Node o) {
        return (int) Math.round(key - o.getKey());
    }
}
