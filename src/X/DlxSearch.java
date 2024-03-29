package X;

import Packing.Grid;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

import Packing.Unit;
import Packing.UnitDatabase;
import Phase3.JFX3D.AlgoRequest;
import Phase3.JFX3D.AlgoResponse;
import Phase3.JFX3D.Renderable;
import Phase3.JFX3D.Updatable;


public class DlxSearch implements Renderable {
    int price = 0;
    Grid grid = new Grid(33,5,8);
    public Grid dlxSearch(UnitDatabase database, double[] values) {
        Object solver;
        LinkedList<N.RNode> solution_stack;
        if(database.getBlockArrayList().size() > 10) {
            if(values[0] == -1) {
                solver = new CargoX(33, 5, 8, PentominoDatabase.database);
                solution_stack = ((CargoX) solver).solvePacking();
            }
            else {
                solver = new CargoXBest(33, 5, 8, PentominoDatabase.database);
                ((CargoXBest) solver).setTimeLimit(30);
                solution_stack = ((CargoXBest) solver).solvePacking(new int[]{300, 300, 300}, values);
            }
            //if the values are all -1 use this code otherwise use cargoXBest
        }
        else {
            solver = new CargoXBest(33, 5, 8, ParcelDatabase.database);
            ((CargoXBest) solver).setTimeLimit(30);
            if(values[0] == -1)
                solution_stack = ((CargoXBest) solver).solvePacking();
            else
                solution_stack = ((CargoXBest) solver).solvePacking(new int[]{16, 62, 82}, values);
        }



        int range = solution_stack.size();
        Iterator<N.RNode> it = solution_stack.iterator();
        int space = 0;
        int total_space = 33 * 8 * 5;

        for(int i = 0; i != range; ++i){
            N.RNode n = it.next();
            price += (int) n.getPrice();
            space += n.getSpace();

            int[][][] piece = n.getPiece();
            int[] coords = n.getCoords();
            int c = n.getColor();

            for(int x = 0; x != piece.length; ++x){
                for(int y = 0; y != piece[0].length; ++y){
                    for(int z = 0; z != piece[0][0].length; ++z){
                        if(piece[x][y][z] == 1){
                            grid.grid[x+coords[0]][y+coords[1]][z+coords[2]] = c;
                        }
                    }
                }
            }
        }

        this.grid.score = price;
        System.out.println("Price: " + price);
        System.out.println(" ,Percentage of cargo filled: " + (space/(double)total_space));
        System.out.println(" ,Price per block: " + (price/(double)total_space));

        int[] count = new int[3];

        for (int i = 0; i < grid.grid.length; i++) {
            for (int j = 0; j < grid.grid[i].length; j++) {
                for (int k = 0; k < grid.grid[i][j].length; k++) {
                    if(grid.grid[i][j][k] == 0)
                        continue;
                    count[grid.grid[i][j][k] - 1]++;
                }
            }
        }

        if(database.getBlockArrayList().size() > 10) {
            count[0] /= 5;
            count[1] /= 5;
            count[2] /= 5;

            if(values[0] == -1)
                values = new double[]{5, 5, 5};
            grid.score = (int) (count[0] * values[0] + count[1] * values[1] + count[2] * values[2]);
        }
        else {
            count[0] /= 16;
            count[1] /= 24;
            count[2] /= 27;

            if(values[0] == -1)
                values = new double[]{16, 24, 27};
            grid.score = (int) (count[0] * values[0] + count[1] * values[1] + count[2] * values[2]);
        }

        return grid;
    }

    @Override
    public AlgoResponse getData(AlgoRequest algoRequest) {
        System.out.println("Supplied values: " + Arrays.toString(algoRequest.values));

        double[] values = new double[3];
        for (int i = 0; i < algoRequest.values.length; i++)
            values[2 - i] = algoRequest.values[i];
        Grid result = dlxSearch(algoRequest.database, values);
        return new AlgoResponse(result.grid, result.score);
    }
}
