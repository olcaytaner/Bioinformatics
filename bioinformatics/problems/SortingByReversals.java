package bioinformatics.problems;

import bioinformatics.datastructure.List;
import bioinformatics.datastructure.ListNode;
import bioinformatics.util.StripList;

import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * User: root
 * Date: Jun 13, 2007
 * Time: 4:20:18 PM
 */
public class SortingByReversals {

    private int n;
    private int[] permutation;
    private List solution;

    public SortingByReversals(String filename){
        /*Page 129*/
        Scanner sc;
        int i;
        try{
            sc = new Scanner(new File(filename));
            n = sc.nextInt();
            permutation = new int[n];
            for (i = 0; i < n; i++){
                    permutation[i] = sc.nextInt();
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

    private void reversal(int start, int end){
        int i, tmp;
        for (i = start; i < (start + end) / 2.0; i++){
            tmp = permutation[i];
            permutation[i] = permutation[start + end - i];
            permutation[start + end - i] = tmp;
        }
    }

    private boolean isIdentity(int start){
        /*Starts checking from start*/
        int i;
        for (i = start; i < n; i++){
            if (permutation[i] != i + 1){
                return false;
            }
        }
        return true;
    }

    public void simpleReversalSort(){
        int i, j;
        solution = new List();
        solution.insertBack(permutation.clone());
        for (i = 0; i < n - 1; i++){
            for (j = i; j < n; j++){
                if (permutation[j] == i + 1){
                    break;
                }
            }
            if (i != j){
                reversal(i, j);
                solution.insertBack(permutation.clone());
            }
            if (isIdentity(i + 1)){
                return;
            }
        }
    }

    public void breakpointReversalSort(){
        StripList strips;
        int direction;
        strips = new StripList(permutation, n);
        solution = new List();
        solution.insertBack(strips.toArray());
        while (strips.firstNode.next() != null){/*while list has more than one element*/
            if (strips.hasDecreasingStrip()){
                direction = strips.reversalMinimizingB();
                strips.reversal(direction);
            }
            else{
                strips.noProgressReversal();
            }
            solution.insertBack(strips.toArray());
        }
    }

    public void generateProblem(int n, String filename){
        FileWriter outfile;
        int[] permutation;
        int i, index, tmp;
        try{
            outfile = new FileWriter(filename);
            permutation = new int [n];
            for (i = 0; i < n; i++){
                permutation[i] = i + 1;
            }
            for (i = 0; i < n - 1; i++){
                index = (int) (Math.random() * (n - i));
                tmp = permutation[i];
                permutation[i] = permutation[index + i];
                permutation[index + i] = tmp;
            }
            outfile.write(n + "\n");
            for (i = 0; i < n; i++){
                outfile.write(permutation[i] + " ");
            }
            outfile.close();
        }
        catch (IOException ioException){
            System.out.println("Output file can not be opened");
        }
    }

    public void outputSolution(String filename){
        int j;
        int[] list;
        FileWriter outfile;
        ListNode current;
        try{
            outfile = new FileWriter(filename);
            current = solution.firstNode;
            while (current != null){
                list = int[].class.cast(current.get());
                for (j = 0; j < n; j++){
                    outfile.write(list[j] + " ");
                }
                outfile.write("\n");
                current = current.next();
            }
            outfile.close();
        }
        catch (IOException ioException){
            System.out.println("Output file can not be opened");
        }
    }

}
