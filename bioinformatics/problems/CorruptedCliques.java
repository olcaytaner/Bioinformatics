package bioinformatics.problems;

import bioinformatics.datastructure.*;

import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * User: root
 * Date: Jul 17, 2007
 * Time: 11:46:55 AM
 */
public class CorruptedCliques {

    private int n, k;
    private float distanceMatrix[][];
    private int clusterbelongings[];
    private List bestPartition;
    private float theta;

    public CorruptedCliques(String filename){
        /*Page 350*/
        int i, j;
        Scanner sc;
        try{
            sc = new Scanner(new File(filename));
            n = sc.nextInt();
            distanceMatrix = new float[n][n];
            for (i = 0; i < n; i++){
                for (j = 0; j < n; j++){
                    distanceMatrix[i][j] = sc.nextFloat();
                }
            }
        }
        catch (FileNotFoundException fileNotFoundException){
            System.out.println("File " + filename + " not found");
        }
        catch (InputMismatchException inputMismatchException){
            System.out.println("Input file does not contain floats");
        }
        catch (NoSuchElementException noSuchElementException){
            System.out.println("Not enough elements in the input file");
        }
    }

    private boolean edgeExists(int fromNode, int toNode){
        return distanceMatrix[fromNode][toNode] < theta;
    }

    private Graph distanceGraph(){
        int i, j;
        Graph g;
        g = new Graph(n);
        for (i = 0; i < n; i++){
            g.addNode(i, i);
        }
        for (i = 0; i < n; i++){
            for (j = i + 1; j < n; j++){
                if (edgeExists(i, j)){
                    g.addEdge(i, j);
                    g.addEdge(j, i);
                }
            }
        }
        return g;
    }

    private void setBestPartition(List[] partition){
        int i;
        bestPartition = new List();
        for (i = 0; i < k; i++){
            bestPartition.insertBack(new List(partition[i]));
        }
    }

    private List[] createPartition(int[] clusterArray, int[] indexArray, int count){
        int i, node;
        List[] partition;
        partition = new List[k];
        for (i = 0; i < k; i++){
            partition[i] = new List();
        }
        for (i = 0; i < count; i++){
            node = indexArray[i];
            partition[clusterArray[i]].insertBack(node);
            clusterbelongings[node] = clusterArray[i];
        }
        return partition;
    }

    private void extendPartition(List[] partition, int[] indexArray, int start, int end){
        int i, j, zeroCluster, maxCluster, fromNode, toNode, Ni, Ci;
        ListNode current;
        float affinity, maxAffinity;
        for (j = start; j < end; j++){
            maxAffinity = -1;
            maxCluster = -1;
            zeroCluster = -1;
            fromNode = indexArray[j];
            for (i = 0; i < k; i++){
                Ci = 0;
                Ni = 0;
                current = partition[i].firstNode;
                while (current != null){
                    toNode = Integer.class.cast(current.get());
                    if (edgeExists(fromNode, toNode)){
                        Ni++;
                    }
                    Ci++;
                    current = current.next();
                }
                if (Ci != 0){
                    affinity = Ni / (float) Ci;
                }
                else{
                    zeroCluster = i;
                    affinity = 0;
                }
                if (affinity > maxAffinity){
                    maxAffinity = affinity;
                    maxCluster = i;
                }
            }
            if (maxAffinity == 0 && zeroCluster != -1){
                partition[zeroCluster].insertBack(fromNode);
                clusterbelongings[fromNode] = zeroCluster;
            }
            else{
                partition[maxCluster].insertBack(fromNode);
                clusterbelongings[fromNode] = maxCluster;
            }
        }
    }

    private int score(List[] partition){
        int i, j, fromNode, toNode, score = 0;
        ListNode inode, jnode;
        for (i = 0; i < k; i++){
            inode = partition[i].firstNode;
            while (inode != null){
                jnode = inode.next();
                while (jnode != null){
                    fromNode = Integer.class.cast(inode.get());
                    toNode = Integer.class.cast(jnode.get());
                    if (!edgeExists(fromNode, toNode)){
                        score++;
                    }
                    jnode = jnode.next();
                }
                inode = inode.next();
            }
        }
        for (i = 0; i < n; i++){
            for (j = i + 1; j < n; j++){
                if (clusterbelongings[i] != clusterbelongings[j] && edgeExists(i, j)){
                    score++;
                }
            }
        }
        return score;
    }

    private double log2(double x){
        return Math.log(x) / Math.log(2);
    }

