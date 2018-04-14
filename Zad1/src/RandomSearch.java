import javafx.util.Pair;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class RandomSearch {

    private RandomNumbers rnd = new RandomNumbers();
    private List<Pair<Integer, Integer>> possibleSwaps = new ArrayList<>();
    private HashMap<List<Integer>, Integer> fitnessCache = new HashMap<>();

    private Individual individual;
    private int genesQuantity;
    private int[][] distanceMatrix;
    private int[][] flowMatrix;

    private int FFENumber = 0;
    private int cacheUsageCount = 0;
    private int bestFoundFitness = Integer.MAX_VALUE;

    public RandomSearch() {

    }

    public void run(String dataFilename, int FFEStopCondition, boolean useGreedy, boolean useCache) {
        fitnessCache.clear();
        FFENumber = cacheUsageCount = 0;
        bestFoundFitness = Integer.MAX_VALUE;
        createIndAndLoadMatrices(Constants.MODULE_PATH + dataFilename);

        BufferedWriter writer = null;
        String outputFilename = new SimpleDateFormat("yyyyMMddHHmmss'.txt'").format(new Date());
        try {
            writer = new BufferedWriter(new FileWriter(Constants.MODULE_PATH + outputFilename));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (useGreedy)
            populatePossibleSwaps();

        while (FFENumber < FFEStopCondition) {
            individual.randomize(rnd);
            evaluate(individual, useCache);

            if (useGreedy)
                greedyAlgorithm(rnd, useCache);
        }

        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        printStats();
    }

    private void printStats() {
        System.out.println(bestFoundFitness + " \t" + cacheUsageCount);
    }

    private void populatePossibleSwaps() {
        possibleSwaps.clear();
        for (int i = 0; i < genesQuantity; i++) {
            for (int j = i + 1; j < genesQuantity; j++) {
                possibleSwaps.add(new Pair<>(i, j));
            }
        }
    }

    private void greedyAlgorithm(RandomNumbers rnd, boolean useCache) {
        List<Integer> swapsIndexList = new ArrayList<>();
        for (int i = 0; i < possibleSwaps.size(); i++) {
            swapsIndexList.add(i);
        }

        Individual newIndiv;
        int index;
        Pair<Integer, Integer> swap;
        while (!swapsIndexList.isEmpty()) {
            newIndiv = new Individual(individual);
            index = rnd.random(swapsIndexList.size());
            swap = possibleSwaps.get(swapsIndexList.get(index));

            newIndiv.swapGenes(swap.getKey(), swap.getValue());
            evaluate(newIndiv, useCache);

            if (newIndiv.getFitness() < individual.getFitness()) {
                individual = new Individual(newIndiv);
                greedyAlgorithm(rnd, useCache);
                return;
            }
            swapsIndexList.remove(index);
        }
    }

    private void printAndSaveToFile(BufferedWriter writer) {
        System.out.println("\t FFE: " + FFENumber + "\t < " + individual.getFitness()
                + " > cache hits: " + cacheUsageCount);

        if (writer != null) {
            try {
                writer.write(FFENumber + "\t" + individual.getFitness() + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void evaluate(Individual ind, boolean useCache) {
        List<Integer> genesList = ind.getGenesList();

        if (useCache) {
            if (fitnessCache.containsKey(genesList)) {
                cacheUsageCount++;
                ind.setFitness(fitnessCache.get(genesList));
                return;
            }
        }

        int fitness = 0;
        for (int i = 0; i < genesList.size(); i++) {
            for (int j = 0; j < genesList.size(); j++) {
                fitness += (flowMatrix[i][j] * distanceMatrix[genesList.get(i)][genesList.get(j)]);
            }
        }

        if (fitness < bestFoundFitness) {
            bestFoundFitness = fitness;
        }

        fitnessCache.put(genesList, fitness);
        FFENumber++;
        ind.setFitness(fitness);
    }

    private void createIndAndLoadMatrices(String dataFilename) {
        try {
            Scanner sc = new Scanner(new File(dataFilename));

            genesQuantity = sc.nextInt();
            individual = new Individual(genesQuantity, rnd);

            flowMatrix = new int[genesQuantity][genesQuantity];
            for (int i = 0; i < genesQuantity; i++) {
                for (int j = 0; j < genesQuantity; j++) {
                    flowMatrix[i][j] = sc.nextInt();
                }
            }

            distanceMatrix = new int[genesQuantity][genesQuantity];
            for (int i = 0; i < genesQuantity; i++) {
                for (int j = 0; j < genesQuantity; j++) {
                    distanceMatrix[i][j] = sc.nextInt();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
