package Packing;

import java.util.TreeSet;

public class Grid {
    int nX;
    int nY;
    int nZ;
    int[][][] grid;
    int numberOfEmptySpaces;

    Grid(int nX, int nY, int nZ) {
        this.nX = nX;
        this.nY = nY;
        this.nZ = nZ;

        numberOfEmptySpaces = nX * nY * nZ;

        grid = new int[nX][nY][nZ];
    }

    boolean validPlacement(int x, int y, int z, Block block) {
        int[][][] volume = block.volume;
        for (int i = 0; i < volume.length; i++) {
            for (int j = 0; j < volume[i].length; j++) {
                for (int k = 0; k < volume[i][j].length; k++) {
                    if(volume[i][j][k] != 0) {
                        if(i + x >= nX || j + y >= nY || k + z >= nZ || grid[i + x][j + y][k + z] != 0)
                            return false;
                    }
                }
            }
        }
        return true;
    }

    void placeBlock(int x, int y, int z, Block block) {
        int[][][] volume = block.volume;
        for (int i = 0; i < volume.length; i++) {
            for (int j = 0; j < volume[i].length; j++) {
                for (int k = 0; k < volume[i][j].length; k++) {
                    if(volume[i][j][k] != 0) {
                        grid[i + x][j + y][k + z] = block.color;
                        numberOfEmptySpaces -= 1;
                    }
                }
            }
        }
    }
}
