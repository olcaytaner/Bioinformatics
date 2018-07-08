package bioinformatics.datastructure;

/**
 * User: Olcay
 * Date: Jul 13, 2007
 * Time: 3:31:44 PM
 */
public class SuffixTree {

    SuffixTreeNode root;
    final String bases = "ATGC";

    public SuffixTree(){
        root = new SuffixTreeNode();
    }

    public void addSuffix(String s, int label){
        root.addSuffix(s, label);
    }

    public SuffixTreeNode thread(String s){
        return root.thread(s);
    }
}
