package ImprovedAlgorithms.greedy;
import ImprovedAlgorithms.Search;
import ImprovedAlgorithms.data.PentominoesDatabase;
import ImprovedAlgorithms.data.ParcelDatabase;
import Packing.Grid;
import Packing.UnitDatabase;
import Phase3.JFX3D.AlgoRequest;
import Phase3.JFX3D.AlgoResponse;
import Phase3.JFX3D.Renderable;

/**
 * This class includes the methods to perform a Greedy search to solve the bin packing / knapsack problem.
 */

public class GrSearch extends Search implements Renderable {

    public Grid search(Grid grid, int[] values, boolean isParcel) {
        boolean[][][][][] database;


        if (isParcel) {
            database = ParcelDatabase.getDatabase();
        } else {
            database = PentominoesDatabase.getDatabase();
        }

        for (int i = 0; i < 3; i++) { // for every parcel

            int largest = 0;

            for (int k = 0; k < values.length; k++) {
                if (values[k] > values[largest]) largest = k;
            }


            for (int j = 0; j < database[largest].length; j++) { // for every rotation
                boolean[][][] pieceToPlace = database[largest][j];

                boolean isRunning = true;
                while (isRunning) {
                    isRunning = addBottomBackLeftForGreedy(grid.grid, pieceToPlace, largest + 1, values);
                }
                addBottomBackLeftForGreedy(grid.grid, pieceToPlace, largest + 1, values);
            }

            values[largest] = 0;
        }
        return grid;
    }

    @Override
    public AlgoResponse getData(AlgoRequest algoRequest) {
        value = 0;
        int[] values = algoRequest.values;
        Grid grid = new Grid(33, 5, 8);
        UnitDatabase database = algoRequest.database;
        boolean isParcel = true;
        if (database.getBlockArrayList().size() > 10)
            isParcel = false;
        Grid result = search(grid, values, isParcel);
        result.score = value;
        return new AlgoResponse(result.grid, result.score);
    }
}