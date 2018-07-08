package bioinformatics.problems;

/**
 * User: Olcay
 * Date: Jul 23, 2007
 * Time: 4:09:14 PM
 */
public class ProfileMotifSearch extends MotifSearch{

    private float profile[][];

    public ProfileMotifSearch(String filename){
        super(filename);
    }

    private void formProfile(int[] motif, int L){
        int i, j;
        int counts[][];
        counts = findCounts(motif, L, t);
        for (i = 0; i < L; i++){
            for (j = 0; j < 4; j++){
                profile[i][j] = counts[i][j] / ((float) t);
            }
        }
    }

    private void formProfileWithException(int[] motif, int L, int selectedSequence){
        int i, j, exception;
        int counts[][];
        counts = findCounts(motif, L, t);
        for (i = 0; i < L; i++){
            exception = bases.indexOf(dna[selectedSequence].charAt(motif[selectedSequence] + i));
            counts[i][exception]--;
            for (j = 0; j < 4; j++){
                profile[i][j] = counts[i][j] / ((float) (t - 1));
            }
        }
    }

    private float probabilityOfLmer(int L, int DnaSequence, int position){
        int i;
        float prob = 1;
        for (i = 0; i < L; i++){
            prob *= profile[i][bases.indexOf(dna[DnaSequence].charAt(position + i))];
        }
        return prob;
    }

    private int mostProbableLmer(int L, int DnaSequence){
        int i, mostProbable = -1;
        float prob, maxProb = 0;
        for (i = 0; i < n - L + 1; i++){
            prob = probabilityOfLmer(L, DnaSequence, i);
            if (prob > maxProb){
                maxProb = prob;
                mostProbable = i;
            }
        }
        return mostProbable;
    }

    private void randomlySelectStartingPositions(int L){
        int i;
        bestMotif = new int[t];
        for (i = 0; i < t; i++){
            bestMotif[i] = (int) (Math.random() * (n - L + 1));
        }
    }

    public void greedyProfileMotifSearch(int L){
        int i, bestScore, score;
        profile = new float[L][4];
        randomlySelectStartingPositions(L);
        bestScore = 0;
        score = score(bestMotif, L, t);
        while (score > bestScore){
            bestScore = score;
            formProfile(bestMotif, L);
            for (i = 0; i < t; i++){
                bestMotif[i] = mostProbableLmer(L, i);
            }
            score = score(bestMotif, L, t);
        }
        produceSolutionFromMotif(L);
    }

    public void gibbsSampling(int L, int maxIteration){
        int count = 0, i, selectedSequence, startingPosition, bestScore = 0, score;
        int[] motif;
        float p[], sum, randomNumber;
        profile = new float[L][4];
        p = new float[n - L + 1];
        randomlySelectStartingPositions(L);
        motif = bestMotif.clone();
        while (count < maxIteration){
            selectedSequence = (int) (Math.random() * t);
            formProfileWithException(motif, L, selectedSequence);
            sum = 0;
            for (i = 0; i < n - L + 1; i++){
                p[i] = probabilityOfLmer(L, selectedSequence, i);
                sum += p[i];
            }
            /*Normalize probabilities*/
            if (sum != 0){
                for (i = 0; i < n - L + 1; i++){
                    p[i] /= sum;
                }
                randomNumber = (float) Math.random();
                sum = 0;
                for (i = 0; i < n - L + 1; i++){
                    if (randomNumber < sum + p[i]){
                        break;
                    }
                    else{
                        sum += p[i];
                    }
                }
                startingPosition = i;
            }
            else{
                startingPosition = 0;
            }
            motif[selectedSequence] = startingPosition;
            score = score(motif, L, t);
            if (score > bestScore){
                bestScore = score;
                bestMotif = motif.clone();
            }
            count++;
        }
        produceSolutionFromMotif(L);
    }
    
}
