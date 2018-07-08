package bioinformatics.problems;

import bioinformatics.util.DnaSequence;
import bioinformatics.util.Sequence;
import bioinformatics.datastructure.Permutation;
import bioinformatics.datastructure.List;
import bioinformatics.datastructure.ListNode;

import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * User: root
 * Date: Jul 5, 2007
 * Time: 9:20:56 AM
 */
public class ShortestSuperString {

    int n;
    int[] order;
    Sequence[] strings;
    String solution;

    public ShortestSuperString(String filename){
        /*Page 264*/
        Scanner sc;
        int i;
        try{
            sc = new Scanner(new File(filename));
            n = sc.nextInt();
            strings = new Sequence[n];
            for (i = 0; i < n; i++){
                strings[i] = new Sequence(sc.next());
            }
        }
        catch (FileNotFoundException fileNotFoundException){
            System.out.println("File " + filename + " not found");
        }
        catch (InputMismatchException inputMismatchException){
            System.out.println("Input file does not contain integers");
        }
        catch (NoSuchElementException noSuchElementException){
            System.out.println("Not enough elements in the input file");
        }
    }

    public void generateProblem(int stringcount, int maxlength, String filename){
        FileWriter outfile;
        DnaSequence seq;
        int i, length;
        try{
            outfile = new FileWriter(filename);
            outfile.write(stringcount + "\n");
            for (i = 0; i < stringcount; i++){
                length = (int) (Math.random() * maxlength) + 1;
                seq = new DnaSequence(length);
                outfile.write(seq.get() + "\n");
            }
            outfile.close();
        }
        catch (IOException ioException){
            System.out.println("Output file can not be opened");
        }
    }

    public void hamiltonianPath(){
        Permutation p;
        int[] currentp;
        int i;
        Sequence superstring;
        superstring = new Sequence("");
        solution = null;
        p = new Permutation(n);
        do{
            currentp = p.get();
            superstring.set(strings[currentp[0]].get());
            for (i = 1; i < n; i++){
                superstring.cat(strings[currentp[i]]);
            }
            if (solution == null || superstring.length() < solution.length()){
                solution = superstring.get();
            }
        }while(p.next());
    }

    public void greedySolution(){
        List seqList;
        int i, bestOverlap, currentOverlap;
        DnaSequence iseq, jseq;
        ListNode inode, jnode, first, second;
        seqList = new List();
        for (i = 0; i < n; i++){
            seqList.insertBack(strings[i]);
        }
        while (seqList.firstNode.next() != null){
            first = seqList.firstNode;
            second = seqList.firstNode.next();
            bestOverlap = DnaSequence.class.cast(first.get()).overlap(DnaSequence.class.cast(second.get()));
            inode = seqList.firstNode;
            while (inode.next() != null){
                jnode = inode.next();
                while (jnode != null){
                    iseq = DnaSequence.class.cast(inode.get());
                    jseq = DnaSequence.class.cast(jnode.get());
                    currentOverlap = iseq.overlap(jseq);
                    if (currentOverlap > bestOverlap){
                        first = inode;
                        second = jnode;
                        bestOverlap = currentOverlap;
                    }
                    currentOverlap = jseq.overlap(iseq);
                    if (currentOverlap > bestOverlap){
                        first = jnode;
                        second = inode;
                        bestOverlap = currentOverlap;
                    }
                    jnode = jnode.next();
                }
                inode = inode.next();
            }
            iseq = DnaSequence.class.cast(first.get());
            jseq = DnaSequence.class.cast(second.get());
            iseq.cat(jseq);
            seqList.remove(second);
        }
        solution = DnaSequence.class.cast(seqList.firstNode.get()).get();
    }

    public void outputSolution(String filename){
        FileWriter outfile;
        try{
            outfile = new FileWriter(filename);
            outfile.write(solution);
            outfile.close();
        }
        catch (IOException ioException){
            System.out.println("Output file can not be opened");
        }
    }

}
