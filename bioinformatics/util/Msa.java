package bioinformatics.util;

import bioinformatics.datastructure.List;

/**
 * Created by IntelliJ IDEA.
 * User: olcay
 * Date: Dec 29, 2007
 * Time: 11:02:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class Msa extends MultipleSequence{

    private int score;

    public Msa(Sequence[] alignment, boolean random){
        int i, maxstart, maxend;
        int[] positions;
        if (!random){
            this.alignment = alignment;
        } else {
            this.alignment = new Sequence[alignment.length];
            maxstart = maxend = 0;
            positions = new int[alignment.length];
            for (i = 0; i < alignment.length; i++){
                positions[i] = (int) (Math.random() * alignment[i].length());
                if (positions[i] > maxstart){
                    maxstart = positions[i];
                }
                if (alignment[i].length() - positions[i] > maxend){
                    maxend = alignment[i].length() - positions[i];
                }
            }
            for (i = 0; i < alignment.length; i++){
                this.alignment[i] = alignment[i].extend(maxstart - positions[i], maxend - alignment[i].length() + positions[i]);
            }
        }
    }

    public void calculateScore(int gapPenalty, int matchScore, int mismatchPenalty){
        int i, j, k, spScore = 0;
        for (i = 0; i < alignment[0].length(); i++){
            for (j = 0; j < alignment.length; j++){
                for (k = j + 1; k < alignment.length; k++){
                    if (alignment[j].charAt(i) == '-' || alignment[k].charAt(i) == '-'){
                        spScore += gapPenalty;
                    } else {
                        if (alignment[j].charAt(i) == alignment[k].charAt(i)){
                            spScore += matchScore;
                        } else {
                            spScore += mismatchPenalty;
                        }
                    }
                }
            }
        }
        score = spScore;
    }

    public void calculateScore(int gapPenalty, ScoringMatrix mat){
        int i, j, k, spScore = 0;
        for (i = 0; i < alignment[0].length(); i++){
            for (j = 0; j < alignment.length; j++){
                for (k = j + 1; k < alignment.length; k++){
                    if (alignment[j].charAt(i) == '-' || alignment[k].charAt(i) == '-'){
                        spScore += gapPenalty;
                    } else {
                        spScore += mat.get(alignment[j].charAt(i), alignment[k].charAt(i));
                    }
                }
            }
        }
        score = spScore;
    }

    public int getScore(){
        return score;
    }

    public Msa mutationInsertGap(int maxGapLength){
        Msa child;
        int i, gapLength, gapPosition;
        gapLength = (int) (Math.random() * maxGapLength) + 1;
        child = new Msa(alignment, false);
        for (i = 0; i < alignment.length; i++){
            gapPosition = (int) (Math.random() * (alignment[i].length() + 1));
            child.alignment[i].insertGap(gapPosition, gapLength);
        }
        return child;
    }

    public Msa mutationMoveBlockHorizontally(int maxMovement){
        Msa child;
        int i, blockIndex, blockMovement;
        blockMovement = (int) (Math.random() * maxMovement) + 1;
        blockIndex = (int) (Math.random() * (alignment.length - 1));
        child = new Msa(alignment, false);
        for (i = 0; i <= blockIndex; i++){
            child.alignment[i] = child.alignment[i].extend(0, blockMovement);
        }
        for (i = blockIndex + 1; i < child.alignment.length; i++){
            child.alignment[i] = child.alignment[i].extend(blockMovement, 0);
        }
        return child;
    }

    public List sameCode(){
        int i, j, len;
        List result = new List();
        boolean same;
        len = alignmentLength();
        for (i = 0; i < len; i++){
            same = true;
            for (j = 0; j < alignment.length - 1; j++){
                if (alignment[j].charAt(i) != alignment[j + 1].charAt(i)){
                    same = false;
                    break;
                }
            }
            if (same){
                result.insertBack(i);
            }
        }
        return result;
    }

}
