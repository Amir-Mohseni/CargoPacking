package X;



// DLXFast is a very optimized dancing links structure
// This structure is similar to 2D static array in the same way linked list is similar to 1D static array
// However here each row and column may have different amount of elements, since we ONLY remember the elemnts which represent fulfilled constraints 
public class DLXFast {


    // DLX takes the amount of contraints we will work with as an arguments, so we can know how many headers to create
    public DLXFast(int const_size){
        this.const_size = const_size;
        initHeaders();

    }

    // getter methods for Algorithm X
    public int getConstSize(){ return const_size;}
    public int getRowSize(){ return row_size; }
    public N.HNode getRoot(){ return headers[0];}


    // adds a row to the DLX structure, it takes arguments about a given Parcel on a given Place in space
    // also connects all Nodes from this row to the upper row (which in the beginning is the header row)
    public void addRow(int[] coords, int[][][] piece, int[] row, int color_id, int space, double price) throws IllegalArgumentException{
        // array of values that say, where a parcel fulfills a constraint has to be the same size as constraint size, otherwise there is a logical error 
        if (row.length != const_size) throw new IllegalArgumentException("Invalid row size");

        ++row_size;

        N.Node[] const_row = new N.Node[const_size+1];
        N.RNode row_root = new N.RNode(coords, piece, color_id, space, price); 
        const_row[0] = row_root;

        // initializes all Nodes/Elements this Row/Set has
        for(int i = 1; i != const_size + 1; ++i){

            N.Node n = null;
            if(row[i-1] == 1){
                n = new N.Node(row[i-1],(N.HNode)headers[i]);
                n.setRowRoot(row_root);
            }
            const_row[i] = n;
        }

        // Now connect Nodes of this row to lowest row of the existing DLX structure, and also connect Nodes to eachother
        // Connected are ONLY node with non 0 value, since we are only interested, which constraints does this row Fulfills
        // the other nonconnected Nodes will get erased from memory by garbage collector, since nothing will point to them one this method exits
        N.Node neighbor = const_row[0];
        for(int i = 0; i != const_size + 1; ++i){
            N.Node n = const_row[i];

            if(n != null){
                
                ((N.HNode)headers[i]).addPossible(1);
                

                n.left = neighbor;
                neighbor.right = n;

                N.Node former_bot = headers[i].top;

                n.bot = headers[i];
                headers[i].top = n;

                former_bot.bot = n;
                n.top = former_bot;

                neighbor = n;
            } 
            const_row[0].left = neighbor;
            neighbor.right = const_row[0];

            

        }


    }


    // the first steap is to build all headers/constraint HNodes and connect them to eachother
    private void initHeaders(){
        headers = new N.HNode[const_size+1];
        for(int i = 0; i != const_size +1; ++i){
            headers[i] = new N.HNode(i-1);
        }
        for(int i = 0; i != const_size +1; ++i){
            int next_ix = (i < const_size) ? i+1 : 0;
            int prev_ix = (i > 0) ? i-1 : const_size;

            headers[i].top = headers[i];
            headers[i].bot = headers[i];

            headers[i].right = headers[next_ix];
            headers[i].left = headers[prev_ix];
        }
    }

    // The DLX structure only rembers the headers and sizes, all the rows are then accessed from the headers
    private N.HNode[] headers;
    private int const_size;
    private int row_size;
    
}
