package Packing;
import java.util.Random;

public class RandomSearch {
    static Grid grid = new Grid(33, 5, 8);
    static Database database = new Database();

    static void randomSearch() {
        int numberOfIterations = 1000000;

        System.out.println("Number of empty cells: " + grid.numberOfEmptySpaces);

        for (int i = 0; i < numberOfIterations; i++) {
//            int x = generateRandomNumber(grid.nX);
//            int y = generateRandomNumber(grid.nY);
//            int z = generateRandomNumber(grid.nZ);

            boolean foundPlacement = false;

            for (Cell emptyCell : grid.emptyCells) {
                int blockIndex = generateRandomNumber(database.blockArrayList.size());

                int x = emptyCell.x;
                int y = emptyCell.y;
                int z = emptyCell.z;

                if (grid.validPlacement(x, y, z, database.blockArrayList.get(blockIndex))) {
                    grid.placeBlock(x, y, z, database.blockArrayList.get(blockIndex));
                    foundPlacement = true;
                    break;
                }
            }

            if(i % 10 == 0)
                System.out.println("Number of empty spaces is: " + grid.emptyCells.size());

            if (!foundPlacement)
                break;
        }
    }

    static int generateRandomNumber(int upperbound) { //From 0 to upperbound - 1
        Random rand = new Random();
        return rand.nextInt(upperbound);
    }

    public static void main(String[] args) {
        randomSearch();
    }
}
