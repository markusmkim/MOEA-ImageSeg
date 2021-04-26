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
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;


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

        System.out.println("\nResulting population: ");
        for (Individual dude: population) {
            // System.out.println(dude);
            // dude.computeSegments();
            // Objectives.evaluateIndividual(dude);
            dude.printObjectiveValues();
        }

        ImageView iv1 = new ImageView();
        iv1.setImage(t.image);
        ImageView iv2 = new ImageView();
        iv2.setImage(population.get(0).constructPhenotype());

        Group root = new Group();
        Scene scene = new Scene(root);
        scene.setFill(Color.BLACK);
        HBox box = new HBox();
        box.getChildren().add(iv1);
        box.getChildren().add(iv2);
        root.getChildren().add(box);

        stage.setTitle("ImageView");
        stage.setWidth(415);
        stage.setHeight(200);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
        //https://docs.oracle.com/javase/8/javafx/api/javafx/scene/image/ImageView.html
    }


    public static void main(String[] args) {
        Application.launch(args);
    }
}
