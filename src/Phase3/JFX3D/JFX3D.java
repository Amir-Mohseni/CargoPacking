package Phase3.JFX3D;

import Packing.RandomSearch;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.*;
import javafx.scene.shape.Box;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class JFX3D extends Application {
    public static final int WIDTH = Settings.Window.WINDOW_WIDTH, HEIGHT = Settings.Window.WINDOW_HEIGHT;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);
    private double anchorX, anchorY;
    private double anchorAngleX = 0, anchorAngleY = 0;


    private final BorderPane bp = new BorderPane();
    private RotatorGroup rg = new RotatorGroup();
    private Stage stage;
    private final int cubeLength = Settings.Cubes.CUBE_LENGTH;
    private final int spacing = Settings.Cubes.CUBE_SPACING;
    private String selectedAlgo;
    private final Map<Integer, String> colorMap = new HashMap<>();
    private double scrollBarPositionX, scrollBarPositionY, scrollBarPositionZ;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        this.setupRender();
        this.setupUI();
    }

    private void setupRender(){
        this.rg = new RotatorGroup();
        PerspectiveCamera perspectiveCamera = new PerspectiveCamera(true);

        SubScene scene = new SubScene(rg, WIDTH, HEIGHT, true, SceneAntialiasing.BALANCED);

        this.bp.setCenter(scene);

        scene.setFill(Settings.Window.BACKGROUND_COLOR);
        scene.setCamera(perspectiveCamera);

        perspectiveCamera.translateXProperty().set(Settings.Camera.OFFSET_X);
        perspectiveCamera.translateYProperty().set(Settings.Camera.OFFSET_Y);
        perspectiveCamera.translateZProperty().set(Settings.Camera.OFFSET_Z);
        perspectiveCamera.setNearClip(Settings.Camera.NEAR_CLIP);
        perspectiveCamera.setFarClip(Settings.Camera.FAR_CLIP);

        PointLight pointLight1 = new PointLight();
        pointLight1.setColor(Settings.Window.LIGHT_COLOR);
        pointLight1.setTranslateX(-1000);
        pointLight1.setTranslateY(-1000);
        pointLight1.setTranslateZ(-1000);
        this.rg.getChildren().add(pointLight1);
        initMouseControl(this.rg, scene, stage);

        stage.setTitle("Project 1 phase 3");
        stage.show();
        stage.setOnCloseRequest(t -> System.exit(0));
    }

    private void setupUI(){
        Scene mainScene = new Scene(this.bp, WIDTH, HEIGHT);
        stage.setScene(mainScene);


        VBox leftPane = new VBox();
        leftPane.setSpacing(20);
        leftPane.setAlignment(Pos.CENTER);

        Button startButton = new Button("Start");
        startButton.setPrefSize(80,40);
        Button resetButton = new Button("Reset");
        resetButton.setPrefSize(80,40);

        Label label1 = new Label("Controls for visualization");
        label1.setFont(Font.font("Times New Roman", FontWeight.BOLD, 23));
        Label label2 = new Label("Select an Algorithm: ");
        label2.setFont(Font.font("Arial", FontWeight.BOLD,12));
        leftPane.getChildren().addAll(label1, label2);


        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton randomButton = new RadioButton("Random");
        RadioButton greedyButton = new RadioButton("Greedy");
        RadioButton algoXButton = new RadioButton("Algo X");

        randomButton.setToggleGroup(toggleGroup);
        greedyButton.setToggleGroup(toggleGroup);
        algoXButton.setToggleGroup(toggleGroup);

        leftPane.getChildren().addAll(randomButton,greedyButton,algoXButton);
        leftPane.getChildren().addAll(startButton,resetButton);

        ScrollBar scrollBarX = new ScrollBar();
        scrollBarX.setMin(0);
        scrollBarX.setMax(200);
        scrollBarX.setUnitIncrement(10);
        scrollBarX.setBlockIncrement(10);

        ScrollBar scrollBarY = new ScrollBar();
        scrollBarY.setMin(0);
        scrollBarY.setMax(200);
        scrollBarY.setUnitIncrement(10);
        scrollBarY.setBlockIncrement(10);

        ScrollBar scrollBarZ = new ScrollBar();
        scrollBarZ.setMin(0);
        scrollBarZ.setMax(200);
        scrollBarZ.setUnitIncrement(10);
        scrollBarZ.setBlockIncrement(10);

        Label label3 = new Label("X Axis:");
        label3.setFont(Font.font("Arial", FontWeight.BOLD,12));
        Label label4 = new Label("Y Axis:");
        label4.setFont(Font.font("Arial", FontWeight.BOLD,12));
        Label label5 = new Label("Z Axis:");
        label5.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        leftPane.getChildren().addAll(label3, scrollBarX, label4, scrollBarY, label5, scrollBarZ);

        Label label6 = new Label("Scoring table:");
        label6.setFont(Font.font("Arial", FontWeight.BOLD,12));
        Label scoreLabel = new Label();

        leftPane.getChildren().addAll(label6,scoreLabel);

        startButton.setOnAction(e ->{
            if(randomButton.isSelected()){
                selectedAlgo = "Random";
                this.render(new RandomSearch());
            }

            if(greedyButton.isSelected()){
                selectedAlgo = "Greedy";
            }

            if(algoXButton.isSelected()){
                selectedAlgo = "Algo X";
            }

            if(selectedAlgo == null){
                System.out.println("No option has been selected");
            }

        });
        resetButton.setOnAction(e -> this.setupRender());
        scrollBarX.valueProperty().addListener((observable,oldValue,newValue)->{
            scrollBarPositionX = scrollBarX.getValue(); //Values for the current position
            System.out.println("ScrollBarX's current position:"+newValue); //To print into the console the current position
        });
        scrollBarY.valueProperty().addListener((observable,oldValue,newValue)->{
            scrollBarPositionY = scrollBarY.getValue();
            System.out.println("ScrollBarY's current position:"+newValue);
        });
        scrollBarZ.valueProperty().addListener((observable,oldValue,newValue)->{
            scrollBarPositionZ = scrollBarZ.getValue();
            System.out.println("ScrollBarZ's current position:"+newValue);
        });
        this.bp.setLeft(leftPane);
    }

    private void render(Renderable renderable){
        this.setupRender();
        this.draw3D(renderable.getData());
    }

    private void draw3D(int[][][] data) {
        int lenX = data.length, lenY = data[0].length, lenZ = data[0][0].length;
        int midX = Math.ceilDiv(lenX, 2), midY = Math.ceilDiv(lenY, 2), midZ = Math.ceilDiv(lenZ, 2);

        for (int dimX = 0; dimX < data.length; dimX++){
            for (int dimY = 0; dimY < data[dimX].length; dimY++){
                for (int dimZ = 0; dimZ < data[dimX][dimY].length; dimZ++){
                    int id = data[dimX][dimY][dimZ];
                    if (id  == 0){
                        continue;
                    }

                    Box newBox = new Box();

                    newBox.setWidth(cubeLength);
                    newBox.setHeight(cubeLength);
                    newBox.setDepth(cubeLength);

                    String hexColor = getHexColor(id);
                    PhongMaterial material = new PhongMaterial();
                    material.setDiffuseColor(Color.web(hexColor, 1));
                    newBox.setMaterial(material);

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

    private String getHexColor(Integer id){
        String color = this.colorMap.get(id);

        if (color == null){
            Random random = new Random();
            int R = random.nextInt(255), G = random.nextInt(255), B = random.nextInt(255);
            color = "#" + leftPad(Integer.toHexString(R)) + leftPad(Integer.toHexString(G)) + leftPad(Integer.toHexString(B));
            colorMap.put(id, color);
        }
        return color;

    }

    private String leftPad(String s){
        return "0".repeat((2 - s.length())) + s;
    }

    class RotatorGroup extends Group {
        Rotate r;
        Transform t = new Rotate();
        void rotateX(int ang) {
            r = new Rotate(ang, Rotate.X_AXIS);
            t = t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().add(t);
        }
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


    public static void main(String[] args) {
        launch(args);
    }
}