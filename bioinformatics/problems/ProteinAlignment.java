package bioinformatics.problems;

import bioinformatics.util.ScoringMatrix;
import bioinformatics.util.Sequence;

import java.util.Arrays;

/**
 * User: root
 * Date: Nov 28, 2007
 * Time: 1:29:00 PM
 */
public class ProteinAlignment extends Alignment{

    private ScoringMatrix mat;

    public ProteinAlignment(){

    }

    public ProteinAlignment(Sequence sequence1, Sequence sequence2, ScoringMatrix mat, int gapPenalty){
        super(sequence1, sequence2, gapPenalty, 0);
        this.mat = mat;
    }

    public ProteinAlignment(Sequence sequence1, Sequence sequence2, ScoringMatrix mat, int gapOpeningPenalty, int gapExtensionPenalty){
        super(sequence1, sequence2, gapOpeningPenalty, gapExtensionPenalty);
        this.mat = mat;
    }

    public void globalAlignment(){
        int n, m;
        int i, j;
        int[][] s;
        n = sequence1.length();
        m = sequence2.length();
        s = new int[n + 1][m + 1];
        b = new int[n + 1][m + 1];
        for (i = 1; i <= n; i++){
            for (j = 1; j <= m; j++){
                s[i][j] = Math.max(Math.max(s[i - 1][j] + gapPenalty, s[i][j - 1] + gapPenalty), s[i - 1][j - 1] + mat.get(sequence1.charAt(i - 1), sequence2.charAt(j - 1)));
                if (s[i][j] == s[i - 1][j] + gapPenalty){
                    b[i][j] = UP;
                }
                else{
                    if (s[i][j] == s[i][j - 1] + gapPenalty){
                        b[i][j] = LEFT;
                    }
                    else{
                        b[i][j] = UPLEFT;
                    }
                }
            }
        }
        backtrackPointers(n, m);
        alignmentScore = s[n][m];
    }

    public void globalAlignmentComplexPenalty(){
        int n, m;
        int i, j, k;
        int sijdown, sijright;
        int[][] s;
        n = sequence1.length();
        m = sequence2.length();
        s = new int[n + 1][m + 1];
        b = new int[n + 1][m + 1];
        for (i = 1; i <= n; i++){
            for (j = 1; j <= m; j++){
                sijdown = s[i - 1][j] + gapPenalty;
                for (k = 2; k <= i; k++){
                    sijdown = Math.max(sijdown, s[i - k][j] + gapPenalty + (k - 1) * gapExtensionPenalty);
                }
                sijright = s[i][j - 1] + gapPenalty;
                for (k = 2; k <= j; k++){
                    sijright = Math.max(sijright, s[i][j - k] + gapPenalty + (k - 1) * gapExtensionPenalty);
                }
                s[i][j] = Math.max(Math.max(sijdown, sijright), s[i - 1][j - 1] + mat.get(sequence1.charAt(i - 1), sequence2.charAt(j - 1)));
                if (s[i][j] == sijdown){
                    b[i][j] = UP;
                }
                else{
                    if (s[i][j] == sijright){
                        b[i][j] = LEFT;
                    }
                    else{
                        b[i][j] = UPLEFT;
                    }
                }
            }
        }
        backtrackPointers(n, m);
        alignmentScore = s[n][m];
    }

    public void randomization(int shuffleCount, boolean complexPenalty){
        int i;
        ProteinAlignment p;
        int[] scores = new int[shuffleCount];
        if (complexPenalty) {
            globalAlignmentComplexPenalty();
        }
        else {
            globalAlignment();
        }
        p = new ProteinAlignment(sequence1, sequence2, mat, gapPenalty, gapExtensionPenalty);
        for (i = 0; i < shuffleCount; i++){
            p.sequence2 = sequence2.shuffle();
            if (complexPenalty){
                p.globalAlignmentComplexPenalty();
            }
            else{
                p.globalAlignment();
            }
            scores[i] = p.alignmentScore;
        }
        Arrays.sort(scores);
        for (i = 0; i < shuffleCount; i++){
            if (alignmentScore < scores[i]){
                break;
            }
        }
        confidenceLevel = 1 - (i + 0.0) / shuffleCount;
    }

