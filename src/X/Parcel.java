package X;

// Object that holds all rotations of one parcel type
public class Parcel {

    public Parcel(int[][][][] shape, int price, int space, int color){
        this(shape); 
        this.color_id = color;
        this.space = space;
        this.price = price;
    }
    public Parcel(int[][][][] shape){
        parcel_matrix = shape;
        position = 0;

        setDimensions();
    }

    // clones this parcel 
    public Parcel freeze(){
        Parcel p  =new Parcel(this.parcel_matrix,this.color_id, this.space, this.color_id);
        p.color_id = color_id;
        p.position = position;
        return p;
    }

    // keeps track on which rotation of the parcel are we
    public void rotate(){
        if(position + 1 < parcel_matrix.length){
            ++position;
        }else{
            position = 0;
        }
        setDimensions();
    }
    // rotates more than by one rotation
    public void rotate(int cnt){
        for(int i = 0; i != cnt; ++i){
            rotate();
        }
    }

    // sets up dimension variables
    private void setDimensions(){
        width = parcel_matrix[position].length;
        height = parcel_matrix[position][0].length;
        depth = parcel_matrix[position][0][0].length;
    }


    // set price of this parcel
    public void setPrice(double price){
        this.price = price;
    }

    // getter methods that return state variables of a given object of this class
    public int getHeight() { return height;}
    public int getDepth() { return depth;}
    public int getWidth() { return width;}
    public double getPrice() { return price; }
    public int getColor(){ return color_id;}
    public int getSpace(){ return space; }
    public int[][][] getParcelMatrix() { return parcel_matrix[position];}
    public int getRotationCnt() { return parcel_matrix.length;}



    // all variables that describe a given Parcel
    private int height, depth, width;
    private int position;
    private int color_id;
    private int space;
    private double price;

    private int[][][][] parcel_matrix;

    // color value that is unversal for all Parcel objects, co each parcel has unique color id
    static int global_color = 1;
    
}
