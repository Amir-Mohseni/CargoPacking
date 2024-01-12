package Phase3.JFX3D;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Box;
import javafx.stage.Stage;

public class JFX3D extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Creating a Box
        Box box = new Box();

        // Setting the properties of the Box
        box.setWidth(150.0);
        box.setHeight(150.0);
        box.setDepth(150.0);

        box.setTranslateX(100);
        box.setTranslateY(100);
        box.setTranslateZ(10);

        // Creating a Group
        Group root = new Group(box);

        // Creating a scene with specified width, height and fill color
        Scene scene = new Scene(root, 600, 300, Color.LIGHTBLUE);

        // Enable 3D rotation for the cube
        PerspectiveCamera camera = new PerspectiveCamera(false);
        camera.setTranslateX(0);
        camera.setTranslateY(0);
        camera.setTranslateZ(-300);

        // Add the camera to the scene
        scene.setCamera(camera);

        // Setting the title to Stage.
        primaryStage.setTitle("Sample Application");

        // Adding the scene to Stage
        primaryStage.setScene(scene);

        // Displaying the contents of the stage
        primaryStage.show();
    }

    public static void main(String args[]) {
        launch(args);
    }
}