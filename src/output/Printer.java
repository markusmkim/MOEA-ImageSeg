package output;

import ea.components.Individual;
import ea.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class Printer {
    public static void printFilepath(String path) {
        System.out.println("Running segmentation on " + path);
    }


    public static void printDimensions(int height, int width) {
        System.out.println("Height: " + height + " - Width: " + width);
    }


    public static void printParetoFrontValues(List<Individual> population) {
        System.out.println("\nPareto front size: " + population.size());
        List<Integer> segmentations = population.stream().map(i -> i.getSegmentsRGBCentroids().size()).collect(Collectors.toList());
        System.out.println("Segmentations:     " + segmentations);
    }


    public static void printScoreStats(List<Integer> indexes, double[] scores) {
        List<Double> scoresList = new ArrayList<>();
        for (int index : indexes) {
            scoresList.add(scores[index]);
        }
        System.out.println("Best indexes: " + indexes);
        System.out.println("Best scores : " + scoresList);
    }


    public static void printResults(List<Individual> results) {
        System.out.println("\nTop five: ");
        for (Individual individual : results) {
            String output = "Edge value: " + Utils.formatValue(individual.getEdgeValue()) +
                    "     |     Connectivity measure: " + Utils.formatValue(individual.getConnectivity()) +
                    "     |     Overall deviation: " + Utils.formatValue(individual.getDeviation()) +
                    "     |     Number of segments: " + Utils.formatValue(individual.getSegmentsRGBCentroids().size());
            System.out.println(output);
        }
    }
}
