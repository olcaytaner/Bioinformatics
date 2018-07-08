package bioinformatics.problems;

import bioinformatics.util.Sequence;
import bioinformatics.datastructure.MultiDimensionalArray;

/**
 * User: root
 * Date: Dec 12, 2007
 * Time: 4:33:35 PM
 */
public class MultipleAlignment {

    protected int gapPenalty;
    protected Sequence sequences[];
    protected int alignmentScore;

    public MultipleAlignment(){

    }

    public MultipleAlignment(Sequence[] sequences, int gapPenalty){
        this.sequences = sequences;
        this.gapPenalty = gapPenalty;
    }

    protected int previousAlignmentScore(MultiDimensionalArray s, int[] arrayIterator, int[] neighborIterator){
        int[] pos = arrayIterator.clone();
        int i;
        for (i = 0; i < sequences.length; i++){
            pos[i] -= neighborIterator[i];
        }
        return s.get(pos);
    }

    protected void backtrackPointers(MultiDimensionalArray b, int[] arrayIterator){
        int back, index = sequences.length - 1;
        back = b.get(arrayIterator);
        if (back <= 0){
            return;
        }
        while (back > 0){
            if (back % 2 == 1){
                arrayIterator[index]--;
            }
            back /= 2;
            index--;
        }
        backtrackPointers(b, arrayIterator);
    }

}
