package Packing;

import java.util.Arrays;
import java.util.TreeMap;

public class Block implements Unit{
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

    @Override
    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public int[][][] getVolume() {
        return this.volume;
    }

    @Override
    public int getColor() {
        return this.color;
    }

    public int getValue() {
        return this.value;
    }
}
