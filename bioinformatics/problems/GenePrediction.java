package bioinformatics.problems;

import bioinformatics.util.PutativeExon;
import bioinformatics.datastructure.List;
import bioinformatics.datastructure.ListNode;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * User: Olcay
 * Date: Jul 2, 2007
 * Time: 10:58:22 AM
 */
public class GenePrediction {

    private PutativeExon[] exons;
    private int n;
    private List solution;

    public GenePrediction(String filename){
        /*Page 201*/
        Scanner sc;
        int exoncount;
        int i, left, right, weight;
        try{
            sc = new Scanner(new File(filename));
            exoncount = sc.nextInt();
            n = sc.nextInt();
            exons = new PutativeExon[n];
            for (i = 0; i < exoncount; i++){
                left = sc.nextInt();
                right = sc.nextInt();
                weight = sc.nextInt();
                exons[right - 1] = new PutativeExon(left, right, weight);
            }
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

    private void produceSolution(int[] s, int[] b){
        int i;
        solution = new List();
        i = n - 1;
        while (i > 0){
            if (s[i] != s[b[i]]){
                solution.insertFront(exons[i]);
            }
            i = b[i];
        }
    }

    public void exonChaining(){
        int[] s;
        int[] b; /*For backtracking*/
        int i, j, w;
        s = new int[n]; /*Initialized to zero*/
        b = new int[n];
        for (i = 1; i < n; i++){
            if (exons[i] != null){
                j = exons[i].getLeft();
                w = exons[i].getWeight();
                s[i] = Math.max(s[j - 1] + w, s[i - 1]);
                if (s[i] == s[j - 1] + w){
                    b[i] = j - 1;
                }
                else{
                    b[i] = i - 1;
                }
            }
            else{
                s[i] = s[i - 1];
                b[i] = i - 1;
            }
        }
        produceSolution(s, b);
    }

    public void generateProblem(int exoncount, int n, int maxweight, String filename){
        FileWriter outfile;
        int i, left, right, weight;
        int[] exonends;
        exonends = new int[n];
        try{
            outfile = new FileWriter(filename);
            outfile.write(exoncount + " " + n + "\n");
            for (i = 0; i < exoncount; i++){
                left = (int) (Math.random() * (n - 1)) + 1;
                right = (int) (Math.random() * (n - left)) + left + 1;
                weight = (int) (Math.random() * maxweight);
                if (exonends[right - 1] == 0){
                    exonends[right - 1] = right - 1;
                    outfile.write(left + " " + right + " " + weight + "\n");
                }
                else{
                    i--;
                }
            }
            outfile.close();
        }
        catch (IOException ioException){
            System.out.println("Output file can not be opened");
        }
    }

    public void outputSolution(String filename){
        FileWriter outfile;
        ListNode current;
        PutativeExon p;
        try{
            outfile = new FileWriter(filename);
            current = solution.firstNode;
            while (current != null){
                p = PutativeExon.class.cast(current.get());
                p.writeToFile(outfile);
                current = current.next();
            }
            outfile.close();
        }
        catch (IOException ioException){
            System.out.println("Output file can not be opened");
        }
    }

}
