package bioinformatics.datastructure;

/**
 * User: Olcay
 * Date: Jul 7, 2007
 * Time: 2:19:23 PM
 */
public class Graph {

    private GraphNode[] vertices;
    private int vertexCount;

    public Graph(int vertexCount){
        vertices = new GraphNode[vertexCount];
        this.vertexCount = vertexCount;
    }

    public void addNode(int index, Object object){
        vertices[index] = new GraphNode(index, object);
    }

    public GraphNode getNode(int index){
        return vertices[index];
    }

    public GraphNode getObjectNode(Object object){
        int i;
        for (i = 0; i < vertexCount; i++){
            if (vertices[i].get() == object){
                return vertices[i];
            }
        }
        return null;
    }

    public void addEdge(int from, int to){
        vertices[from].addEdge(to);
    }

    public void addUndirectedEdge(int from, int to, int weight){
        vertices[from].addEdge(to, weight);
        vertices[to].addEdge(from, weight);
    }

    public void addEdge(int from, int to, int weight){
        vertices[from].addEdge(to, weight);
    }

    public void removeEdgesofNodes(List l){
        ListNode current;
        current = l.firstNode;
        while (current != null){
            removeEdgesofNode(Integer.class.cast(current.get()));
            current = current.next();
        }
    }

    private void removeEdgesofNode(int index){
        int i;
        List edges;
        ListNode current, prev = null;
        GraphEdge edge;
        vertices[index].removeEdges();
        for (i = 0; i < vertexCount; i++){
            edges = vertices[i].getEdges();
            current = edges.firstNode;
            while (current != null){
                edge = GraphEdge.class.cast(current.get());
                if (edge.to() == index){
                    if (prev != null){
                        prev.nextNode = current.nextNode;
                    }
                    else{
                        edges.firstNode = edges.firstNode.nextNode;
                    }
                    if (current == edges.lastNode){
                        edges.lastNode = prev;
                    }
                    break;
                }
                prev = current;
                current = current.next();
            }
        }
    }

    private boolean hamiltonianVisit(int[] path, int currentVisited, int visitedCount){
        List edges;
        ListNode current;
        GraphNode currentNode;
        int toNode;
        if (visitedCount == vertexCount){
            return true;
        }
        edges = vertices[currentVisited].getEdges();
        current = edges.firstNode;
        while (current != null){
            toNode = GraphEdge.class.cast(current.get()).to();
            currentNode = getNode(toNode);
            if (!currentNode.isVisited()){
                currentNode.visit();
                path[visitedCount] = toNode;
                if (hamiltonianVisit(path, toNode, visitedCount + 1)){
                    return true;
                }
                currentNode.undoVisit();
            }
            current = current.nextNode;
        }
        return false;
    }

    private boolean constructPathRecursive(List path, int fromNode, int toNode){
        ListNode current;
        GraphEdge edge;
        current = vertices[fromNode].getEdges().firstNode;
        while (current != null){
            edge = GraphEdge.class.cast(current.get());
            if (!getNode(edge.to()).isVisited()){
                path.insertBack(edge);
                getNode(edge.to()).visit();
                if (edge.to() == toNode){
                    return true;
                }
                if (constructPathRecursive(path, edge.to(), toNode)){
                    return true;
                }
                path.removeBack();
                getNode(edge.to()).undoVisit();
            }
            current = current.nextNode;
        }
        return false;
    }

    public List constructPath(int fromNode, int toNode){
        int i;
        List path;
        /*Initialize all nodes to unvisited*/
        for (i = 0; i < vertexCount; i++){
            if (getNode(i) != null){
                getNode(i).undoVisit();                
            }
        }
        path = new List();
        getNode(fromNode).visit();
        constructPathRecursive(path, fromNode, toNode);
        return path;
    }

    public int[] hamiltonianPath(){
        int i;
        int[] path;
        path = new int[vertexCount];
        /*Initialize all nodes to unvisited*/
        for (i = 0; i < vertexCount; i++){
            getNode(i).undoVisit();
        }
        /*For each vertex search an hamiltonian path starting from that vertex*/
        for (i = 0; i < vertexCount; i++){
            getNode(i).visit();
            path[0] = i;
            if (hamiltonianVisit(path, i, 1)){
                return path;
            }
            getNode(i).undoVisit();
        }
        return null;
    }

    public List getEdges(int index){
        if (vertices[index] != null){
            return vertices[index].getEdges();
        }
        return null;
    }

    public Queue[] getEdges(){
        List edges;
        Queue[] edgeQueues;
        ListNode current;
        int i;
        edgeQueues = new Queue[vertexCount];
        for (i = 0; i < vertexCount; i++){
            edgeQueues[i] = new Queue();
            edges = getNode(i).getEdges();
            current = edges.firstNode;
            while (current != null){
                edgeQueues[i].enqueue(GraphEdge.class.cast(current.get()).to());
                current = current.nextNode;
            }
        }
        return edgeQueues;
    }

