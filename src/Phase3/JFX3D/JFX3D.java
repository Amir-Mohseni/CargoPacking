package Phase3.JFX3D;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.*;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

import java.util.Random;

public class JFX3D extends Application {
    public static final int WIDTH = 1280, HEIGHT = 720;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);
    private double anchorX, anchorY;
    private double anchorAngleX = 0, anchorAngleY = 0;

    private final RotatorGroup rg = new RotatorGroup();
    private Stage stage;
    private final int cubeLength = 30;
    private final int spacing = 1;

    @Override
    public void start(Stage primaryStage) throws InterruptedException {
        this.stage = primaryStage;
        setup();

        int[][][] data = new int[33][5][8];
        Random randomNum = new Random();

        for (int[][] dimX : data){
            for (int[] dimY : dimX){
                for (int dimZ = 0; dimZ < dimY.length; dimZ++){
                    dimY[dimZ] = randomNum.nextInt(2);
                }
            }
        }

        draw3D(data);
    }

    private void setup(){
        BorderPane bp = new BorderPane();

        Scene mainScene = new Scene(bp, WIDTH, HEIGHT);

        PerspectiveCamera perspectiveCamera = new PerspectiveCamera(true);

        SubScene scene = new SubScene(rg, WIDTH, HEIGHT, true, null);

        bp.setCenter(scene);

        scene.setFill(Color.LIGHTGRAY);
        scene.setCamera(perspectiveCamera);

        perspectiveCamera.translateXProperty().set(0);
        perspectiveCamera.translateYProperty().set(0);
        perspectiveCamera.translateZProperty().set(-4000);
        perspectiveCamera.setNearClip(1);
        perspectiveCamera.setFarClip(100000);

        PointLight pointLight1 = new PointLight();
//        pointLight.setColor(Color.RED);
        pointLight1.setTranslateX(1000);
        pointLight1.setTranslateY(0);
        pointLight1.setTranslateZ(0);
        this.rg.getChildren().add(pointLight1);

        PointLight pointLight2 = new PointLight();
//        pointLight.setColor(Color.RED);
        pointLight2.setTranslateX(-1000);
        pointLight2.setTranslateY(0);
        pointLight2.setTranslateZ(0);
        this.rg.getChildren().add(pointLight2);

        PointLight pointLight3 = new PointLight();
//        pointLight.setColor(Color.RED);
        pointLight3.setTranslateX(0);
        pointLight3.setTranslateY(1000);
        pointLight3.setTranslateZ(0);
        this.rg.getChildren().add(pointLight3);

        PointLight pointLight4 = new PointLight();
//        pointLight.setColor(Color.RED);
        pointLight4.setTranslateX(0);
        pointLight4.setTranslateY(-1000);
        pointLight4.setTranslateZ(0);
        this.rg.getChildren().add(pointLight4);

        PointLight pointLight5 = new PointLight();
//        pointLight.setColor(Color.RED);
        pointLight5.setTranslateX(0);
        pointLight5.setTranslateY(0);
        pointLight5.setTranslateZ(1000);
        this.rg.getChildren().add(pointLight5);

        PointLight pointLight6 = new PointLight();
//        pointLight.setColor(Color.RED);
        pointLight6.setTranslateX(0);
        pointLight6.setTranslateY(0);
        pointLight6.setTranslateZ(-1000);
        this.rg.getChildren().add(pointLight6);

        initMouseControl(this.rg, scene, stage);

        stage.setTitle("Project 1 phase 3");
        stage.setScene(mainScene);
        stage.show();
        stage.setOnCloseRequest(t -> System.exit(0));
    }

    private void draw3D(int[][][] data) throws InterruptedException {
        int lenX = data.length, lenY = data[0].length, lenZ = data[0][0].length;
        int midX = Math.ceilDiv(lenX, 2), midY = Math.ceilDiv(lenY, 2), midZ = Math.ceilDiv(lenZ, 2);

        for (int dimX = 0; dimX < data.length; dimX++){
            for (int dimY = 0; dimY < data[dimX].length; dimY++){
                for (int dimZ = 0; dimZ < data[dimX][dimY].length; dimZ++){
                    if (data[dimX][dimY][dimZ] == 0){
                        continue;
                    }

                    Box newBox = new Box();

                    newBox.setWidth(cubeLength);
                    newBox.setHeight(cubeLength);
                    newBox.setDepth(cubeLength);

                    String hexColor = randomHexColor();
                    try{
                        PhongMaterial material = new PhongMaterial();
                        material.setDiffuseColor(Color.web(hexColor, 0.1));
                        newBox.setMaterial(material);
                    } catch (Exception e){
                        System.out.println(hexColor);
                    }

                    int transX = ((-1 * midX + dimX) * this.cubeLength + this.spacing * this.cubeLength * (-1 * midX + dimX));
                    int transY = ((-1 * midY + dimY) * this.cubeLength + this.spacing * this.cubeLength * (-1 * midY + dimY));
                    int transZ = ((-1 * midZ + dimZ) * this.cubeLength + this.spacing * this.cubeLength * (-1 * midZ + dimZ));

                    newBox.setTranslateX(transX);
                    newBox.setTranslateY(transY);
                    newBox.setTranslateZ(transZ);

                    this.rg.getChildren().add(newBox);
                }
            }
        }



    }

    private String randomHexColor(){
        Random random = new Random();
        int R = random.nextInt(255), G = random.nextInt(255), B = random.nextInt(255);
        return "#" + leftPad(Integer.toHexString(R)) + leftPad(Integer.toHexString(G)) + leftPad(Integer.toHexString(B));

    }

    private String leftPad(String s){
        return "0".repeat((2 - s.length())) + s;
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