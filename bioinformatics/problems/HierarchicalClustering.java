package bioinformatics.problems;

import bioinformatics.util.EvolutionaryTree;
import bioinformatics.util.Cluster;
import bioinformatics.datastructure.List;
import bioinformatics.datastructure.ListNode;

import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * User: Olcay
 * Date: Jul 14, 2007
 * Time: 7:00:01 PM
 */
public class HierarchicalClustering {

    private int n;
    private float distanceMatrix[][];
    private EvolutionaryTree tree;

    public HierarchicalClustering(String filename){
        /*Page 345*/
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

    public void upgma(){
        int i;
        List clusters;
        ListNode inode, jnode, bestinode, bestjnode;
        Cluster clusteri, clusterj, clusterMerge;
        float minDistance, distance;
        clusters = new List();
        for (i = 0; i < n; i++){
            clusters.insertBack(new Cluster(i));
        }
        while (clusters.firstNode.next() != null){
            inode = clusters.firstNode;
            bestinode = bestjnode = inode;
            minDistance = -1;
            while (inode != null){
                clusteri = Cluster.class.cast(inode.get());
                jnode = inode.next();
                while (jnode != null){
                    clusterj = Cluster.class.cast(jnode.get());
                    distance = clusteri.distanceAverage(clusterj, distanceMatrix);
                    if (minDistance == -1 || distance < minDistance){
                        minDistance = distance;
                        bestinode = inode;
                        bestjnode = jnode;
                    }
                    jnode = jnode.next();
                }
                inode = inode.next();
            }
            if (bestinode == null){
                return;
            }
            clusteri = Cluster.class.cast(bestinode.get());
            clusteri.getNode().setLength(minDistance / 2 - clusteri.getNode().getLength());
            clusterj = Cluster.class.cast(bestjnode.get());
            clusterj.getNode().setLength(minDistance / 2 - clusterj.getNode().getLength());
            clusterMerge = new Cluster(clusteri, clusterj, i);
            clusters.insertBack(clusterMerge);
            i++;
            clusters.remove(bestinode);
            clusters.remove(bestjnode);
        }
        tree = new EvolutionaryTree(Cluster.class.cast(clusters.firstNode.get()).getNode());
    }

    public void neighborJoining(){
        int i;
        List clusters;
        ListNode inode, jnode, bestinode, bestjnode;
        Cluster clusteri, clusterj, clusterMerge;
        float minDistance, distance, d12, u1, u2;
        clusters = new List();
        for (i = 0; i < n; i++){
            clusters.insertBack(new Cluster(i));
        }
        while (clusters.firstNode.next() != null){
            inode = clusters.firstNode;
            bestinode = bestjnode = inode;
            minDistance = -1;
            while (inode != null){
                clusteri = Cluster.class.cast(inode.get());
                jnode = inode.next();
                while (jnode != null){
                    clusterj = Cluster.class.cast(jnode.get());
                    d12 = clusteri.distanceAverage(clusterj, distanceMatrix);
                    u1 = clusteri.separation(clusters, distanceMatrix);
                    u2 = clusterj.separation(clusters, distanceMatrix);
                    distance = d12 - u1 - u2;
                    if (minDistance == -1 || distance < minDistance){
                        minDistance = distance;
                        bestinode = inode;
                        bestjnode = jnode;
                    }
                    jnode = jnode.next();
                }
                inode = inode.next();
            }
            if (bestinode == null){
                return;
            }
            clusteri = Cluster.class.cast(bestinode.get());
            clusterj = Cluster.class.cast(bestjnode.get());
            d12 = clusteri.distanceAverage(clusterj, distanceMatrix);
            u1 = clusteri.separation(clusters, distanceMatrix);
            u2 = clusterj.separation(clusters, distanceMatrix);
            clusteri.getNode().setLength((d12 + u1 - u2) / 2);
            clusterj.getNode().setLength((d12 + u2 - u1) / 2);
            clusterMerge = new Cluster(clusteri, clusterj, i);
            clusters.insertBack(clusterMerge);
            i++;
            clusters.remove(bestinode);
            clusters.remove(bestjnode);
        }
        tree = new EvolutionaryTree(Cluster.class.cast(clusters.firstNode.get()).getNode());
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

    public EvolutionaryTree getTree(){
        return tree;
    }

}
