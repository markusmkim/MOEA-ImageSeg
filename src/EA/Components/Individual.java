package EA.Components;


import Graph.Edge;
import Graph.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
        this.PrimMST(nodes);
        List<Edge> edges = new ArrayList<>();
        for (Node n : nodes) {
            for (Edge e : n.getEdges()) {
                if (! edges.contains(e)) {
                    edges.add(e);
                }
            }
        }
        for (Edge e: edges) {
            System.out.println("" + e +" - " + e.getDistance());
        }
        System.out.println(" ");
        for (Node n : nodes) {
            System.out.println("Node " + n + " has direction: " + n.getDirection());
        }
    }

    public void PrimMST(List<Node> nodesOriginalOrdering) {
        List<Node> nodes = new ArrayList<>(nodesOriginalOrdering);
        List<Edge> edges = new ArrayList<>();
        for (Node n : nodes) {
            for (Edge e : n.getEdges()) {
                if (! edges.contains(e)) {
                    edges.add(e);
                }
            }
        }
        Node treeFrom = nodes.get(5);
        nodes.remove(5);
        nodes.add(0, treeFrom);
        System.out.println("Tree from node " + treeFrom);
        treeFrom.setKey(0);
        while (nodes.size() > 0) {
            Node u = nodes.get(0);
            nodes.remove(0);
            for (Edge e : u.getEdges()) {
                Node v = e.traverse(u);
                if (nodes.contains(v) && e.getDistance() < v.getKey()) {
                    v.setParent(u);
                    v.setKey(e.getDistance());
                    char direction = e.getTraverseDirection(v);
                    v.setDirection(direction);
                }
            }
            Collections.sort(nodes);  // Denne kan muligens bruke litt tid --> se på binary heaps
        }

    }
}
