package EA.Components;

import EA.Utils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.*;


public class Individual {
    private final String genotype;
    private final int height;
    private final int width;

    private final PixelReader pixelReader;  // pixelReader for Image objective to segmentation

    private Map<Integer, Color> segmentsRGBCentroids;
    private Map<Integer, Integer> pixelSegmentMappings;

    // Objective values ("fitness values" for MOEA)
    private double edgeValue;
    private double connectivity;
    private double deviation;
    private boolean isEvaluated = false;

    // Used for non-dominated sorting by MOEA
    private int rank;
    private double crowdingDistance;

    // Fitness GA
    private double fitness; // Fitness value = weighted average of objective values


    public Individual(String genotype, int height, int width, PixelReader pixelReader) {
        this.genotype = genotype;
        this.height = height;
        this.width = width;
        this.pixelReader = pixelReader;
    }


    // Getters
    public String getGenotype()                                 { return genotype; }
    public int getHeight()                                      { return height; }
    public int getWidth()                                       { return width; }
    public PixelReader getPixelReader()                         { return this.pixelReader; }
    public Map<Integer, Integer> getPixelSegmentMappings()      { return pixelSegmentMappings; }
    public Map<Integer, Color> getSegmentsRGBCentroids()        { return segmentsRGBCentroids; }
    public double getEdgeValue()                                { return edgeValue; }
    public double getConnectivity()                             { return connectivity; }
    public double getDeviation()                                { return deviation; }
    public boolean isEvaluated()                                { return isEvaluated; }
    public double getCrowdingDistance()                         { return crowdingDistance; }
    public int getRank()                                        { return rank; }
    public double getFitness()                                  { return fitness; }


    // Setters
    public void setObjectives(double edgeValue, double connectivity, double deviation) {
        this.edgeValue = edgeValue;
        this.connectivity = connectivity;
        this.deviation = deviation;
        this.isEvaluated = true;
    }

    public void setCrowdingDistance(double crowdingDistance) { this.crowdingDistance = crowdingDistance; }
    public void setRank(int rank)                            { this.rank = rank; }

    public void incrementCrowdingDistance(double increment) {
        if (this.crowdingDistance == 1000000000) {
            // do nothing
            return;
        }
        this.crowdingDistance += increment;
    }


    public void calculateFitness(double[] weights) {
        // Fitness should be maximized
        this.fitness = 4000 + (weights[0] * edgeValue - weights[1] * connectivity - weights[2] * deviation);
    }


    public Image[] constructPhenotype() {
        WritableImage type1 = new WritableImage(this.pixelReader, this.width, this.height);
        PixelWriter pixelWriterType1 = type1.getPixelWriter();

        WritableImage type2 = new WritableImage(this.width, this.height);
        PixelWriter pixelWriterType2 = type2.getPixelWriter();

        for (int i = 0; i < this.genotype.length(); i++) {
            int[] c = Utils.convertIndexToCoordinates(i, this.width);
            pixelWriterType2.setColor(c[0], c[1], Color.WHITE);
        }

        for (int i = 0; i < this.genotype.length(); i++) {
            int[] coordinates = Utils.convertIndexToCoordinates(i, this.width);
            int[] neighbourIndexes = Utils.getNeighbourIndexes(i, this.height, this.width, false);
            for (int j = 0; j < neighbourIndexes.length; j++) {
                // System.out.println(neighbourIndexes[j]);
                if (neighbourIndexes[j] == -1 || !this.pixelSegmentMappings.get(i).equals(this.pixelSegmentMappings.get(neighbourIndexes[j]))) {
                    pixelWriterType1.setColor(coordinates[0], coordinates[1], Color.LIME);
                    pixelWriterType2.setColor(coordinates[0], coordinates[1], Color.BLACK);

                    /*
                    int oppositeIndex;
                    if (j < 2) {
                        oppositeIndex = (j + 1) % 2;
                    }
                    else {
                        oppositeIndex = ((j - 1) % 2) + 2;
                    }
                    int[] c = Utils.convertIndexToCoordinates(neighbourIndexes[oppositeIndex], this.width);
                    if (0 <= c[0] && c[0] < this.width && 0 <= c[1] && c[1] < this.height) {
                        pixelWriterType1.setColor(c[0], c[1], Color.PINK);
                        pixelWriterType2.setColor(c[0], c[1], Color.BLACK);
                    }
                     */
                }
            }
        }
        return new Image[]{type1, type2};
    }


