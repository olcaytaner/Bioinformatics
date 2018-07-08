package bioinformatics.datastructure;

/**
 * User: root
 * Date: Dec 3, 2007
 * Time: 4:25:32 PM
 */
public class MultiDimensionalArray {

    private int[] array;
    private int[] dimensions;
    private int N, size;

    public MultiDimensionalArray(int[] dimensions){
        int i;
        size = 1;
        this.dimensions = dimensions;
        N = dimensions.length;
        for (i = 0; i < N; i++){
            size *= dimensions[i];
        }
        array = new int[size];
    }

    private int getPos(int[] indices){
        int i, pos = 0;
        for (i = 0; i < N - 1; i++){
            pos = (pos + indices[i]) * dimensions[i + 1];
        }
        pos += indices[N - 1];
        return pos;
    }

    public int get(int[] indices){
        return array[getPos(indices)];
    }

    public int getSize(){
        return size;
    }

    public void set(int[] indices, int value){
        array[getPos(indices)] = value;
    }
}
