package bioinformatics.problems;

import java.io.FileWriter;
import java.io.IOException;

/**
 * User: Olcay
 * Date: Jun 30, 2007
 * Time: 4:30:21 PM
 */
public class LongestCommonSubsequence {

    protected String v;
    protected String w;
    protected int[][] b;
    protected String solution;
    protected String alignedv;
    protected String alignedw;

    final int UP = 1, LEFT = 2, UPLEFT = 3, FREERIDE = 4;
    final String bases = "ATGC";

    public LongestCommonSubsequence(){

    }

    public LongestCommonSubsequence(String sequence1, String sequence2){
        this.v = sequence1;
        this.w = sequence2;
    }

    protected void printLcs(int i, int j){
        if (i == 0 || j == 0){
            return;
        }
        if (b[i][j] == UPLEFT){
            printLcs(i - 1, j - 1);
            alignedv += v.charAt(i - 1);
            alignedw += w.charAt(j - 1);
            solution += v.charAt(i - 1);
        }
        else{
            if (b[i][j] == UP){
                printLcs(i - 1, j);
                alignedv += v.charAt(i - 1);
                alignedw += "-";
                solution += v.charAt(i - 1);
            }
            else{
                if (b[i][j] == LEFT){
                    printLcs(i, j - 1);
                    alignedv += "-";
                    alignedw += w.charAt(j - 1);
                    solution += w.charAt(j - 1); 
                }
            }
        }
    }

    protected int similarityScore(){
        int n, m;
        int i, j;
        int[][] s;
        n = v.length();
        m = w.length();
        s = new int[n + 1][m + 1];
        b = new int[n + 1][m + 1];
        for (i = 1; i <= n; i++){
            for (j = 1; j <= m; j++){
                if (v.charAt(i - 1) == w.charAt(j - 1)){
                    s[i][j] = Math.max(Math.max(s[i - 1][j], s[i][j - 1]), s[i - 1][j - 1] + 1);
                }
                else{
                    s[i][j] = Math.max(s[i - 1][j], s[i][j - 1]);
                }
                if (s[i][j] == s[i - 1][j]){
                    b[i][j] = UP;
                }
                else {
                    if (s[i][j] == s[i][j - 1]){
                        b[i][j] = LEFT;
                    }
                    else{
                        b[i][j] = UPLEFT;
                    }
                }
            }
        }
        return s[n][m];
    }

    public void lcs(){
        similarityScore();
        solution = "";
        alignedv = "";
        alignedw = "";
        printLcs(v.length(), w.length());
    }

    public void outputSolution(String filename){
        FileWriter outfile;
        try{
            outfile = new FileWriter(filename);
            outfile.write(alignedv + "\n");
            outfile.write(alignedw + "\n");
            outfile.write(solution + "\n");
            outfile.close();
        }
        catch (IOException ioException){
            System.out.println("Output file can not be opened");
        }
    }
}
