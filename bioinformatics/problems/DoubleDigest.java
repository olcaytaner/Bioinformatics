package bioinformatics.problems;

import bioinformatics.datastructure.Permutation;

import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Arrays;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * User: root
 * Date: Oct 25, 2007
 * Time: 9:26:33 AM
 */
public class DoubleDigest {

    private int[] A, B, APlusB;
    private int[] solutiona, solutionb;

    public DoubleDigest(String filename){
        /*Page 121*/
        Scanner sc;
        int i, count;
        try{
            sc = new Scanner(new File(filename));
            count = sc.nextInt();
            A = new int[count];
            for (i = 0; i < count; i++){
                A[i] = sc.nextInt();
            }
            count = sc.nextInt();
            B = new int[count];
            for (i = 0; i < count; i++){
                B[i] = sc.nextInt();
            }
            count = sc.nextInt();
            APlusB = new int[count];
            for (i = 0; i < count; i++){
                APlusB[i] = sc.nextInt();
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

    private int[] generateDoubleDigest(int[] digesta, int[] digestb){
        int[] doubleDigest;
        int leftA = A[digesta[0]], leftB = B[digestb[0]], i = 0, j = 0, count = 0;
        doubleDigest = new int[digesta.length + digestb.length - 1];
        while (i < digesta.length || j < digestb.length){
            if (leftA < leftB){
                doubleDigest[count] = leftA;
                leftB -= leftA;
                i++;
                leftA = i != digesta.length ? A[digesta[i]] : 1000000;
            }
            else{
                if (leftA > leftB){
                    doubleDigest[count] = leftB;
                    leftA -= leftB;
                    j++;
                    leftB = j != digestb.length ? B[digestb[j]] : 1000000;
                }
                else{
                    doubleDigest[count] = leftA;
                    i++;
                    leftA = i != digesta.length ? A[digesta[i]] : 1000000;
                    j++;
                    leftB = j != digestb.length ? B[digestb[j]] : 1000000;
                }
            }
            count++;
        }
        Arrays.sort(doubleDigest);
        return doubleDigest;
    }

    public boolean bruteForceDdp(){
        Permutation pa, pb;
        int[] digesta, digestb, doubleDigest;
        pa = new Permutation(A.length);
        digesta = pa.get();
        do{
            pb = new Permutation(B.length);
            digestb = pb.get();
            do{
                doubleDigest = generateDoubleDigest(digesta, digestb);
                if (Arrays.equals(APlusB, doubleDigest)){
                    solutiona = digesta.clone();
                    solutionb = digestb.clone();
                    return true;
                }
            }while(pb.next());
        }while(pa.next());
        return false;
    }

    public void outputSolution(String filename){
        FileWriter outfile;
        int i;
        try{
            outfile = new FileWriter(filename);
            for (i = 0; i < solutiona.length; i++){
                outfile.write(A[solutiona[i]] + " ");
            }
            outfile.write("\n");
            for (i = 0; i < solutionb.length; i++){
                outfile.write(B[solutionb[i]] + " ");
            }
            outfile.close();
        }
        catch (IOException ioException){
            System.out.println("Output file can not be opened");
        }
    }


}
