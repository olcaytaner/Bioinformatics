package bioinformatics.problems;

import bioinformatics.util.MultipleSequence;

/**
 * Created by IntelliJ IDEA.
 * User: olcay
 * Date: Jan 15, 2008
 * Time: 8:41:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class GibbsSampler extends LocalMultipleAlignment{

    private double table[][];
    private int motifs[];

    public GibbsSampler(MultipleSequence ms){
        super(ms);
    }

    private void initialize(){
        int i;
        motifs = new int[ms.sequenceCount()];
        for (i = 0; i < ms.sequenceCount(); i++){
            motifs[i] = (int) (Math.random() * (ms.sequence(i).length() - siteLength + 1));
        }
    }

    private int iterateSequence(int index, String searchSt, int rowCount){
        int i, j, column, row, maxSiteStart = ms.sequence(index).length() + siteLength + 1;
        double a[];
        double p, q, sum, randValue;
        for (i = 0; i < rowCount; i++){
            for (j = 0; j < siteLength + 1; j++){
                table[i][j] = 0;
            }
        }
        for (i = 0; i < ms.sequenceCount(); i++){
            if (i != index){
                for (j = 0; j < ms.sequence(i).length(); j++){
                    row = searchSt.indexOf(ms.sequence(i).charAt(j));
                    column = positionInSite(j, motifs[i]);
                    table[row][column]++;
                }
            }
        }
        normalize(table, rowCount, siteLength + 1, true);
        a = new double[maxSiteStart];
        sum = 0.0;
        for (i = 0; i < maxSiteStart; i++){
            p = 1.0;
            q = 1.0;
            for (j = 0; j < ms.sequence(index).length(); j++){
                row = searchSt.indexOf(ms.sequence(index).charAt(j));
                column = positionInSite(j, i);
                if (column != 0){ //In the site
                    p *= table[row][column];
                } else { //Outside the site
                    q *= table[row][column];                    
                }
            }
            a[i] = p / q;
            sum += a[i];
        }
        for (i = 0; i < maxSiteStart; i++){
            a[i] /= sum;
        }
        randValue = Math.random();
        sum = 0.0;
        for (i = 0; i < maxSiteStart; i++){
            if (sum + a[i] > randValue){
                return i;
            }
        }
        return -1;
    }

    public void execute(int length, int iteration){
        int i, j, rowCount;
        String searchSt;
        siteLength = length;
        if (isProteinSequence){
            rowCount = 20;
            searchSt = codons;
        } else {
            rowCount = 4;
            searchSt = bases;
        }
        initialize();
        table = new double[rowCount][siteLength + 1];
        for (i = 0; i < iteration; i++){
            for (j = 0; j < ms.sequenceCount(); j++){
                motifs[j] = iterateSequence(j, searchSt, rowCount);
            }
        }
    }
}
