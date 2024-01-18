package Phase3.JFX3D;

import Packing.*;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.*;
import javafx.scene.shape.Box;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Random;

public class JFX3D extends Application {
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);
    private double anchorX, anchorY;
    private double anchorAngleX = 0, anchorAngleY = 0;


    private BorderPane borderPane = new BorderPane();
    private final BorderPane menuPane = new BorderPane();
    private RotatorGroup rotatorGroup = new RotatorGroup();
    private Stage renderStage;
    private Stage menuStage = new Stage();
    private final int cubeLength = Settings.Cubes.CUBE_LENGTH;
    private final int spacing = Settings.Cubes.CUBE_SPACING;
    private Constants.Settings.AlgorithmSettings.Algorithm selectedAlgo;
    private Constants.Settings.ParcelSettings.Parcel selectedFilling;
//    private final Map<Integer, String> colorMap = new HashMap<>();
    private final Map<Integer, String> colorMap = Settings.Cubes.COLOR_MAP;
    private int value1, value2, value3;
    private int quantity1, quantity2, quantity3;

    @Override
    public void start(Stage primaryStage) {
        this.renderStage = primaryStage;
        this.setupScenes();
        this.setupUI();
        this.setupRender();
    }

    private void setupScenes(){
        this.borderPane = new BorderPane();
        Scene mainScene = new Scene(this.borderPane, Settings.Window.CONTENT_WINDOW_WIDTH, Settings.Window.CONTENT_WINDOW_HEIGHT);
        renderStage.setScene(mainScene);
        renderStage.setTitle(Constants.Windows.RENDER_WINDOW_TITLE);
        renderStage.setOnCloseRequest(t -> System.exit(0));
        renderStage.show();

        Scene menuScene = new Scene(this.menuPane, Settings.Window.MENU_WINDOW_WIDTH, Settings.Window.MENU_WINDOW_HEIGHT);
        menuStage.setScene(menuScene);
        menuStage.setTitle(Constants.Windows.UI_WINDOW_TITLE);
        menuStage.setOnCloseRequest(t -> System.exit(0));
        menuStage.show();
    }

    private void setupRender(){

        this.rotatorGroup = new RotatorGroup();
        PerspectiveCamera perspectiveCamera = new PerspectiveCamera(true);

        SubScene scene = new SubScene(rotatorGroup, Settings.Window.CONTENT_WINDOW_WIDTH, Settings.Window.CONTENT_WINDOW_HEIGHT, true, SceneAntialiasing.BALANCED);

        this.borderPane.setCenter(scene);

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
        VBox column = new VBox();
        column.setSpacing(10);
        column.setAlignment(Pos.CENTER);

        GridPane leftPane = new GridPane();
        //leftPane.setSpacing(20);
       // leftPane.setPrefSize(200,300);
        leftPane.setAlignment(Pos.CENTER);
        leftPane.setPadding(new Insets(10));
        leftPane.setHgap(10);
        leftPane.setVgap(10);


        Button startButton = new Button(Constants.Settings.Buttons.START_BUTTON_TEXT);
        startButton.setPrefSize(50,10);
        Button stopButton = new Button(Constants.Settings.Buttons.STOP_BUTTON_TEXT);
        stopButton.setPrefSize(50,10);
        Button resetButton = new Button(Constants.Settings.Buttons.RESET_BUTTON_TEXT);
        resetButton.setPrefSize(50,10);

        Label label1 = new Label(Constants.Settings.Title.UI_DISPLAY_TITLE_TEXT);
        label1.setFont(Font.font(Constants.Settings.Title.UI_DISPLAY_TITLE_FONT, Constants.Settings.Title.UI_DISPLAY_TITLE_WEIGHT, Constants.Settings.Title.UI_DISPLAY_TITLE_SIZE));
        //leftPane.getChildren().add(label1);

        Menu algorithmMenu = new Menu(Constants.Settings.AlgorithmSettings.ALGORITHM_MENU_TEXT);
        MenuItem random = new MenuItem(Constants.Settings.AlgorithmSettings.Algorithm.RANDOM.toString());
        MenuItem greedy = new MenuItem(Constants.Settings.AlgorithmSettings.Algorithm.GREEDY.toString());
        MenuItem algoX = new MenuItem(Constants.Settings.AlgorithmSettings.Algorithm.ALGORITHM_X.toString());

        random.setOnAction(e->{this.selectedAlgo = Constants.Settings.AlgorithmSettings.Algorithm.RANDOM;});
        greedy.setOnAction(e->{this.selectedAlgo = Constants.Settings.AlgorithmSettings.Algorithm.GREEDY;});
        algoX.setOnAction(e->{this.selectedAlgo = Constants.Settings.AlgorithmSettings.Algorithm.ALGORITHM_X;});
        algorithmMenu.getItems().addAll(random,greedy,algoX);

        MenuBar menuBar1 = new MenuBar();
        menuBar1.getMenus().add(algorithmMenu);
        setWidthOfMenuBar(100,menuBar1);
        //leftPane.getChildren().add(menuBar1);

        Menu typeOfFilling = new Menu(Constants.Settings.ParcelSettings.PARCEL_MENU_TEXT);
        MenuItem pentominoes = new MenuItem(Constants.Settings.ParcelSettings.Parcel.PENTOMINOES.toString());
        MenuItem cubes = new MenuItem(Constants.Settings.ParcelSettings.Parcel.BOXES.toString());

        pentominoes.setOnAction(e->{selectedFilling = Constants.Settings.ParcelSettings.Parcel.PENTOMINOES;});
        cubes.setOnAction(e->{selectedFilling = Constants.Settings.ParcelSettings.Parcel.BOXES;});

        typeOfFilling.getItems().addAll(pentominoes,cubes);

        MenuBar menuBar2 = new MenuBar();
        menuBar2.getMenus().add(typeOfFilling);
        setWidthOfMenuBar(100,menuBar2);
        //leftPane.getChildren().add(menuBar2);


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

        //leftPane.getChildren().addAll(valuesLabel,valueText1,valueText2,valueText3);
        //leftPane.getChildren().addAll(quantityLabel,quantityText1,quantityText2,quantityText3);


        //leftPane.getChildren().addAll(startButton,stopButton,resetButton);


        Label label6 = new Label("Score:");
        label6.setFont(Font.font("Arial", FontWeight.BOLD,12));
        Label scoreLabel = new Label("_");

        //leftPane.getChildren().addAll(label6,scoreLabel);

        leftPane.add(label1,1,0);
        leftPane.add(menuBar1,1,1);
        leftPane.add(menuBar2,1,2);

        leftPane.add(valuesLabel,0,3);
        leftPane.add(valueText1,0,4);
        leftPane.add(valueText2,1,4);
        leftPane.add(valueText3,2,4);
        leftPane.add(quantityLabel,0,5);
        leftPane.add(quantityText1,0,6);
        leftPane.add(quantityText2,1,6);
        leftPane.add(quantityText3,2,6);
        leftPane.add(startButton,0,7);
        leftPane.add(stopButton,1,7);
        leftPane.add(resetButton,2,7);
        leftPane.add(label6,0,8);
        leftPane.add(scoreLabel,1,8);

        //column.getChildren().addAll(label1,menuBar1,menuBar2,leftPane);

        startButton.setOnAction(e ->{
            try {
                this.render((Renderable) selectedAlgo.getRenderable().getConstructor().newInstance(), (UnitDatabase) selectedFilling.getDatabase().getConstructor().newInstance());
            } catch (InstantiationException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            } catch (InvocationTargetException ex) {
                throw new RuntimeException(ex);
            } catch (NoSuchMethodException ex) {
                throw new RuntimeException(ex);
            }

            //For retrieving the input from the TextFields for values
            String valuesText1 = valueText1.getText();
            String valuesText2 = valueText2.getText();
            String valuesText3 = valueText3.getText();

            if(!valuesText1.isEmpty() && !valuesText2.isEmpty() && !valuesText3.isEmpty()){
                value1 = Integer.parseInt(valuesText1);
                value2 = Integer.parseInt(valuesText2);
                value3 = Integer.parseInt(valuesText3);
                System.out.println("Values: "+value1+", "+value2+", "+value3);
            } else {
                System.out.println("Values not entered");
            }

            //For retrieving the input from Quantity TextFields

            String quantityString1 = quantityText1.getText();
            String quantityString2 = quantityText2.getText();
            String quantityString3 = quantityText3.getText();

            if(!quantityString1.isEmpty() && !quantityString2.isEmpty() && !quantityString3.isEmpty()){
                quantity1 = Integer.parseInt(quantityString1);
                quantity2 = Integer.parseInt(quantityString2);
                quantity3 = Integer.parseInt(quantityString3);
                System.out.println("Quantities: "+quantity1+", "+quantity2+", "+quantity3);
            } else{
                System.out.println("Quantities not entered");
            }

        });
        stopButton.setOnAction(e->{});//ActionListener is empty, functionality to be added.
        resetButton.setOnAction(e -> this.setupRender());

        this.menuPane.setLeft(leftPane);
    }

    public void setWidthOfTextField(int width, TextField textField){
        textField.setPrefWidth(width);
        textField.setMaxWidth(width);
    }

    public void setWidthOfMenuBar(int width, MenuBar menuBar){
        menuBar.setPrefWidth(width);
        menuBar.setMaxWidth(width);
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

                    this.rotatorGroup.getChildren().add(newBox);
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