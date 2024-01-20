package Phase3.JFX3D;

import Packing.UnitDatabase;

public class AlgoRequest {
    public UnitDatabase database;
    public int[] values;
    public Updatable updatable;

    public AlgoRequest(UnitDatabase unitDatabase, int[] values, Updatable updatable) {
        this.database = unitDatabase;
        this.values = values;
        for (int i = 0; i < database.getBlockArrayList().size(); i++) {
            int[][][] vol = database.getBlockArrayList().get(i).getVolume();
            int oneCount = 0;
            int index = database.getBlockArrayList().get(i).getColor() - 1;
            for (int x = 0; x < vol.length; x++)
                for (int y = 0; y < vol[0].length; y++)
                    for (int z = 0; z < vol[0][0].length; z++)
                        oneCount += vol[x][y][z];
            if(values[index] == -1)
                database.getBlockArrayList().get(i).setValue(oneCount);
            else
                database.getBlockArrayList().get(i).setValue(values[index]);
        }
        this.updatable = updatable;
    }

}
