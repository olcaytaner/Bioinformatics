package bioinformatics.util;

/**
 * User: Olcay
 * Date: Jun 30, 2007
 * Time: 5:28:35 PM
 */
public class DnaSequence extends Sequence{

    public DnaSequence(){

    }

    public DnaSequence(int m){
        int i;
        s = "";
        for (i = 0; i < m; i++){
            s += bases.charAt((int) (Math.random() * 4));
        }
    }

    public DnaSequence(String s){
        super(s);
    }

    public DnaSequence(int [] word){
        int i;
        s = "";
        for (i = 0; i < word.length; i++){
            s += bases.charAt(word[i]);
        }
    }

    public int getCode(){
        int sum = 0, i;
        for (i = s.length() - 1; i >= 0; i--){
            sum = sum * 4 + bases.indexOf(s.charAt(i));
        }
        return sum;
    }

    private char codeOfAminoacid(String s){
        int i, j = 16, index = 0;
        for (i = 0; i < 3; i++){
            index += bases.indexOf(s.charAt(i)) * j;
            j /= 4;
        }
        return standardGeneticCode.charAt(index);
    }

    public ProteinSequence toAminoAcidSequence(int readingFrame, boolean direction){
        String st = "", codon;
        int i, j;
        if (direction){
            for (i = readingFrame - 1; i < s.length() - 2; i += 3){
                codon = "";
                for (j = 0; j < 3; j++){
                    codon += s.charAt(i + j);
                }
                st += codeOfAminoacid(codon);
            }
        }
        else{
            for (i = s.length() - readingFrame; i > 1; i -= 3){
                codon = "";
                for (j = 2; j >= 0; j--){
                    codon += s.charAt(i - j);
                }
                st += codeOfAminoacid(codon);
            }
        }
        return new ProteinSequence(st);
    }

    public ProteinSequence[] toAminoAcidSequences(int readingFrame, boolean direction){
        int i;
        ProteinSequence[] result;
        result = new ProteinSequence[6];
        for (i = 1; i <= 3; i++){
            result[i - 1] = toAminoAcidSequence(i, true);
        }
        for (i = 4; i <= 6; i++){
            result[i - 1] = toAminoAcidSequence(i - 3, false);
        }
        return result;
    }

}
