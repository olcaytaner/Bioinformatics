package bioinformatics.datastructure;

/**
 * User: Olcay
 * Date: Jun 3, 2007
 * Time: 4:03:30 PM
 */
public class List {
    public ListNode firstNode;
    public ListNode lastNode;

    public List(){
        firstNode = lastNode = null;
    }

    public List(int[] array){
        int i;
        firstNode = lastNode = null;
        for (i = 0; i < array.length; i++){
            insertBack(array[i]);
        }
    }

    public List(List l){
        ListNode current;
        firstNode = lastNode = null;
        current = l.firstNode;
        while (current != null){
            insertBack(Integer.class.cast(current.get()));
            current = current.nextNode;
        }
    }

    public synchronized boolean isEmpty(){
        return firstNode == null;
    }

    public synchronized void insertFront(Object item){
        if (isEmpty()){
            firstNode = lastNode = new ListNode(item);
        }
        else{
            firstNode = new ListNode(item, firstNode);
        }
    }

    public synchronized void insertBack(Object item){
        if (isEmpty()){
            firstNode = lastNode = new ListNode(item);
        }
        else{
            lastNode = lastNode.nextNode = new ListNode(item);
        }
    }

    public synchronized void insertBack(ListNode Lnode){
        ListNode current = Lnode;
        while (current != null){
            insertBack(current.data);
            current = current.nextNode;
        }
    }

    public synchronized void insert(int item){
        if (isEmpty()){
            firstNode = lastNode = new ListNode(item);
        }
        else{
            //Insert front of the list
            if (item < Integer.class.cast(firstNode.data)){
                firstNode = new ListNode(item, firstNode);
            }
            else{
                //Insert back of the list
                if (item > Integer.class.cast(lastNode.data)){
                    lastNode = lastNode.nextNode = new ListNode(item);
                }
                else{
                    //Insert middle of the list
                    ListNode current = firstNode;
                    while (item > Integer.class.cast(current.nextNode.data)){
                        current = current.nextNode;
                    }
                    current.nextNode = new ListNode(item, current.nextNode);
                }
            }
        }
    }

    public synchronized void insert(List L){
        if (isEmpty()){
            insertBack(L.firstNode);
        }
        else{
            ListNode before = null;
            ListNode current = firstNode;
            ListNode currentL = L.firstNode;
            while (currentL != null){
                if (Integer.class.cast(currentL.data) <= Integer.class.cast(current.data)){
                    if (before == null){
                        before = firstNode = new ListNode(currentL.data, current);
                    }
                    else{
                        before = before.nextNode = new ListNode(currentL.data, current);
                    }
                    currentL = currentL.nextNode;
                }
                else{
                    before = current;
                    current = current.nextNode;
                    if (current == null){
                        insertBack(currentL);
                        break;
                    }
                }
            }
        }
    }

    public synchronized void merge(List L){
        if (L.isEmpty()){
            return;
        }
        if (firstNode == null){
            firstNode = L.firstNode;
            lastNode = L.lastNode;
        }
        else{
            lastNode.nextNode = L.firstNode;
            lastNode = L.lastNode;
        }
    }

    public synchronized Object removeFront() throws EmptyListException{
        if (isEmpty()){
            throw new EmptyListException();
        }
        Object item = firstNode.data;
        if (firstNode == lastNode){
            firstNode = lastNode = null;
        }
        else{
            firstNode = firstNode.nextNode;
        }
        return item;
    }

    public synchronized void removeAfter(ListNode prevnode){
        if (prevnode == null){
            removeFront();
        }
        else{
            if (prevnode.nextNode == lastNode){
                lastNode = prevnode;
            }
            prevnode.nextNode = prevnode.nextNode.nextNode;
        }
    }

    public synchronized void remove(ListNode node){
        ListNode current, prev;
        if (firstNode == node){
            removeFront();
        }
        else{
            prev = firstNode;
            current = prev.nextNode;
            while (current != node){
                prev = current;
                current = current.nextNode;
            }
            removeAfter(prev);
        }
    }