    public void relate(int subSequenceLength, boolean complexPenalty){
        int i, j, index;
        Sequence subsequence1, subsequence2;
        ProteinAlignment[] p;
        ProteinAlignmentComparator pc = new ProteinAlignmentComparator();
        p = new ProteinAlignment[(sequence1.length() - subSequenceLength) * (sequence2.length() - subSequenceLength)];
        index = 0;
        for (i = 0; i < sequence1.length() - subSequenceLength; i++){
            for (j = 0; j < sequence2.length() - subSequenceLength; j++){
                subsequence1 = new Sequence(sequence1.get().substring(i, i + subSequenceLength));
                subsequence2 = new Sequence(sequence2.get().substring(j, j + subSequenceLength));
                p[index] = new ProteinAlignment(subsequence1, subsequence2, mat, gapPenalty, gapExtensionPenalty);
                if (complexPenalty){
                    p[index].globalAlignmentComplexPenalty();
                }
                else{
                    p[index].globalAlignment();
                }
                index++;
            }
        }
        Arrays.sort(p, pc);
    }

    public void localAlignment(double K, double lambda){
        int n, m;
        int i, j, alignmentScore;
        int[][] s;
        double SPrime;
        n = sequence1.length();
        m = sequence2.length();
        s = new int[n + 1][m + 1];
        b = new int[n + 1][m + 1];
        for (i = 0; i <= n; i++){
            s[i][0] = 0;
        }
        for (j = 1; j <= m; j++){
            s[0][j] = 0;
        }
        for (i = 1; i <= n; i++){
            for (j = 1; j <= m; j++){
                s[i][j] = Math.max(0, Math.max(Math.max(s[i - 1][j] + gapPenalty, s[i][j - 1] + gapPenalty), s[i - 1][j - 1] + mat.get(sequence1.charAt(i - 1), sequence2.charAt(j - 1))));
                if (s[i][j] == s[i - 1][j] + gapPenalty){
                    b[i][j] = UP;
                }
                else{
                    if (s[i][j] == s[i][j - 1] + gapPenalty){
                        b[i][j] = LEFT;
                    }
                    else{
                        if (s[i][j] == 0){
                            b[i][j] = FREERIDE;
                        }
                        else{
                            b[i][j] = UPLEFT;
                        }
                    }
                }
            }
        }
        alignmentScore = localMaxScore(s);
        SPrime = lambda * alignmentScore - Math.log(K * n * m);
        confidenceLevel = Math.log(-SPrime);
    }

    public void localAlignmentComplexPenalty(double K, double lambda){
        int n, m;
        double SPrime;
        int i, j, k, alignmentScore;
        int sijdown, sijright;
        int[][] s;
        n = sequence1.length();
        m = sequence2.length();
        s = new int[n + 1][m + 1];
        b = new int[n + 1][m + 1];
        for (i = 1; i <= n; i++){
            for (j = 1; j <= m; j++){
                sijdown = s[i - 1][j] + gapPenalty;
                for (k = 2; k <= i; k++){
                    sijdown = Math.max(sijdown, s[i - k][j] + gapPenalty + (k - 1) * gapExtensionPenalty);
                }
                sijright = s[i][j - 1] + gapPenalty;
                for (k = 2; k <= j; k++){
                    sijright = Math.max(sijright, s[i][j - k] + gapPenalty + (k - 1) * gapExtensionPenalty);
                }
                s[i][j] = Math.max(0, Math.max(Math.max(sijdown, sijright), s[i - 1][j - 1] + mat.get(sequence1.charAt(i - 1), sequence2.charAt(j - 1))));
                if (s[i][j] == sijdown){
                    b[i][j] = UP;
                }
                else{
                    if (s[i][j] == sijright){
                        b[i][j] = LEFT;
                    }
                    else{
                        if (s[i][j] == 0){
                            b[i][j] = FREERIDE;
                        }
                        else{
                            b[i][j] = UPLEFT;
                        }
                    }
                }
            }
        }
        alignmentScore = localMaxScore(s);
        SPrime = lambda * alignmentScore - Math.log(K * n * m);
        confidenceLevel = Math.log(-SPrime);
    }
}
