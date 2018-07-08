package bioinformatics.datastructure;

/**
 * User: root
 * Date: Jul 12, 2007
 * Time: 3:40:20 PM
 */
public class KeywordTreeNode {

    private KeywordTreeNode children[];
    private boolean isLeaf;

    public KeywordTreeNode(){
        children = new KeywordTreeNode[4];
        isLeaf = true;
    }

    public KeywordTreeNode addChild(int index){
        children[index] = new KeywordTreeNode();
        isLeaf = false;
        return children[index];
    }

    public KeywordTreeNode getChild(int index){
        return children[index];
    }

    public boolean isLeafNode(){
        return isLeaf;
    }
}
