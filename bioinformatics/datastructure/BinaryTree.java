package bioinformatics.datastructure;

/**
 * User: Olcay
 * Date: Jun 3, 2007
 * Time: 5:19:39 PM
 */
public class BinaryTree {

    private BinaryTreeNode root;

    public BinaryTree(){
        root = null;
    }

    public synchronized void insert(int value){
        if (root == null){
            root = new BinaryTreeNode(value);
        }
        else{
            root.insert(value);
        }
    }
}
