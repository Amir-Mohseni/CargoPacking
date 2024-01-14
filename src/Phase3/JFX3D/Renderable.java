package Phase3.JFX3D;

import Packing.Database;
import Packing.UnitDatabase;

public interface Renderable {
    int[][][] getData(UnitDatabase database);
    int getScore();
}
