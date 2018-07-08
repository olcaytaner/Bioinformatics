package bioinformatics.datastructure;

/**
 * User: Olcay
 * Date: Jul 7, 2007
 * Time: 2:12:28 PM
 */
public class GraphNode {

    private Object data;
    private List edges;
    private boolean visited;
    private int index;

    public GraphNode(int index, Object object){
        this.index = index;
        data = object;
        edges = new List();
        visited = false;
    }

    public void addEdge(int toNode){
        edges.insertBack(new GraphEdge(index, toNode));
    }

    public void addEdge(int toNode, int weight){
        edges.insertBack(new GraphEdge(index, toNode, weight));
    }

    public void setEdgeTo(int prevTo, int newTo){
        ListNode current;
        GraphEdge edge;
        current = edges.firstNode;
        while (current != null){
            edge = GraphEdge.class.cast(current.get());
            if (edge.to() == prevTo){
                edge.setTo(newTo);
                break;
            }
            current = current.nextNode;
        }
    }

    public void setEdgeWeight(int toNode, int newWeight){
        ListNode current;
        GraphEdge edge;
        current = edges.firstNode;
        while (current != null){
            edge = GraphEdge.class.cast(current.get());
            if (edge.to() == toNode){
                edge.setWeight(newWeight);
                break;
            }
            current = current.nextNode;
        }
    }

    public void removeEdges(){
        edges = new List();
    }

    public Object get(){
        return data;
    }

    public int getIndex(){
        return index;
    }

    public List getEdges(){
        return edges;
    }

    public void visit(){
        visited = true;
    }

    public boolean isVisited(){
        return visited;
    }

    public void undoVisit(){
        visited = false;
    }
}
