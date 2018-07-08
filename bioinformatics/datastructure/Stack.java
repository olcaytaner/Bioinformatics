package bioinformatics.datastructure;

/**
 * User: Olcay
 * Date: Jun 3, 2007
 * Time: 4:59:52 PM
 */
public class Stack{
    private List stackList;

    public Stack(){
        stackList = new List();
    }

    public synchronized void push(Object item){
        stackList.insertFront(item);
    }

    public synchronized Object pop() throws EmptyListException{
        return stackList.removeFront();
    }

    public synchronized boolean isEmpty(){
        return stackList.isEmpty();
    }
}
