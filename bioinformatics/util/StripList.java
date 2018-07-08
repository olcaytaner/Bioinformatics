package bioinformatics.util;

import bioinformatics.datastructure.List;
import bioinformatics.datastructure.ListNode;

/**
 * User: root
 * Date: Jun 28, 2007
 * Time: 1:42:58 PM
 */
public class StripList extends List {

    private int n;

    public StripList(int permutation[], int n){
        Strip current;
        int i, lastone, striplength;
        boolean decreasing;
        this.n = n;
        lastone = 0;
        decreasing = false;
        striplength = 0;
        for (i = 0; i < n; i++){
            if (Math.abs(lastone - permutation[i]) == 1){
                decreasing = (lastone - permutation[i]) > 0;
                lastone = permutation[i];
                striplength++;
            }
            else{
                if (striplength == 0){
                    decreasing = true;
                }
                current = new Strip(decreasing, lastone, striplength);
                insertBack(current);
                lastone = permutation[i];
                decreasing = false;
                striplength = 0;
            }
        }
        if (n - lastone == 0){
            striplength++;
            current = new Strip(n - striplength + 1, n + 1, false);
            insertBack(current);
        }
        else{
            if (striplength == 0){
                decreasing = true;
            }
            current = new Strip(decreasing, lastone, striplength);
            insertBack(current);
            current = new Strip(n + 1, n + 1, false);
            insertBack(current);
        }
    }

    public int[] toArray(){
        int[] currentPermutation;
        int i, j = 0, increment, lowerBound, upperBound;
        Strip s;
        ListNode current;
        currentPermutation = new int [n];
        current = firstNode;
        while (current != null){
            s = Strip.class.cast(current.get());
            lowerBound = s.getStart();
            upperBound = s.getEnd();
            if (!s.isDecreasing() || s.singleElement()){
                increment = +1;
            }
            else{
                increment = -1;
            }
            for (i = lowerBound; (i * increment) <= (increment * upperBound); i += increment){
                if (i != 0 && i != n + 1){
                    currentPermutation[j] = i;
                    j++;
                }
            }
            current = current.next();
        }
        return currentPermutation;
    }

    public boolean hasDecreasingStrip(){
        ListNode current;
        Strip s;
        current = firstNode;
        while (current != null){
            s = Strip.class.cast(current.get());
            if (s.isDecreasing()){
                return true;
            }
            current = current.next();
        }
        return false;
    }

    public void noProgressReversal(){
        Strip s;
        ListNode current;
        current = firstNode.next();
        while (current != null){
            s = Strip.class.cast(current.get());
            if (!s.singleElement()){
                s.reverse();
                break;
            }
        }
    }

    public void reverseStrips(ListNode firstnode, ListNode lastnode){
        ListNode current = firstnode.next();
        Strip strip;
        while (current != lastnode){
            strip = Strip.class.cast(current.get());
            strip.reverse();
            current = current.next();
        }
    }

    public void mergeStrips(ListNode firstnode, ListNode secondnode){
        Strip firststrip, secondstrip;
        firststrip = Strip.class.cast(firstnode.get());
        secondstrip = Strip.class.cast(secondnode.get());
        if (firststrip.canBeMerged(secondstrip)){
            firststrip.merge(secondstrip);
            removeAfter(firstnode);
        }
    }

    public void reversal(int direction){
        /*If direction is 1 reverse from left to right, otherwise reverse from right to left*/
        Strip istrip, jstrip;
        ListNode inode, iprevnode, inextnode, jnode, jprevnode, jnextnode;
        /*Find first selected node*/
        inode = firstNode;
        iprevnode = null;
        istrip = Strip.class.cast(inode.get());
        while (!istrip.isSelected()){
            iprevnode = inode;
            inode = inode.next();
            istrip = Strip.class.cast(inode.get());
        }
        istrip.deselect();
        /*Find second selected node*/
        jprevnode = inode;
        jnode = inode.next();
        jstrip = Strip.class.cast(jnode.get());
        while (!jstrip.isSelected()){
            jprevnode = jnode;
            jnode = jnode.next();
            jstrip = Strip.class.cast(jnode.get());
        }
        jstrip.deselect();
        if (direction == 1){
            //Reverse Link List
            reverse(iprevnode, jnode);
            //Reverse strips in the list nodes
            reverseStrips(iprevnode, jnode);
            mergeStrips(iprevnode, jprevnode);
            mergeStrips(inode, jnode);
        }
        else{
            inextnode = inode.next();
            jnextnode = jnode.next();
            reverse(inode, jnextnode);
            reverseStrips(inode, jnextnode);
            mergeStrips(inode, jnode);
            if (jnode != inextnode){
                mergeStrips(inextnode, jnextnode);
            }
            else{
                mergeStrips(inode, jnextnode);
            }
        }
    }

    public int reversalMinimizingB(){
        ListNode inode, jnode, iprevnode, jprevnode;
        Strip istrip, jstrip, iprevstrip, jprevstrip, inextstrip, jnextstrip, startstrip = null, endstrip = null;
        iprevnode = null;
        int direction = 0;
        inode = firstNode;
        while (inode.next().next() != null){
            istrip = Strip.class.cast(inode.get());
            jprevnode = inode;
            jnode = inode.next();
            while (jnode.next() != null){
                jstrip = Strip.class.cast(jnode.get());
                if (istrip.isNeighbor(jstrip)){
                    if (istrip.startNeighbor(jstrip)){
                        if (iprevnode != null){
                            iprevstrip = Strip.class.cast(iprevnode.get());
                            jprevstrip = Strip.class.cast(jprevnode.get());
                            if (iprevstrip.endNeighbor(jprevstrip)){
                                istrip.select();
                                jstrip.select();
                                return 1;
                            }
                            direction = 1;
                        }
                        else{
                            direction = 2;
                        }
                    }
                    if (istrip.endNeighbor(jstrip)){
                        inextstrip = Strip.class.cast(inode.next().get());
                        jnextstrip = Strip.class.cast(jnode.next().get());
                        if (inextstrip.startNeighbor(jnextstrip)){
                            istrip.select();
                            jstrip.select();
                            return 2;
                        }
                        direction = 2;
                    }
                    startstrip = istrip;
                    endstrip = jstrip;
                }
                jprevnode = jnode;
                jnode = jnode.next();
            }
            iprevnode = inode;
            inode = inode.next();
        }
        if (startstrip != null && endstrip != null){
            startstrip.select();
            endstrip.select();
        }
        return direction;
    }

}
