package bioinformatics.userinterface;

import javax.swing.*;
import java.awt.*;

/**
 * User: root
 * Date: Nov 13, 2007
 * Time: 2:57:49 PM
 */
public class DotMatrix extends JFrame {

    private int width, height;
    private boolean [][] matrix;

    public DotMatrix(boolean[][] matrix){
        super("Dot Matrix");
        this.matrix = matrix;
        width = matrix.length;
        height = matrix[0].length;
        setSize(width, height);
        setVisible(true);
    }

    public void paint(Graphics g){
        int i, j;
        super.paint(g);
        for (i = 0; i < width; i++){
            for (j = 0; j < height; j++){
                if (matrix[i][j]){
                    g.drawString(".", i, j);                    
                }
            }
        }
    }
}
