package X;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;





// slightly altered Algorithm X focused on finding the best way of filling a cargo space regaedless of it being exact cover
// also this algorithm provides the option, to fill the cargo with custom amounts of parcels and custom values of them
// ONLY methods that differ from CargoX are described

public class CargoXBest {

    public CargoXBest(int width, int height, int depth, ArrayList<Parcel> parcels){
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.parcels = parcels;


        this.solution_stack = new LinkedList<N.RNode>();
    }


    // returns List of rows that make up the soltion
    public LinkedList<N.RNode> solvePacking(){

        // build dancing links; in case price have changed, since the higher a row is the bigger price should it have
        buildDancingLinks();

        // in case there is no amounts limit, set a limit that is unreachable, thus allowing to find a GLOBAL maximum 
        if(parcel_limit == null){parcel_limit = new int[]{1000,1000,1000};}

        // if time limit is set, remember at which time did the algorithm start
        if(time_limit != 0){time_begin = System.currentTimeMillis();}

        row_size = map.getRowSize();
        const_size = map.getConstSize();


        N.HNode root = map.getRoot();
        int solution_found = findSolution(root, 1);
        System.out.println("All solutions:" + all_solutions);
        System.out.println("Best solution value:" + best_price);
        return best_solution_stack;
    }

    // overloaded methods, that takes array of custom amounts and custom prices; order -> [T piece, P piece, L Piece] 
    public LinkedList<N.RNode> solvePacking(int[] parcel_include, double[] prices){
        ArrayList<Parcel> filtered_parcels = new ArrayList<Parcel>();

        for(int x = 0; x != parcel_include.length; ++x){
            if(parcel_include[x] != 0){
                filtered_parcels.add(parcels.get(x));
                parcels.get(x).setPrice(prices[x]);
            }
        }
        parcels = filtered_parcels;
        parcel_limit = parcel_include;

        // sort parcels price descending, so when the DLX is built, it is build price descending too
        Collections.<Parcel>sort(parcels, (a,b) -> {
            if(a.getPrice() < b.getPrice()) return 1;
            if(a.getPrice() > b.getPrice()) return -1;
            return 0;
        });

        // run the default solvePacking method, that will find a solution
        return solvePacking();
    }

    // set a running time limit
    public void setTimeLimit(long seconds){
        time_limit = Math.abs(seconds)*1000;
    }

    // heart of the algorithm
    // returns one of three integers 1- no more rows can be added; -1 - this path doesnt lead to solution; 0 - terminate program
    private int findSolution(N.HNode root, int iteration){



        // if we placed all parcels we wanted to, terminate program, since its the best case
        if(parcel_limit[0] == 0 && parcel_limit[1] == 0 && parcel_limit[2] == 0){
            ++all_solutions;
            return 0;
        }
        
        // no rows can be added
        if(root.bot == root){ // no rows left
            ++all_solutions;
            return 1;
        }

        // if time limit is reached, terminate the program
        if(time_limit != 0 && System.currentTimeMillis() - time_begin > time_limit) return 0;
        
        

        // find the best contraint to fulfill
        N.HNode best_const = getBestNode(root);

        // compute an estimate what is the max price we can fill in the space right now
        double const_price = maxConstPrice(root);

        // value of parcels "placed" in the space
        double now_price = calculatePrice();

        
        // if the maximum possible price we can reach will not be bigger than the best
        if(now_price + const_price <= best_price){  
               
           return (int)Math.round((const_price + now_price - best_price)*3) - 1;
        }
        
        N.Node const_to_solve = best_const;
        N.Node field = const_to_solve.bot;
        
        while(field != const_to_solve){ // find field that satisfies the constraint
            
            // contraint has to be satisfied but ALSO we also have that parcel available
            if(field.getValue() == 1 && parcel_limit[field.row_root.getColor()-1] != 0){  
                
                disconnect(field); // size down row size
                solution_stack.add(field.row_root);
                
                const_size -= field.row_root.getSpace(); 
                row_size -= 1;
                
                // since we added this row/parcel, we have one less
                --parcel_limit[field.row_root.getColor()-1];
                
                
                int result  = findSolution(root, iteration+1);
                
                // if 
                double local_price = calculatePrice();
                if(result > -1){ // solution was found
                    // ++all_solutions;
                    if(local_price > best_price){
                        best_price = local_price;
                        best_solution_stack = (LinkedList<N.RNode>)solution_stack.clone();
                        System.out.println("NOW BEST: " + best_price);
                        
                    }
                    if(result == 0) return 0;
                }


                reconnect(field);
                solution_stack.removeLast();
                
                const_size += field.row_root.getSpace();
                row_size += 1;
                ++parcel_limit[field.row_root.getColor()-1];

                if(now_price + const_price <= best_price){
                    return (int)Math.round((const_price + now_price - best_price)*3) - 1;
                }

                if(iteration > 5){
                    if(result < -1){
                        return result + 1;
                    }                    
                }
                
                
            }
            field = field.bot;
        }
        return -1;
    }
    
