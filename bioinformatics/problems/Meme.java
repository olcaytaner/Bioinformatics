package bioinformatics.problems;

import bioinformatics.util.MultipleSequence;

/**
 * Created by IntelliJ IDEA.
 * User: olcay
 * Date: Jan 6, 2008
 * Time: 12:57:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class Meme extends LocalMultipleAlignment{

    private double table[][];
    private double siteProbabilities[][];

    public Meme(MultipleSequence ms){
        super(ms);
    }

    private void initializeEm(String searchSt, int rowCount, int siteCount){
        int i, j, randomSite, row, column;
        table = new double[rowCount][siteLength + 1];
        for (i = 0; i < ms.sequenceCount(); i++){
            randomSite = (int) (Math.random() * siteCount);
            for (j = 0; j < ms.alignmentLength(); j++){
                row = searchSt.indexOf(ms.sequence(i).charAt(j));
                column = positionInSite(j, randomSite);
                table[row][column]++;
            }
        }
        normalize(table, rowCount, siteLength + 1, true);
    }

    private void expectationStep(String searchSt, int siteCount){
        int i, j, k, row, column;
        double probability, sum;
        for (i = 0; i < ms.sequenceCount(); i++){
            for (j = 0; j < siteCount; j++){
                probability = 1.0;
                for (k = 0; k < ms.alignmentLength(); k++){
                    row = searchSt.indexOf(ms.sequence(i).charAt(k));
                    column = positionInSite(k, j);
                    probability *= table[row][column];
                }
                siteProbabilities[i][j] = probability;
            }
        }
        //Normalization Step
        for (i = 0; i < ms.sequenceCount(); i++){
            sum = 0.0;
            for (j = 0; j < siteCount; j++){
                sum += siteProbabilities[i][j];
            }
            for (j = 0; j < siteCount; j++){
                siteProbabilities[i][j] /= sum;
            }
        }
    }

    private void maximizationStep(String searchSt, int rowCount, int siteCount){
        int i, j, k, row, column;
        for (i = 0; i < rowCount; i++){
            for (j = 0; j < siteLength + 1; j++){
                table[i][j] = 0.0;
            }
        }
        for (i = 0; i < ms.sequenceCount(); i++){
            for (j = 0; j < siteCount; j++){
                for (k = 0; k < ms.alignmentLength(); k++){
                    row = searchSt.indexOf(ms.sequence(i).charAt(k));
                    column = positionInSite(k, j);
                    table[row][column] += siteProbabilities[i][j];
                }
            }
        }
        normalize(table, rowCount, siteLength + 1, false);
    }

    public void em(int length, int iteration){
        int i, rowCount, siteCount = ms.alignmentLength() - length + 1;
        String searchSt;
        siteLength = length;
        if (isProteinSequence){
            rowCount = 20;
            searchSt = codons;
        } else {
            rowCount = 4;
            searchSt = bases;
        }
        siteProbabilities = new double[ms.sequenceCount()][siteCount];
        initializeEm(searchSt, rowCount, siteCount);
        for (i = 0; i < iteration; i++){
            expectationStep(searchSt, siteCount);
            maximizationStep(searchSt, rowCount, siteCount);
        }
    }
}
