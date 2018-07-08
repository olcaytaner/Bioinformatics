package bioinformatics.datastructure;

/**
 * User: Olcay
 * Date: Jun 9, 2007
 * Time: 12:35:26 PM
 */
public class Iterator {
    private int[] a;
    private int L;
    private int k;
    private int level;

    public Iterator(int L, int k){
        int i;
        this.L = L;
        this.k = k;
        a = new int[L];
        for (i = 0; i < L; i++){
            a[i] = 0;
        }
    }

    public boolean nextLeaf(){
        int i;
        for (i = L - 1; i >= 0; i--){
            if (a[i] < k){
                a[i]++;
                return true;
            }
            a[i] = 0;
        }
        return false;
    }

    public int[] get(){
        return a;
    }

    public void set(int index, int value){
        a[index] = value;
    }

    public int getLevel(){
        return level;
    }

    public boolean nextVertex(int i){
        int j;
        if (i < L - 1){
            a[i + 1] = 0;
            level = i + 1;
            return true;
        }
        else{
            for (j = L - 1; j >= 0; j--){
                if (a[j] < k){
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
            if (a[j] < k){
                a[j]++;
                level = j;
                return true;
            }
        }
        return false;
    }

    public int[] generateSequence(){
        int[] list;
        int i;
        list = new int[L + 1];
        for (i = 0; i < L; i++){
            list[i + 1] = list[i] + a[i];
        }
        return list;
    }
}
