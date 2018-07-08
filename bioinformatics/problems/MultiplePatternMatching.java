package bioinformatics.problems;

import bioinformatics.datastructure.List;
import bioinformatics.datastructure.ListNode;
import bioinformatics.datastructure.KeywordTree;
import bioinformatics.util.DnaSequence;

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
 * Time: 1:22:23 PM
 */
public class MultiplePatternMatching {

    private String text;
    private String patterns[];
    private List solution;
    private int k;

    public MultiplePatternMatching(String filename){
        /*Page 318*/
        int i;
        Scanner sc;
        try{
            sc = new Scanner(new File(filename));
            k = sc.nextInt();
            text = sc.next();
            patterns = new String[k];
            for (i = 0; i < k; i++){
                patterns[i] = sc.next();
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

    public void generateProblem(int textLength, int patternCount, int maxPatternLength, String filename){
        int i, patternLength;
        FileWriter outfile;
        DnaSequence seq;
        try{
            outfile = new FileWriter(filename);
            outfile.write(patternCount + "\n");
            seq = new DnaSequence(textLength);
            outfile.write(seq.get() + "\n");
            for (i = 0; i < patternCount; i++){
                patternLength = (int) (Math.random() * maxPatternLength) + 1;
                seq = new DnaSequence(patternLength);
                outfile.write(seq.get() + "\n");
            }
            outfile.close();
        }
        catch (IOException ioException){
            System.out.println("Output file can not be opened");
        }
    }

    public void simpleSolution(){
        int i;
        PatternMatching p;
        List current;
        solution = new List();
        for (i = 0; i < k; i++){
            p = new PatternMatching(text, patterns[i]);
            p.exactPatternMatching();
            current = p.getSolution();
            solution.merge(current);
        }
    }

    public void keywordTreeSolution(){
        KeywordTree tree;
        int i;
        tree = new KeywordTree();
        for (i = 0; i < k; i++){
            tree.addPattern(patterns[i]);
        }
        solution = new List();
        for (i = 0; i < text.length(); i++){
            if (tree.checkPattern(text.substring(i, text.length()))){
                solution.insertBack(i + 1);
            }
        }
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
