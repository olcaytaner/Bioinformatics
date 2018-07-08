package bioinformatics.datastructure;

/**
 * User: root
 * Date: Dec 5, 2007
 * Time: 4:06:21 PM
 */
public class IteratorComplex {

    private int[] dimensions;
    private int[] a;
    private int L;
    private int level;
    private int start;

    public IteratorComplex(int[] dimensions, int start){
        int i;
        this.dimensions = dimensions;
        this.L = dimensions.length;
        this.start = start;
        a = new int[L];
        for (i = 0; i < L; i++){
            a[i] = this.start;
        }
    }

    public boolean nextLeaf(){
        int i;
        for (i = L - 1; i >= 0; i--){
            if (a[i] < dimensions[i]){
                a[i]++;
                return true;
            }
            a[i] = start;
        }
        return false;
    }

    public int[] last(){
        int i;
        int[] array;
        array = new int[L];
        for (i = 0;i < L; i++){
            array[i] = dimensions[i];
        }
        return array;
    }
    
    public int[] get(){
        return a;
    }

    public int getLevel(){
        return level;
    }

    public boolean nextVertex(int i){
        int j;
        if (i < L - 1){
            a[i + 1] = start;
            level = i + 1;
            return true;
        }
        else{
            for (j = L - 1; j >= 0; j--){
                if (a[j] < dimensions[j]){
                    a[j]++;
                    level = j;
                    return true;
                }
            }
        }
        return false;
    }

    public boolean byPass(int i){
        int j;
        for (j = i; j >= 0; j--){
            if (a[j] < dimensions[j]){
                a[j]++;
                level = j;
                return true;
            }
        }
        return false;
    }

}
