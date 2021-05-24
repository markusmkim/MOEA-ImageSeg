package ea.operations;

import ea.components.Individual;

import java.util.List;
import java.util.Random;


/*
Parent selection
 */
public class Selection {

    /**
     * Selects two random individuals to be recombined as parents
     * @param population individuals to pick from
     * @return chosen parents
     */
    public static Individual[] selectRandomPair(List<Individual> population) {
        Random random = new Random();
        Individual p1 = population.get(random.nextInt(population.size()));
        Individual p2 = population.get(random.nextInt(population.size()));
        return new Individual[]{p1, p2};
    }


    /**
     * Selects two individuals as parents by tournament selection.
     * Tournament selection (with tournament size = 2):
     * - Pick two individuals at random
     * - If one dominates the other, choose the dominator
     * - Else, choose randomly. Now you have one parent.
     * - Repeat procedure for next parent
     * @param population individuals to pick from
     * @return chosen parents
     */
    public static Individual[] tournamentSelection(List<Individual> population) {
        Individual[] winners = new Individual[2];
        for (int i = 0; i < 2; i++) {
            Individual[] competitors = Selection.selectRandomPair(population);
            if (competitors[0].getRank() == 0 || competitors[1].getRank() == 0) {
                winners[i] = competitors[0];
            }
            else {
                if (competitors[0].getRank() < competitors[1].getRank()) {
                    winners[i] = competitors[0];
                }
                else {
                    winners[i] = competitors[1];
                }
            }
        }
        return winners;
    }
}
