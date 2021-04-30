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

    public static Individual[] tournamentSelection(List<Individual> population) {
        Individual[] winners = new Individual[2];
        for (int i = 0; i < 2; i++) {
            Individual[] competitors = Selection.selectRandomPair(population);
            if (competitors[0].getRank() == 0 || competitors[1].getRank() == 0) {
                System.out.println("Individual without rank");
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


    /*
    Tournament selection

    public static Individual[] selectRandomPairByTournament(List<Individual> population) {
        Random random = new Random();

    }
    */
}
