package bioinformatics.problems;

import bioinformatics.datastructure.Iterator;
import bioinformatics.util.DnaSequence;

import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * User: Olcay
 * Date: Jun 8, 2007
 * Time: 3:42:50 PM
 */
public class MotifSearch {

    protected int t, n;
    protected String[] dna;
    private String solution;
    protected int[] bestMotif;
    final String bases = "ATGC";

    public MotifSearch(String filename){
        /*Page 98*/
        Scanner sc;
        int i;
        try{
            sc = new Scanner(new File(filename));
            t = sc.nextInt();
            n = sc.nextInt();
            dna = new String[t];
            for (i = 0; i < t; i++){
                    dna[i] = sc.next();
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

    protected int[][] findCounts(int[] s, int L, int range){
        int counts[][];
        int i, j;
        counts = new int[L][4];
        for (i = 0; i < range; i++){
            for (j = 0; j < L; j++){
                counts[j][bases.indexOf(dna[i].charAt(s[i] + j))]++;
            }
        }
        return counts;
    }

    protected int score(int[] s, int L, int range){
        int counts[][];
        int i, j, sc = 0, max;
        counts = findCounts(s, L, range);
        for (i = 0; i < L; i++){
            max = 0;
            for (j = 0; j < 4; j++){
                if (counts[i][j] > max){
                    max = counts[i][j];
                }
            }
            sc += max;
        }
        return sc;
    }

    public String getSolution(){
        return solution;
    }

    protected void produceSolutionFromMotif(int L){
        int[][] counts;
        int i, j, max, maxIndex;
        counts = findCounts(bestMotif, L, t);
        solution = "";
        for (i = 0; i < L; i++){
            max = 0;
            maxIndex = -1;
            for (j = 0; j < 4; j++){
                if (counts[i][j] > max){
                    max = counts[i][j];
                    maxIndex = j;
                }
            }
            solution += bases.charAt(maxIndex);
        }
    }

    public void bruteForceMotifSearch(int L){
        Iterator s;
        int[] motif;
        int bestScore, currentScore;
        s = new Iterator(t, n - L);
        motif = s.get();
        bestMotif = motif.clone();
        bestScore = score(motif, L, t);
        while (s.nextLeaf()){
            currentScore = score(motif, L, t);
            if (currentScore > bestScore){
                bestScore = currentScore;
                bestMotif = motif.clone();
            }
        }
        produceSolutionFromMotif(L);
    }

    public void branchAndBoundMotifSearch(int L){
        Iterator s;
        int bestScore, currentScore, optimisticScore, i;
        int [] motif;
        boolean continueing = true;
        s = new Iterator(t, n - L);
        motif = s.get();
        bestMotif = motif.clone();
        bestScore = 0;
        i = 0;
        while (continueing){
            if (i < t - 1){
                optimisticScore = score(motif, L, i + 1) + (t - i - 1) * L;
                if (optimisticScore < bestScore){
                    continueing = s.byPass(i);
                }
                else{
                    continueing = s.nextVertex(i);
                }
            }
            else{
                currentScore = score(motif, L, t);
                if (currentScore > bestScore){
                    bestScore = currentScore;
                    bestMotif = motif.clone();
                }
                continueing = s.nextVertex(i);
            }
            i = s.getLevel();
        }
        produceSolutionFromMotif(L);
    }

    public void greedyMotifSearch(int L){
        Iterator s;
        int[] motif;
        int i, j;
        s = new Iterator(t, n - L);
        motif = s.get();
        bestMotif = motif.clone();
        for (i = 0; i < n - L + 1; i++){
            s.set(0, i);
            for (j = 0; j < n - L + 1; j++){
                s.set(1, j);
                if (score(motif, L, 2) > score(bestMotif, L, 2)){
                    bestMotif[0] = i;
                    bestMotif[1] = j;
                }
            }
        }
        for (i = 2; i < t; i++){
            for (j = 0; j < n - L + 1; j++){
                s.set(i, j);
                if (score(motif, L, i + 1) > score(bestMotif, L, i + 1)){
                    bestMotif[i] = j;
                }
            }
            s.set(i, bestMotif[i]);
        }
        produceSolutionFromMotif(L);
    }

    private int totalDistance(int[] word, int L, int range, int[] motif){
        int total = 0, min, distance;
        int i, j, k;
        for (i = 0; i < t; i++){
            min = L + 1;
            for (j = 0; j < n - L + 1; j++){
                distance = 0;
                for (k = 0; k < range; k++){
                    if (word[k] != bases.indexOf(dna[i].charAt(j + k))){
                        distance++;
                    }
                }
                if (distance < min){
                    min = distance;
                    motif[i] = j;
                }
            }
            total += min;
        }
        return total;
    }

    public void bruteForceMedianSearch(int L){
        Iterator s;
        DnaSequence seq;
        int[] bestWord, word, motif;
        int bestDistance, distance;
        motif = new int[t];
        s = new Iterator(L, 3);
        word = s.get();
        bestWord = word.clone();
        bestDistance = totalDistance(word, L, L, motif);
        bestMotif = motif.clone();
        while (s.nextLeaf()){
            distance = totalDistance(word, L, L, motif);
            if (distance < bestDistance){
                bestDistance = distance;
                bestWord = word.clone();
                bestMotif = motif.clone();
            }
        }
        seq = new DnaSequence(bestWord);
        solution = seq.get();
    }

    public void branchAndBoundMedianSearch(int L){
        Iterator s;
        DnaSequence seq;
        int bestDistance, distance, i, optimisticDistance;
        int[] bestWord, word, motif;
        boolean continueing = true;
        motif = new int[t];
        s = new Iterator(L, 3);
        word = s.get();
        bestWord = word.clone();
        bestDistance = totalDistance(word, L, L, motif);
        bestMotif = motif.clone();
        i = 0;
        while (continueing){
            if (i < L - 1){
                optimisticDistance = totalDistance(word, L, i + 1, motif);
                if (optimisticDistance > bestDistance){
                    continueing = s.byPass(i);
                }
                else{
                    continueing = s.nextVertex(i);
                }
            }
            else{
                distance = totalDistance(word, L, L, motif);
                if (distance < bestDistance){
                    bestDistance = distance;
                    bestWord = word.clone();
                    bestMotif = motif.clone();
                }
                continueing = s.nextVertex(i);
            }
            i = s.getLevel();
        }
        seq = new DnaSequence(bestWord);
        solution = seq.get();
    }

    public void generateProblem(int t, int n, String filename){
        FileWriter outfile;
        DnaSequence seq;
        int i;
        try{
            outfile = new FileWriter(filename);
            outfile.write(t + " " + n + "\n");
            for (i = 0; i < t; i++){
                seq = new DnaSequence(n);
                outfile.write(seq.get());
                outfile.write("\n");
            }
            outfile.close();
        }
        catch (IOException ioException){
            System.out.println("Output file can not be opened");
        }        
    }

    public void outputSolution(String filename, int L){
        int i;
        FileWriter outfile;
        try{
            outfile = new FileWriter(filename);
            for (i = 0; i < t; i++){
                outfile.write(dna[i].substring(bestMotif[i], bestMotif[i] + L) + "\n");
            }
            outfile.write("---------------\n");
            outfile.write(solution);
            outfile.close();
        }
        catch (IOException ioException){
            System.out.println("Output file can not be opened");
        }
    }

}
