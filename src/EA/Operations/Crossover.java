package EA.Operations;

import EA.Components.Individual;
import javafx.scene.image.PixelReader;

/*
Recombination operator
 */
public class Crossover {
    public static Individual[] applyUniformCrossover(Individual p1, Individual p2) {
        StringBuilder child1Genotype = new StringBuilder();
        StringBuilder child2Genotype  = new StringBuilder();

        String parent1Genotype = p1.getGenotype();
        String parent2Genotype = p2.getGenotype();

        /*
        For every gene (character in genotype) is selected randomly from one of the
        corresponding genes of the parent genotypes:
         */
        for (int i = 0; i < parent1Genotype.length(); i++ ) {
            if (Math.random() < 0.5) {
                child1Genotype.append(parent1Genotype.charAt(i));
            }
            else {
                child1Genotype.append(parent2Genotype.charAt(i));
            }

            if (Math.random() < 0.5) {
                child2Genotype.append(parent1Genotype.charAt(i));
            }
            else {
                child2Genotype.append(parent2Genotype.charAt(i));
            }
        }

        int height = p1.getHeight();
        int width = p1.getWidth();
        PixelReader pixelReader = p1.getPixelReader();

        Individual child1 = new Individual(child1Genotype.toString(), height, width, pixelReader);
        Individual child2 = new Individual(child2Genotype.toString(), height, width, pixelReader);
        return new Individual[]{child1, child2};
    }
}
