package bioinformatics.datastructure;

/**
 * User: Olcay
 * Date: Jul 9, 2007
 * Time: 10:31:43 AM
 */
public class GraphEdge {

    private int fromNode, toNode, weight;

    public GraphEdge(int fromNode, int toNode){
        this.fromNode = fromNode;
        this.toNode = toNode;
    }

    public GraphEdge(int fromNode, int toNode, int weight){
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.weight = weight;
    }

    public int from(){
        return fromNode;
    }

    public int to(){
        return toNode;
    }

    public void setTo(int toNode){
        this.toNode = toNode;
    }

    public int weight(){
        return weight;
    }

    public void setWeight(int weight){
        this.weight = weight;
    }

}
