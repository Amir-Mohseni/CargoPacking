package Packing;
import Phase3.JFX3D.Renderable;

import java.util.ArrayList;
import java.util.Collection;

public class GreedySearch implements Renderable {
    public Grid grid = new Grid(33, 5, 8);

    public Grid greedySearch(UnitDatabase database) {

        System.out.println("Number of empty cells: " + grid.numberOfEmptySpaces);

        while(!grid.emptyCells.isEmpty()) {
            ArrayList <int[]> options = new ArrayList<>();
            Cell emptyCell = grid.emptyCells.getFirst();
            grid.emptyCells.remove(emptyCell);
            for (int blockIndex = 0; blockIndex < database.getBlockArrayList().size(); blockIndex++) {
                int x = emptyCell.x;
                int y = emptyCell.y;
                int z = emptyCell.z;

                if (grid.validPlacement(x, y, z, database.getBlockArrayList().get(blockIndex))) {
                    options.add(new int[]{database.getBlockArrayList().get(blockIndex).getValue(), x, y, z, blockIndex});
//                    grid.placeBlock(x, y, z, database.getBlockArrayList().get(blockIndex));
//                    break;
                }
            }
            //sort options by first element, bigger first
            if(!options.isEmpty()) {
                options.sort((o1, o2) -> o2[0] - o1[0]);
                System.out.println(options.get(0)[0]);
                grid.placeBlock(options.get(0)[1], options.get(0)[2], options.get(0)[3], database.getBlockArrayList().get(options.get(0)[4]));
            }
        }
        System.out.println("Number of empty cells: " + grid.numberOfEmptySpaces);
        System.out.println("Score: " + grid.score);
        return grid;
    }

    @Override
    public int[][][] getData(UnitDatabase database){
        return greedySearch(database).grid;
    }

    @Override
    public int getScore() {
        return grid.score;
    }

}
