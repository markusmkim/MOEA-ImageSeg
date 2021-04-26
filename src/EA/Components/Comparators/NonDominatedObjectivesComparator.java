package EA.Components.Comparators;

import EA.Components.Individual;

import java.util.Comparator;

public class NonDominatedObjectivesComparator implements Comparator<Individual> {
    @Override
    public int compare(Individual o1, Individual o2) {
        if (o1.getEdgeValue() > o2.getEdgeValue() && o1.getConnectivity() < o2.getConnectivity() && o1.getDeviation() < o2.getDeviation()) {
            // hvis o1 dominerer o2, return -1
            return -1;
        }
        if (o1.getEdgeValue() < o2.getEdgeValue() && o1.getConnectivity() > o2.getConnectivity() && o1.getDeviation() > o2.getDeviation()) {
            return 1;
        }
        return 0;
    }
}
