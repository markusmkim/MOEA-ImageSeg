package Graph;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Node {
    // A node in a rectangular grid. All nodes have 2 - 4 neighours (North, South, West, East)
    private String id;

    private Edge edgeEast;
    private Edge edgeWest;
    private Edge edgeNorth;
    private Edge edgeSouth;

    private int direction = 0;

    public Node(String id) {
        this.id = id;
    }


    // Getters
    public Edge getEdgeEast()    { return edgeEast; }
    public Edge getEdgeWest()    { return edgeWest; }
    public Edge getEdgeNorth()   { return edgeNorth; }
    public Edge getEdgeSouth()   { return edgeSouth; }
    public List<Edge> getEdges() { return new ArrayList<>(Arrays.asList(edgeEast, edgeWest, edgeNorth, edgeSouth)); }
    public int getDirection()    { return direction; }
    public String getId()        { return id; }


    // Setters
    private void setEdgeEast(Edge edgeEast)   { this.edgeEast = edgeEast; }
    private void setEdgeWest(Edge edgeWest)   { this.edgeWest = edgeWest; }
    private void setEdgeNorth(Edge edgeNorth) { this.edgeNorth = edgeNorth; }
    private void setEdgeSouth(Edge edgeSouth) { this.edgeSouth = edgeSouth; }


    public void setEdgeEast(Node neighbourEast) {
        if (this.edgeEast == null) {
            Edge edge = new Edge(this, neighbourEast);
            this.edgeEast = edge;
            neighbourEast.setEdgeWest(edge);
        }
    }

    public void setEdgeWest(Node neighbourWest) {
        if (this.edgeWest == null) {
            Edge edge = new Edge(this, neighbourWest);
            this.edgeWest = edge;
            neighbourWest.setEdgeEast(edge);
        }
    }

    public void setEdgeNorth(Node neighbourNorth) {
        if (this.edgeNorth == null) {
            Edge edge = new Edge(this, neighbourNorth);
            this.edgeNorth = edge;
            neighbourNorth.setEdgeSouth(edge);
        }
    }

    public void setEdgeSouth(Node neighbourSouth) {
        if (this.edgeSouth == null) {
            Edge edge = new Edge(this, neighbourSouth);
            this.edgeSouth = edge;
            neighbourSouth.setEdgeNorth(edge);
        }
    }

    public void setDirection(int direction) {
        if (this.direction == 0) {
            this.direction = direction;
        }
    }

}
