package bioinformatics.problems;

import java.util.Comparator;

/**
 * User: olcay
 * Date: 29-Nov-2007
 * Time: 22:16:19
 */
public class DnaAlignmentComparator implements Comparator {

    public int compare(Object o1, Object o2) {
        DnaAlignment p1 = (DnaAlignment) o1, p2 = (DnaAlignment) o2;
        return p1.compareTo(p2);
    }

}
