package Phase3.JFX3D;

import Packing.UnitDatabase;

public interface Renderable {
    int[][][] getData(UnitDatabase database, Updatable updatable);
    int getScore();
}
