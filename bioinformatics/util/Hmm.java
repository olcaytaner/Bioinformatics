package bioinformatics.util;

/**
 * User: Olcay
 * Date: Nov 17, 2007
 * Time: 1:17:57 PM
 */
public class Hmm {

    private double[][] a;
    private HmmState[] states;
    private int stateCount;
    private String symbols;

    private double[] scale;
    private double[][] alpha;
    private double[][] beta;
    private double[][] gamma;
    private double[][][] xi;
    private double[] accumA;
    private double[] accumB;

    public Hmm(int stateCount, String symbols){
        int i;
        this.symbols = symbols;
        this.stateCount = stateCount;
        a = new double[stateCount][stateCount];
        states = new HmmState[stateCount];
        for (i = 0; i < stateCount; i++){
            states[i] = new HmmState(symbols.length());
        }
    }

    private Hmm(Hmm h){
        int i;
        symbols = h.symbols;
        stateCount = h.stateCount;
        a = new double[stateCount][stateCount];
        states = new HmmState[stateCount];
        for (i = 0; i < stateCount; i++){
            states[i] = new HmmState(h.symbols.length());
        }
    }
    
    private void randomize(){
        int i;
        double[] pi;
        pi = new RandomNormalizedArray(stateCount).get();
        for (i = 0; i < stateCount; i++){
            states[i].setPi(pi[i]);
            states[i].randomize();
            a[i] = new RandomNormalizedArray(stateCount).get();
        }
    }

    private double safelog(double x){
        if (x <= 0){
            return 0.0;
        }
        else{
            return Math.log(x);
        }
    }

    private double[] logOfColumn(int column){
        double[] result;
        int i;
        result = new double[stateCount];
        for (i = 0; i < stateCount; i++){
            result[i] = safelog(a[i][column]);
        }
        return result;
    }

    private double[] sumTwoArrays(double[] array1, double[] array2){
        double[] result;
        int i;
        result = new double[stateCount];
        for (i = 0; i < stateCount; i++){
            result[i] = array1[i] + array2[i];
        }
        return result;
    }

