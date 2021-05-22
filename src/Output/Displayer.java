package Output;

import EA.Components.Individual;

import java.util.List;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class Displayer {
    public static void showSolutions(Stage stage, List<Individual> results) {
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
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

}
