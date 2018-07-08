package bioinformatics;

import bioinformatics.util.DnaSequence;
import bioinformatics.util.MultipleSequence;
import bioinformatics.problems.Saga;

public class Bioinformatics {

    public static void main(String args[]){
        MultipleSequence s;
        Saga sa;
        s = new MultipleSequence(4);
        s.setSequence(0, new DnaSequence("AATCTGGCTGCTCGC"));
        s.setSequence(1, new DnaSequence("ATGCTATCGACTGGA"));
        s.setSequence(2, new DnaSequence("AGCATTGCTCATCCT"));
        s.setSequence(3, new DnaSequence("ACATGCTGATATCAA"));
        sa = new Saga(s, 100, 20, 3, 3, -1, 4, -1);
        sa.evolution();
    }

}
