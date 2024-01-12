package Phase3.JFX3D;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.*;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class JFX3D extends Application {
    public static final int WIDTH = 800, HEIGHT = 600;

    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);
    private double anchorX, anchorY;
    private double anchorAngleX = 0, anchorAngleY = 0;

    @Override
    public void start(Stage primaryStage) {
        BorderPane bp = new BorderPane();
        RotatorGroup rg = new RotatorGroup();

        Scene mainScene = new Scene(bp, WIDTH, HEIGHT);

        PerspectiveCamera perspectiveCamera = new PerspectiveCamera(true);

        SubScene scene = new SubScene(rg, WIDTH, HEIGHT, true, null);

        bp.setCenter(scene);

        scene.setFill(Color.LIGHTGRAY);
        scene.setCamera(perspectiveCamera);

        perspectiveCamera.translateXProperty().set(0);
        perspectiveCamera.translateYProperty().set(0);
        perspectiveCamera.translateZProperty().set(-3000);
        perspectiveCamera.setNearClip(1);
        perspectiveCamera.setFarClip(100000);

        initMouseControl(rg, scene, primaryStage);

        Box newBox = new Box();
        newBox.setWidth(150);
        newBox.setHeight(150);
        newBox.setDepth(150);

        rg.getChildren().add(newBox);

        primaryStage.setTitle("Project 1 phase 3");
        primaryStage.setScene(mainScene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override public void handle(WindowEvent t) {
                System.exit(0);
            }
        });

    }

    class RotatorGroup extends Group {
        Rotate r;
        Transform t = new Rotate();
        /**
         * Rotate around the x-axis
         * @param ang
         */
        void rotateX(int ang) {
            r = new Rotate(ang, Rotate.X_AXIS);
            t = t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().add(t);
        }
        /**
         * Rotates around the Y-axis
         * @param ang
         */
        void rotateY(int ang) {
            r = new Rotate(ang, Rotate.Y_AXIS);
            t = t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().add(t);
        }
    }

    private void initMouseControl(RotatorGroup group, SubScene scene, Stage stage) {
        Rotate xRotate;
        Rotate yRotate;
        group.getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRotate = new Rotate(0, Rotate.Y_AXIS)
        );
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });

        scene.setOnMouseDragged(event -> {
            angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
            angleY.set(anchorAngleY + (anchorX - event.getSceneX()));
        });

        stage.addEventHandler(ScrollEvent.SCROLL, event -> {
            double movement = -event.getDeltaY();
            group.translateZProperty().set(group.getTranslateZ() + movement);
        });
    }


    public static void main(String args[]) {
        launch(args);
    }
}