package EA.Operations;

import EA.Components.Individual;
import EA.Utils;

import java.util.*;


/*
Mutation operator
 */
public class Mutation {
    public static Individual applySingleBitMutation(Individual individual) {
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
