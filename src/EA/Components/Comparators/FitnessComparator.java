package EA.Components.Comparators;

import EA.Components.Individual;

import java.util.Comparator;


public class FitnessComparator implements Comparator<Individual> {
    @Override
    public int compare(Individual o1, Individual o2) {
        return Double.compare(o2.getFitness(), o1.getFitness());
    }
}
