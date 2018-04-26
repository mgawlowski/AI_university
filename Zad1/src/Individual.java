import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class Individual implements Comparable<Individual> {

    private List<Integer> genesList = new ArrayList<>();
    private int genesQuantity;
    private boolean genotypeChanged;
    private int fitness = 0;

    public Individual(int genesQuantity, RandomNumbers rnd) {
        this.genesQuantity = genesQuantity;
        randomize(rnd);
        genotypeChanged = true;
    }

    public Individual(Individual otherInd) {
        genesList.addAll(otherInd.genesList);
        fitness = otherInd.fitness;
        genesQuantity = otherInd.genesQuantity;
        genotypeChanged = otherInd.genotypeChanged;
    }

    public List<Integer> getGenesList() {
        return genesList;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
        genotypeChanged = false;
    }

    public boolean hasGenotypeChanged() {
        return genotypeChanged;
    }

    public void randomize(RandomNumbers rnd) {
        List<Integer> genesToAdd = new ArrayList<>();
        for (int i = 0; i < genesQuantity; i++) {
            genesToAdd.add(i);
        }
        genesList.clear();
        for (int i = 0; i < genesQuantity; i++) {
            genesList.add(genesToAdd.remove(rnd.random(genesQuantity - i)));
        }
    }

    public void swapGenes(int firstIndex, int secondIndex) {
        int temp = genesList.get(firstIndex);
        genesList.set(firstIndex, genesList.get(secondIndex));
        genesList.set(secondIndex, temp);
        genotypeChanged = true;
    }

    public void cross(Individual ind, int crossPoint) {
        int temp;
        for (int i = crossPoint; i < genesList.size(); i++) {
            temp = genesList.get(i);
            genesList.set(i, ind.genesList.get(i));
            ind.genesList.set(i, temp);
        }
        genotypeChanged = true;
        ind.genotypeChanged = true;
    }

    public void repair(RandomNumbers rnd) {
        for (int i = 0; i < genesList.size(); i++) {
            if (!genesList.contains(i)) {
                genesList.set(getRandomIndexOfDuplicate(rnd), i);
            }
        }
    }

    private int getRandomIndexOfDuplicate(RandomNumbers rnd) {
        int genesQuantity = genesList.size();
        int currNumber;

        for (int i = 0; i < genesQuantity; i++) {
            currNumber = genesList.get(i);
            for (int j = i + 1; j < genesQuantity; j++) {
                if (currNumber == genesList.get(j)) {
                    // i and j are indexes of duplicates
                    if (rnd.random(2) == 0)
                        return i;
                    return j;
                }
            }
        }

        return -1;
    }

    public String toString() {
        StringBuilder output = new StringBuilder();
        for (int i : genesList) {
            output.append(i).append(", ");
        }
        return output.toString();
    }

    @Override
    public int compareTo(Individual ind) {
        return ind.fitness - fitness;
    }
}