    private List getNextCycle(int startNode, Queue[] edges){
        List cycle;
        int currentNode, nextNode;
        cycle = new List();
        cycle.insertBack(startNode);
        currentNode = startNode;
        while (!edges[currentNode].isEmpty()){
            nextNode = Integer.class.cast(edges[currentNode].dequeue());
            cycle.insertBack(nextNode);
            if (nextNode == startNode){
                break;
            }
            currentNode = nextNode;
        }
        return cycle;
    }

    private ListNode getNextStart(List path, Queue[] edges){
        ListNode current;
        current = path.firstNode;
        while (current != null){
            if (!edges[Integer.class.cast(current.get())].isEmpty()){
                break;
            }
            current = current.nextNode;
        }
        return current;
    }

    public int maximalOutDegreeVertex(){
        int i, v, outDegree, maxOutDegree;
        v = 0;
        maxOutDegree = vertices[0].getEdges().elementCount();
        for (i = 1; i < vertexCount; i++){
            outDegree = vertices[i].getEdges().elementCount();
            if (outDegree > maxOutDegree){
                v = i;
                maxOutDegree = outDegree;
            }
        }
        return v;
    }

    private int[] outDegreeArray(){
        int[] out;
        int i;
        out = new int[vertexCount];
        for (i = 0; i < vertexCount; i++){
            out[i] = vertices[i].getEdges().elementCount();
        }
        return out;
    }

    private int[] inDegreeArray(){
        int[] in;
        int i;
        ListNode current;
        in = new int[vertexCount];
        for (i = 0; i < vertexCount; i++){
            current = vertices[i].getEdges().firstNode;
            while (current != null){
                in[GraphEdge.class.cast(current.get()).to()]++;
                current = current.nextNode;
            }
        }
        return in;
    }

    public List eulerianCycle(){
        List eulerian, nextCycle;
        Queue[] edgeList;
        ListNode nextStartNode;
        int[] indegrees, outdegrees;
        int i, startNode = -1;
        /*Check if there is an eulerian cycle, if not return null*/
        indegrees = inDegreeArray();
        outdegrees = outDegreeArray();
        for (i = 0; i < vertexCount; i++){
            if (indegrees[i] != outdegrees[i]){
                return null;
            }
        }
        /*Start eulerian cycle with the first cycle*/
        edgeList = getEdges();
        for (i = 0; i < vertexCount; i++){
            if (!edgeList[i].isEmpty()){
                startNode = i;
                break;
            }
        }
        eulerian = getNextCycle(startNode, edgeList);
        while ((nextStartNode = getNextStart(eulerian, edgeList)) != null){
            startNode = Integer.class.cast(nextStartNode.get());
            nextCycle = getNextCycle(startNode, edgeList);
            nextCycle.lastNode.nextNode = nextStartNode.nextNode;
            nextStartNode.nextNode = nextCycle.firstNode.nextNode;
        }
        return eulerian;
    }

    public List eulerianPath(){
        int[] indegrees, outdegrees;
        int i, s = -1, t = -1;
        List eulerian;
        ListNode current;
        boolean addedge;
        /*Check if there is an eulerian path, if not return null*/
        indegrees = inDegreeArray();
        outdegrees = outDegreeArray();
        for (i = 0; i < vertexCount; i++){
            if (indegrees[i] != outdegrees[i]){
                switch (indegrees[i] - outdegrees[i]){
                    case +1:if (s == -1){
                                s = i;
                            }
                            else{
                                return null;
                            }
                            break;
                    case -1:if (t == -1){
                                t = i;
                            }
                            else{
                                return null;
                            }
                            break;
                    default:return null;
                }
            }
        }
        addedge = s != -1 && t!= -1;
        /*Add an edge from source to target*/
        if (addedge){
            vertices[s].addEdge(t);
        }
        else{
            s = 0;
            t = 1;
        }
        eulerian = eulerianCycle();
        current = eulerian.firstNode;
        while (current != null){
            if (Integer.class.cast(current.get()) == s && Integer.class.cast(current.nextNode.get()) == t){
                eulerian.lastNode.nextNode = eulerian.firstNode.nextNode;
                eulerian.firstNode = current.nextNode;
                eulerian.lastNode = current;
                current.nextNode = null;
                break;
            }
            current = current.nextNode;
        }
        /*Remove the edge from source to target*/
        if (addedge){
            vertices[s].getEdges().removeBack();
        }
        return eulerian;
    }

    public int getVertexCount(){
        return vertices.length;
    }

}
