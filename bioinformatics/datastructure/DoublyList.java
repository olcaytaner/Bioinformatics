package bioinformatics.datastructure;

/**
 * User: Olcay
 * Date: Jun 3, 2007
 * Time: 4:43:46 PM
 */
public class DoublyList {
    private DoublyListNode firstNode;
    private DoublyListNode lastNode;

    public DoublyList(){
        firstNode = lastNode = null;
    }

    public synchronized boolean isEmpty(){
        return firstNode == null;
    }

    public synchronized void insertFront(int item){
        if (isEmpty()){
            firstNode = lastNode = new DoublyListNode(item);
        }
        else{
            firstNode = new DoublyListNode(item, firstNode);
        }
    }

    public synchronized void insertBack(int item){
        if (isEmpty()){
            firstNode = lastNode = new DoublyListNode(item);
        }
        else{
            lastNode = lastNode.nextNode = new DoublyListNode(item, null, lastNode);
        }
    }

    public synchronized int removeFront() throws EmptyListException{
        if (isEmpty()){
            throw new EmptyListException();
        }
        int item = firstNode.data;
        if (firstNode == lastNode){
            firstNode = lastNode = null;
        }
        else{
            firstNode = firstNode.nextNode;
            firstNode.prevNode = null;
        }
        return item;
    }

    public synchronized int removeBack() throws EmptyListException{
        if (isEmpty()){
            throw new EmptyListException();
        }
        int item = lastNode.data;
        if (firstNode == lastNode){
            firstNode = lastNode = null;
        }
        else{
            lastNode = lastNode.prevNode;
            lastNode.nextNode = null;
        }
        return item;
    }
}
