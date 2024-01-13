package Packing;

import java.util.Arrays;
import java.util.TreeMap;

public class Block {
    int[][][] volume;
    int value;
    int color;

    Block(int nX, int nY, int nZ, int value, int color) {
        this.volume = new int[nX][nY][nZ];
        this.value = value;
        this.color = color;

        for (int[][] row : this.volume)
            for (int[] anInt : row) Arrays.fill(anInt, 1);
    }
}
