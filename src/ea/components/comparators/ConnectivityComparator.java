package ea.components.comparators;

import ea.components.Individual;
import java.util.Comparator;


public class ConnectivityComparator implements Comparator<Individual> {
    @Override
    public int compare(Individual o1, Individual o2) {
        return Double.compare(o1.getConnectivity(), o2.getConnectivity());
    }
}
