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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;


public class JFX3D extends Application implements Updatable {
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);
    private double anchorX, anchorY;
    private double anchorAngleX = 0, anchorAngleY = 0;


    private BorderPane renderPane = new BorderPane();
//    private final BorderPane menuPane = new BorderPane();

    private RotatorGroup rotatorGroup = new RotatorGroup();
    private Stage renderStage;
//    private Stage menuStage = new Stage();
    private final int cubeLength = Settings.Cubes.CUBE_LENGTH;
    private final int spacing = Settings.Cubes.CUBE_SPACING;
    private Constants.Settings.AlgorithmSettings.Algorithm selectedAlgo = Constants.Settings.AlgorithmSettings.Algorithm.RANDOM;
    private Constants.Settings.ParcelSettings.Parcel selectedFilling = Constants.Settings.ParcelSettings.Parcel.BOXES;
    private final Map<Integer, String> colorMap = Settings.Cubes.COLOR_MAP;
    private Constants.Settings.AlgorithmSettings.CoverageMode coverageMode = Constants.Settings.AlgorithmSettings.CoverageMode.MAXIMUM_COVERAGE;
    private int[] parcelValues = new int[]{1, 1, 1};
    private int quantity1, quantity2, quantity3;

    private int[][][] currentData = new int[0][0][0];
    private int[] positionValues = new int[3];
    private Label currentScoreLabel;


    @Override
    public void start(Stage primaryStage) {
        this.renderStage = primaryStage;
        this.setupScenes();
        this.setupUI();
        this.setupRender();
    }

    private void setupScenes(){
        this.renderPane = new BorderPane();
        Scene mainScene = new Scene(this.renderPane, Settings.Window.MAIN_WINDOW_WIDTH, Settings.Window.MAIN_WINDOW_HEIGHT);
        renderStage.setScene(mainScene);
        renderStage.setTitle(Constants.Windows.RENDER_WINDOW_TITLE);
        renderStage.setOnCloseRequest(t -> System.exit(0));
        renderStage.show();

//        Scene menuScene = new Scene(this.menuPane, Settings.Window.MENU_WINDOW_WIDTH, Settings.Window.MENU_WINDOW_HEIGHT);
//        menuStage.setScene(menuScene);
//        menuStage.setTitle(Constants.Windows.UI_WINDOW_TITLE);
//        menuStage.setOnCloseRequest(t -> System.exit(0));
//        menuStage.show();
    }

    private void setupRender(){

        this.rotatorGroup = new RotatorGroup();
        PerspectiveCamera perspectiveCamera = new PerspectiveCamera(true);

        SubScene scene = new SubScene(rotatorGroup, Settings.Window.CONTENT_WINDOW_WIDTH, Settings.Window.CONTENT_WINDOW_HEIGHT, true, SceneAntialiasing.BALANCED);

        this.renderPane.setRight(scene);

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
        this.rotatorGroup.getChildren().add(pointLight1);
        initMouseControl(this.rotatorGroup, scene, renderStage);

    }


    private void setupUI(){
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
        MenuItem greedy2 = new MenuItem("Greedy2");
        MenuItem genetic = new MenuItem("Genetic");
        MenuItem genetic2 = new MenuItem("Genetic2");
        MenuItem algoX = new MenuItem("Algorithm X");

        algorithmMenu.getItems().addAll(
                random,
                greedy,
                greedy2,
                genetic,
                genetic2,
                algoX
        );

        MenuBar menuBar1 = new MenuBar();
        menuBar1.getMenus().add(algorithmMenu);
        setWidthOfMenuBar(100,menuBar1);

        random.fire();

        Menu typeOfFilling = new Menu("Parcels");
        MenuItem pentominoes = new MenuItem("Pentominoes");
        MenuItem boxes = new MenuItem("Boxes");

        typeOfFilling.getItems().addAll(
                pentominoes,
                boxes
        );

        MenuBar menuBar2 = new MenuBar();
        menuBar2.getMenus().add(typeOfFilling);
        setWidthOfMenuBar(100,menuBar2);

        boxes.fire();


        Label selectedAlgorithmLabel = new Label("Random");
        Label selectedParcelLabel = new Label("Boxes");


        Label valuesLabel = new Label("Values");
        valuesLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 23));
        TextField parcelValueText1 = new TextField("1");
        TextField parcelValueText2 = new TextField("1");
        TextField parcelValueText3 = new TextField("1");

        Label quantityLabel = new Label("Quantity");
        quantityLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 23));
        TextField quantityText1 = new TextField();
        TextField quantityText2 = new TextField();
        TextField quantityText3 = new TextField();

        setWidthOfTextField(50,parcelValueText1);
        setWidthOfTextField(50,parcelValueText2);
        setWidthOfTextField(50,parcelValueText3);
        setWidthOfTextField(50,quantityText1);
        setWidthOfTextField(50,quantityText2);
        setWidthOfTextField(50,quantityText3);

        Label scoreLabel = new Label("Score:");
        scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD,12));
        this.currentScoreLabel = new Label();

        ScrollBar scrollBarX = new ScrollBar();
        ScrollBar scrollBarY = new ScrollBar();
        ScrollBar scrollBarZ = new ScrollBar();

        SIMD.doSIMD(new ScrollBar[]{scrollBarX, scrollBarY, scrollBarZ}, item -> {
            item.setUnitIncrement(1);
            item.setPrefWidth(300);
        });


