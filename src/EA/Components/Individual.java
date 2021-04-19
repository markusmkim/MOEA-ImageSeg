package EA.Components;

import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

import java.util.*;


public class Individual {
    private final String genotype;
    private final int height;
    private final int width;

    private final PixelReader pixelReader;  // pixelReader for Image objective to segmentation

    Map<Integer, Map<String, Double>> segmentsRGBSums;
    Map<Integer, Integer> pixelSegmentKeys;


    public Individual(String genotype, int height, int width, PixelReader pixelReader) {
        this.genotype = genotype;
        this.height = height;
        this.width = width;
        this.pixelReader = pixelReader;
    }


    // Getters
    public String getGenotype() { return genotype; }
    public int getHeight()      { return height; }
    public int getWidth()       { return width; }


    public void evaluate() {
        int c = 0;
        for (int i = 0; i < 40000; i++) {
            for (int j = 0; j < 100; j++) {
                c += j;
            }
        }
        System.out.println(c);
    }


    public void computeSegments() {
        Map<Integer, Map<String, Double>> segmentsRGBSums = new HashMap<>();
        Map<Integer, Integer> pixelSegmentKeys = new HashMap<>();
        int pixelSegmentKeyCounter = 0;
        List<Integer> indexesToProcess = new ArrayList<>();
        for (int i = 0; i < this.genotype.length(); i++) {
            indexesToProcess.add(i);
        }
        while (indexesToProcess.size() > 0) {
            List<Integer> processedIndexes = new ArrayList<>();
            int index = indexesToProcess.get(0);

            while (this.genotype.charAt(index) != '0' && !pixelSegmentKeys.containsKey(index) && !processedIndexes.contains(index)) {
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
            if (!pixelSegmentKeys.containsKey(index)){
                indexesToProcess.remove(Integer.valueOf(index));
            }
            // processedIndexes.add(index);
            int segmentKey;
            Map<String, Double> rgbSums;
            if (pixelSegmentKeys.containsKey(index)) {
                segmentKey = pixelSegmentKeys.get(index);
                rgbSums = segmentsRGBSums.get(segmentKey);
            }
            else {
                segmentKey = pixelSegmentKeyCounter++;
                pixelSegmentKeys.put(index, segmentKey);
                Color pixelColor = this.getPixelColor(index);
                rgbSums = new HashMap<>();
                this.updateRgbSums(rgbSums, pixelColor);
            }
            for (Integer processedIndex : processedIndexes) {
                pixelSegmentKeys.put(processedIndex, segmentKey);
                indexesToProcess.remove(Integer.valueOf(processedIndex));
                Color pixelColor = this.getPixelColor(processedIndex);
                this.updateRgbSums(rgbSums, pixelColor);
            }
            segmentsRGBSums.put(segmentKey, rgbSums);
        }
        System.out.println("Segment key count: " + pixelSegmentKeyCounter);
        // pixelSegmentKeys.forEach((key, value) -> System.out.println(key + " " + value));
        segmentsRGBSums.forEach((key, value) -> System.out.println(key + " " + value));
        this.pixelSegmentKeys = pixelSegmentKeys;
        this.segmentsRGBSums = segmentsRGBSums;
    }

    private void updateRgbSums(Map<String, Double> map, Color pixelColor) {
        map.merge("red", pixelColor.getRed(), Double::sum);
        map.merge("green", pixelColor.getGreen(), Double::sum);
        map.merge("blue", pixelColor.getBlue(), Double::sum);
        map.merge("count", 1.0, Double::sum);
    }

    private Color getPixelColor(int index) {
        int[] imageCoordinates = this.convertIndexToCoordinates(index, this.width);
        return this.pixelReader.getColor(imageCoordinates[0], imageCoordinates[1]);
    }

    private int[] convertIndexToCoordinates(int index, int width) {  // [x, y] coordinate
        int row = index / width;
        int col = index % width;
        return new int[]{col, row};
    }

    @Override
    public String toString() {
        return this.genotype;
    }
}
