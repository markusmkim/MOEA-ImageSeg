import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import EA.Components.Individual;
import EA.Operations.Initializer;
import javafx.application.Platform;
import javafx.application.Application;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;

public class Test extends Application {

    private final String path = "data/86016/Test image.jpg";

    private Image image;


    public Test() {
        this.image = new Image(this.path, false);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Test t = new Test();
        PixelReader pixelReader = t.image.getPixelReader();
        Color color = pixelReader.getColor(1, 1);
        int height = (int) Math.round(t.image.getHeight());
        int width = (int) Math.round(t.image.getWidth());
        System.out.println("" + color.getGreen());

        System.out.println("Height: " + height + " - Width: " + width);

        // Individual ind = new Individual(3, 4);
        List<Individual> population = Initializer.init(10, height, width, pixelReader);

        System.out.println("\nPopulation: ");
        for (Individual dude: population) {
            System.out.println(dude);
            dude.computeSegments();
            dude.evaluate();
        }

        ImageView iv1 = new ImageView();
        iv1.setImage(t.image);
        ImageView iv2 = new ImageView();
        iv2.setImage(t.image);

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
