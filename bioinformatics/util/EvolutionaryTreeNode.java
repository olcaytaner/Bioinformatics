package bioinformatics.util;

/**
 * User: Olcay
 * Date: Jul 14, 2007
 * Time: 7:03:41 PM
 */
public class EvolutionaryTreeNode {
    private EvolutionaryTreeNode left, right, parent;
    private int index;
    private float length;

    public EvolutionaryTreeNode(int index){
        left = right = parent = null;
        this.index = index;
        length = 0;
    }

    public EvolutionaryTreeNode mergeNode(EvolutionaryTreeNode node, int index){
        EvolutionaryTreeNode parentNode;
        parentNode = new EvolutionaryTreeNode(index);
        parentNode.left = this;
        parentNode.right = node;
        this.parent = parentNode;
        node.parent = parentNode;
        return parentNode;
    }

    public float getLength(){
        return length;
    }

    public void setLength(float length){
        this.length = length;
    }

}