    private void disconnect(N.Node n){
        N.Node next = n;
        do{
            if(next.getValue() == 1){ 
        
                disconnectColumn(next);
            }else{  
                
                next.top.bot = next.bot;
                next.bot.top = next.top;
            }
            next = next.right;
            
        }while(next != n);   
    }
    private void disconnectColumn(N.Node n){
        N.Node next = n.bot;
        while(next != n){
            
            if( next.getValue() == 1){

                row_size -= 1;

                N.Node horizontal = next.right;
                while(horizontal != next){

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
            next = next.bot;
        }
    }
    
    private void reconnect(N.Node n){
        n = n.left; // we have add rows from the reverse order so no duplicates are created
        N.Node next = n;
        do{
            if(next.getValue() == 1){ // reconnect N.Node horizontally

                reconnectColumn(next);
            }else{  // reconnect N.Node verically

                next.top.bot = next;
                next.bot.top = next;
            }
            next = next.left;

        }while(next != n);   
    }
    private void reconnectColumn(N.Node n){
        N.Node next = n.bot;
        while(next != n){

            if( next.getValue() == 1){

                row_size += 1; 

                N.Node horizontal = next.right;
                while(horizontal != next){

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
            next = next.bot;
        }
    }


    // return the best header/contraint to fulfill, since this is no exact cover, do NOT return null if there is a header with zero possible rows that could fulfill it
    private N.HNode getBestNode(N.HNode root){
        N.HNode best = (N.HNode)root.right;
        N.HNode next = (N.HNode)root.right;
        while(next != root){

            if(next.getPossible() < best.getPossible() && next.getPossible() != 0){ best = next;}
            else if(best.getPossible() == 0 && next.getPossible() != 0){ best = next;}
            next = (N.HNode)next.right;
        }
        return best;
    }




    private void buildDancingLinks(){
        map = new DLXFast(width*height*depth);
        int[] cargo_row = new int[width*height*depth];

        for(int i = 0; i != parcels.size(); ++i){
            Parcel p = parcels.get(i);
            for(int r = 0; r != p.getRotationCnt(); ++r){
                p.rotate();

                int p_width = p.getWidth();
                int p_height = p.getHeight();
                int p_depth = p.getDepth();

                for(int x = 0; x < width - p_width + 1; ++x){
                    for(int y = 0; y < height - p_height +1; ++y){
                        for(int z = 0; z < depth - p_depth + 1; ++z){

                            addPiece(x, y, z, cargo_row, p.getParcelMatrix());
                            map.addRow(new int[]{x,y,z}, p.getParcelMatrix(), cargo_row, p.getColor(),p.getSpace(),p.getPrice());
                            clearCargo(cargo_row);
                        }
                    }
                }
            }
        }
        System.out.println("All possible placements: "+ map.getRowSize());
    }

    private void addPiece(int j, int k, int l, int[] cargo_row, int[][][] piece){
        for(int x = 0;x != piece.length; ++x){
            for(int y = 0; y != piece[0].length; ++y){
                for(int z = 0; z != piece[0][0].length; ++z){
                    if(piece[x][y][z] == 1){
                        int x_component = (x+j) * (height * depth);
                        int y_component = (y+k) * (depth);
                        int z_component = (z+l);
                        cargo_row[x_component + y_component + z_component] = 1;
                    }
                }
            }
        }
    }

    private void clearCargo(int[] cargo_row){
        for(int i = 0; i != cargo_row.length; ++i) cargo_row[i] = 0;
    }


    private double calculatePrice(){
        double v = 0;
        for(N.RNode n : solution_stack){
            v += n.getPrice();
        }
        return v;
    }


    // new method for finding the best value
    // returns the maximum price that can be filled into the empty space
    private double maxConstPrice(N.HNode root){
        N.HNode next = (N.HNode)root.right;
        double total_price = 0;

        // iterate through all Headers
        while(next != root){

            // if there are possible fufillemnts of the header, iterate through them and find the best value 
            // the header can be fulfilled by
            if(next.getPossible() != 0){
                double best = 0;
                N.Node bottom = next.bot;
                while(bottom != (N.Node) next){
                    double now = bottom.row_root.getPrice()/bottom.row_root.getSpace(); // best price this contraint can be filled by is price per block of this row
                    if(now > best && parcel_limit[bottom.row_root.getColor()-1] != 0){
                        best = now;
                    }
                    bottom = bottom.bot;
                }
                total_price += best; // sum the best value this header/constraint can be fulfilled by 
            } 
            next = (N.HNode)next.right;
        }
        return total_price;
    }


    private Random generator = new Random();
    LinkedList<N.RNode> solution_stack;
    DLXFast map;
    private ArrayList<Parcel> parcels;
    private int width, height, depth;
    private int const_size, row_size;

    private LinkedList<N.RNode> best_solution_stack = new LinkedList<N.RNode>();
    private double best_price = 0;
    private int all_solutions = 0;
    
    private int[] parcel_limit;
    private long time_limit = 0;
    private long time_begin;
    
}
