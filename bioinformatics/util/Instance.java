package bioinformatics.util;

import java.io.FileWriter;
import java.io.IOException;

/**
 * User: Olcay
 * Date: Jul 16, 2007
 * Time: 10:37:37 AM
 */
public class Instance {

    private float[] attributes;

    public Instance(int m){
        attributes = new float[m];
    }

    public Instance(float[] attributes){
        this.attributes = attributes.clone();
    }

    public void clear(){
        int i;
        for (i = 0; i < attributes.length; i++){
            attributes[i] = (float) 0.0;
        }
    }

    public void addTo(Instance secondInstance){
        int i;
        for (i = 0; i < attributes.length; i++){
            attributes[i] += secondInstance.attributes[i];
        }
    }

    public void divideTo(float c){
        int i;
        if (c == 0.0){
            return;
        }
        for (i = 0; i < attributes.length; i++){
            attributes[i] /= c;
        }
    }

    public void writeFile(FileWriter outfile){
        int i;
        try{
            for (i = 0; i < attributes.length; i++){
                outfile.write(attributes[i] + " ");
            }
            outfile.write("\n");
        }
        catch (IOException ioException){
            System.out.println("Output file can not be opened");
        }
    }

    public float distance(Instance secondInstance){
        int i;
        float sum = 0;
        for (i = 0; i < attributes.length; i++){
            sum += (attributes[i] - secondInstance.attributes[i]) * (attributes[i] - secondInstance.attributes[i]);
        }
        return sum;
    }

    public float[] getAttributes(){
        return attributes;
    }

}
