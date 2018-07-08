package bioinformatics.util;

import java.util.Arrays;

/**
 * User: root
 * Date: Oct 25, 2007
 * Time: 10:20:19 AM
 */
public class RandomArray{

    private int[] array;

    public RandomArray(int itemCount, int length){
        int i, j;
        array = new int[itemCount];
        array[0] = 0;
        array[itemCount - 1] = length;
        for (i = 1; i < itemCount - 1; i++){
            array[i] = (int) (Math.random() * length);
            for (j = 0; j < i; j++){
                if (array[j] == array[i]){
                    i--;
                    break;
                }
            }
        }
        Arrays.sort(array);
    }

    public int[] get(){
        return array;
    }
}
