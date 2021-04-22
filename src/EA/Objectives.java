package EA;

import EA.Components.Individual;
import javafx.scene.paint.Color;

public class Objectives {
    public static void evaluateIndividual(Individual individual) {
        double edgeValue = 0;
        double connectivity = 0;
        double deviation = 0;

        for (int index = 0; index < individual.getGenotype().length(); index++) {
            int indexSegment = individual.getPixelSegmentMappings().get(index);
            int[] indexCoordinates = Utils.convertIndexToCoordinates(index, individual.getWidth());
            Color indexColor = individual.getPixelReader().getColor(indexCoordinates[0], indexCoordinates[1]);

            int[] neighbourIndexes = Utils.getNeighbourIndexes(index, individual.getHeight(), individual.getWidth(), true);

            for (int j = 0; j < neighbourIndexes.length; j++) {
                int neighbourIndex = neighbourIndexes[j];

                if (neighbourIndex > -1) {
                    int neighbourIndexSegment = individual.getPixelSegmentMappings().get(neighbourIndex);
                    if (indexSegment != neighbourIndexSegment) {

                        if (j < 4) {
                            int[] neighbourIndexCoordinates = Utils.convertIndexToCoordinates(neighbourIndex, individual.getWidth());
                            Color neighbourIndexColor = individual.getPixelReader().getColor(neighbourIndexCoordinates[0], neighbourIndexCoordinates[1]);

                            edgeValue += Metrics.distance(indexColor, neighbourIndexColor);
                        }

                        connectivity += 1.0 / 8.0;

                    }
                }
            }

            deviation += Metrics.distance(indexColor, individual.getSegmentsRGBCentroids().get(indexSegment));
        }


        individual.setObjectives(edgeValue, connectivity, deviation);
    }
}
