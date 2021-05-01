package EA.Operations;

import EA.Components.Comparators.NonDominatedObjectivesComparator;
import EA.Components.Individual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NonDominatedRanking {
    private final NonDominatedObjectivesComparator comparator;

    public NonDominatedRanking(NonDominatedObjectivesComparator comparator) {
        this.comparator = comparator;
    }


    public List<List<Individual>> sort(List<Individual> population) {
        List<Individual> queue = new ArrayList<>(population);
        List<List<Individual>> fronts = new ArrayList<>();

        while (queue.size() > 0) {
            Individual next = queue.remove(0);
            int frontIndex = 0;
            boolean iterateFronts = fronts.size() > 0;
            while (iterateFronts) {
                boolean isDominated = this.isDominated(next, fronts.get(frontIndex));
                if (isDominated) {
                    frontIndex += 1;
                }
                iterateFronts = isDominated && frontIndex < fronts.size();
            }

            if (fronts.size() > frontIndex) {
                List<Individual> homeFront = fronts.get(frontIndex);
                List<Individual> dominatedInHomeFront = dominates(next, homeFront);
                for (Individual dominated : dominatedInHomeFront) {
                    //System.out.println("\n\nRemoving ");
                    //dominated.printObjectiveValues();
                    //System.out.println("For ");
                    //next.printObjectiveValues();
                    homeFront.remove(dominated);
                    queue.add(dominated);
                }
                homeFront.add(next);
            }
            else {
                fronts.add(new ArrayList<>(Collections.singletonList(next)));
            }
        }

        // Assign ranks
        int rankCounter = 1;
        for (List<Individual> front : fronts) {
            for (Individual individual : front) {
                individual.setRank(rankCounter);
            }
            rankCounter ++;
        }

        return fronts;
    }

    /*
    Returns true if individual is dominated by at least one of the individuals in front
     */
    private boolean isDominated(Individual individual, List<Individual> front) {
        for (Individual dominator : front) {
            if (this.comparator.compare(dominator, individual) < 0) {
                return true;
            }
        }
        return false;
    }


    /*
   Returns the individuals in front that is dominated by dominator
    */
    private List<Individual> dominates(Individual dominator, List<Individual> front) {
        List<Individual> dominated = new ArrayList<>();
        for (Individual individual : front) {
            if (this.comparator.compare(dominator, individual) < 0) {
                dominated.add(individual);
            }
        }
        return dominated;
    }
}
