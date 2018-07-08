package bioinformatics.util;

/**
 * Created by IntelliJ IDEA.
 * User: olcay
 * Date: Jan 6, 2008
 * Time: 12:15:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class MultipleSequence {

    protected Sequence[] alignment;

    public MultipleSequence(){

    }

    public MultipleSequence(int count){
        alignment = new Sequence[count];
    }

    public MultipleSequence(Sequence[] sequences){
        alignment = sequences;
    }

    public boolean isProteinSequence(){
        int i;
        for (i = 0; i < alignment.length; i++){
            if (alignment[i].isProteinSequence()){
                return true;
            }
        }
        return false;
    }

    public Sequence[] get(){
        return alignment;
    }

    public int sequenceCount(){
        return alignment.length;
    }

    public Sequence sequence(int index){
        return alignment[index];
    }

    public void setSequence(int index, Sequence s){
        alignment[index] = s;
    }

    public int alignmentLength(){
        return alignment[0].length();
    }

}
