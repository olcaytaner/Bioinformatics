package bioinformatics.problems;

import bioinformatics.datastructure.Subset;
import bioinformatics.datastructure.SubsetFromList;
import bioinformatics.datastructure.List;
import bioinformatics.util.RandomArray;

import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Arrays;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;


public class PartialDigest {

    /**
     * @param args the command line arguments
     */
    private int[] L;
    private int[] solution;
    private int n;

    public PartialDigest(String filename){
        /*Page 86*/
        Scanner sc;
        int i, count;   
        try{
            sc = new Scanner(new File(filename));
            n = sc.nextInt();
            count = (n * (n - 1)) / 2;
            L = new int[count];
            for (i = 0; i < count; i++){
                L[i] = sc.nextInt();
            }
            Arrays.sort(L);
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

    public boolean skienaPdp(){
        int width;
        List X, LL;
        width = L[L.length - 1];
        LL = new List(L);
        /*DELETE(width, L)*/
        LL.removeBack();
        /*X = {0, width}*/
        X = new List();
        X.insert(0);
        X.insert(width);
        return place(LL, X, width);
    }

    private boolean place(List LL, List X, int width){
        int y;
        boolean left, right;
        List deltaleft, deltaright;
        if (LL.isEmpty()){
            solution = X.toArray();
            return true;
        }
        y = LL.max();
        deltaleft = X.delta(y);
        if (LL.subset(deltaleft)){
            X.insert(y);
            LL.remove(deltaleft);
            left = place(LL, X, width);
            if (left){
                return true;
            }
            X.remove(y);
            LL.insert(deltaleft);
        }
        deltaright = X.delta(width - y);
        if (LL.subset(deltaright)){
            X.insert(width - y);
            LL.remove(deltaright);
            right = place(LL, X, width);
            if (right){
                return true;
            }
            X.remove(width - y);
            LL.insert(deltaright);
        }
        return false;
    }

    public boolean bruteForcePdp(){
        int M;
        int deltaX[];
        Subset s;
        M = L[L.length - 1];
        s = new Subset(1, M - 1, n - 2);
        do{
            s.multiset(M);
            deltaX = s.getmultiset();
            if (Arrays.equals(L, deltaX)){
                solution = s.getX(M);
                return true;
            }
        }while(s.next());
        return false;
    }

    public boolean anotherBruteForcePdp(){
        int M;
        int deltaX[];
        SubsetFromList s;
        M = L[L.length - 1];
        s = new SubsetFromList(L, n - 2);
        do{
            s.multiset(M);
            deltaX = s.getmultiset();
            if (Arrays.equals(L, deltaX)){
                solution = s.getX(M);
                return true;
            }
        }while(s.next());
        return false;
    }

    public int[] getSolution(){
        return solution;
    }

    public void generateProblem(int M, int n, String filename){
        FileWriter outfile;
        int[] list, delta;
        int i, j, k;
        list = new RandomArray(n, M).get();
        delta = new int[(n * (n - 1)) / 2];
        k = 0;
        for (i = 0; i < n; i++){
            for (j = i + 1; j < n; j++){
                delta[k] = list[j] - list[i];
                k++;
            }
        }
        Arrays.sort(delta);
        try{
            outfile = new FileWriter(filename);
            outfile.write(n + " ");
            for (i = 0; i < k; i++){
                outfile.write(delta[i] + " ");
            }
            outfile.close();
        }
        catch (IOException ioException){
            System.out.println("Output file can not be opened");
        }
    }

    public void outputSolution(String filename){
        FileWriter outfile;
        int i;
        try{
            outfile = new FileWriter(filename);
            for (i = 0; i < solution.length; i++){
                outfile.write(solution[i] + " ");
            }
            outfile.close();
        }
        catch (IOException ioException){
            System.out.println("Output file can not be opened");
        }
    }

}
