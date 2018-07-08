package bioinformatics.util;

import java.io.FileWriter;
import java.io.IOException;

/**
 * User: Olcay
 * Date: Jul 2, 2007
 * Time: 10:53:43 AM
 */
public class PutativeExon {

    private int left, right, weight;

    public PutativeExon(int left, int right, int weight){
        /*Page 201*/
        this.left = left;
        this.right = right;
        this.weight = weight;
    }

    public int getLeft(){
        return left;
    }

    public int getWeight(){
        return weight;
    }

    public int getRight(){
        return right;
    }

    public void writeToFile(FileWriter outfile){
        try{
            outfile.write(left + " " + right + " " + weight + "\n");
        }
        catch (IOException ioException){
            System.out.println("Error writing output file");
        }
    }

}
