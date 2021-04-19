package Graph;

import EA.Metrics;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;


public class Node implements Comparable<Node> {
    // A node in a rectangular grid. All nodes have 2 - 4 neighbours (North, South, West, East)
    private final String id;
    private final Color color;

    private Edge edgeEast;
    private Edge edgeWest;
    private Edge edgeNorth;
    private Edge edgeSouth;

    // Used by MST algorithm
    private double key = 1000000;
    private Node parent;
    private char direction = '0';


    public Node(String id, Color color) {
        this.id = id;
        this.color = color;
    }

    public Node(String id, Edge edgeEast, Edge edgeWest, Edge edgeNorth, Edge edgeSouth, Color color) {
        this.id = id;
        this.edgeEast = edgeEast;
        this.edgeWest = edgeWest;
        this.edgeNorth = edgeNorth;
        this.edgeSouth = edgeSouth;
        this.color = color;
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
            double distance = Metrics.distance(this.color, neighbourEast.color);
            Edge edge = new Edge(this, neighbourEast, true, distance);
            this.edgeEast = edge;
            neighbourEast.setEdgeWest(edge);
        }
    }

    public void setEdgeWest(Node neighbourWest) {
        if (this.edgeWest == null) {
            double distance = Metrics.distance(this.color, neighbourWest.color);
            Edge edge = new Edge(this, neighbourWest, true, distance);
            this.edgeWest = edge;
            neighbourWest.setEdgeEast(edge);
        }
    }

    public void setEdgeNorth(Node neighbourNorth) {
        if (this.edgeNorth == null) {
            double distance = Metrics.distance(this.color, neighbourNorth.color);
            Edge edge = new Edge(this, neighbourNorth, false, distance);
            this.edgeNorth = edge;
            neighbourNorth.setEdgeSouth(edge);
        }
    }

    public void setEdgeSouth(Node neighbourSouth) {
        if (this.edgeSouth == null) {
            double distance = Metrics.distance(this.color, neighbourSouth.color);
            Edge edge = new Edge(this, neighbourSouth, false, distance);
            this.edgeSouth = edge;
            neighbourSouth.setEdgeNorth(edge);
        }
    }


    public Node getClone() {
        return new Node(this.id, this.edgeEast, this.edgeWest, this.edgeNorth, this.edgeSouth, this.color);
    }


    public void resetTreeValues() {
        this.key = 1000000;
        this.parent = null;
        this.direction = '0';
    }


    @Override
    public String toString() {
        return "" + this.id;
    }

    @Override
    public int compareTo(Node o) {
        return Double.compare(key, o.getKey());
        // (int) Math.round(key - o.getKey());
    }
}
