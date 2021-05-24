package ea.components.comparators;

import ea.components.Individual;

import java.util.Comparator;


public class NonDominatedObjectivesComparator implements Comparator<Individual> {
    private final int minSeg;   // minimum number of segmentations for a feasible solution
    private final int maxSeg;   // maximum number of segmentations for a feasible solution

    public NonDominatedObjectivesComparator(int minSeg, int maxSeg) {
        this.minSeg = minSeg;
        this.maxSeg = maxSeg;
    }

    @Override
    /*
    If one individual is feasible (minSeg <= segmentations <= maxSeg) and the other is not,
    the feasible individual dominates the other.

    Else, an individual A dominates another individual B if both the following conditions are true
    - A is at least as good as B on all objective values
    - A is strictly better than B on one or more objective values
     */
    public int compare(Individual o1, Individual o2) {
        if (o1.isFeasible(this.minSeg, this.maxSeg) && !o2.isFeasible(this.minSeg, this.maxSeg)) {
            return -1;
        }
        if (!o1.isFeasible(this.minSeg, this.maxSeg) && o2.isFeasible(this.minSeg, this.maxSeg)) {
            return 1;
        }


        if (o1.getEdgeValue() > o2.getEdgeValue() && o1.getConnectivity() <= o2.getConnectivity() && o1.getDeviation() <= o2.getDeviation()) {
            // hvis o1 dominerer o2, return -1
            return -1;
        }
        if (o1.getEdgeValue() >= o2.getEdgeValue() && o1.getConnectivity() < o2.getConnectivity() && o1.getDeviation() <= o2.getDeviation()) {
            return -1;
        }
        if (o1.getEdgeValue() >= o2.getEdgeValue() && o1.getConnectivity() <= o2.getConnectivity() && o1.getDeviation() < o2.getDeviation()) {
            return -1;
        }

        if (o1.getEdgeValue() < o2.getEdgeValue() && o1.getConnectivity() >= o2.getConnectivity() && o1.getDeviation() >= o2.getDeviation()) {
            return 1;
        }
        if (o1.getEdgeValue() <= o2.getEdgeValue() && o1.getConnectivity() > o2.getConnectivity() && o1.getDeviation() >= o2.getDeviation()) {
            return 1;
        }
        if (o1.getEdgeValue() <= o2.getEdgeValue() && o1.getConnectivity() >= o2.getConnectivity() && o1.getDeviation() > o2.getDeviation()) {
            return 1;
        }

        return 0;
    }
}
