package EA.Operations;

import EA.Components.Individual;
import Graph.Builder;
import Graph.MST;
import Graph.Node;
import javafx.scene.image.PixelReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/*
Population initializer
 */
public class Initializer {
    public static List<Individual> init(Integer populationSize, int height, int width, PixelReader pixelReader) {

        // Build graph
        List<Node> nodes = Builder.buildGrid(height, width, pixelReader);

        // Generate population
        List<Individual> population = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < populationSize; i++) {
            if (i > 0) {
                nodes.forEach(Node::resetTreeValues);
            }

            int MSTstartingIndex = random.nextInt(nodes.size());
            MST.PrimsAlgorithm(nodes, MSTstartingIndex);

            StringBuilder g = new StringBuilder();
            // System.out.println(" ");
            for (Node n : nodes) {
                // System.out.println("Node " + n + " has direction: " + n.getDirection());
                g.append(n.getDirection());
            }
            String genotype = g.toString();
            // System.out.println(genotype);
            Individual individual = new Individual(genotype, height, width, pixelReader);
            population.add(individual);
        }
        return population;
    }
}
