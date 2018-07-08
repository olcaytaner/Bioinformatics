package bioinformatics.datastructure;

/**
 * User: root
 * Date: Jul 12, 2007
 * Time: 3:46:32 PM
 */
public class KeywordTree {

    KeywordTreeNode root;
    final String bases = "ATGC";

    public KeywordTree(){
        root = new KeywordTreeNode();
    }

    public void addPattern(String s){
        int i, index;
        KeywordTreeNode current = root, child;
        for (i = 0; i < s.length(); i++){
            index = bases.indexOf(s.charAt(i));
            child = current.getChild(index);
            if (child != null){
                current = child;
            }
            else{
                current = current.addChild(index);
            }
        }
    }

    public boolean checkPattern(String text){
        int i, index;
        KeywordTreeNode current = root, child;
        for (i = 0; i < text.length(); i++){
            index = bases.indexOf(text.charAt(i));
            child = current.getChild(index);
            if (child == null){
                return false;
            }
            else{
                if (child.isLeafNode()){
                    return true;
                }
                else{
                    current = current.getChild(index);
                }
            }
        }
        return false;
    }
}
