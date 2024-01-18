package X;

import X.N.*;

import java.util.ArrayList;
import java.util.LinkedList;
//import testing.CargoTruck;

// Algorithm X

// Notice that we keep everywhere checks if a node has value 1 even though all Nodes in the DLXFast have value 1
// this is in case we want to switch up for the DLX structure, for debugging purposes, and ALSO HNodes and RNodes do NOT have values 1, so it would still cause bugs 
public class CargoX {

    // takes size of our space to eaxct cover and the parcels that can cover the space
    public CargoX(int width, int height, int depth, ArrayList<Parcel> parcels){
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.parcels = parcels;


        this.solution_stack = new LinkedList<N.RNode>();
        buildDancingLinks();
    }


    // public method that solves the exact cover and returns list of all rows which together fill up the space
    public LinkedList<RNode> solvePacking(){
        row_size = map.getRowSize();
        const_size = map.getConstSize();
        

        N.HNode root = map.getRoot();
        boolean solution_found = findSolution(root, 1);

       
        if(solution_found){
            System.out.println("Value:" + calculatePrice());
        }
        else System.out.println("NO SOLUTION FOUND");

        // return solution list, if there is no solution, list is empty
        return solution_stack;
        
       

    }
    

    // heart of the algorithm
    private boolean findSolution(N.HNode root, int iteration){
        
        // check if there are no rows left, therefore nothing can be placed anymore
        if(root.bot == root){ // no rows left
            return root.right == root; // if also no constraints are left, the space was filled exactly
        }

        // if all the possible rows cannot fill all contraints together, there is no solution
        if(row_size * parcels.get(0).getSpace() < const_size){
            return false;
        } 

        // pick best Header/Constraint to fulfill
        N.HNode best_const = getBestNode(root);
        if(best_const == null){ // if best_const is null, it means there is a row that cannot be fulfilled, thus, there is no solution
            return false;
        }  

        N.Node const_to_solve = best_const;
        N.Node field = const_to_solve.bot;
        
        while(field != const_to_solve){ // iterate through Nodes that satisfy this header/constraint
            
            if(field.getValue() == 1){  // node fulfills the header

                disconnect(field); // remove row of this node since its added to the solution
                solution_stack.add(field.row_root); 
                
                const_size -= field.row_root.getSpace(); // keep tract of how rows and constraints do we have left
                row_size -= 1;

                boolean result  = findSolution(root, iteration+1); // go one recursion deeper, now without this given row,
                
                if(result){ // solution was found
                    return true;
                }
                else{
                    // this path didnt lead to solution, so reconnect and remove this given row from solution stack
                    reconnect(field);
                    solution_stack.removeLast();

                    const_size += field.row_root.getSpace();
                    row_size += 1;
                    
                }
            }
            field = field.bot; /// move to other Node that could fulfill the header/constraint
        }
        return false; // return false if none of the Nodes led to a solution
    }
    

    // disconnect the row which this row is on and also ALL headers/constraints that it fulfills

    // ALSO removes all rows that intersect with this row, because, we consider this row to be placed in the cargo,
    // so other Parcels/Rows that intersect with this one would not fit
    private void disconnect(N.Node n){

        N.Node next = n;
        do{
            
            if(next.getValue() == 1){ 
                // disconnect this column since its Header/Constraint is fulfilled
                disconnectColumn(next);
            }else{  // disconnect Node only from upper and lower rows
                
                next.top.bot = next.bot;
                next.bot.top = next.top;
            }
            next = next.right; // go to next node in this row
            
        }while(next != n);   
    }

    // disconnects a given column and ALL rows that fulfill this row, because they intersect with row that already fulfilled this columns Header/Constraint
    private void disconnectColumn(N.Node n){
        N.Node next = n.bot;
        while(next != n){
            
            if( next.getValue() == 1){ // if this node could fulfill its header, it intersect with already chosen row

                row_size -= 1; 
                
                // remove this WHOLE row, because its constraint is already fufilled
                N.Node horizontal = next.right;
                while(horizontal != next){

                    // the header can now be fulfilled by one less row, since this will be removed
                    if(horizontal.getValue() == 1){ horizontal.header.addPossible(-1);} 

                    horizontal.top.bot = horizontal.bot;
                    horizontal.bot.top = horizontal.top;
                    
                    horizontal = horizontal.right;
                }
            }
            else{
                next.left.right = next.right;
                next.right.left = next.left;
            }       
            next = next.bot; // go to the lower node 
        }
    }
    
     // reconnect the row which this row is on and also ALL headers/constraints that it fulfills

