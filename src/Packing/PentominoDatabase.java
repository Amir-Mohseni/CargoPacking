package Packing;

import java.util.ArrayList;


// Class that serves as a generator of all possible rotations of blocks L,P,T
public class PentominoDatabase implements UnitDatabase {

    static int[][][] l_piece = new int[2][4][1];

    static int[][][] p_piece=  new int[2][3][1];

    static int[][][] t_piece = new int[3][3][1];

    public PentominoDatabase(){ // default shape matrices

        l_piece[0][0][0]= 1;
        l_piece[0][1][0]= 1;
        l_piece[0][2][0]= 1;
        l_piece[0][3][0]= 1;
        l_piece[1][0][0]= 1;

        p_piece[0][0][0] = 1;
        p_piece[0][1][0] = 1;
        p_piece[0][2][0] = 1;
        p_piece[1][1][0] = 1;
        p_piece[1][2][0] = 1;

        t_piece[0][2][0] = 1;
        t_piece[1][0][0] = 1;
        t_piece[1][1][0] = 1;
        t_piece[1][2][0] = 1;
        t_piece[2][2][0] = 1;

        build_database();
    }

    // builds database of all Parcels, with given prices ordered descending
    private void build_database(){

        int[][][][] l = build_piece(l_piece);
        int[][][][] p = build_piece(p_piece);
        int[][][][] t = build_piece(t_piece);

//        database = new ArrayList<Unit>(3);
        database = new ArrayList<Unit>();

        for (int i = 0; i < l.length; i++) {
            Block block = new Block(1, 4, 2, 1, 1);
            block.setVolume(l[i]);
            database.add(block);
        }

        for (int i = 0; i < p.length; i++) {
            Block block = new Block(1, 3, 2, 1, 2);
            block.setVolume(p[i]);
            database.add(block);
        }

        for (int i = 0; i < t.length; i++) {
            Block block = new Block(1, 3, 3, 1, 3);
            block.setVolume(t[i]);
            database.add(block);
        }

/*        database.add(new Parcel(t,5,5,1));
        database.add(new Parcel(p,4,5,2));
        database.add(new Parcel(l,3,5,3));

 */


    }

    // returns true if this rotation is already included, so we remove identical rotation matrices
    private boolean arrayIncludes(ArrayList<int[][][]> p , int[][][] piece){

        for(int[][][] perm : p){
            int x_len = perm.length, y_len  =perm[0].length, z_len = perm[0][0].length;
            if(x_len == piece.length && y_len == piece[0].length && z_len == piece[0][0].length){
                equal: {
                    for(int x = 0; x != x_len; ++x){
                        for(int y = 0; y != y_len; ++y){
                            for(int z = 0; z != z_len; ++z){
                                if(piece[x][y][z] != perm[x][y][z]) break equal;
                            }
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    // clones 3D array
    public int[][][] clone_piece(int[][][] piece){

        int[][][] clone = new int[piece.length][piece[0].length][piece[0][0].length];
        for(int x = 0; x != piece.length; ++x){
            for(int y = 0; y != piece[0].length; ++y){
                for(int z = 0; z != piece[0][0].length; ++z){
                    clone[x][y][z] = piece[x][y][z];
                }
            }
        }
        return clone;
    }

    // takes one starting 3D matrix of a piece and generated array of all 3D rotations of that piece
    public int[][][][] build_piece(int[][][] piece){
        ArrayList<int[][][]> permutations = new ArrayList<int[][][]>(24);

        // first four possible way how to face a cube/ 3D array
        for(int i = 0; i != 4; ++i){ // the rotations around Y

            piece = flipAxisY(piece);
            for(int r = 0; r != 4; ++r){
                // each facing has four rotations of the other axes
                piece = rotateAroundZ(piece);
                // add this rotation ONLY if its not already included
                if(!arrayIncludes(permutations, piece)){
                    permutations.add(clone_piece(piece));
                }
            }

        }

        // fift possible way how to face a cube
        piece = flipAxisX(piece);
        for(int r = 0; r != 4; ++r){
            // each facing has four rotations of the other axes
            piece = rotateAroundZ(piece);
            if(!arrayIncludes(permutations, piece)) permutations.add(clone_piece(piece));
        }
        piece = flipAxisX(piece);

        // sixth and the last possible way how to face a cube
        piece = flipAxisX(piece);
        for(int r = 0; r != 4; ++r){
            // each facing has four rotations of the other axes
            piece = rotateAroundZ(piece);
            if(!arrayIncludes(permutations, piece)) permutations.add(clone_piece(piece));
        }

        return permutations.toArray(new int[permutations.size()][][][]);
    }

    // rotates around X axis
    private int[][][] flipAxisX(int[][][] piece){

        int[][][] rotated_piece = new int[piece.length][piece[0][0].length][piece[0].length];

        for(int x = 0; x != piece.length; ++x){
            for(int y = 0; y != piece[0].length; ++y){
                for(int z = 0; z != piece[0][0].length; ++z){

                    rotated_piece[x][piece[0][0].length - z - 1][y] = piece[x][y][z];
                }
            }
        }
        return rotated_piece;
    }
    //rotates around Y axis
    private int[][][] flipAxisY(int[][][] piece){

        int[][][] rotated_piece = new int[piece[0][0].length][piece[0].length][piece.length];

        for(int x = 0; x != piece.length; ++x){
            for(int y = 0; y != piece[0].length; ++y){
                for(int z = 0; z != piece[0][0].length; ++z){
                    rotated_piece[piece[0][0].length - z - 1][y][x] = piece[x][y][z];
                }
            }
        }
        return rotated_piece;
    }

    // rotates around Z axis
    private int[][][] rotateAroundZ(int[][][] piece){

        int[][][] rotated_piece = new int[piece[0].length][piece.length][piece[0][0].length];

        for(int x = 0; x != piece.length; ++x){
            for(int y = 0; y != piece[0].length; ++y){
                for(int z = 0; z != piece[0][0].length; ++z){
                    rotated_piece[piece[0].length - y - 1][x][z] = piece[x][y][z];
                }
            }
        }
        return rotated_piece;
    }

    public ArrayList<Unit> database;

    @Override
    public ArrayList<Unit> getBlockArrayList() {
        return this.database;
    }
}
