package bioinformatics.problems;

import bioinformatics.util.DnaSequence;
import bioinformatics.datastructure.List;
import bioinformatics.datastructure.ListNode;
import bioinformatics.datastructure.SuffixTree;
import bioinformatics.datastructure.SuffixTreeNode;

import java.util.HashSet;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * User: root
 * Date: Jul 11, 2007
 * Time: 10:35:36 AM
 */
public class PatternMatching {

    private String pattern, text;
    private List solution;

    public PatternMatching(String filename){
        /*Page 317*/
        Scanner sc;
        try{
            sc = new Scanner(new File(filename));
            text = sc.next();
            pattern = sc.next();
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

    public PatternMatching(String text, String pattern){
        this.text = text;
        this.pattern = pattern;
    }

    public void exactPatternMatching(){
        int n, m, i;
        n = pattern.length();
        m = text.length();
        solution = new List();
        for (i = 0; i <= m - n; i++){
            if (text.substring(i, i + n).equals(pattern)){
                solution.insertBack(i + 1);
            }
        }
    }

    public void suffixTreePatternMatching(){
        int i;
        SuffixTree tree;
        SuffixTreeNode node;
        tree = new SuffixTree();
        for (i = 0; i < text.length(); i++){
            tree.addSuffix(text.substring(i, text.length()), i);
        }
        node = tree.thread(pattern);
        solution = new List();
        if (node != null){
            node.descendantLabels(solution);            
        }
    }

    public void approximatePatternMatching(int k){
        int n, m, i, j, dist;
        n = pattern.length();
        m = text.length();
        solution = new List();
        for (i = 0; i < m - n + 1; i++){
            dist = 0;
            for (j = 0; j < n; j++){
                if (text.charAt(i + j) != pattern.charAt(j)){
                    dist++;
                }
            }
            if (dist <= k){
                solution.insertBack(i + 1);
            }
        }
    }

    public void generateProblem(int textLength, int patternLength, String filename){
        FileWriter outfile;
        DnaSequence seq;
        try{
            outfile = new FileWriter(filename);
            seq = new DnaSequence(textLength);
            outfile.write(seq.get() + "\n");
            seq = new DnaSequence(patternLength);
            outfile.write(seq.get() + "\n");
            outfile.close();
        }
        catch (IOException ioException){
            System.out.println("Output file can not be opened");
        }
    }

    public List getSolution(){
        return solution;
    }

    public void outputSolution(String filename){
        FileWriter outfile;
        ListNode current;
        try{
            outfile = new FileWriter(filename);
            current = solution.firstNode;
            while (current != null){
                outfile.write(Integer.class.cast(current.get()) + "\n");
                current = current.next();
            }
            outfile.close();
        }
        catch (IOException ioException){
            System.out.println("Output file can not be opened");
        }
    }

}
