package Packing;
import java.util.Random;

public class RandomSearch {
    static Grid grid = new Grid(33, 5, 8);
    static Database database = new Database();

    static void randomSearch() {
        int numberOfIterations = 10000;

        for (int i = 0; i < numberOfIterations; i++) {
            int x = generateRandomNumber(grid.nX);
            int y = generateRandomNumber(grid.nY);
            int z = generateRandomNumber(grid.nZ);

            int blockIndex = generateRandomNumber(database.blockArrayList.size());

            if (grid.validPlacement(x, y, z, database.blockArrayList.get(blockIndex)))
                grid.placeBlock(x, y, z, database.blockArrayList.get(blockIndex));

            if(i % 10 == 0)
                System.out.println("Number of empty spaces is: " + grid.numberOfEmptySpaces);
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
