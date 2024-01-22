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


public class DlxSearch implements Renderable{
    int price = 0;
    Grid grid = new Grid(33,5,8);
    public Grid dlxSearch(UnitDatabase database) {
        Object solver;
        LinkedList<N.RNode> solution_stack;
        if(database.getBlockArrayList().size() > 10) {
            solver = new CargoX(33, 5, 8, PentominoDatabase.database);
            solution_stack = ((CargoX) solver).solvePacking();
        }
        else {
            solver = new CargoXBest(33, 5, 8, ParcelDatabase.database);
            ((CargoXBest) solver).setTimeLimit(30);
            solution_stack = ((CargoXBest) solver).solvePacking();
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

        grid.score = price;
        System.out.println("Price: " + price);
        System.out.println(" ,Percentage of cargo filled: " + (space/(double)total_space));
        System.out.println(" ,Price per block: " + (price/(double)total_space));

        return grid;
    }

    @Override
    public AlgoResponse getData(AlgoRequest algoRequest) {
        System.out.println(Arrays.toString(algoRequest.values));
        Grid result = dlxSearch(algoRequest.database);
        int score = result.score;
        return new AlgoResponse(result.grid, result.score);
    }
}
