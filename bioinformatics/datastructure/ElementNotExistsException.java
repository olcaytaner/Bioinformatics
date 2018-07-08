package bioinformatics.datastructure;

/**
 * User: root
 * Date: Jun 7, 2007
 * Time: 9:42:58 AM
 */
public class ElementNotExistsException extends RuntimeException{
    public ElementNotExistsException(int value){
        super("Element " + value + " does not exist");
    }
}
