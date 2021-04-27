import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import EA.Components.Individual;
import EA.GA;
import EA.MOEA;
import EA.Objectives;
import EA.Operations.Crossover;
import EA.Operations.Initializer;
import EA.Operations.Mutation;
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
        Main t = new Main();
        PixelReader pixelReader = t.image.getPixelReader();
        Color color = pixelReader.getColor(1, 1);
        int height = (int) Math.round(t.image.getHeight());
        int width = (int) Math.round(t.image.getWidth());
        System.out.println("" + color.getGreen());

        System.out.println("Height: " + height + " - Width: " + width);

        Crossover crossover = new Crossover(Config.CROSSOVER_RATE);
        Mutation mutation = new Mutation(Config.MUTATION_RATE);
        GA geneticAlgorithm = new GA(Config.POPULATION_SIZE, Config.GENERATIONS, crossover, mutation, Config.FITNESS_WEIGHTS);
        MOEA moea = new MOEA(Config.POPULATION_SIZE, Config.GENERATIONS, crossover, mutation);

        // Individual ind = new Individual(3, 4);
        List<Individual> population = Initializer.init(Config.POPULATION_SIZE, height, width, pixelReader);

        // population = geneticAlgorithm.run(population);
        population = moea.run(population);

        // population = Arrays.asList(Crossover.applyUniformCrossover(population.get(0), population.get(1)));

        // Individual mutated = Mutation.applySingleBitMutation(population.get(0));
        // population.add(0, mutated);

        this.saveSolutions(population);

        this.showSolutions(stage, population);

    }


    private void showSolutions(Stage stage, List<Individual> results) {
        System.out.println("\nResulting population size: " + results.size());
        System.out.println("\nResulting population: ");
        for (Individual dude: results) {
            dude.printObjectiveValues();
        }

        Group root = new Group();
        Scene scene = new Scene(root);
        scene.setFill(Color.BLACK);
        HBox hbox = new HBox();
        root.getChildren().add(hbox);

        for (int i = 0; i < 5; i++) {
            if (results.size() < i - 1) {
                break;
            }

            ImageView type1 = new ImageView();
            ImageView type2 = new ImageView();
            Image[] res = results.get(i).constructPhenotype();
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


    private void saveSolutions(List<Individual> results) {
        /*
        try {
            myObj = new File("filename.png");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
         */
        this.clearDirectory(Config.studentBlackWhitePath);
        this.clearDirectory(Config.studentColorPath);

        for (int i = 0; i < results.size(); i++) {
            Image[] res = results.get(i).constructPhenotype();
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(res[0], null), "png", new File(Config.studentColorPath + "c" + i + ".png"));
                ImageIO.write(SwingFXUtils.fromFXImage(res[1], null), "png", new File(Config.studentBlackWhitePath + "bw" + i + ".png"));
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
