package EA.Components.Comparators;

import EA.Components.Individual;

import java.math.BigDecimal;
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
        if (this.isFeasibleSegmentations(o1) && !this.isFeasibleSegmentations(o2)) {
            return -1;
        }
        if (!this.isFeasibleSegmentations(o1) && this.isFeasibleSegmentations(o2)) {
            return 1;
        }

        /*
        int o1EdgeValue = (int) Math.round(o1.getEdgeValue());
        int o1Connectivity = (int) Math.round(o1.getConnectivity());
        int o1Deviation = (int) Math.round(o1.getDeviation());

        int o2EdgeValue = (int) Math.round(o2.getEdgeValue());
        int o2Connectivity = (int) Math.round(o2.getConnectivity());
        int o2Deviation = (int) Math.round(o2.getDeviation());

        if (o1EdgeValue > o2EdgeValue && o1Connectivity <= o2Connectivity && o1Deviation <= o2Deviation) {
            // hvis o1 dominerer o2, return -1
            return -1;
        }
        if (o1EdgeValue >= o2EdgeValue && o1Connectivity < o2Connectivity && o1Deviation <= o2Deviation) {
            return -1;
        }
        if (o1EdgeValue >= o2EdgeValue && o1Connectivity <= o2Connectivity && o1Deviation < o2Deviation) {
            return -1;
        }

        if (o1EdgeValue < o2EdgeValue && o1Connectivity >= o2Connectivity && o1Deviation >= o2Deviation) {
            return 1;
        }
        if (o1EdgeValue <= o2EdgeValue && o1Connectivity > o2Connectivity && o1Deviation >= o2Deviation) {
            return 1;
        }
        if (o1EdgeValue <= o2EdgeValue && o1Connectivity >= o2Connectivity && o1Deviation > o2Deviation) {
            return 1;
        }



        BigDecimal o1EdgeValue = BigDecimal.valueOf(o1.getEdgeValue());
        BigDecimal o1Connectivity = BigDecimal.valueOf(o1.getConnectivity());
        BigDecimal o1Deviation = BigDecimal.valueOf(o1.getDeviation());

        BigDecimal o2EdgeValue = BigDecimal.valueOf(o2.getEdgeValue());
        BigDecimal o2Connectivity = BigDecimal.valueOf(o2.getConnectivity());
        BigDecimal o2Deviation = BigDecimal.valueOf(o2.getDeviation());

        if (o1EdgeValue.compareTo(o2EdgeValue) > 0 && o1Connectivity.compareTo(o2Connectivity) <= 0 &&  o1Deviation.compareTo(o2Deviation) <= 0) {
            // hvis o1 dominerer o2, return -1
            return -1;
        }
        if (o1EdgeValue.compareTo(o2EdgeValue) >= 0 && o1Connectivity.compareTo(o2Connectivity) < 0 &&  o1Deviation.compareTo(o2Deviation) <= 0) {
            return -1;
        }
        if (o1EdgeValue.compareTo(o2EdgeValue) >= 0 && o1Connectivity.compareTo(o2Connectivity) <= 0 &&  o1Deviation.compareTo(o2Deviation) < 0) {
            return -1;
        }

        if (o2EdgeValue.compareTo(o1EdgeValue) > 0 && o2Connectivity.compareTo(o1Connectivity) <= 0 &&  o2Deviation.compareTo(o1Deviation) <= 0) {
            return 1;
        }
        if (o2EdgeValue.compareTo(o1EdgeValue) >= 0 && o2Connectivity.compareTo(o1Connectivity) < 0 &&  o2Deviation.compareTo(o1Deviation) <= 0) {
            return 1;
        }
        if (o2EdgeValue.compareTo(o1EdgeValue) >= 0 && o2Connectivity.compareTo(o1Connectivity) <= 0 &&  o2Deviation.compareTo(o1Deviation) < 0) {
            return 1;
        }

         */


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
