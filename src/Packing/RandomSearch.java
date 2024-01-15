package Packing;
import Phase3.JFX3D.Renderable;

import java.util.Random;

public class RandomSearch implements Renderable {
    public Grid grid = new Grid(33, 5, 8);

    public Grid randomSearch(UnitDatabase database) {
        int numberOfIterations = 1000000;
        int count = 1;

        System.out.println("Number of empty cells: " + grid.numberOfEmptySpaces);

        for (int i = 0; i < numberOfIterations; i++) {
//            int x = generateRandomNumber(grid.nX);
//            int y = generateRandomNumber(grid.nY);
//            int z = generateRandomNumber(grid.nZ);

            boolean foundPlacement = false;

            for (Cell emptyCell : grid.emptyCells) {
                int blockIndex = generateRandomNumber(database.getBlockArrayList().size());

                int x = emptyCell.x;
                int y = emptyCell.y;
                int z = emptyCell.z;

                if (grid.validPlacement(x, y, z, database.getBlockArrayList().get(blockIndex))) {
                    grid.placeBlock(x, y, z, database.getBlockArrayList().get(blockIndex));
                    foundPlacement = true;
                    break;
                }
            }

            if(i % 10 == 0)
                System.out.println("Number of empty spaces is: " + grid.emptyCells.size());

            if (!foundPlacement)
                break;
        }
        return grid;
    }

    private int generateRandomNumber(int upperbound) { //From 0 to upperbound - 1
        Random rand = new Random();
        return rand.nextInt(upperbound);
    }

    @Override
    public int[][][] getData(UnitDatabase database){
        return randomSearch(database).grid;
    }

    @Override
    public int getScore() {
        return grid.score;
    }

}
