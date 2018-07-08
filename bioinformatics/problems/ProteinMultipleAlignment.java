package bioinformatics.problems;

import bioinformatics.util.ScoringMatrix;
import bioinformatics.util.Sequence;
import bioinformatics.datastructure.MultiDimensionalArray;
import bioinformatics.datastructure.IteratorComplex;
import bioinformatics.datastructure.Iterator;

/**
 * User: root
 * Date: Dec 3, 2007
 * Time: 3:42:43 PM
 */
public class ProteinMultipleAlignment extends MultipleAlignment{

    private ScoringMatrix mat;
    private int alignmentScore;

    public ProteinMultipleAlignment(Sequence[] sequences, ScoringMatrix mat, int gapPenalty){
        super(sequences, gapPenalty);
        this.mat = mat;
    }

    private int multipleAlignmentScore(int[] arrayIterator, int[] neighborIterator){
        int i, j, score = 0;
        for (i = 0; i < sequences.length; i++){
            for (j = i + 1; j < sequences.length; j++){
                if (neighborIterator[i] == 0 && neighborIterator[j] == 1 || neighborIterator[i] == 1 && neighborIterator[j] == 0){
                    score += gapPenalty;
                } else {
                    if (neighborIterator[i] == 1 && neighborIterator[j] == 1){
                        score += mat.get(sequences[i].charAt(arrayIterator[i] - 1), sequences[j].charAt(arrayIterator[j] - 1));
                    }
                }
            }
        }
        return score;
    }

    public void globalAlignment(){
        int[] dimensions;
        int[] iteratorDimensions;
        int k, maxValue, score, best;
        MultiDimensionalArray s, b;
        IteratorComplex iterator1;
        Iterator iterator2;
        int[] arrayIterator, neighborIterator;
        dimensions = new int[sequences.length];
        iteratorDimensions = new int[sequences.length];
        for (k = 0; k < sequences.length; k++){
            dimensions[k] = sequences[k].length() + 1;
            iteratorDimensions[k] = sequences[k].length();
        }
        s = new MultiDimensionalArray(dimensions);
        b = new MultiDimensionalArray(dimensions);
        iterator1 = new IteratorComplex(iteratorDimensions, 1);
        arrayIterator = iterator1.get();
        do{
            iterator2 = new Iterator(sequences.length, 1);
            neighborIterator = iterator2.get();
            best = 1;
            k = 1;
            iterator2.nextLeaf();
            maxValue = sequences.length * sequences.length * -100;
            do{
                score = previousAlignmentScore(s, arrayIterator, neighborIterator) + multipleAlignmentScore(arrayIterator, neighborIterator);
                if (score > maxValue){
                    maxValue = score;
                    best = k;
                }
                k++;
            }while(iterator2.nextLeaf());
            s.set(arrayIterator, maxValue);
            b.set(arrayIterator, best);
        }while (iterator1.nextLeaf());
        alignmentScore = maxValue;
        backtrackPointers(b, iterator1.last());        
    }

    public void localAlignment(){
        int[] dimensions, bestIterator = null;
        int[] iteratorDimensions;
        int k, maxValue, score, best;
        MultiDimensionalArray s, b;
        IteratorComplex iterator1;
        Iterator iterator2;
        int[] arrayIterator, neighborIterator;
        dimensions = new int[sequences.length];
        iteratorDimensions = new int[sequences.length];
        for (k = 0; k < sequences.length; k++){
            dimensions[k] = sequences[k].length() + 1;
            iteratorDimensions[k] = sequences[k].length();
        }
        s = new MultiDimensionalArray(dimensions);
        b = new MultiDimensionalArray(dimensions);
        iterator1 = new IteratorComplex(iteratorDimensions, 1);
        arrayIterator = iterator1.get();
        alignmentScore = 0;
        do{
            iterator2 = new Iterator(sequences.length, 1);
            neighborIterator = iterator2.get();
            best = -1;
            k = 1;
            iterator2.nextLeaf();
            maxValue = 0;
            do{
                score = previousAlignmentScore(s, arrayIterator, neighborIterator) + multipleAlignmentScore(arrayIterator, neighborIterator);
                if (score > maxValue){
                    maxValue = score;
                    best = k;
                }
                k++;
            }while(iterator2.nextLeaf());
            s.set(arrayIterator, maxValue);
            b.set(arrayIterator, best);
            if (maxValue > alignmentScore){
                alignmentScore = maxValue;
                bestIterator = arrayIterator.clone();
            }
        }while (iterator1.nextLeaf());
        backtrackPointers(b, bestIterator);
    }

}
