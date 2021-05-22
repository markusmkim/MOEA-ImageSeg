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

    /**
     * Initializes a population of individuals.
     * To initialize an individual:
     * - Start with a grid-shaped graph (vertical and horizontal edges) where each node corresponds
     *   a pixel in the image. The distances of each edge is proportional to the Euclidian distance in RGB-space
     *   between the neighbouring pixels (nodes).
     * - Run Prims's algorithm from a random starting node to form a minimum spanning tree
     * - Convert minimum spanning tree to string representation - this is the genotype
     * - Initialize individual
     * @param populationSize number of individuals in population
     * @param height image height (number of pixels)
     * @param width image width (number of pixels)
     * @param pixelReader PixelReader object for image
     * @return population
     */
    public static List<Individual> init(Integer populationSize, int height, int width, PixelReader pixelReader) {

        // Build graph //
        List<Node> nodes = Builder.buildGrid(height, width, pixelReader);

        // Generate population //
        List<Individual> population = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < populationSize; i++) {
            if (i > 0) {
                nodes.forEach(Node::resetTreeValues);
            }

            int MSTstartingIndex = random.nextInt(nodes.size());
            MST.PrimsAlgorithm(nodes, MSTstartingIndex);

            StringBuilder g = new StringBuilder();
            for (Node n : nodes) {
                g.append(n.getDirection());
            }
            String genotype = g.toString();
            Individual individual = new Individual(genotype, height, width, pixelReader);
            population.add(individual);
        }
        return population;
    }
}
