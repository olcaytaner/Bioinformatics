package bioinformatics.util;

/**
 * User: root
 * Date: Nov 8, 2007
 * Time: 11:58:27 AM
 */
public class ProteinSequence extends Sequence{

    public ProteinSequence(){

    }

    public ProteinSequence(int m){
        int i;
        s = "";
        for (i = 0; i < m; i++){
            s += codons.charAt((int) (Math.random() * 20));
        }
    }

    public ProteinSequence(String s){
        super(s);
    }

    public ProteinSequence(int [] word){
        int i;
        s = "";
        for (i = 0; i < word.length; i++){
            s += codons.charAt(word[i]);
        }
    }

    public int getCode(){
        int sum = 0, i;
        for (i = s.length() - 1; i >= 0; i--){
            sum = sum * 20 + codons.indexOf(s.charAt(i));
        }
        return sum;
    }

    private String codeOfNucleotides(char ch){
        String st = "";
        int index = standardGeneticCode.indexOf(ch);
        st += bases.charAt(index / 16);
        st += bases.charAt((index / 4) % 4);
        st += bases.charAt(index % 4);
        return st;
    }

    public DnaSequence toNucleicAcidSequence(){
        String st = "";
        int i;
        for (i = 0; i < s.length(); i++){
            st += codeOfNucleotides(s.charAt(i));
        }
        return new DnaSequence(st);
    }

}
