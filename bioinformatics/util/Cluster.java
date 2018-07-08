package bioinformatics.util;

import bioinformatics.datastructure.List;
import bioinformatics.datastructure.ListNode;

/**
 * User: Olcay
 * Date: Jul 14, 2007
 * Time: 7:23:24 PM
 */
public class Cluster {

    private List items;
    private EvolutionaryTreeNode node;

    public Cluster(int index){
        items = new List();
        items.insertBack(index);
        node = new EvolutionaryTreeNode(index);
    }

    public Cluster(Cluster c1, Cluster c2, int index){
        if (c1.items.elementCount() < c2.items.elementCount()){
            items = new List(c1.items);
            items.merge(c2.items);
        }
        else{
            items = new List(c2.items);
            items.merge(c1.items);
        }
        node = c1.node.mergeNode(c2.node, index);
    }

    public float distanceMin(Cluster secondCluster, float[][] distanceMatrix){
        int i, j;
        ListNode inode, jnode;
        float min = (float) 10000.0;
        inode = items.firstNode;
        while (inode != null){
            jnode = secondCluster.items.firstNode;
            while (jnode != null){
                i = Integer.class.cast(inode.get());
                j = Integer.class.cast(jnode.get());
                if (distanceMatrix[i][j] < min){
                    min = distanceMatrix[i][j];
                }
                jnode = jnode.next();
            }
            inode = inode.next();
        }
        return min;
    }

    public float distanceAverage(Cluster secondCluster, float[][] distanceMatrix){
        int i, j, count = 0;
        ListNode inode, jnode;
        float sum = 0;
        inode = items.firstNode;
        while (inode != null){
            jnode = secondCluster.items.firstNode;
            while (jnode != null){
                i = Integer.class.cast(inode.get());
                j = Integer.class.cast(jnode.get());
                sum += distanceMatrix[i][j];
                count++;
                jnode = jnode.next();
            }
            inode = inode.next();
        }
        return sum / count;
    }

    public float separation(List clusters, float[][] distanceMatrix){
        ListNode node;
        Cluster c;
        int count = 0;
        float sum = 0;
        node = clusters.firstNode;
        while (node != null){
            c = Cluster.class.cast(node.get());
            sum += distanceAverage(c, distanceMatrix);
            count++;
            node = node.next();
        }
        if (count > 2){
            return sum / (count - 2);
        }
        else{
            return 0;
        }
    }

    public EvolutionaryTreeNode getNode(){
        return node;
    }

}
