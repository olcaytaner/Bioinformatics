package bioinformatics.problems;

import bioinformatics.datastructure.Permutation;

import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * User: root
 * Date: Oct 30, 2007
 * Time: 1:22:28 PM
 */
public class MultipleBreakpointDistance {

    private int k;
    private int n;
    private String[] permutations;
    private int[] solution;

    public MultipleBreakpointDistance(String filename){
        int i;
        Scanner sc;
        try{
            sc = new Scanner(new File(filename));
            k = sc.nextInt();
            permutations = new String[k];
            n = sc.nextInt();
            for (i = 0; i < k; i++){
                permutations[i] = sc.next();
            }
        }
        catch (FileNotFoundException fileNotFoundException){
            System.out.println("File " + filename + " not found");
        }
        catch (InputMismatchException inputMismatchException){
            System.out.println("Input file does not contain strings");
        }
        catch (NoSuchElementException noSuchElementException){
            System.out.println("Not enough elements in the input file");
        }
    }

    public int[] getSolution(){
        return solution;
    }

    public int bruteForceSolution(){
        int i, j, distance, bestDistance = k * (n - 1), pos1, pos2;
        Permutation p;
        int[] ancestor;
        p = new Permutation(n);
        ancestor = p.get();
        do{
            distance = 0;
            for (i = 0; i < k; i++){
                for (j = 0; j < n - 1; j++){
                    pos1 = permutations[i].indexOf('1' + ancestor[j]);
                    pos2 = permutations[i].indexOf('1' + ancestor[j + 1]);
                    if (Math.abs(pos1 - pos2) != 1){
                        distance++;
                    }
                }
            }
            if (distance < bestDistance){
                bestDistance = distance;
                solution = ancestor.clone();
            }
        }while(p.next());
        return bestDistance;
    }

    public int greedySolution(){
        int i, j, pos, pos1, pos2, distance, bestDistance = k * (n - 1);
        for (i = 0; i < k; i++){
            distance = 0;
            for (j = 0; j < k; j++){
                for (pos = 0; pos < n - 1; pos++){
                    pos1 = permutations[j].indexOf(permutations[i].charAt(pos));
                    pos2 = permutations[j].indexOf(permutations[i].charAt(pos + 1));
                    if (Math.abs(pos1 - pos2) != 1){
                        distance++;
                    }
                }
            }
            if (distance < bestDistance){
                bestDistance = distance;
                solution = new int[n];
                for (pos = 0; pos < n; pos++){
                    solution[pos] = permutations[i].charAt(pos) - '1';
                }
            }
        }
        return bestDistance;
    }

    public void outputSolution(String filename){
        FileWriter outfile;
        int i;
        try{
            outfile = new FileWriter(filename);
            for (i = 0; i < n; i++){
                outfile.write('1' + solution[i]);
            }
            outfile.close();
        }
        catch (IOException ioException){
            System.out.println("Output file can not be opened");
        }
    }

}
