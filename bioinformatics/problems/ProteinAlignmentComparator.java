package bioinformatics.problems;

import java.util.Comparator;

/**
 * User: olcay
 * Date: 29-Nov-2007
 * Time: 21:39:06
 */
public class ProteinAlignmentComparator implements Comparator {

    public int compare(Object o1, Object o2) {
        ProteinAlignment p1 = (ProteinAlignment) o1, p2 = (ProteinAlignment) o2;
        return p1.compareTo(p2);
    }

}
