package Packing;
import Phase3.JFX3D.Renderable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GreedySearch implements Renderable{
    static Grid grid = new Grid(33, 5, 8);
    static UnitDatabase database;

    static Grid greedySearch(UnitDatabase database) {
        int numberOfIterations = 1000000;
        int count = 1;

        System.out.println("Number of empty cells: " + grid.numberOfEmptySpaces);

        for (int i = 0; i < numberOfIterations; i++) {

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

            System.out.println("Number of empty spaces is: " + grid.emptyCells.size());

            if (!foundPlacement)
                break;
        }
        return grid;
    }
    static int findPlacement(int x, int y, int z, UnitDatabase database) {
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
//    public void run() {
//        greedySearch();
//    }
//    public int[][][] getData(){
//        return greedySearch().grid;
//    }

    @Override
    public int[][][] getData(UnitDatabase database) {
        return greedySearch(database).grid;
    }

    @Override
    public int getScore() {
        return 0;
    }
}

