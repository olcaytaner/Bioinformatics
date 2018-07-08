package bioinformatics.problems;

import bioinformatics.util.DnaSequence;
import bioinformatics.datastructure.Iterator;

import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * User: Olcay
 * Date: Jul 2, 2007
 * Time: 3:48:51 PM
 */
public class BlockAlignment extends LongestCommonSubsequence{

    private int t;
    private int sigma;
    private int[][] table;
    final String indels = "--------------------";

    public BlockAlignment(String filename){
        /*Page 237*/
        Scanner sc;
        try{
            sc = new Scanner(new File(filename));
            t = sc.nextInt();
            sigma = sc.nextInt();
            v = sc.next();
            w = sc.next();
        }
        catch (FileNotFoundException fileNotFoundException){
            System.out.println("File " + filename + " not found");
        }
        catch (InputMismatchException inputMismatchException){
            System.out.println("Input file does not contain valid characters");
        }
        catch (NoSuchElementException noSuchElementException){
            System.out.println("Not enough elements in the input file");
        }
    }

    protected void constructLookupTable(){
        Iterator i, j;
        DnaSequence seq1, seq2;
        LongestCommonSubsequence minialignment;
        int score, index1, index2, tablesize;
        tablesize = (int)Math.pow(4, t);
        table = new int[tablesize][tablesize];
        i = new Iterator(t, 3);
        j = new Iterator(t, 3);
        index1 = 0;
        do{
            seq1 = new DnaSequence(i.get());
            index2 = 0;
            do{
                seq2 = new DnaSequence(j.get());
                minialignment = new LongestCommonSubsequence(seq1.get(), seq2.get());
                score = minialignment.similarityScore();
                table[index1][index2] = score;
                index2++;
            }while(j.nextLeaf());
            index1++;
        }while(i.nextLeaf());
    }

    protected void printlcs(int i, int j){
        if (i == 0 || j == 0){
            return;
        }
        if (b[i][j] == UPLEFT){
            printLcs(i - 1, j - 1);
            alignedv += v.substring((i - 1) * t, i * t);
            alignedw += w.substring((j - 1) * t, j * t);
            solution += v.substring((i - 1) * t, i * t);
        }
        else{
            if (b[i][j] == UP){
                printLcs(i - 1, j);
                alignedv += v.substring((i - 1) * t, i * t);
                alignedw += indels.substring(0, t);
            }
            else{
                if (b[i][j] == LEFT){
                    printLcs(i, j - 1);
                    alignedv += indels.substring(0, t);
                    alignedw += w.substring((j - 1) * t, j * t);
                }
            }
        }
    }

    public void lcs(){
        int n, m;
        int i, j;
        int score;
        int[][] s;
        DnaSequence iblock, jblock;
        n = v.length() / t;
        m = w.length() / t;
        s = new int[n + 1][m + 1];
        b = new int[n + 1][m + 1];
        constructLookupTable();
        for (i = 1; i <= n; i++){
            iblock = new DnaSequence(v.substring((i - 1) * t, i * t));
            for (j = 1; j <= m; j++){
                jblock = new DnaSequence(w.substring((j - 1) * t, j * t));
                score = table[iblock.getCode()][jblock.getCode()];
                s[i][j] = Math.max(Math.max(s[i - 1][j] - sigma, s[i][j - 1] - sigma), s[i - 1][j - 1] + score);
                if (s[i][j] == s[i - 1][j] - sigma){
                    b[i][j] = UP;
                }
                else {
                    if (s[i][j] == s[i][j - 1] - sigma){
                        b[i][j] = LEFT;
                    }
                    else{
                        b[i][j] = UPLEFT;
                    }
                }
            }
        }
        alignedv = "";
        alignedw = "";
        solution = "";
        printlcs(n, m);
    }

    public void generateProblem(int m, int n, int t, String filename){
        FileWriter outfile;
        DnaSequence seq;
        int sigma;
        try{
            outfile = new FileWriter(filename);
            sigma = (int) (Math.random() * t) + 1;
            outfile.write(t + " " + sigma + "\n");
            seq = new DnaSequence(m * t);
            outfile.write(seq.get());
            outfile.write("\n");
            seq = new DnaSequence(n * t);
            outfile.write(seq.get());
            outfile.write("\n");
            outfile.close();
        }
        catch (IOException ioException){
            System.out.println("Output file can not be opened");
        }
    }

}
