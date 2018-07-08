package bioinformatics.datastructure;

/**
 * User: Olcay
 * Date: Jun 3, 2007
 * Time: 5:15:35 PM
 */
public class BinaryTreeNode {

    BinaryTreeNode leftNode;
    int data;
    BinaryTreeNode rightNode;

    public BinaryTreeNode(int nodeData){
        data = nodeData;
        leftNode = rightNode = null;
    }

    public synchronized void insert(int value){
        if (value < data){
            if (leftNode == null){
                leftNode = new BinaryTreeNode(value);
            }
            else{
                leftNode.insert(value);
            }
        }
        else{
            if (value > data){
                if (rightNode == null){
                    rightNode = new BinaryTreeNode(value);
                }
                else{
                    rightNode.insert(value);
                }
            }
        }
    }
}