//        scrollBarX.setPrefWidth(300);
//        scrollBarY.setPrefWidth(300);
//        scrollBarZ.setPrefWidth(300);

        setParametersScrollBar(scrollBarX,0,33-1);
        setParametersScrollBar(scrollBarY,0,5-1);
        setParametersScrollBar(scrollBarZ,0,8-1);

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
        valuesTextFieldGroup.getChildren().addAll(parcelValueText1,parcelValueText2,parcelValueText3);
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


        Label checkBoxLabel = new Label("Select options:");
        HBox checkBoxGroup = new HBox();

        RadioButton totalCoverageRadioButton = new RadioButton("Maximize coverage");
        RadioButton maximizeScoreRadioButton = new RadioButton("Maximize score");
        ToggleGroup coverageToggleGroup = new ToggleGroup();
        totalCoverageRadioButton.setToggleGroup(coverageToggleGroup);
        maximizeScoreRadioButton.setToggleGroup(coverageToggleGroup);

        checkBoxGroup.getChildren().addAll(totalCoverageRadioButton,maximizeScoreRadioButton);
        checkBoxGroup.setSpacing(10);
        checkBoxGroup.setAlignment(Pos.CENTER);

        totalCoverageRadioButton.setSelected(true);

        VBox mainGroup = new VBox();
        mainGroup.getChildren().addAll(label1,algorithmMenuGroup,parcelMenuGroup,valuesLabel,valuesTextFieldGroup,quantityLabel,quantityTextFieldGroup,
        buttonsGroup,checkBoxLabel,checkBoxGroup,scoreGroup,scrollerXGroup,scrollerYGroup,scrollerZGroup);
        mainGroup.setAlignment(Pos.CENTER);
        mainGroup.setPadding(new Insets(10));
        mainGroup.setSpacing(15);

        //ActionListener for StartButton
        startButton.setOnAction(e ->{
            try {
                this.parcelValues[0] = Integer.parseInt(parcelValueText1.getText());
                this.parcelValues[1] = Integer.parseInt(parcelValueText2.getText());
                this.parcelValues[2] = Integer.parseInt(parcelValueText3.getText());
            } catch (NumberFormatException ex){}
            this.renderStage.requestFocus();
            this.update();


            System.out.println("Values: "+ parcelValues[0] +", "+ parcelValues[1] +", "+ parcelValues[2]);
            System.out.println("Quantities: "+quantity1+", "+quantity2+", "+quantity3);

        });

        totalCoverageRadioButton.setOnAction(e->{
            if(totalCoverageRadioButton.isSelected()){
                this.coverageMode = Constants.Settings.AlgorithmSettings.CoverageMode.MAXIMUM_COVERAGE;
            }
        });

        maximizeScoreRadioButton.setOnAction(e->{
            if(maximizeScoreRadioButton.isSelected()){
                this.coverageMode = Constants.Settings.AlgorithmSettings.CoverageMode.MAXIMUM_SCORE;
            }
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
        greedy2.setOnAction(e->{
            selectedAlgo = Constants.Settings.AlgorithmSettings.Algorithm.GREEDY2;
            selectedAlgorithmLabel.setText(selectedAlgo.getName());
        });
        genetic.setOnAction(e->{
            selectedAlgo = Constants.Settings.AlgorithmSettings.Algorithm.GENETIC;
            selectedAlgorithmLabel.setText(selectedAlgo.getName());
        });
        genetic2.setOnAction(e->{
            selectedAlgo = Constants.Settings.AlgorithmSettings.Algorithm.GENETIC2;
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
        boxes.setOnAction(e->{
            selectedFilling = Constants.Settings.ParcelSettings.Parcel.BOXES;
            selectedParcelLabel.setText(selectedFilling.getName());
        });

        //TODO: Set ActionListener for the currentScoreLabel

        //ActionListeners for getting the text from the value text cells
        parcelValueText1.setOnAction(e->{
            String text = parcelValueText1.getText();
            if(!text.isEmpty()){
                parcelValues[0] = Integer.parseInt(text);}
        });
        parcelValueText2.setOnAction(e->{
            String text = parcelValueText2.getText();
            if(!text.isEmpty()){
                parcelValues[1] = Integer.parseInt(text);}
        });
        parcelValueText3.setOnAction(e->{
            String text = parcelValueText3.getText();
            if(!text.isEmpty()){
                parcelValues[2] = Integer.parseInt(text);}
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
            int currentPosition = (int) Math.floor(newValue.doubleValue());
            if (currentPosition != this.positionValues[0]){
                this.positionValues[0] = currentPosition;
                update(this.positionValues);
            }
        });
        scrollBarY.valueProperty().addListener((observable,oldValue,newValue)->{
            int currentPosition = (int) Math.floor(newValue.doubleValue());
            if (currentPosition != this.positionValues[1]){
                this.positionValues[1] = currentPosition;
                update(this.positionValues);
            }
        });
        scrollBarZ.valueProperty().addListener((observable,oldValue,newValue)->{
            int currentPosition = (int) Math.floor(newValue.doubleValue());
            if (currentPosition != this.positionValues[2]){
                this.positionValues[2] = currentPosition;
                update(this.positionValues);
            }
        });


        stopButton.setOnAction(e->{});//ActionListener is empty, functionality to be added. TODO
        resetButton.setOnAction(e -> this.setupRender());
        this.renderPane.setLeft(mainGroup);
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

    private int calculateScore(int[][][] data, int[] values){
        BigDecimal score = BigDecimal.valueOf(0);
        for (int dimX = 0; dimX < data.length; dimX++) {
            for (int dimY = 0; dimY < data[dimX].length; dimY++) {
                for (int dimZ = 0; dimZ < data[dimX][dimY].length; dimZ++) {
                    int id = data[dimX][dimY][dimZ];
                    BigDecimal unit_value = BigDecimal.valueOf(0);
                    switch (id) {
                        case 0:
                            continue;

                            // parcel A
                        case 1:
                            unit_value = BigDecimal.valueOf(parcelValues[id-1]).divide(BigDecimal.valueOf(16), 10, RoundingMode.FLOOR);
                            break;

                        // parcel B
                        case 2:
                            unit_value = BigDecimal.valueOf(parcelValues[id-1]).divide(BigDecimal.valueOf(24), 10, RoundingMode.FLOOR);
                            break;

                        // parcel C
                        case 3:
                            unit_value = BigDecimal.valueOf(parcelValues[id-1]).divide(BigDecimal.valueOf(27), 10, RoundingMode.FLOOR);
                            break;

                    }
                    score = score.add(unit_value);
                }
            }
        }
        return score.setScale(0, RoundingMode.CEILING).intValue();
    }


    public void update() {
        try{
            AlgoResponse algoResponse;

            switch (this.coverageMode){
                case Constants.Settings.AlgorithmSettings.CoverageMode.MAXIMUM_COVERAGE:
                    algoResponse = ((Renderable) this.selectedAlgo.getRenderable().getConstructor().newInstance()).getData(new AlgoRequest((UnitDatabase) this.selectedFilling.getDatabase().getConstructor().newInstance(), new int[]{-1, -1, -1}, this));
                    break;

                default:
                    algoResponse = ((Renderable) this.selectedAlgo.getRenderable().getConstructor().newInstance()).getData(new AlgoRequest((UnitDatabase) this.selectedFilling.getDatabase().getConstructor().newInstance(), parcelValues, this));
                    break;
            }

            this.currentData = algoResponse.data;
            int currentScore = this.calculateScore(this.currentData, this.parcelValues);
            System.out.println("Current score: " + currentScore);
            currentScoreLabel.setText(String.valueOf(currentScore));
            this.setupRender();
            this.draw3D(this.currentData, this.positionValues);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(int[] startingPositions) {
        System.out.println("Slice: " + Arrays.toString(this.positionValues));
        this.setupRender();
        this.draw3D(this.currentData, startingPositions);
    }

    private void draw3D(int[][][] data, int[] start) {
        try{
            int lenX = data.length, lenY = data[0].length, lenZ = data[0][0].length;
            int midX = Math.ceilDiv(lenX, 2), midY = Math.ceilDiv(lenY, 2), midZ = Math.ceilDiv(lenZ, 2);

            for (int dimX = start[0]; dimX < data.length; dimX++){
                for (int dimY = start[1]; dimY < data[dimX].length; dimY++){
                    for (int dimZ = start[2]; dimZ < data[dimX][dimY].length; dimZ++){
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

                        this.rotatorGroup.getChildren().add(newBox);
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e){}
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