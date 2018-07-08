package bioinformatics.problems;

import bioinformatics.util.MultipleSequence;

/**
 * Created by IntelliJ IDEA.
 * User: olcay
 * Date: Jan 15, 2008
 * Time: 8:35:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class LocalMultipleAlignment {

    protected MultipleSequence ms;
    protected boolean isProteinSequence;
    protected int siteLength;    
    final String bases = "TCAG";
    final String codons = "ACDEFGHIKLMNPQRSTVWYZ";

    public LocalMultipleAlignment(){

    }

    public LocalMultipleAlignment(MultipleSequence ms){
        this.ms = ms;
        this.isProteinSequence = ms.isProteinSequence();        
    }

    private boolean inSite(int pos, int sitePos){
        return pos >= sitePos && pos < sitePos + siteLength;
    }

    protected int positionInSite(int pos, int sitePos){
        if (inSite(pos, sitePos)){
            return pos - sitePos + 1;
        } else {
            return 0;
        }
    }

    protected void normalize(double[][] table, int rowCount, int colCount, boolean pseudocount){
        int i, j;
        double sum;
        for (j = 0; j < colCount; j++){
            sum = 0;
            for (i = 0; i < rowCount; i++){
                sum += table[i][j];
            }
            for (i = 0; i < rowCount; i++){
                if (pseudocount){
                    //Add pseudocounts not to get 0 probabilities
                    table[i][j] = (table[i][j] + 1) / (sum + rowCount);
                } else {
                    table[i][j] /= sum;
                }
            }
        }
    }

}
