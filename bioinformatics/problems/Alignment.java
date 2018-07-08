package bioinformatics.problems;

import bioinformatics.util.Sequence;

/**
 * User: Olcay
 * Date: Jul 1, 2007
 * Time: 3:35:11 PM
 */
public class Alignment  implements Comparable{

    private boolean [][] dotMatrix = null;
    protected Sequence sequence1, sequence2, consensus;
    protected int[][] b;
    protected int alignmentScore;
    protected double confidenceLevel;    
    protected int gapPenalty;
    protected int gapExtensionPenalty;

    final int UP = 1, LEFT = 2, UPLEFT = 3, FREERIDE = 4;
    final String bases = "ATGC";

    public Alignment(){

    }

    public Alignment(Sequence sequence1, Sequence sequence2, int gapPenalty, int gapExtensionPenalty){
        this.sequence1 = sequence1;
        this.sequence2 = sequence2;
        this.gapPenalty = gapPenalty;
        this.gapExtensionPenalty = gapExtensionPenalty;
    }

    public boolean[][] getDotMatrix(){
        return dotMatrix;
    }

    public void dotMatrixMethod(int windowsize, int stringency){
        int i, j, k, matchCount;
        dotMatrix = new boolean[sequence1.length() - windowsize + 1][sequence2.length() - windowsize + 1];
        for (i = 0; i < sequence1.length() - windowsize; i++){
            for (j = 0; j < sequence2.length() - windowsize; j++){
                matchCount = 0;
                for (k = 0; k < windowsize; k++){
                    if (sequence1.charAt(i + k) == sequence2.charAt(j + k)){
                        matchCount++;
                    }
                }
                dotMatrix[i][j] = (matchCount >= stringency);
            }
        }
    }

    private void printLcs(String solution, int i, int j){
        if (i == 0 || j == 0){
            return;
        }
        if (b[i][j] == UPLEFT){
            printLcs(solution, i - 1, j - 1);
            solution += sequence1.charAt(i - 1);
        }
        else{
            if (b[i][j] == UP){
                printLcs(solution, i - 1, j);
                solution += sequence1.charAt(i - 1);
            }
            else{
                if (b[i][j] == LEFT){
                    printLcs(solution, i, j - 1);
                    solution += sequence2.charAt(j - 1);
                }
            }
        }
    }

    protected void backtrackPointers(int i, int j){
        String solution = "";
        printLcs(solution, i, j);
        consensus = new Sequence(solution);
    }

    protected int localMaxScore(int[][] s){
        int i, j, n, m, maxi, maxj, maxScore;
        maxi = n = sequence1.length();
        maxj = m = sequence2.length();
        maxScore = s[n][m];
        for (i = 0; i <= n; i++){
            for (j = 0; j <=m; j++){
                if (s[i][j] > maxScore){
                    maxScore = s[i][j];
                    maxi = i;
                    maxj = j;
                }
            }
        }
        backtrackPointers(maxi, maxj);
        return maxScore;
    }

    public int compareTo(Object o) {
        ProteinAlignment p = (ProteinAlignment)o;
        if (this.alignmentScore < p.alignmentScore){
            return -1;
        } else {
            if (this.alignmentScore > p.alignmentScore){
                return 1;
            } else {
                return 0;
            }
        }
    }
    
}
