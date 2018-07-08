package bioinformatics.util;

/**
 * User: root
 * Date: Jun 15, 2007
 * Time: 3:50:22 PM
 */
public class Strip {

    private int start;
    private int end;
    private boolean selected;
    private boolean decreasing;

    public Strip(int start, int end, boolean decreasing){
        this.start = start;
        this.end = end;
        this.selected = false;
        this.decreasing = decreasing;
    }

    public Strip(boolean decreasing, int end, int striplength){
        this.decreasing = decreasing;
        if (decreasing){
            this.start = end + striplength;
            this.end = end;
        }
        else{
            this.start = end - striplength;
            this.end = end;
        }
        this.selected = false;
    }

    public boolean isNeighbor(Strip s){
        return (Math.abs(start - s.start) == 1 || Math.abs(end - s.end) == 1);
    }

    public boolean startNeighbor(Strip s){
        return singleElement() && s.singleElement() && Math.abs(start - s.start) == 1 || (Math.abs(start - s.start) == 1);
    }

    public boolean endNeighbor(Strip s){
        return singleElement() && s.singleElement() && Math.abs(start - s.start) == 1 || (Math.abs(end - s.end) == 1);
    }

    public boolean canBeMerged(Strip s){
        return (Math.abs(this.end - s.start) == 1);
    }

    public void merge(Strip s){
        this.end = s.end;
        this.decreasing = this.end < this.start;
    }

    public int getStart(){
        return start;
    }

    public int getEnd(){
        return end;
    }

    public boolean singleElement(){
        return start == end;
    }
    
    public boolean isDecreasing(){
        return decreasing;
    }

    public boolean isSelected(){
        return selected;
    }

    public void select(){
        selected = true;
    }

    public void deselect(){
        selected = false;
    }

    public void reverse(){
        int tmp;
        if (!singleElement()){
            tmp = end;
            end = start;
            start = tmp;
            decreasing = !decreasing;            
        }
    }

}
