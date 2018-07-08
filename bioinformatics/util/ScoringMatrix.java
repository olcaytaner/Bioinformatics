package bioinformatics.util;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * User: root
 * Date: Nov 13, 2007
 * Time: 4:27:29 PM
 */
public class ScoringMatrix {

    final String codons = "ARNDCQEGHILKMFPSTWYVBZX";
    private int[][] matrix;

    public ScoringMatrix(String filename){
        Scanner sc;
        int i, j;
        matrix = new int[23][23];
        try{
            sc = new Scanner(new File(filename));
            sc.nextLine();
            for (i = 0; i < 23; i++){
                sc.next();
                for (j = 0; j < 23; j++){
                    matrix[i][j] = sc.nextInt();
                }
            }
        }
        catch (FileNotFoundException fileNotFoundException){
            System.out.println("File " + filename + " not found");
        }
    }

    public int get(int i, int j){
        return matrix[i][j];
    }

    public int get(char i, char j){
        return matrix[codons.indexOf(i)][codons.indexOf(j)];
    }
}
