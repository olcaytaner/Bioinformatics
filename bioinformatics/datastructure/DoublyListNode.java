package bioinformatics.datastructure;

/**
 * User: Olcay
 * Date: Jun 3, 2007
 * Time: 4:30:27 PM
 */
public class DoublyListNode {
    int data;
    DoublyListNode nextNode;
    DoublyListNode prevNode;

    DoublyListNode(int object){
        this(object, null, null);
    }

    DoublyListNode(int object, DoublyListNode nnode){
        this(object, nnode, null);
    }

    DoublyListNode(int object, DoublyListNode nnode, DoublyListNode pnode){
        data = object;
        nextNode = nnode;
        prevNode = pnode;
    }

    Object get(){
        return data;
    }

    DoublyListNode next(){
        return nextNode;
    }

    DoublyListNode previous(){
        return prevNode;
    }
}
