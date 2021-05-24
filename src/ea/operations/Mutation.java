package ea.operations;

import ea.components.Individual;
import ea.Utils;

import java.util.*;


/*
Mutation operator
 */
public class Mutation {
    private final double mutationRate;

    public Mutation(double mutationRate) {
        this.mutationRate = mutationRate;
    }


    /**
     * Apply mutation to single individual.
     * Mutation rate determines how often mutation is actually applied.
     * Mutation: Switch a single character to a different random, feasible character.
     * @param individual the individual to mutate
     * @return mutated individual
     */
    public Individual applySingleBitMutation(Individual individual) {
        if (Math.random() > this.mutationRate) {
            return individual;
        }


        StringBuilder mutableGenotype = new StringBuilder(individual.getGenotype());

        int height = individual.getHeight();
        int width = individual.getWidth();

        /*
        Select a random gene and set to a random feasible value:
         */
        Random random = new Random();
        int randomIndex = random.nextInt(mutableGenotype.length());

        int[] coordinates = Utils.convertIndexToCoordinates(randomIndex, width);
        int col = coordinates[0];
        int row = coordinates[1];

        List<Character> feasibleGenes = new ArrayList<>(Collections.singletonList('0'));
        if (row > 0) {
            feasibleGenes.add('N');
        }
        if (row < height - 1) {
            feasibleGenes.add('S');
        }
        if (col > 0) {
            feasibleGenes.add('W');
        }
        if (col < width - 1) {
            feasibleGenes.add('E');
        }

        char oldGene = mutableGenotype.charAt(randomIndex);
        feasibleGenes.remove(Character.valueOf(oldGene));

        char chosenGene = feasibleGenes.get(random.nextInt(feasibleGenes.size()));

        mutableGenotype.setCharAt(randomIndex, chosenGene);

        return new Individual(mutableGenotype.toString(), height, width, individual.getPixelReader());
    }
}
