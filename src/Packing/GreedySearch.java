package Packing;
import Phase3.JFX3D.Renderable;

public class GreedySearch implements Renderable {
    public Grid grid = new Grid(33, 5, 8);

    public Grid greedySearch(UnitDatabase database) {

        System.out.println("Number of empty cells: " + grid.numberOfEmptySpaces);

        while(!grid.emptyCells.isEmpty()) {
            Cell emptyCell = grid.emptyCells.getFirst();
            grid.emptyCells.remove(emptyCell);
            for (int blockIndex = 0; blockIndex < database.getBlockArrayList().size(); blockIndex++) {
                int x = emptyCell.x;
                int y = emptyCell.y;
                int z = emptyCell.z;

                if (grid.validPlacement(x, y, z, database.getBlockArrayList().get(blockIndex))) {
                    grid.placeBlock(x, y, z, database.getBlockArrayList().get(blockIndex));
                    break;
                }
            }
        }
        System.out.println("Number of empty cells: " + grid.numberOfEmptySpaces);
        return grid;
    }

    @Override
    public int[][][] getData(UnitDatabase database){
        return greedySearch(database).grid;
    }

    @Override
    public int getScore() {
        return 0;
    }

}
