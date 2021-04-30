package EA.Operations;

import EA.Components.Individual;
import javafx.scene.image.PixelReader;

import java.util.Random;

/*
Recombination operator
 */
public class Crossover {
    private final double crossoverRate;


    public Crossover(double crossoverRate) {
        this.crossoverRate = crossoverRate;
    }


    public Individual[] apply(Individual p1, Individual p2) {
        if (Math.random() < 0.2) {
            return this.applyTwoPointCrossover(p1, p2);
        }
        return this.applyUniformCrossover(p1, p2);
    }


    public Individual[] applyUniformCrossover(Individual p1, Individual p2) {
        if (Math.random() > this.crossoverRate) {
            return new Individual[]{p1, p2};
        }

        StringBuilder child1Genotype = new StringBuilder();
        StringBuilder child2Genotype  = new StringBuilder();

        String parent1Genotype = p1.getGenotype();
        String parent2Genotype = p2.getGenotype();

        /*
        For every gene (character in genotype) is selected randomly from one of the
        corresponding genes of the parent genotypes:
         */
        for (int i = 0; i < parent1Genotype.length(); i++ ) {
            if (Math.random() < 0.95) {
                child1Genotype.append(parent1Genotype.charAt(i));
            }
            else {
                child1Genotype.append(parent2Genotype.charAt(i));
            }

            if (Math.random() < 0.05) {
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


    public Individual[] applyTwoPointCrossover(Individual p1, Individual p2) {
        if (Math.random() > this.crossoverRate) {
            return new Individual[]{p1, p2};
        }

        String parent1Genotype = p1.getGenotype();
        String parent2Genotype = p2.getGenotype();

        Random random = new Random();
        int firstCrossoverIndex = random.nextInt(parent1Genotype.length() - 1);
        int secondCrossoverIndex = random.nextInt(parent1Genotype.length() - firstCrossoverIndex) + firstCrossoverIndex;

        String child1Genotype = parent1Genotype.substring(0, firstCrossoverIndex) +
                parent2Genotype.substring(firstCrossoverIndex, secondCrossoverIndex) +
                parent1Genotype.substring(secondCrossoverIndex);

        String child2Genotype = parent2Genotype.substring(0, firstCrossoverIndex) +
                parent1Genotype.substring(firstCrossoverIndex, secondCrossoverIndex) +
                parent2Genotype.substring(secondCrossoverIndex);

        int height = p1.getHeight();
        int width = p1.getWidth();
        PixelReader pixelReader = p1.getPixelReader();

        Individual child1 = new Individual(child1Genotype, height, width, pixelReader);
        Individual child2 = new Individual(child2Genotype, height, width, pixelReader);
        return new Individual[]{child1, child2};
    }


}