    public void computeSegments() {
        Map<Integer, Map<String, Double>> segmentsRGBSums = new HashMap<>();
        Map<Integer, Integer> pixelSegmentMappings = new HashMap<>();
        int pixelSegmentKeyCounter = 0;
        List<Integer> indexesToProcess = new ArrayList<>();
        for (int i = 0; i < this.genotype.length(); i++) {
            indexesToProcess.add(i);
        }
        while (indexesToProcess.size() > 0) {
            List<Integer> processedIndexes = new ArrayList<>();
            int index = indexesToProcess.get(0);

            while (this.genotype.charAt(index) != '0' && !pixelSegmentMappings.containsKey(index) && !processedIndexes.contains(index)) {
                processedIndexes.add(index);
                char direction = this.genotype.charAt(index);
                if (direction == 'W') {
                    index -= 1;
                }
                else if (direction == 'E') {
                    index += 1;
                }
                else if (direction == 'N') {
                    index -= width;
                }
                else if (direction == 'S') {
                    index += width;
                }
            }
            if (!pixelSegmentMappings.containsKey(index)){
                indexesToProcess.remove(Integer.valueOf(index));
            }
            // processedIndexes.add(index);
            int segmentKey;
            Map<String, Double> rgbSums;
            if (pixelSegmentMappings.containsKey(index)) {
                segmentKey = pixelSegmentMappings.get(index);
                rgbSums = segmentsRGBSums.get(segmentKey);
            }
            else {
                segmentKey = pixelSegmentKeyCounter++;
                pixelSegmentMappings.put(index, segmentKey);
                Color pixelColor = this.getPixelColor(index);
                rgbSums = new HashMap<>();
                this.updateRgbSums(rgbSums, pixelColor);
            }
            for (Integer processedIndex : processedIndexes) {
                pixelSegmentMappings.put(processedIndex, segmentKey);
                indexesToProcess.remove(Integer.valueOf(processedIndex));
                Color pixelColor = this.getPixelColor(processedIndex);
                this.updateRgbSums(rgbSums, pixelColor);
            }
            segmentsRGBSums.put(segmentKey, rgbSums);
        }
        // System.out.println("Segment key count: " + pixelSegmentKeyCounter);
        // pixelSegmentKeys.forEach((key, value) -> System.out.println(key + " " + value));
        // segmentsRGBSums.forEach((key, value) -> System.out.println(key + " " + value));
        this.pixelSegmentMappings = pixelSegmentMappings;

        Map<Integer, Color> segmentsRGBCentroids = new HashMap<>();
        for (Map.Entry<Integer, Map<String, Double>> entry : segmentsRGBSums.entrySet()) {
            int segmentKey = entry.getKey();
            Map<String, Double> rgbSums = entry.getValue();
            double redCentroid = rgbSums.get("red") / rgbSums.get("count");
            double greenCentroid = rgbSums.get("green") / rgbSums.get("count");
            double blueCentroid = rgbSums.get("blue") / rgbSums.get("count");
            Color c = Color.color(redCentroid, greenCentroid, blueCentroid);
            segmentsRGBCentroids.put(segmentKey, c);
        }
        this.segmentsRGBCentroids = segmentsRGBCentroids;
    }

    private void updateRgbSums(Map<String, Double> map, Color pixelColor) {
        map.merge("red", pixelColor.getRed(), Double::sum);
        map.merge("green", pixelColor.getGreen(), Double::sum);
        map.merge("blue", pixelColor.getBlue(), Double::sum);
        map.merge("count", 1.0, Double::sum);
    }

    private Color getPixelColor(int index) {
        int[] imageCoordinates = Utils.convertIndexToCoordinates(index, this.width);
        return this.pixelReader.getColor(imageCoordinates[0], imageCoordinates[1]);
    }


    public void printObjectiveValues() {
        System.out.println("Edge value:    " + edgeValue);
        System.out.println("Connectivity:  " + connectivity);
        System.out.println("Deviation:     " + deviation);
    }


    @Override
    public String toString() {
        return this.genotype;
    }

}
