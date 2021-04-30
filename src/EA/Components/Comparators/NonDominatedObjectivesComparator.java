package EA.Components.Comparators;

import EA.Components.Individual;

import java.util.Comparator;

public class NonDominatedObjectivesComparator implements Comparator<Individual> {
    private final int minSeg;
    private final int maxSeg;

    public NonDominatedObjectivesComparator(int minSeg, int maxSeg) {
        this.minSeg = minSeg;
        this.maxSeg = maxSeg;
    }

    @Override
    public int compare(Individual o1, Individual o2) {
        if (o1.getEdgeValue() < 0 || o2.getEdgeValue() < 0) {
            System.out.println("Edgevalue negative");
        }
        if (o1.getConnectivity() < 0 || o2.getConnectivity() < 0) {
            System.out.println("Connectivity negative");
        }
        if (o1.getDeviation() < 0 || o2.getDeviation() < 0) {
            System.out.println("Deviation negative");
        }


        if (this.isFeasibleSegmentations(o1) && !this.isFeasibleSegmentations(o2)) {
            return -1;
        }
        if (!this.isFeasibleSegmentations(o1) && this.isFeasibleSegmentations(o2)) {
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


    private boolean isFeasibleSegmentations(Individual individual) {
        int segmentations = individual.getSegmentsRGBCentroids().size();
        if (segmentations < this.minSeg) {
            return false;
        }
        return segmentations <= this.maxSeg;
    }
}


/*
old

public int compare(Individual o1, Individual o2) {
        if (o1.getEdgeValue() > o2.getEdgeValue() && o1.getConnectivity() < o2.getConnectivity() && o1.getDeviation() < o2.getDeviation()) {
            return -1;
        }
        if (o1.getEdgeValue() < o2.getEdgeValue() && o1.getConnectivity() > o2.getConnectivity() && o1.getDeviation() > o2.getDeviation()) {
            return 1;
        }
        return 0;
    }
 */
