package graph;

import java.util.*;


public class MST {
    /*
     * Constructs a minimal spanning tree (MST) by using Prims algorithm with a MinHeap queue
     * @param nodesOriginalOrdering the nodes to construct MST from
     * @param startingIndex the index of the node to construct the MST from (root node)
     */
    public static void PrimsAlgorithm(List<Node> nodesOriginalOrdering, int startingIndex) {
        List<Node> nodes = new ArrayList<>();
        Map<String, Boolean> nodesVisited = new HashMap<>();
        for (Node node : nodesOriginalOrdering) {
            nodes.add(node);
            nodesVisited.put(node.getId(), false);
        }

        Node treeFrom = nodes.get(startingIndex);
        nodes.remove(startingIndex);
        nodes.add(0, treeFrom);
        treeFrom.setKey(0);

        PriorityQueue<Node> minHeap = new PriorityQueue<>(nodes);

        while (minHeap.size() > 0) {
            Node u = minHeap.poll();
            nodesVisited.put(u.getId(), true);
            for (Edge e : u.getEdges()) {
                Node v = e.traverse(u);
                if (!nodesVisited.get(v.getId()) && e.getDistance() < v.getKey()) {
                    minHeap.remove(v);
                    v.setParent(u);
                    v.setKey(e.getDistance());
                    char direction = e.getTraverseDirection(v);
                    v.setDirection(direction);
                    minHeap.add(v);
                }
            }
        }
    }
}