    private int maxArray(double[] array){
        double maxValue = array[0];
        int i, maxIndex = 0;
        for (i = 1; i < stateCount; i++){
            if (array[i] > maxValue){
                maxValue = array[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    private void calculateForward(Hmm h, String s){
        double obslik, sum;
        int i, t, j, k, emission, sequenceLength = s.length();
        emission = symbols.indexOf(s.charAt(0));
        for (i = 0; i < stateCount; i++){
            obslik = states[i].getEmitProb(emission);
            alpha[0][i] = obslik * h.states[i].getPi();
            scale[0] += alpha[0][i];
        }
        for (t = 1; t < sequenceLength; t++){
            emission = symbols.indexOf(s.charAt(t));
            for (j = 0; j < stateCount; j++){
                sum = 0.0;
                for (k = 0; k < stateCount; k++){
                    sum += alpha[t - 1][k] * h.a[k][j];
                }
                obslik = h.states[j].getEmitProb(emission);
                alpha[t][j] = sum * obslik;
                scale[t] += alpha[t][j];
            }
            for (j = 0; j < stateCount; j++){
                alpha[t][j] /= scale[t];
            }
        }
    }

    private void calculateBackward(Hmm h, String s){
        int i, j, t, emission, sequenceLength = s.length();
        double obslik, sum;
        for (i = 0; i < stateCount; i++){
            beta[sequenceLength - 1][i] = 1.0 / scale[sequenceLength - 1];
        }
        for (t = sequenceLength - 2; t >= 0; t--){
            emission = symbols.indexOf(s.charAt(t));
            for (i = 0; i < stateCount; i++){
                sum = 0.0;
                for (j = 0; j < stateCount; j++){
                    obslik = h.states[j].getEmitProb(emission);
                    sum += beta[t + 1][j] * h.a[i][j] * obslik;
                }
                beta[t][i] = sum / scale[t];
            }
        }
    }

    private void calculateGamma(String s){
        double denominator;
        int t, j, sequenceLength = s.length();
        for (t = 0; t < sequenceLength; t++){
            denominator = 0.0;
            for (j = 0; j < stateCount; j++){
                gamma[t][j] = alpha[t][j] * beta[t][j];
                denominator += gamma[t][j];
            }
            for (j = 0; j < stateCount; j++){
                gamma[t][j] /= denominator;
            }
        }
    }

    private void calculateXi(Hmm h, String s){
        int i, j, t, emission, sequenceLength = s.length();
        double sum, obslik;
        for (t = 0; t < sequenceLength - 1; t++){
            emission = symbols.indexOf(s.charAt(t));
            sum = 0.0;
            for (i = 0; i < stateCount; i++){
                for (j = 0; j < stateCount; j++){
                    obslik = h.states[j].getEmitProb(emission);
                    xi[t][i][j] = alpha[t][i] * beta[t + 1][j] * h.a[i][j] * obslik;
                    sum += xi[t][i][j];
                }
            }
            for (i = 0; i < stateCount; i++){
                for (j = 0; j < stateCount; j++){
                    xi[t][i][j] /= sum;
                }
            }
        }
    }

    private void expectationMaximization(Hmm h, String s){
        int i, j, k, t, emission, sequenceLength = s.length();
        double numeratorA, denominatorA, numeratorB, denominatorB;
        scale = new double[sequenceLength];
        alpha = new double[sequenceLength][stateCount];
        beta = new double[sequenceLength][stateCount];
        gamma = new double[sequenceLength][stateCount];
        xi = new double[sequenceLength][stateCount][stateCount];
        calculateForward(h, s);
        calculateBackward(h, s);
        calculateGamma(s);
        calculateXi(h, s);
        for (i = 0; i < stateCount; i++){
            h.states[i].setPi(gamma[0][i]);
        }
        for (i = 0; i < stateCount; i++){
            denominatorA = 0.0;
            for (t = 0; t < sequenceLength - 1; t++){
                denominatorA += gamma[t][i];
            }
            accumA[i] += denominatorA;
            for (j = 0; j < stateCount; j++){
                numeratorA = 0.0;
                for (t = 0; t < sequenceLength - 1; t++){
                    numeratorA += xi[t][i][j];
                }
                h.a[i][j] = numeratorA;
            }
            denominatorB = denominatorA + gamma[sequenceLength - 1][i];
            accumB[i] += denominatorB;
            for (k = 0; k < symbols.length(); k++){
                numeratorB = 0.0;
                for (t = 0; t < sequenceLength; t++){
                    emission = symbols.indexOf(s.charAt(t));
                    if (emission == k){
                        numeratorB += gamma[t][i];
                    }
                }
                h.states[i].setEmitProb(k, numeratorB);
            }
        }
    }

    private void addHmm(Hmm dest, Hmm src){
        int i, j;
        for (i = 0; i < stateCount; i++){
            for (j = 0; j < stateCount; j++){
                dest.a[i][j] += src.a[i][j];
            }
            dest.states[i].addPi(src.states[i].getPi());
            dest.states[i].addEmitProb(src.states[i].getEmitProb());
        }
    }

    private void copyHmm(Hmm src){
        int i;
        this.a = src.a.clone();
        for (i = 0; i < stateCount; i++){
            this.states[i].setPi(src.states[i].getPi());
            this.states[i].setEmitProb(src.states[i].getEmitProb());
        }
    }

    public void baumWelch(String[] data, int iteration) throws CloneNotSupportedException {
        int i, j, t;
        Hmm hmmiter, hmmtemp;
        accumA = new double[stateCount];
        accumB = new double[stateCount];
        randomize();
        for (t = 0; t < iteration; t++){
            hmmtemp = new Hmm(this);
            for (i = 0; i < stateCount; i++){
                accumA[i] = 0.0;
                accumB[i] = 0.0;
            }
            for (i = 0; i < data.length; i++){
                hmmiter = (Hmm) this.clone();
                expectationMaximization(hmmiter, data[i]);
                addHmm(hmmtemp, hmmiter);
            }
            for (i = 0; i < stateCount; i++){
                hmmtemp.states[i].dividePi(data.length);
                for (j = 0; j < stateCount; j++){
                    hmmtemp.a[i][j] /= accumA[i];
                }
                for (j = 0; j < symbols.length(); j++){
                    hmmtemp.states[i].divideEmitProb(j, accumB[i]);
                }
            }
            copyHmm(hmmtemp);
        }
    }

    public int[] viterbi(String s){
        int i, j, t, emission, maxIndex;
        int[] qs;
        double[][] gamma;
        double[] tempArray;
        double obslik;
        int[][] phi;
        int sequenceLength = s.length();
        gamma = new double[sequenceLength][stateCount];
        phi = new int[sequenceLength][stateCount];
        qs = new int[sequenceLength];
        /*Initialize*/
        emission = symbols.indexOf(s.charAt(0));
        for (i = 0; i < stateCount; i++){
            obslik = states[i].getEmitProb(emission);
            gamma[0][i] = safelog(states[i].getPi()) + safelog(obslik);
            phi[0][i] = 0;
        }
        /*Iterate Dynamic Programming*/
        for (t = 1; t < sequenceLength; t++){
            emission = symbols.indexOf(s.charAt(t));
            for (j = 0; j < stateCount; j++){
                tempArray = sumTwoArrays(logOfColumn(j), gamma[j - 1]);
                maxIndex = maxArray(tempArray);
                obslik = states[j].getEmitProb(emission);
                gamma[t][j] = tempArray[maxIndex] + safelog(obslik);
                phi[t][j] = maxIndex + 1;
            }
        }
        /*Backtrack pointers*/
        qs[sequenceLength - 1] = maxArray(gamma[sequenceLength - 1]) + 1;
        for (i = sequenceLength - 2; i >= 0; i--){
            qs[i] = phi[i + 1][qs[i + 1] - 1];
        }
        return qs;
    }
}
