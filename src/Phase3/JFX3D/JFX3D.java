package Phase3.JFX3D;

import Packing.UnitDatabase;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.*;
import javafx.scene.shape.Box;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static Phase3.JFX3D.Settings.Window.*;

public class JFX3D extends Application {
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);
    private double anchorX, anchorY;
    private double anchorAngleX = 0, anchorAngleY = 0;


    private final BorderPane bp = new BorderPane();
    private RotatorGroup rg = new RotatorGroup();
    private Stage stage;
    private final int cubeLength = Settings.Cubes.CUBE_LENGTH;
    private final int spacing = Settings.Cubes.CUBE_SPACING;
    private Constants.Settings.AlgorithmSettings.Algorithm selectedAlgo;
    private Constants.Settings.ParcelSettings.Parcel selectedFilling;
    private final Map<Integer, String> colorMap = new HashMap<>();
    private int value1, value2, value3;
    private int quantity1, quantity2, quantity3;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        this.setupRender();
        this.setupUI();
    }

    private void setupRender(){
        this.rg = new RotatorGroup();
        PerspectiveCamera perspectiveCamera = new PerspectiveCamera(true);

        SubScene scene = new SubScene(rg, CONTENT_WINDOW_WIDTH, CONTENT_WINDOW_HEIGHT, true, SceneAntialiasing.BALANCED);

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
        Scene mainScene = new Scene(this.bp, MENU_WINDOW_WIDTH, MENU_WINDOW_HEIGHT);
        stage.setScene(mainScene);


        Button startButton = new Button("Start");
        startButton.setPrefSize(50,10);
        Button stopButton = new Button("Stop");
        stopButton.setPrefSize(50,10);
        Button resetButton = new Button("Reset");
        resetButton.setPrefSize(50,10);

        Label label1 = new Label("3D PACKING SOLVER");
        label1.setFont(Font.font("Times New Roman", FontWeight.BOLD, 23));

        Menu algorithmMenu = new Menu("Algorithms");
        MenuItem random = new MenuItem("Random");
        MenuItem greedy = new MenuItem("Greedy");
        MenuItem genetic = new MenuItem("Genetic");
        MenuItem algoX = new MenuItem("Algorithm X");


        algorithmMenu.getItems().addAll(random,greedy,algoX);

        MenuBar menuBar1 = new MenuBar();
        menuBar1.getMenus().add(algorithmMenu);
        setWidthOfMenuBar(100,menuBar1);

        Menu typeOfFilling = new Menu("Parcels");
        MenuItem pentominoes = new MenuItem("Pentominoes");
        MenuItem cubes = new MenuItem("Cubes");

        typeOfFilling.getItems().addAll(pentominoes,cubes);

        MenuBar menuBar2 = new MenuBar();
        menuBar2.getMenus().add(typeOfFilling);
        setWidthOfMenuBar(100,menuBar2);


        Label selectedAlgorithmLabel = new Label();
        Label selectedParcelLabel = new Label();


        Label valuesLabel = new Label("Values");
        valuesLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 23));
        TextField valueText1 = new TextField();
        TextField valueText2 = new TextField();
        TextField valueText3 = new TextField();

        Label quantityLabel = new Label("Quantity");
        quantityLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 23));
        TextField quantityText1 = new TextField();
        TextField quantityText2 = new TextField();
        TextField quantityText3 = new TextField();

        setWidthOfTextField(50,valueText1);
        setWidthOfTextField(50,valueText2);
        setWidthOfTextField(50,valueText2);
        setWidthOfTextField(50,valueText3);
        setWidthOfTextField(50,quantityText1);
        setWidthOfTextField(50,quantityText2);
        setWidthOfTextField(50,quantityText3);


        Label scoreLabel = new Label("Score:");
        scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD,12));
        Label currentScoreLabel = new Label();

        ScrollBar scrollBarX = new ScrollBar();
        ScrollBar scrollBarY = new ScrollBar();
        ScrollBar scrollBarZ = new ScrollBar();

        setParametersScrollBar(scrollBarX,0,200);
        setParametersScrollBar(scrollBarY,0,200);
        setParametersScrollBar(scrollBarZ,0,200);

        Label scrollerLabelX = new Label("X axis");
        Label scrollerLabelY = new Label("Y axis");
        Label scrollerLabelZ = new Label("Z axis");


        HBox algorithmMenuGroup = new HBox();
        algorithmMenuGroup.getChildren().addAll(menuBar1,selectedAlgorithmLabel);
        algorithmMenuGroup.setAlignment(Pos.CENTER);
        algorithmMenuGroup.setSpacing(10);

        HBox parcelMenuGroup = new HBox();
        parcelMenuGroup.getChildren().addAll(menuBar2,selectedParcelLabel);
        parcelMenuGroup.setAlignment(Pos.CENTER);
        parcelMenuGroup.setSpacing(10);

        HBox valuesTextFieldGroup = new HBox();
        valuesTextFieldGroup.getChildren().addAll(valueText1,valueText2,valueText3);
        valuesTextFieldGroup.setAlignment(Pos.CENTER);
        valuesTextFieldGroup.setSpacing(10);

        HBox quantityTextFieldGroup = new HBox();
        quantityTextFieldGroup.getChildren().addAll(quantityText1,quantityText2,quantityText3);
        quantityTextFieldGroup.setAlignment(Pos.CENTER);
        quantityTextFieldGroup.setSpacing(10);

        HBox scoreGroup = new HBox();
        scoreGroup.getChildren().addAll(scoreLabel,currentScoreLabel);
        scoreGroup.setAlignment(Pos.CENTER);
        scoreGroup.setSpacing(10);

        HBox buttonsGroup = new HBox();
        buttonsGroup.getChildren().addAll(startButton,stopButton,resetButton);
        buttonsGroup.setAlignment(Pos.CENTER);
        buttonsGroup.setSpacing(10);

        HBox scrollerXGroup = new HBox();
        scrollerXGroup.getChildren().addAll(scrollerLabelX,scrollBarX);
        scrollerXGroup.setAlignment(Pos.CENTER);
        scrollerXGroup.setSpacing(10);

        HBox scrollerYGroup = new HBox();
        scrollerYGroup.getChildren().addAll(scrollerLabelY,scrollBarY);
        scrollerYGroup.setAlignment(Pos.CENTER);
        scrollerYGroup.setSpacing(10);

        HBox scrollerZGroup = new HBox();
        scrollerZGroup.getChildren().addAll(scrollerLabelZ,scrollBarZ);
        scrollerZGroup.setAlignment(Pos.CENTER);
        scrollerZGroup.setSpacing(10);

        VBox mainGroup = new VBox();
        mainGroup.getChildren().addAll(label1,algorithmMenuGroup,parcelMenuGroup,valuesLabel,valuesTextFieldGroup,quantityLabel,quantityTextFieldGroup,
        buttonsGroup,scoreGroup,scrollerXGroup,scrollerYGroup,scrollerZGroup);
        mainGroup.setAlignment(Pos.CENTER);
        mainGroup.setPadding(new Insets(10));
        mainGroup.setSpacing(15);

        //ActionListener for StartButton
        startButton.setOnAction(e ->{
            try {
                this.render((Renderable) this.selectedAlgo.getRenderable().getConstructor().newInstance(), (UnitDatabase) this.selectedFilling.getDatabase().getConstructor().newInstance());
            } catch (InstantiationException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            } catch (InvocationTargetException ex) {
                throw new RuntimeException(ex);
            } catch (NoSuchMethodException ex) {
                throw new RuntimeException(ex);
            }

            System.out.println("Values: "+value1+", "+value2+", "+value3);
            System.out.println("Quantities: "+quantity1+", "+quantity2+", "+quantity3);

        });

        //ActionListeners for updating the testAre with the selected algorithm
        random.setOnAction(e->{
            selectedAlgo = Constants.Settings.AlgorithmSettings.Algorithm.RANDOM;
            selectedAlgorithmLabel.setText(selectedAlgo.getName());
        });
        greedy.setOnAction(e->{
            selectedAlgo = Constants.Settings.AlgorithmSettings.Algorithm.GREEDY;
            selectedAlgorithmLabel.setText(selectedAlgo.getName());
        });
        genetic.setOnAction(e->{
            selectedAlgo = Constants.Settings.AlgorithmSettings.Algorithm.GENETIC;
            selectedAlgorithmLabel.setText(selectedAlgo.getName());
        });
        algoX.setOnAction(e->{
            selectedAlgo = Constants.Settings.AlgorithmSettings.Algorithm.ALGORITHM_X;
            selectedAlgorithmLabel.setText(selectedAlgo.getName());
        });

        //ActionListeners for updating the textArea with the selected parcel type
        pentominoes.setOnAction(e->{
            selectedFilling = Constants.Settings.ParcelSettings.Parcel.PENTOMINOES;
            selectedParcelLabel.setText(selectedFilling.getName());
        });
        cubes.setOnAction(e->{
            selectedFilling = Constants.Settings.ParcelSettings.Parcel.BOXES;
            selectedParcelLabel.setText(selectedFilling.getName());
        });

        //TODO: Set ActionListener for the currentScoreLabel

        //ActionListeners for getting the text from the value text cells
        valueText1.setOnAction(e->{
            String text = valueText1.getText();
            if(!text.isEmpty()){value1 = Integer.parseInt(text);}
        });
        valueText2.setOnAction(e->{
            String text = valueText2.getText();
            if(!text.isEmpty()){value2 = Integer.parseInt(text);}
        });
        valueText3.setOnAction(e->{
            String text = valueText3.getText();
            if(!text.isEmpty()){value3 = Integer.parseInt(text);}
        });

        //ActionListeners for getting the text from the quantity text cells
        quantityText1.setOnAction(e->{
            String text = quantityText1.getText();
            if(!text.isEmpty()){quantity1 = Integer.parseInt(text);}
        });
        quantityText2.setOnAction(e->{
            String text = quantityText2.getText();
            if(!text.isEmpty()){quantity2 = Integer.parseInt(text);}
        });
        quantityText3.setOnAction(e->{
            String text = quantityText3.getText();
            if(!text.isEmpty()){quantity3 = Integer.parseInt(text);}
        });

        //ActionListeners for getting the current positions of each scroller
        scrollBarX.valueProperty().addListener((observable,oldValue,newValue)->{
            System.out.println("Current position of ScrollBarX: "+newValue);
        });
        scrollBarY.valueProperty().addListener((observable,oldValue,newValue)->{
            System.out.println("Current position of ScrollBarY: "+newValue);
        });
        scrollBarZ.valueProperty().addListener((observable,oldValue,newValue)->{
            System.out.println("Current position of ScrollBarZ: "+newValue);
        });


        stopButton.setOnAction(e->{});//ActionListener is empty, functionality to be added. TODO
        resetButton.setOnAction(e -> this.setupRender());
        this.bp.setLeft(mainGroup);
    }

    public void setWidthOfTextField(int width, TextField textField){
        textField.setPrefWidth(width);
        textField.setMaxWidth(width);
    }

    public void setWidthOfMenuBar(int width, MenuBar menuBar){
        menuBar.setPrefWidth(width);
        menuBar.setMaxWidth(width);
    }
    public void setParametersScrollBar(ScrollBar scrollBar, double min, double max){
        scrollBar.setMin(min);
        scrollBar.setMax(max);
    }

    private void render(Renderable renderable, UnitDatabase database){
        this.setupRender();
        this.draw3D(renderable.getData(database));
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