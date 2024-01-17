package Packing;

import java.io.Serializable;

public class Cell implements Comparable<Cell> {
    int x;
    int y;
    int z;

    Cell(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final Cell other = (Cell) obj;
        return this.x == other.x && this.y == other.y && this.z == other.z;
    }

    @Override
    public int compareTo(Cell cell) {
        if (this.x < cell.x)
            return -1;
        else if (this.x > cell.x)
            return 1;
        else {
            if (this.y < cell.y)
                return -1;
            else if (this.y > cell.y)
                return 1;
            else {
                if (this.z < cell.z)
                    return -1;
                else if (this.z > cell.z)
                    return 1;
                else
                    return 0;
            }
        }
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getZ() {
        return z;
    }
}
