package Packing;
import Phase3.JFX3D.AlgoRequest;
import Phase3.JFX3D.AlgoResponse;
import Phase3.JFX3D.Renderable;

import java.util.List;
import java.util.Random;

public class GreedySearch implements Renderable{
    private Grid grid = new Grid(33, 5, 8);

    public Grid greedySearch(UnitDatabase database) {
        grid = new Grid(33, 5, 8);

        System.out.println("Number of empty cells: " + grid.numberOfEmptySpaces);

        while (true){

            boolean foundPlacement = false;

            //method to iterate through all empty cells
            for (Cell cell : grid.emptyCells) {
                int x = cell.getX();
                int y = cell.getY();
                int z = cell.getZ();

                int blockIndex = findPlacement(x, y, z, database);

                if (grid.validPlacement(x, y, z, database.getBlockArrayList().get(blockIndex))) {
                    grid.placeBlock(x, y, z, database.getBlockArrayList().get(blockIndex));
                    foundPlacement = true;
                    break;
                }
            }

            if (!foundPlacement)
                break;
        }
        System.out.println("Score: " + grid.score);
        return grid;
    }
    private int findPlacement(int x, int y, int z, UnitDatabase database) {
        int j = 0;
        for (int i = 0; i < database.getBlockArrayList().size(); i++) {
            if (grid.validPlacement(x, y, z, database.getBlockArrayList().get(i))) {
                j = i;
            }
        }
        return j;
    }
    static void shuffle(List<Block> blocks) {
        Random rand = new Random();
        for (int i = blocks.size() - 1; i > 0; i--) {
            int index = rand.nextInt(i + 1);
            // Simple swap
            Block a = blocks.get(index);
            blocks.set(index, blocks.get(i));
            blocks.set(i, a);
        }
    }
    @Override
    public AlgoResponse getData(AlgoRequest algoRequest) {
        Grid result = greedySearch(algoRequest.database);
        return new AlgoResponse(result.grid, result.score);
    }
}

