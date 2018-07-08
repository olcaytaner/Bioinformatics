package bioinformatics.util;

/**
 * User: Olcay
 * Date: Nov 17, 2007
 * Time: 1:27:16 PM
 */
public class HmmState {

    private double pi;
    private double[] emitProb;
    private int symbolCount;

    public HmmState(int symbolCount){
        this.symbolCount = symbolCount;
        emitProb = new double[symbolCount];
        pi = 0.0;
    }

    public void setPi(double value){
        this.pi = pi;
    }

    public void addPi(double value){
        this.pi += value;
    }

    public void dividePi(double value){
        this.pi /= value;
    }

    public double getPi(){
        return pi;
    }

    public double getEmitProb(int i){
        return emitProb[i];
    }

    public double[] getEmitProb(){
        return emitProb;
    }

    public void addEmitProb(double[] prob){
        int i;
        for (i = 0; i < symbolCount; i++){
            emitProb[i] += prob[i];            
        }
    }

    public void setEmitProb(double[] prob){
        emitProb = prob;
    }

    public void setEmitProb(int i, double value){
        emitProb[i] = value;
    }

    public void divideEmitProb(int i, double value){
        emitProb[i] /= value;
    }

    public void randomize(){
        RandomNormalizedArray a;
        a = new RandomNormalizedArray(symbolCount);
        emitProb = a.get(); 
    }
}
