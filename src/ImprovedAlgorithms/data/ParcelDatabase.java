package ImprovedAlgorithms.data;

/**
* The following class contains all the methods and data regarding the Parcels.
*/

public class ParcelDatabase {

    // parcel type A
    public static boolean[][][][] aRotBool = {
        {{{true,true,true,true},{true,true,true,true}},{{true,true,true,true},{true,true,true,true}}},
        {{{true,true},{true,true},{true,true},{true,true}},{{true,true},{true,true},{true,true},{true,true}}},
        {{{true,true},{true,true}},{{true,true},{true,true}},{{true,true},{true,true}},{{true,true},{true,true}}}
    };

    // parcel type B
    public static boolean[][][][] bRotBool = {
        {{{true,true},{true,true},{true,true}},{{true,true},{true,true},{true,true}},{{true,true},{true,true},{true,true}},{{true,true},{true,true},{true,true}}},
        {{{true,true},{true,true},{true,true},{true,true}},{{true,true},{true,true},{true,true},{true,true}},{{true,true},{true,true},{true,true},{true,true}}},
        {{{true,true,true},{true,true,true}},{{true,true,true},{true,true,true}},{{true,true,true},{true,true,true}},{{true,true,true},{true,true,true}}},
        {{{true,true,true},{true,true,true},{true,true,true},{true,true,true}},{{true,true,true},{true,true,true},{true,true,true},{true,true,true}}},
        {{{true,true,true,true},{true,true,true,true}},{{true,true,true,true},{true,true,true,true}},{{true,true,true,true},{true,true,true,true}}},
        {{{true,true,true,true},{true,true,true,true},{true,true,true,true}},{{true,true,true,true},{true,true,true,true},{true,true,true,true}}}
    };

    //parcel type C
    public static boolean[][][][] cRotBool = {
        {{{true,true,true},{true,true,true},{true,true,true}},{{true,true,true},{true,true,true},{true,true,true}},{{true,true,true},{true,true,true},{true,true,true}}}
    };

    /**
    * return a database of parcels in exact cover format
    * @return database
    */

    public static boolean[][][][][] getDatabase() {

        boolean[][][][][] rotations = {
            aRotBool,
            bRotBool,
            cRotBool
        };

        return rotations;
    }

    /**
    * return the value of a single parcel based on the order of the DatabaseMax
    * @param id id of the parcel
    * @return value of the parcel
    */

    public static int getValue(int id, int[] values) {
        if(id <= 3) {
            if (values[id - 1] != -1)
                return values[id - 1];
            else {
                if (id == 1)
                    return 16;
                else if (id == 2)
                    return 24;
                else
                    return 27;
            }
        }
        return -1;
    }

    /**
    * get the parcel based on chromosome and index (GA)
    * @param chromosomes GA chromosome
    * @param index index of the piece
    * @return piece
    */

    public static boolean[][][] getParcel(int[][] chromosomes, int index) {
        return getDatabase()[chromosomes[0][index]][chromosomes[1][index]];
    }
}