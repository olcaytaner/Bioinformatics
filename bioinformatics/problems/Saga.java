package bioinformatics.problems;

import bioinformatics.util.Msa;
import bioinformatics.util.Sequence;
import bioinformatics.util.ScoringMatrix;
import bioinformatics.util.MultipleSequence;
import bioinformatics.datastructure.List;
import bioinformatics.datastructure.ListNode;

/**
 * Created by IntelliJ IDEA.
 * User: olcay
 * Date: Dec 31, 2007
 * Time: 11:54:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class Saga {

    private int populationSize, iterationCount;
    private boolean isProteinSequence;
    private int gapPenalty, matchScore, mismatchPenalty;
    private ScoringMatrix mat;
    private MultipleSequence ms;
    private int maxGapLength, maxMovement;

    public Saga(MultipleSequence ms, int populationSize, int iterationCount, int maxGapLength, int maxMovement, int gapPenalty, int matchScore, int mismatchPenalty){
        this.populationSize = populationSize;
        this.iterationCount = iterationCount;
        this.isProteinSequence = false;
        this.gapPenalty = gapPenalty;
        this.matchScore = matchScore;
        this.mismatchPenalty = mismatchPenalty;
        this.ms = ms;
        this.maxGapLength = maxGapLength;
        this.maxMovement = maxMovement;
    }

    public Saga(MultipleSequence ms, int populationSize, int iterationCount, int maxGapLength, int maxMovement, int gapPenalty, ScoringMatrix mat){
        this.populationSize = populationSize;
        this.iterationCount = iterationCount;
        this.isProteinSequence = true;
        this.gapPenalty = gapPenalty;
        this.mat = mat;
        this.ms = ms;
        this.maxGapLength = maxGapLength;
        this.maxMovement = maxMovement;
    }

    private Msa[] recombinationStaggered(Msa parent1, Msa parent2){
        //Parent1 A--C-G--C--G-T
        //Parent2 -ACGC-GT
        //Child1  A--C-G--C-GT
        //Child2  -ACGC--G-T
        Msa[] children;
        MultipleSequence seq1, seq2;
        int i, pos, pos2, max1 = 0, max2 = 0;
        String s1, s2;
        children = new Msa[2];
        seq1 = new MultipleSequence(parent1.sequenceCount());
        seq2 = new MultipleSequence(parent2.sequenceCount());
        pos = 1 + (int) (Math.random() * (parent1.alignmentLength() - 1));
        for (i = 0; i < parent1.sequenceCount(); i++){
            s1 = parent1.sequence(i).get().substring(0, pos);
            //s1 = A--C-G--C
            pos2 = parent1.sequence(i).samePosition(pos, parent2.sequence(i));
            s2 = parent2.sequence(i).get().substring(0, pos2);
            // s2 = -ACGC
            s1 += parent2.sequence(i).get().substring(pos2);
            // s1 += -GT
            s2 += parent1.sequence(i).get().substring(pos);
            // s2 += --G-T
            if (s1.length() > max1){
                max1 = s1.length();
            }
            if (s2.length() > max2){
                max2 = s2.length();
            }
            seq1.setSequence(i, new Sequence(s1));
            seq2.setSequence(i, new Sequence(s2));
        }
        for (i = 0; i < parent1.sequenceCount(); i++){
            seq1.sequence(i).extendItself(0, max1 - seq1.sequence(i).length());
            seq2.sequence(i).extendItself(0, max2 - seq2.sequence(i).length());
        }
        children[0] = new Msa(seq1.get(), false);
        children[1] = new Msa(seq2.get(), false);
        return children;
    }

    private Msa[] recombinationHomologyDriven(Msa parent1, Msa parent2){
        Msa[] children;
        MultipleSequence seq1, seq2;
        List same1 = parent1.sameCode(), same2 = parent2.sameCode();
        ListNode item11, item12, item21, item22;
        int i, index11, index12, index21, index22;
        boolean same;
        String s1, s2;
        children = new Msa[2];
        seq1 = new MultipleSequence(parent1.sequenceCount());
        seq2 = new MultipleSequence(parent2.sequenceCount());
        for (item11 = same1.firstNode; item11 != null; item11 = item11.next()){
            for (item12 = item11.next(); item12 != null; item12 = item12.next()){
                for (item21 = same2.firstNode; item21 != null; item21 = item21.next()){
                    for (item22 = item21.next(); item22 != null; item22 = item22.next()){
                        index11 = Integer.class.cast(item11.get());
                        index12 = Integer.class.cast(item12.get());
                        index21 = Integer.class.cast(item21.get());
                        index22 = Integer.class.cast(item22.get());
                        if (parent1.sequence(0).charAt(index11) == parent2.sequence(0).charAt(index21) &&
                            parent1.sequence(0).charAt(index12) == parent2.sequence(0).charAt(index22)){
                            same = true;
                            for (i = 0; i < parent1.sequenceCount(); i++){
                                if (parent1.sequence(i).codeCount(index11, index12) != parent2.sequence(i).codeCount(index21, index22)){
                                    same = false;
                                    break;
                                }
                            }
                            if (same){
                                for (i = 0; i < parent1.sequenceCount(); i++){
                                    s1 = parent1.sequence(i).get().substring(0, index11);
                                    s2 = parent2.sequence(i).get().substring(0, index21);
                                    s1 += parent2.sequence(i).get().substring(index21, index22);
                                    s2 += parent1.sequence(i).get().substring(index11, index12);
                                    s1 += parent1.sequence(i).get().substring(index12);
                                    s2 += parent2.sequence(i).get().substring(index22);
                                    seq1.setSequence(i, new Sequence(s1));
                                    seq2.setSequence(i, new Sequence(s2));
                                }
                                children[0] = new Msa(seq1.get(), false);
                                children[1] = new Msa(seq2.get(), false);
                                return children;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private void sortPopulation(Msa[] population, int size){
        int i, j;
        Msa tmp;
        for (i = 0; i < size; i++){
            for (j = i + 1; j < size; j++){
                if (population[i].getScore() < population[j].getScore()){
                    tmp = population[i];
                    population[i] = population[j];
                    population[j] = tmp;
                }
            }
        }
    }

    private void mutatePopulation(Msa[] population){
        int i, operator;
        for (i = 0; i < populationSize; i++){
            operator = (int)(Math.random() * 2);
            if (operator == 0){
                population[i + populationSize] = population[i].mutationInsertGap(maxGapLength);
            } else {
                population[i + populationSize] = population[i].mutationMoveBlockHorizontally(maxMovement);
            }
            if (isProteinSequence){
                population[i + populationSize].calculateScore(gapPenalty, mat);
            } else {
                population[i + populationSize].calculateScore(gapPenalty, matchScore, mismatchPenalty);
            }
        }
    }

    private void recombinePopulation(Msa[] population){
        int i;
        Msa[] children;
        for (i = 0; i < populationSize / 2; i++){
            children = recombinationHomologyDriven(population[i], population[populationSize - i - 1]);
            if (children == null){
                children = recombinationStaggered(population[i], population[populationSize - i - 1]);
            }
            population[populationSize + i] = children[0];
            population[2 * populationSize - 1 - i] = children[1];
        }
        for (i = populationSize; i < 2 * populationSize; i++){
            if (isProteinSequence){
                population[i].calculateScore(gapPenalty, mat);
            } else {
                population[i].calculateScore(gapPenalty, matchScore, mismatchPenalty);
            }
        }
    }

    public Msa evolution(){
        int i;
        Msa[] population;
        population = new Msa[2 * populationSize];
        for (i = 0; i < populationSize; i++){
            population[i] = new Msa(ms.get(), true);
            if (isProteinSequence){
                population[i].calculateScore(gapPenalty, mat);
            } else {
                population[i].calculateScore(gapPenalty, matchScore, mismatchPenalty);
            }
        }
        sortPopulation(population, populationSize);
        for (i = 0; i < iterationCount; i++){
            mutatePopulation(population);
            sortPopulation(population, 2 * populationSize);
            recombinePopulation(population);
            sortPopulation(population, 2 * populationSize);
        }
        return population[0];
    }

}
