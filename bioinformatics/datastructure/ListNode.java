package bioinformatics.datastructure;

/**
 * User: Olcay
 * Date: Jun 3, 2007
 * Time: 3:58:26 PM
 */
public class ListNode {

    Object data;
    ListNode nextNode;

    public ListNode(Object object){
        this(object, null);
    }

    public ListNode(Object object, ListNode node){
        data = object;
        nextNode = node;
    }

    public Object get(){
        return data;
    }

    public ListNode next(){
        return nextNode;
    }
}
