package X;


// group of polymorfic classes which are hte building blocks of the DLX data structure
public class N{


    // Node holds value which is either 1- node is full or 0- node is empty 
    public static class Node{

        public Node(int val, HNode header){ 
            this.header = header; // we also keep track of what header does this Node belong to
            this.value = val;
        }

        // each node has a root note that is like a Subset name, and its Nodes are the elements
        // node remembers what is its root node for Algorithm X optimaliation methods
        public void setRowRoot(RNode r){this.row_root = r;}
        
        // returns 0 or 1 value
        public int getValue(){ return value;}
        


        private int value;
        
        // neighbors of this node to all cardinal directions
        public Node left,right,top,bot;

        // pointers to its Row node and to Constraint Header node which it might fulfill
        public HNode header;
        public RNode row_root;
    }
    
    // each Header Node is onenconstraint that has to be fulfilled
    public static class HNode extends Node{
        
        
        public HNode(int cargo_position){
            super(-999,null); // -999 is the ID of header node
            this.position = cargo_position;
        }

        // keeps track of how many Nodes can fulfill this Header
        public void addPossible(int c){ possible_consts_cnt += c;}

        // getter methods
        public int getPossible(){ return possible_consts_cnt;}
        public int getPosition() { return position;}
        
        // state variables used for optimalization
        private int position;
        private int possible_consts_cnt = 0;
    }

    // each Row Node is like a Subset 
    public static class RNode extends Node{
        
        // each Row Node has is created with a particular parcel on a particular place in space, thus it has to remember information
        // about the parcel such as what color_id does the parcel have
        public RNode(int[] coords, int[][][] piece, int color_id, int space, double price){
            super(999, null); // 999 is the ID of root node
            this.piece = piece;
            this.coordinates = coords;
            this.color_id = color_id;
            this.position = row_postion++;
            this.row_root = this;
            this.space = space;
            this.price = price;
        }
        
        // getter methods
        public int[] getCoords(){ return coordinates;}
        public int[][][] getPiece(){ return piece;}
        public int getColor(){ return color_id; }
        public int getPosition(){ return position; }
        public int getSpace(){ return space;}
        public double getPrice(){ return price;}
        
        // state variables from which we can later build up and draw a graphical solution 
        private int color_id;
        private int[][][] piece;
        private int[] coordinates;
        private int position;
        private int space;
        private double price;
    
        private static int row_postion = 0;
    }

}