    // ALSO adds all rows that intersect with this row, because, we consider this row to be removed from the cargo,
    // so other Parcels/Rows that intersect with this one would again fit
    private void reconnect(N.Node n){
        n = n.left; // we have to add rows from the reverse order so no duplicate adding do not happen
        N.Node next = n;
        do{
            if(next.getValue() == 1){

               // reconnect this column since its Header/Constraint is again unfulfilled
                reconnectColumn(next);
            }
            else{  // reconnect Node to upper and lower rows

                next.top.bot = next;
                next.bot.top = next;
            }
            next = next.left;

        }while(next != n);   
    }
     // reconnects a given column and ALL rows that fulfill this row, because they intersect with row that fulfilled this columns Header/Constraint,
     // Hovever now the row is being removed from solutions, so all the rows which intersect can be again possibly placed
    private void reconnectColumn(N.Node n){
        N.Node next = n.bot;
        while(next != n){


            if( next.getValue() == 1){ // if this row intersects with row  being added back ,we have to reconnect it, because now its pssible placemnt

                row_size += 1;
               

                // reconnect this row, because it was removed
                N.Node horizontal = next.right;
                while(horizontal != next){

                    // Header of this node has again has one more possible row that fulfills it
                    if(horizontal.getValue() == 1){ horizontal.header.addPossible(1);}

                    horizontal.top.bot = horizontal;
                    horizontal.bot.top = horizontal;

                    horizontal = horizontal.right;
                }
            }
            else{
                next.left.right = next;
                next.right.left = next;
            }       
            next = next.bot; // go to the next row
        }
    }

    // find the contraint/header that can be filled by the least rows, and return it;
    // if there is a constraint/header that cannot be filled by any row, we return null which indicates that
    private N.HNode getBestNode(N.HNode root){
        N.HNode best = (N.HNode)root.right;
        N.HNode next = (N.HNode)root.right;
        while(next != root){

            if(next.getPossible() == 0) return null; // constraint cannot be fufilled, so there us no solution here, return null
            if(next.getPossible() < best.getPossible()){ best = next;}
            next = (N.HNode)next.right;
        }
        return best;
    }

    // build the DLX data structure
    private void buildDancingLinks(){
        map = new DLXFast(width*height*depth);

        // cargo row rwpresents contraint row, each position is  either 1 - constraint is fulfilled; 0- it is not fulfilled
        int[] cargo_row = new int[width*height*depth];

        // for each parcel, for each position it can be placed on add a new row in the DLX structure
        for(Parcel p : parcels){
            for(int r = 0; r != p.getRotationCnt(); ++r){
                p.rotate();

                int p_width = p.getWidth();
                int p_height = p.getHeight();
                int p_depth = p.getDepth();

                for(int x = 0; x < width - p_width + 1; ++x){
                    for(int y = 0; y < height - p_height +1; ++y){
                        for(int z = 0; z < depth - p_depth + 1; ++z){

                            // adds piece to the cargo row
                            addPiece(x, y, z, cargo_row, p.getParcelMatrix());

                            // add a new row with given parcel on a given position to the DLX
                            map.addRow(new int[]{x,y,z}, p.getParcelMatrix(), cargo_row, p.getColor(), p.getSpace(), p.getPrice());
                            // clear cargo row so we can add a new row
                            clearCargo(cargo_row);
                        }
                    }
                }
            }
        }
        System.out.println("All possible placements: "+ map.getRowSize());
    }

    // We work with 3D space, However we want co represent is as on contraint row, so we have to convert x,y,z values to ONE index for 1D array
    private void addPiece(int j, int k, int l, int[] cargo_row, int[][][] piece){
        for(int x = 0;x != piece.length; ++x){
            for(int y = 0; y != piece[0].length; ++y){
                for(int z = 0; z != piece[0][0].length; ++z){
                    // change values to 1 (filled) only if the piece at a give coordinate fills the space
                    if(piece[x][y][z] == 1){
                        // compute how each coordinate contributes to the final index and then value at that index as filled  
                        int x_component = (x+j) * (height * depth);
                        int y_component = (y+k) * (depth);
                        int z_component = (z+l);
                        cargo_row[x_component + y_component + z_component] = 1;
                    }
                }
            }
        }
    }

    // clears up a 1D array
    private void clearCargo(int[] cargo_row){
        for(int i = 0; i != cargo_row.length; ++i) cargo_row[i] = 0;
    }

    
    // iteration through all Rows which contribute to solution, and calculate which price together do they hold
    private int calculatePrice(){
        int val = 0;
        for(N.RNode n : solution_stack){
            val += n.getPrice();
        }
        return val;
    }


    LinkedList<N.RNode> solution_stack;
    DLXFast map;
    //CargoTruck gui;
    private ArrayList<Parcel> parcels;
    private int width, height, depth;
    private int const_size, row_size;
    
}
