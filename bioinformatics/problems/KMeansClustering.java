package bioinformatics.problems;

import bioinformatics.util.Instance;

import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * User: Olcay
 * Date: Jul 16, 2007
 * Time: 10:36:49 AM
 */
public class KMeansClustering {

    int n, m, k;
    Instance[] data;
    Instance[] clusters;
    int[] clusterBelongings;

    public KMeansClustering(String filename){
        int i, j;
        float[] attributes;
        Scanner sc;
        try{
            sc = new Scanner(new File(filename));
            n = sc.nextInt();
            data = new Instance[n];
            m = sc.nextInt();
            attributes = new float[m];
            for (i = 0; i < n; i++){
                for (j = 0; j < m; j++){
                    attributes[j] = sc.nextFloat();
                }
                data[i] = new Instance(attributes);
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

    private void clusterCenters(){
        int i, index;
        int clusterCounts[];
        clusterCounts = new int[k];
        for (i = 0; i < k; i++){
            clusters[i].clear();
        }
        for (i = 0; i < n; i++){
            index = clusterBelongings[i]; 
            clusterCounts[index]++;
            clusters[index].addTo(data[i]);
        }
        for (i = 0; i < k; i++){
            clusters[i].divideTo(clusterCounts[i]);
        }
    }

    private float costOfCluster(int cluster){
        int i, count = 0;
        float sum = 0;
        Instance clusterCenter;
        clusterCenter = new Instance(m);
        for (i = 0; i < n; i++){
            if (clusterBelongings[i] == cluster){
                clusterCenter.addTo(data[i]);
                count++;
            }
        }
        clusterCenter.divideTo(count);
        for (i = 0; i < n; i++){
            if (clusterBelongings[i] == cluster){
                sum += data[i].distance(clusterCenter);
            }
        }
        return sum;
    }

    private float reductionInCost(int dataIndex, int from, int to){
        float costBefore, costAfter;
        costBefore = costOfCluster(from) + costOfCluster(to);
        clusterBelongings[dataIndex] = to;
        costAfter = costOfCluster(from) + costOfCluster(to);
        clusterBelongings[dataIndex] = from;
        return costBefore - costAfter;
    }

    public void progressiveGreedy(int k){
        int i, j, besti, bestCluster, randomIndex;
        float bestChange, costChange;
        this.k = k;
        clusters = new Instance[k];
        clusterBelongings = new int[n];
        /*Initialize cluster centers to random points in the data*/
        for (i = 0; i < k; i++){
            randomIndex = (int) (Math.random() * n);
            clusters[i] = new Instance(data[randomIndex].getAttributes());
            clusterBelongings[randomIndex] = i;
        }
        while (true){
            bestChange = 0;
            besti = 0;
            bestCluster = 0;
            for (i = 0; i < n; i++){
                for (j = 0; j < k; j++){
                    if (clusterBelongings[i] != j){
                        costChange = reductionInCost(i, clusterBelongings[i], j);
                        if (costChange > bestChange){
                            bestChange = costChange;
                            besti = i;
                            bestCluster = j;
                        }
                    }
                }
            }
            if (bestChange > 0){
                clusterBelongings[besti] = bestCluster;
            }
            else{
                break;
            }
        }
        clusterCenters();
    }

    public void lloydsAlgorithm(int k){
        int i, j, belongsTo, randomIndex;
        boolean change;
        float minDistance;
        this.k = k;
        clusters = new Instance[k];
        clusterBelongings = new int[n];
        /*Initialize cluster centers to random points in the data*/
        for (i = 0; i < k; i++){
            randomIndex = (int) (Math.random() * n);
            clusters[i] = new Instance(data[randomIndex].getAttributes());
            clusterBelongings[randomIndex] = i;
        }
        while (true){
            /*Calculate cluster belongings*/
            change = false;
            for (i = 0; i < n; i++){
                minDistance = data[i].distance(clusters[0]);
                belongsTo = 0;
                for (j = 1; j < k; j++){
                    if (data[i].distance(clusters[j]) < minDistance){
                        minDistance = data[i].distance(clusters[j]);
                        belongsTo = j;
                    }
                }
                if (belongsTo != clusterBelongings[i]){
                    change = true;
                    clusterBelongings[i] = belongsTo;
                }
            }
            if (!change){
                break;
            }
            clusterCenters();
        }
    }

    public void outputSolution(String filename){
        FileWriter outfile;
        int i;
        try{
            outfile = new FileWriter(filename);
            outfile.write("Cluster Centers\n");
            for (i = 0; i < k; i++){
                clusters[i].writeFile(outfile);
            }
            outfile.write("Cluster Belongings\n");
            for (i = 0; i < n; i++){
                outfile.write("Instance " + (i + 1) + " belongs to Cluster " + (clusterBelongings[i] + 1) + "\n");
            }
            outfile.close();
        }
        catch (IOException ioException){
            System.out.println("Output file can not be opened");
        }
    }

}