    public void gcc(float theta, int k){
        Iterator p;
        int i, j, tmp, bestScore, score, count1, count2;
        int[] indexArray;
        List[] partition;
        this.k = k;
        this.theta = theta;
        clusterbelongings = new int[n];
        bestScore = n * n;
        /*Shuffle indexes*/
        indexArray = new int[n];
        for (i = 0; i < n; i++){
            indexArray[i] = i;
        }
        for (i = 0; i < n; i++){
            j = (int) (Math.random() * (n - i));
            tmp = indexArray[i];
            indexArray[i] = indexArray[j];
            indexArray[j] = tmp;
        }
        /*Number of elements in S'*/
        count1 = (int) log2(log2(n)) + 1;
        /*Number of elements in S''*/
        count2 = (int) log2(n) + 1;
        /*p is a partition of count1 elements into k partitions*/
        p = new Iterator(count1, k - 1);
        do{
            partition = createPartition(p.get(), indexArray, count1);
            extendPartition(partition, indexArray, count1, count1 + count2);
            extendPartition(partition, indexArray, count1 + count2, n);
            score = score(partition);
            if (score < bestScore){
                setBestPartition(partition);
                bestScore = score;
            }
        }while(p.nextLeaf());
    }

    private float totalDistance(int fromNode, List l){
        ListNode current;
        int toNode;
        float distanceSum = 0;
        current = l.firstNode;
        while (current != null){
            toNode = Integer.class.cast(current.get());
            distanceSum += distanceMatrix[fromNode][toNode];
            current = current.next();
        }
        return distanceSum;
    }

    private int farthestGene(List c, float theta2){
        ListNode current;
        int fromNode, count, maxGene = -1;
        float distance, maxDistance = -1;
        count = c.elementCount();
        current = c.firstNode;
        while (current != null){
            fromNode = Integer.class.cast(current.get());
            distance = totalDistance(fromNode, c) / count;
            if (distance > maxDistance){
                maxDistance = distance;
                maxGene = fromNode;
            }
            current = current.next();
        }
        if (maxDistance > theta2){
            return maxGene;
        }
        else{
            return -1;
        }
    }

    private int closestGene(List s, List c, float theta2){
        ListNode current;
        int fromNode, count, minGene = -1;
        float distance, minDistance = +10000;
        count = c.elementCount();
        current = s.firstNode;
        while (current != null){
            fromNode = Integer.class.cast(current.get());
            distance = totalDistance(fromNode, c) / count;
            if (distance < minDistance){
                minDistance = distance;
                minGene = fromNode;
            }
            current = current.next();
        }
        if (minDistance < theta2){
            return minGene;
        }
        else{
            return -1;
        }
    }

    public void cast(float theta, float theta2){
        int i, v, farthest, closest;
        List s, c;
        Graph g;
        this.theta = theta;
        g = distanceGraph();
        /*S = set of vertices in the distance graph*/
        s = new List();
        for (i = 0; i < n; i++){
            s.insertBack(i);
        }
        /*P = empty set*/
        bestPartition = new List();
        /*while s is not empty*/
        while (s.firstNode != null){
            v = g.maximalOutDegreeVertex();
            c = new List();
            c.insertBack(v);
            s.remove(v);
            farthest = farthestGene(c, theta2);
            closest = closestGene(s, c, theta2);
            while (farthest != -1 || closest != -1){
                if (farthest != -1){
                    c.remove(farthest);
                    s.insert(farthest);
                }
                if (closest != -1){
                    c.insert(closest);
                    s.remove(closest);
                }
                farthest = farthestGene(c, theta2);
                closest = closestGene(s, c, theta2);
            }
            g.removeEdgesofNodes(c);
            bestPartition.insertBack(c);
        }
    }

    public void generateProblem(int count, float maxDistance, String filename){
        FileWriter outfile;
        int i, j;
        try{
            outfile = new FileWriter(filename);
            outfile.write(count + "\n");
            for (i = 0; i < count; i++){
                for (j = 0; j < count; j++){
                    outfile.write(String.valueOf((float) (Math.random() * maxDistance)));
                }
                outfile.write("\n");
            }
            outfile.close();
        }
        catch (IOException ioException){
            System.out.println("Output file can not be opened");
        }
    }

    public void outputSolution(String filename){
        ListNode currentList, currentNode;
        FileWriter outfile;
        try{
            outfile = new FileWriter(filename);
            currentList = bestPartition.firstNode;
            while (currentList != null){
                currentNode = List.class.cast(currentList.get()).firstNode;
                while (currentNode != null){
                    outfile.write((Integer.class.cast(currentNode.get()) + 1) + " ");
                    currentNode = currentNode.next();
                }
                currentList = currentList.next();
                outfile.write("\n");
            }
            outfile.close();
        }
        catch (IOException ioException){
            System.out.println("Output file can not be opened");
        }
    }

}
