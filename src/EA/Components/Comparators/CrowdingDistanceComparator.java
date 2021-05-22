package EA.Components.Comparators;

import EA.Components.Individual;
import java.util.Comparator;


public class CrowdingDistanceComparator implements Comparator<Individual> {
    @Override
    public int compare(Individual o1, Individual o2) {
        return Double.compare(o2.getCrowdingDistance(), o1.getCrowdingDistance());
    }
}
