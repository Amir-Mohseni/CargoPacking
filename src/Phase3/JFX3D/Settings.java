package Phase3.JFX3D;

import javafx.scene.paint.Color;

import java.util.Map;

public class Settings {
    public static class Window{
        public static final Color BACKGROUND_COLOR = Color.WHITE;
        public static final Color LIGHT_COLOR = Color.WHITE;

        public static final int CONTENT_WINDOW_WIDTH = 720;
        public static final int CONTENT_WINDOW_HEIGHT = 720;

        public static final int MENU_WINDOW_WIDTH = 480;
        public static final int MENU_WINDOW_HEIGHT = 640;
    }

    public static class Camera {
        public static final int OFFSET_X = 0;
        public static final int OFFSET_Y = 0;
        public static final int OFFSET_Z = -2150;
        public static final int NEAR_CLIP = 1;
        public static final int FAR_CLIP = 100000;
    }

    public static class Cubes {
        public static final int CUBE_LENGTH = 30;
        public static final int CUBE_SPACING = 0;

        public static final Map<Integer, String> COLOR_MAP = Map.ofEntries(
                Map.entry(1, "#FF0000"),
                Map.entry(2, "#00FF00"),
                Map.entry(3, "#0000ff")
        );
    }

}
