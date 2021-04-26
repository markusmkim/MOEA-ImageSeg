package EA.Operations;

import EA.Components.Individual;

import java.util.List;
import java.util.Random;

/*
Parent selection
 */
public class Selection {

    public static Individual[] selectRandomPair(List<Individual> population) {
        Random random = new Random();
        Individual p1 = population.get(random.nextInt(population.size()));
        Individual p2 = population.get(random.nextInt(population.size()));
        return new Individual[]{p1, p2};
    }


    /*
    Tournament selection

    public static Individual[] selectRandomPairByTournament(List<Individual> population) {
        Random random = new Random();

    }
    */
}
