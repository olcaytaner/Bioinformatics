package bioinformatics.problems;

import bioinformatics.util.DnaSequence;
import bioinformatics.util.Sequence;
import bioinformatics.datastructure.Graph;
import bioinformatics.datastructure.List;
import bioinformatics.datastructure.ListNode;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * User: Olcay
 * Date: Jul 6, 2007
 * Time: 2:14:18 PM
 */
public class SequencingByHybridization {

    int n, l;
    Sequence[] lmers;
    String solution;

    public SequencingByHybridization(String filename){
        /*Page 270*/
        Scanner sc;
        int i;
        try{
            sc = new Scanner(new File(filename));
            n = sc.nextInt();
            lmers = new DnaSequence[n];
            for (i = 0; i < n; i++){
                lmers[i] = new Sequence(sc.next());
            }
            l = lmers[0].length();
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

    private Graph prepareHamiltonianGraph(){
        Graph g;
        int i, j;
        g = new Graph(n);
        for (i = 0; i < n; i++){
            g.addNode(i, lmers[i]);
            for (j = 0; j < n; j++){
                if (lmers[i].overlap(lmers[j]) == l - 1){
                    g.getNode(i).addEdge(j);
                }
            }
        }
        return g;
    }

    public void hamiltonianPath(){
        Graph g;
        int[] path;
        int i, nodeIndex;
        g = prepareHamiltonianGraph();
        path = g.hamiltonianPath();
        nodeIndex = path[0];
        solution = lmers[nodeIndex].get();
        for (i = 1; i < n; i++){
            nodeIndex = path[i];
            solution += lmers[nodeIndex].get().charAt(l - 1);
        }
    }

    private Graph prepareEulerianGraph(){
        Graph g;
        int i, j;
        int from, to;
        String prefix, postfix;
        boolean prefound, postfound;
        List tuples;
        ListNode current;
        tuples = new List();
        /*Find and count number of nodes in the graph*/
        for (i = 0; i < n; i++){
            prefix = lmers[i].get().substring(0, l - 1);
            postfix = lmers[i].get().substring(1, l);
            prefound = postfound = false;
            current = tuples.firstNode;
            while (current != null && (!prefound || !postfound)){
                if (String.class.cast(current.get()).equals(prefix)){
                    prefound = true;
                }
                if (String.class.cast(current.get()).equals(postfix)){
                    postfound = true;
                }
                current = current.next();
            }
            if (!prefound){
                tuples.insertBack(prefix);
            }
            if (!postfound){
                tuples.insertBack(postfix);
            }
        }
        /*Add nodes to the graph*/
        g = new Graph(tuples.elementCount());
        current = tuples.firstNode;
        i = 0;
        while (current != null){
            g.addNode(i, String.class.cast(current.get()));
            current = current.next();
            i++;
        }
        /*Add edges to the graph*/
        for (i = 0; i < n; i++){
            prefix = lmers[i].get().substring(0, l - 1);
            postfix = lmers[i].get().substring(1, l);
            from = to = -1;
            for (j = 0; j < g.getVertexCount(); j++){
                if (prefix.equals(String.class.cast(g.getNode(j).get()))){
                    from = j;
                    break;
                }
            }
            for (j = 0; j < g.getVertexCount(); j++){
                if (postfix.equals(String.class.cast(g.getNode(j).get()))){
                    to = j;
                    break;
                }
            }
            g.addEdge(from, to);
        }
        return g;
    }

    public void eulerianPath(){
        Graph g;
        int nodeIndex;
        List path;
        ListNode current;
        g = prepareEulerianGraph();
        path = g.eulerianPath();
        current = path.firstNode;
        nodeIndex = Integer.class.cast(current.get());
        solution = String.class.cast(g.getNode(nodeIndex).get());
        current = current.next();
        while (current != null){
            nodeIndex = Integer.class.cast(current.get());
            solution += String.class.cast(g.getNode(nodeIndex).get()).charAt(l - 2);
            current = current.next();
        }
    }

    public void generateProblem(int length, int l, String filename){
        FileWriter outfile;
        DnaSequence seq;
        int i, j, count, tmp;
        int[] indexArray;
        try{
            outfile = new FileWriter(filename);
            seq = new DnaSequence(length);
            count = length - l + 1;
            /*Shuffle indexes*/
            indexArray = new int[count];
            for (i = 0; i < count; i++){
                indexArray[i] = i;
            }
            for (i = 0; i < count; i++){
                j = (int) (Math.random() * (count - i));
                tmp = indexArray[i];
                indexArray[i] = indexArray[j];
                indexArray[j] = tmp;
            }
            outfile.write(count + "\n");
            for (i = 0; i < count; i++){
                outfile.write(seq.get().substring(indexArray[i], indexArray[i] + l) + "\n");
            }
            outfile.close();
        }
        catch (IOException ioException){
            System.out.println("Output file can not be opened");
        }
    }

    public void outputSolution(String filename){
        FileWriter outfile;
        try{
            outfile = new FileWriter(filename);
            outfile.write(solution);
            outfile.close();
        }
        catch (IOException ioException){
            System.out.println("Output file can not be opened");
        }
    }

}
