package EA.Components.Comparators;

import EA.Components.Individual;
import java.util.Comparator;


public class FitnessComparator implements Comparator<Individual> {
    private final int minSeg;   // minimum number of segmentations for a feasible solution
    private final int maxSeg;   // maximum number of segmentations for a feasible solution

    public FitnessComparator(int minSeg, int maxSeg) {
        this.minSeg = minSeg;
        this.maxSeg = maxSeg;
    }


    @Override
     /*
    If one individual is feasible (minSeg <= segmentations <= maxSeg) and the other is not,
    prefer the feasible one.
    Else, prefer the one with best fitness
      */
    public int compare(Individual o1, Individual o2) {
        if (o1.isFeasible(this.minSeg, this.maxSeg) && !o2.isFeasible(this.minSeg, this.maxSeg)) {
            return -1;
        }
        if (!o1.isFeasible(this.minSeg, this.maxSeg) && o2.isFeasible(this.minSeg, this.maxSeg)) {
            return 1;
        }
        return Double.compare(o2.getFitness(), o1.getFitness());
    }
}
