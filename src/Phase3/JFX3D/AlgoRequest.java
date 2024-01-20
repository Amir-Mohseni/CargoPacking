package Phase3.JFX3D;

import Packing.UnitDatabase;

public class AlgoRequest {
    public UnitDatabase database;
    public int[] values;
    public Updatable updatable;

    public AlgoRequest(UnitDatabase unitDatabase, int[] values, Updatable updatable) {
        this.database = unitDatabase;
        this.values = values;
        this.updatable = updatable;
    }

}
