package X;

import java.util.ArrayList;


// Class that serves as a generator of all possible rotations of blocks A,B,C
// uses methods from PentominoDatabse but on different blocks
public class ParcelDatabase {

    static int[][][] blockA = new int[2][2][4];
    static int[][][] blockB = new int[2][3][4];
    static int[][][] blockC = new int[3][3][3];

    public static ArrayList<Parcel> database;


    static{ // default shape matrices

        for(int x = 0; x != 2; ++x){
            for(int y = 0; y != 2; ++y){
                for(int z = 0; z != 4; ++z){
                    blockA[x][y][z] = 1;
                }
            }
        }
        
        for(int x = 0; x != 2; ++x){
            for(int y = 0; y != 3; ++y){
                for(int z = 0; z != 4; ++z){
                    blockB[x][y][z] = 1;
                }
            }
        }
        for(int x = 0; x != 3; ++x){
            for(int y = 0; y != 3; ++y){
                for(int z = 0; z != 3; ++z){
                    blockC[x][y][z] = 1;
                }
            }
        }
        build_database();
    }

    // builds database of all Parcels, with given prices ordered descending
    private static void build_database(){

        int[][][][] A = PentominoDatabase.build_piece(blockA);
        int[][][][] B = PentominoDatabase.build_piece(blockB);
        int[][][][] C = PentominoDatabase.build_piece(blockC);

        database = new ArrayList<Parcel>(3);
        database.add(new Parcel(C,5,27,1));
        database.add(new Parcel(B,4,24,2));
        database.add(new Parcel(A,3,16,3));

        
    }
}
