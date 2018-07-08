package bioinformatics.problems;

import bioinformatics.datastructure.*;

import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * User: root
 * Date: Jul 19, 2007
 * Time: 3:21:15 PM
 */
public class DistanceBasedPhylogeny {

    private int n;
    private int distanceMatrix[][];
    private Graph g;
    private int degi, degj, degk;

    public DistanceBasedPhylogeny(String filename){
        /*Page 360*/
        int i, j;
        Scanner sc;
        try{
            sc = new Scanner(new File(filename));
            n = sc.nextInt();
            distanceMatrix = new int[n][n];
            for (i = 0; i < n; i++){
                for (j = 0; j < n; j++){
                    distanceMatrix[i][j] = sc.nextInt();
                }
            }
        }
        catch (FileNotFoundException fileNotFoundException){
            System.out.println("File " + filename + " not found");
        }
        catch (InputMismatchException inputMismatchException){
            System.out.println("Input file does not contain integers");
        }
        catch (NoSuchElementException noSuchElementException){
            System.out.println("Not enough elements in the input file");
        }
    }

    private void degenerateTriple(int[][] D, int n){
        int i, j, k;
        for (i = 0; i < n; i++){
            for (j = 0; j < n; j++){
                for (k = 0; k < n; k++){
                    if (i != j && i != k && j != k && D[i][j] + D[j][k] == D[i][k]){
                        degi = i;
                        degj = j;
                        degk = k;
                        return;
                    }
                }
            }
        }
    }

    private int trimmingParameter(int[][] D, int n){
        int i, j, k, delta, minDelta = 10000;
        for (i = 0; i < n; i++){
            for (j = 0; j < n; j++){
                for (k = 0; k < n; k++){
                    if (i != j && i != k && j != k){
                        if (D[i][k] == D[i][j] + D[j][k]){
                            return 0;
                        }
                        else{
                            if (D[i][k] < D[i][j] + D[j][k] && (D[i][j] + D[j][k] - D[i][k]) % 2 == 0){
                                delta = (D[i][j] + D[j][k] - D[i][k]) / 2;
                                if (delta < minDelta){
                                    minDelta = delta;
                                }
                            }
                        }
                    }
                }
            }
        }
        return minDelta;
    }

    private void trim(int[][] D, int n, int delta){
        int i, j;
        for (i = 0; i < n; i++){
            for (j = 0; j < n; j++){
                if (i != j){
                    D[i][j] -= 2 * delta;                    
                }
            }
        }
    }

    private void removeRowAndColumn(int[][] D, int[] names, int n, int degj){
        int i, j;
        /*Delete j'th index from names*/
        for (i = degj; i < n - 1; i++){
            names[i] = names[i + 1];
        }
        /*Delete j'th column of the rows 0..j - 1*/
        for (i = 0; i < degj; i++){
            for (j = degj; j < n - 1; j++){
                D[i][j] = D[i][j + 1];
            }
        }
        /*Delete j'th row*/
        for (i = degj; i < n - 1; i++){
            /*Delete j'th row of the columns 0..j - 1*/
            for (j = 0; j < degj; j++){
                D[i][j] = D[i + 1][j];
            }
            /*Delete j'th row of the columns j + 1..n - 1*/
            for (j = degj; j < n - 1; j++){
                D[i][j] = D[i + 1][j + 1];
            }
        }
    }

    private void extendHangingEdges(int delta){
        List edges;
        ListNode current;
        GraphEdge edge;
        int i;
        for (i = 0; i < g.getVertexCount(); i++){
            edges = g.getEdges(i);
            if (edges != null){
                current = edges.firstNode;
                while (current != null){
                    edge = GraphEdge.class.cast(current.get());
                    if (edge.from() < n || edge.to() < n){
                        edge.setWeight(edge.weight() + delta);
                    }
                    current = current.next();
                }
            }
        }
    }

    private void additivePhylogenyRecursive(int[][] D, int[] names, int n){
        int i, j, k;
        int delta, x, fromNode, toNode, weight, v;
        GraphEdge edge = null;
        List path;
        ListNode current;
        if (n == 2){
            g.addNode(names[0], names[0]);
            g.addNode(names[1], names[1]);
            g.addUndirectedEdge(names[0], names[1], D[0][1]);
            return;
        }
        delta = trimmingParameter(D, n);
        if (delta > 0){
            trim(D, n, delta);
        }
        degenerateTriple(D, n);
        i = names[degi];
        j = names[degj];
        k = names[degk];
        x = D[degi][degj];
        removeRowAndColumn(D, names, n, degj);
        additivePhylogenyRecursive(D, names, n - 1);
        /*Find the edge where the new vertex v at distance x from i to k will be added*/
        path = g.constructPath(i, k);
        current = path.firstNode;
        while (current != null){
            edge = GraphEdge.class.cast(current.get());
            if (x > edge.weight()){
                x = x - edge.weight();
            }
            else{
                break;
            }
            current = current.next();
        }
        if (edge == null){
            return;
        }
        fromNode = edge.from();
        toNode = edge.to();
        weight = edge.weight();
        v = this.n + n - 3;
        /* Add new node v*/
        edge.setTo(v);
        edge.setWeight(x);
        g.addNode(v, v);
        g.addEdge(v, fromNode, x);
        /* Add new edge from node v to middle node*/
        g.getNode(toNode).setEdgeWeight(fromNode, weight - x);
        g.getNode(toNode).setEdgeTo(fromNode, v);
        g.addEdge(v, toNode, weight - x);
        /*Add node j*/
        g.addNode(j, j);
        /*Add edge from new node v to node j*/
        g.addUndirectedEdge(v, j, 0);
        if (delta > 0){
            extendHangingEdges(delta);
        }
    }

    private boolean isAdditive(int[][] D, int n){
        int i, j, k, l;
        int sum1, sum2, sum3;
        for (i = 0; i < n; i++){
            for (j = 0; j < n; j ++){
                for (k = 0; k < n; k++){
                    for (l = 0; l < n; l++){
                        if (i != j && i != k && i != l && j != k && j != l && k != l){
                            sum1 = D[i][j] + D[k][l];
                            sum2 = D[i][k] + D[j][l];
                            sum3 = D[i][l] + D[j][k];
                            if (sum1 == sum2 && sum3 < sum1){
                                continue;
                            }
                            if (sum1 == sum3 && sum2 < sum1){
                                continue;
                            }
                            if (sum2 == sum3 && sum1 < sum2){
                                continue;
                            }
                            return false;                            
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean additivePhylogeny(){
        int i, j;
        int[][] D;
        int[] names;
        if (!isAdditive(distanceMatrix, n)){
            return false;
        }
        D = new int[n][n];
        names = new int[n];
        for (i = 0; i < n; i++){
            names[i] = i;
        }
        for (i = 0; i < n; i++){
            for (j = 0; j < n; j++){
                D[i][j] = distanceMatrix[i][j];
            }
        }
        g = new Graph(2 * n - 2);
        additivePhylogenyRecursive(D, names, n);
        return true;
    }

    public Graph getUnrootedTree(){
        return g;
    }

}
