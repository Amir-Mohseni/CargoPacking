package Phase3.JFX3D;

import Packing.*;
import javafx.scene.text.FontWeight;

public class Constants {
    public static class Windows{
        public static final String RENDER_WINDOW_TITLE = "Packing Render";
        public static final String UI_WINDOW_TITLE = "Menu";
    }
    public static class Settings{
        public static class Buttons{
            public static final String START_BUTTON_TEXT = "Start";
            public static final String STOP_BUTTON_TEXT = "Stop";
            public static final String RESET_BUTTON_TEXT = "Reset";
        }

        public static class Title{
            public static final String UI_DISPLAY_TITLE_TEXT = "3D PACKING SOLVER";
            public static final String UI_DISPLAY_TITLE_FONT = "Times New Roman";
            public static final FontWeight UI_DISPLAY_TITLE_WEIGHT = FontWeight.BOLD;
            public static final int UI_DISPLAY_TITLE_SIZE = 23;
        }

        public static class AlgorithmSettings{
            public static final String ALGORITHM_MENU_TEXT = "Algorithms";

            public enum Algorithm {
                RANDOM(RandomSearch.class,"Random"),
                GREEDY(GreedySearch.class, "Greedy"),
                ALGORITHM_X(null,"Algorithm X");

                private final String name;
                private final Class renderable;

                Algorithm(Class renderable, String name) {
                    this.name = name;
                    this.renderable = renderable;
                }

                public Class getRenderable(){
                    return this.renderable;
                }

                public boolean equalsName(String otherName) {
                    return name.equals(otherName);
                }

                public String toString() {
                    return this.name;
                }
            }
        }

        public static class ParcelSettings {
            public static final String PARCEL_MENU_TEXT = "Parcels";

            public enum Parcel {
                PENTOMINOES(PentominoDatabase.class, "Pentominoes"),
                BOXES(BoxesDatabase.class, "Boxes");

                private final String name;
                private final Class database;

                Parcel(Class database, String name) {
                    this.name = name;
                    this.database = database;
                }

                public Class getDatabase(){
                    return this.database;
                }

                public boolean equalsName(String otherName) {
                    return name.equals(otherName);
                }

                public String toString() {
                    return this.name;
                }
            }
        }
    }
}
