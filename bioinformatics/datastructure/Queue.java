package bioinformatics.datastructure;

/**
 * User: Olcay
 * Date: Jun 3, 2007
 * Time: 5:07:50 PM
 */
public class Queue {
    private List queueList;

    public Queue(){
        queueList = new List();
    }

    public synchronized void enqueue(Object item){
        queueList.insertBack(item);
    }

    public synchronized Object dequeue(){
        return queueList.removeFront();
    }

    public synchronized boolean isEmpty(){
        return queueList.isEmpty();
    }

    public int elementCount(){
        return queueList.elementCount();
    }
}
