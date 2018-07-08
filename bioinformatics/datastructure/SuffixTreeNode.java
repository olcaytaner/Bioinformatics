package bioinformatics.datastructure;

/**
 * User: Olcay
 * Date: Jul 13, 2007
 * Time: 3:30:16 PM
 */
public class SuffixTreeNode {

    private SuffixTreeNode child;
    private SuffixTreeNode sister;
    private int label;
    private String text;

    public SuffixTreeNode(){
        child = sister = null;
        this.label = -1;
        text = "";
    }

    public SuffixTreeNode(String s, int label){
        child = sister = null;
        this.label = label;
        text = s;
    }

    public SuffixTreeNode(String s, int label, SuffixTreeNode child){
        sister = null;
        this.label = label;
        this.child = child;
        text = s;
    }

    public SuffixTreeNode(String s, int label, SuffixTreeNode child, SuffixTreeNode sister){
        this.label = label;
        this.child = child;
        this.sister = sister;
        text = s;
    }

    public void descendantLabels(List labels){
        SuffixTreeNode current;
        if (label != -1){
            labels.insertFront(label + 1);
        }
        current = child;
        while (current != null){
            current.descendantLabels(labels);
            current = current.sister;
        }
    }

    public SuffixTreeNode thread(String s){
        int i;
        SuffixTreeNode current;
        current = child;
        /*Check all children until there is a match in the first character*/
        while (current != null && current.text.charAt(0) != s.charAt(0)){
            current = current.sister;
        }
        if (current == null){
            /*If no match return null*/
            return null;
        }
        i = 1;
        /*If match check for other matches until (i) current nodes text exhausts (ii) s exhausts (iii) there is no match*/
        while (i < current.text.length() && i < s.length() && current.text.charAt(i) == s.charAt(i)){
            i++;
        }
        /*Case (ii)*/
        if (i == s.length()){
            return current;
        }
        else{
            /*Case (i)*/
            if (i == current.text.length()){
                return current.thread(s.substring(i, s.length()));
            }
            /*Case (iii)*/
            else{
                return null;
            }
        }
    }

    public void addSuffix(String s, int label){
        int i;
        SuffixTreeNode current, current2, prev = null, newchild, newsister;
        current = child;
        /*Check all children until there is a match in the first character*/
        while (current != null && current.text.charAt(0) != s.charAt(0)){
            prev = current;
            current = current.sister;
        }
        if (current == null){
            /*If no match create a new child and add to the children*/
            newchild = new SuffixTreeNode(s, label);
            /*If child exists add it as a sister*/
            if (prev != null){
                prev.sister = newchild;
            }
            /*If no child exists create as the first child*/
            else{
                child = newchild;
            }
        }
        else{
            i = 1;
            /*If match check for other matches until (i) current nodes text exhausts (ii) s exhausts (iii) there is no match*/
            while (i < current.text.length() && i < s.length() && current.text.charAt(i) == s.charAt(i)){
                i++;
            }
            /*Case (i)*/
            if (i == current.text.length()){
                if (i == s.length()){
                    if (current.label != -1){
                        current.label = label;
                    }
                }
                else{
                    if (current.child != null){
                        current.addSuffix(s.substring(i, s.length()), label);
                    }
                    else{
                        current.child = new SuffixTreeNode(s.substring(i, s.length()), label);
                    }
                }
            }
            else{
                /*Case (ii)*/
                if (i == s.length()){
                    if (current.label != -1 || current.child == null){
                        current.child = new SuffixTreeNode(current.text.substring(i, current.text.length()), current.label, current.child);
                    }
                    else{
                        current2 = current.child;
                        while (current2 != null){
                            current2.text = current.text.substring(i, current.text.length()) + current2.text;
                            current2 = current2.sister;
                        }
                    }
                    current.label = label;
                    current.text = s;
                }
                /*Case (iii)*/
                else{
                    newsister = new SuffixTreeNode(s.substring(i, s.length()), label);
                    current.child = new SuffixTreeNode(current.text.substring(i, current.text.length()), current.label, current.child, newsister);
                    current.label = -1;
                    current.text = current.text.substring(0, i);
                }
            }
        }
    }

}