    public synchronized void remove(int item) throws ElementNotExistsException{
        if (isEmpty()){
            throw new EmptyListException();
        }
        ListNode before = null;
        ListNode current = firstNode;
        while (current != null && Integer.class.cast(current.data) < item){
            before = current;
            current = current.nextNode;
        }
        if (current == null || Integer.class.cast(current.data) > item){
            throw new ElementNotExistsException(item);
        }
        else{
            if (before == null){
                removeFront();
            }
            else{
                before.nextNode = current.nextNode;
                if (current == lastNode){
                    lastNode = before;
                }
            }
        }
    }

    public synchronized void remove(List L) throws ElementNotExistsException{
        ListNode current = firstNode;
        ListNode before = null;
        ListNode currentL = L.firstNode;
        if (isEmpty()){
            throw new EmptyListException();
        }
        while (currentL != null){
            if (current.data == currentL.data){
                if (before != null){
                    before.nextNode = current.nextNode;
                }
                else{
                    firstNode = current.nextNode;
                }
                currentL = currentL.nextNode;
            }
            else{
                if (Integer.class.cast(current.data) < Integer.class.cast(currentL.data)){
                    before = current;
                }
                else{
                    throw new ElementNotExistsException(Integer.class.cast(currentL.data));
                }
            }
            current = current.nextNode;
        }
        if (before == null){
            firstNode = lastNode = null;
        }
        if (current == null){
            lastNode = before;
        }
    }

    public synchronized void reverse(ListNode firstnode, ListNode lastnode){
        ListNode current, next = lastnode, tmp;
        current = firstnode.nextNode;
        while (current != lastnode){
            tmp = current.nextNode;
            current.nextNode = next;
            next = current;
            current = tmp;
        }
        firstnode.nextNode = next;
    }

    public synchronized int[] toArray(){
        int[] array;
        int i = 0;
        ListNode current = firstNode;
        array = new int[this.elementCount()];
        while (current != null){
            array[i] = Integer.class.cast(current.data);
            current = current.nextNode;
            i++;
        }
        return array;
    }

    public synchronized List delta(int y){
        List delta;
        ListNode current = firstNode;
        delta = new List();
        while (current != null){
            delta.insert(java.lang.Math.abs(Integer.class.cast(current.data) - y));
            current = current.nextNode;
        }
        return delta;
    }

    public synchronized boolean subset(List L){
        ListNode current = firstNode;
        ListNode currentL = L.firstNode;
        while (current != null && currentL != null){
            if (current.data == currentL.data){
                current = current.nextNode;
                currentL = currentL.nextNode;
            }
            else{
                if (Integer.class.cast(current.data) < Integer.class.cast(currentL.data)){
                    current = current.nextNode;
                }
                else{
                    return false;
                }
            }
        }
        return currentL == null;
    }

    public synchronized int elementCount(){
        int count = 0;
        ListNode current = firstNode;
        while (current != null){
            current = current.nextNode;
            count++;
        }
        return count;
    }

    public synchronized int max(){
        return Integer.class.cast(lastNode.data);
    }
    
    public synchronized Object removeBack() throws EmptyListException{
        if (isEmpty()){
            throw new EmptyListException();
        }
        Object item = lastNode.data;
        if (firstNode == lastNode){
            firstNode = lastNode = null;
        }
        else{
            ListNode current = firstNode;
            while (current.nextNode != lastNode){
                current = current.nextNode;
            }
            lastNode = current;
            current.nextNode = null;
        }
        return item;
    }

    public synchronized void print(){
        if (isEmpty()){
            System.out.println("List is empty");
            return;
        }
        ListNode current = firstNode;
        while (current != null){
            System.out.print(current.data + " ");
            current = current.nextNode;
        }
        System.out.println();
    }
}
