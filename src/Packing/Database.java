package Packing;

import java.util.ArrayList;
import java.util.Arrays;

public class Database implements UnitDatabase {
    // 2 x 2 x 4
    Unit[] A;
    // 2 x 3 x 4
    Unit[] B;
    // 3 x 3 x 3
    Unit[] C;
    ArrayList<Unit> blockArrayList = new ArrayList<>();
    public Database() {
        A = new Block[]{
                new Block(2, 2, 4, 0, 1),
                new Block(2, 4, 2, 0, 1),
                new Block(4, 2, 2, 0, 1)
        };

        B = new Block[]{
                new Block(2, 3, 4, 0, 2),
                new Block(2, 4, 3, 0, 2),
                new Block(3, 2, 4, 0, 2),
                new Block(3, 4, 2, 0, 2),
                new Block(4, 2, 3, 0, 2),
                new Block(4, 3, 2, 0, 2)
        };

        C = new Block[]{
                new Block(3, 3, 3, 0, 3)
        };

        blockArrayList.addAll(Arrays.asList(A));
        blockArrayList.addAll(Arrays.asList(B));
        blockArrayList.addAll(Arrays.asList(C));
    }

    @Override
    public ArrayList<Unit> getBlockArrayList() {
        return this.blockArrayList;
    }
}
