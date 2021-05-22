import EA.Components.Individual;
import EA.GA;
import EA.MOEA;
import EA.Operations.Crossover;
import EA.Operations.Initializer;
import EA.Operations.Mutation;
import Output.*;
import evaluator.Evaluator;
import evaluator.Score;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.*;
import javafx.stage.Stage;

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
        int height = (int) Math.round(main.image.getHeight());      // image height
        int width = (int) Math.round(main.image.getWidth());        // image width
        Printer.printFilepath(Config.FILEPATH);
        Printer.printDimensions(height, width);

        Crossover crossover = new Crossover(Config.CROSSOVER_RATE);
        Mutation mutation = new Mutation(Config.MUTATION_RATE);

        // Initialize population //
        List<Individual> population = Initializer.init(Config.POPULATION_SIZE, height, width, pixelReader);


        // Run evolutionary algorithm //
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

        // At this point optimization is done --> population = final pareto front //
        Printer.printParetoFrontValues(population);

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

        Printer.printScoreStats(topFiveIndexes, scores);

        List<Individual> bestResults = new ArrayList<>();
        for (int index : topFiveIndexes) {
            bestResults.add(population.get(index));
        }

        Printer.printResults(bestResults);
        Displayer.showSolutions(stage, bestResults);
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
