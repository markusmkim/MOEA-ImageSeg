import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import EA.Components.Individual;
import EA.GA;
import EA.MOEA;
import EA.Objectives;
import EA.Operations.Crossover;
import EA.Operations.Initializer;
import EA.Operations.Mutation;
import EA.Utils;
import evaluator.Evaluator;
import evaluator.Score;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;

import javax.imageio.ImageIO;


public class Main extends Application {

    private final Image image;


    public Main() {
        this.image = new Image(Config.FILEPATH, false);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Main main = new Main();
        PixelReader pixelReader = main.image.getPixelReader();
        int height = (int) Math.round(main.image.getHeight());
        int width = (int) Math.round(main.image.getWidth());
        System.out.println("Running segmentation on " + Config.FILEPATH);
        System.out.println("Height: " + height + " - Width: " + width);

        Crossover crossover = new Crossover(Config.CROSSOVER_RATE);
        Mutation mutation = new Mutation(Config.MUTATION_RATE);

        List<Individual> population = Initializer.init(Config.POPULATION_SIZE, height, width, pixelReader);

        if (Config.RUN_MOEA) {
            System.out.println("Running NSGA-II");
            MOEA moea = new MOEA(Config.POPULATION_SIZE, Config.GENERATIONS, crossover, mutation, Config.MIN_SEG, Config.MAX_SEG);
            population = moea.run(population);
        }
        else {
            System.out.println("Running simple GA");
            GA geneticAlgorithm = new GA(Config.POPULATION_SIZE, Config.GENERATIONS, crossover, mutation, Config.FITNESS_WEIGHTS, Config.MIN_SEG, Config.MAX_SEG);
            population = geneticAlgorithm.run(population);
        }


        System.out.println("\nPareto front size: " + population.size());
        this.printAllSegmentations(population);

        this.saveSolutions(population);

        Evaluator evaluator = new Evaluator(Config.optimalBlackWhitePath, Config.studentBlackWhitePath);

        double[] scores = evaluator.runSameThread();
        List<Score> scoreObjs = new ArrayList<>();
        double best = 0.0;
        for (int i = 0; i < scores.length; i++) {
            scoreObjs.add(new Score(scores[i], i));
            if (scores[i] > best) {
                best = scores[i];
            }
        }
        System.out.println("\nScores: " + Arrays.toString(scores));
        System.out.println("\nBest score: " + best);

        List<Integer> topFiveIndexes = Score.findTopFiveIndexes(scoreObjs);

        this.printScoreStats(topFiveIndexes, scores);

        List<Individual> bestResults = new ArrayList<>();
        for (int index : topFiveIndexes) {
            bestResults.add(population.get(index));
        }
        this.showSolutions(stage, bestResults);
    }


    private void showSolutions(Stage stage, List<Individual> results) {
        this.printResults(results);

        Group root = new Group();
        Scene scene = new Scene(root);
        scene.setFill(Color.BLACK);
        HBox hbox = new HBox();
        root.getChildren().add(hbox);

        for (Individual result : results) {
            ImageView type1 = new ImageView();
            ImageView type2 = new ImageView();
            Image[] res = result.constructPhenotype();
            type1.setImage(res[0]);
            type2.setImage(res[1]);

            VBox vbox = new VBox();
            vbox.getChildren().addAll(type1, type2);
            hbox.getChildren().add(vbox);
        }

        stage.setTitle("ImageView");
        // stage.setWidth(415);
        // stage.setHeight(200);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
        //https://docs.oracle.com/javase/8/javafx/api/javafx/scene/image/ImageView.html

    }


    private void printAllSegmentations(List<Individual> population) {
        List<Integer> segmentations = population.stream().map(i -> i.getSegmentsRGBCentroids().size()).collect(Collectors.toList());
        System.out.println("Segmentations: " + segmentations);
    }


    private void printScoreStats(List<Integer> indexes, double[] scores) {
        List<Double> scoresList = new ArrayList<>();
        for (int index : indexes) {
            scoresList.add(scores[index]);
        }
        System.out.println("Best indexes: " + indexes);
        System.out.println("Best scores : " + scoresList);
    }


    private void printResults(List<Individual> results) {
        System.out.println("\nTop five: ");
        for (Individual individual : results) {
            String output = "Edge value: " + Utils.formatValue(individual.getEdgeValue()) +
                    "     |     Connectivity measure: " + Utils.formatValue(individual.getConnectivity()) +
                    "     |     Overall deviation: " + Utils.formatValue(individual.getDeviation()) +
                    "     |     Number of segments: " + Utils.formatValue(individual.getSegmentsRGBCentroids().size());
            System.out.println(output);
        }
    }


    private void saveSolutions(List<Individual> results) {
        this.clearDirectory(Config.studentBlackWhitePath);
        this.clearDirectory(Config.studentColorPath);

        for (int i = 0; i < results.size(); i++) {
            Image[] res = results.get(i).constructPhenotype();
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(res[0], null), "png", new File(Config.studentColorPath + "c_" + i + ".png"));
                ImageIO.write(SwingFXUtils.fromFXImage(res[1], null), "png", new File(Config.studentBlackWhitePath + "bw_" + i + ".png"));
            } catch (IOException e) {
                System.out.println("Save error");
                e.printStackTrace();
            }
        }
    }


    private void clearDirectory(String dirPath) {
        File[] files = new File(dirPath).listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
    }


    public static void main(String[] args) {
        Application.launch(args);
    }
}
