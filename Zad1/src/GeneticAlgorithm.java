import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings({"WeakerAccess", "unused"})
public class GeneticAlgorithm {

    private RandomNumbers rnd = new RandomNumbers();

    private List<Individual> population = new ArrayList<>();
    private List<Integer> wholeSetOfGenes = new ArrayList<>();
    private HashMap<List<Integer>, Integer> fitnessCache = new HashMap<>();

    private int[][] distanceMatrix;
    private int[][] flowMatrix;
    private int genesQuantity;

    private int generationNumber = 0;
    private int cacheUsageCount = 0;
    private int FFENumber = 0; // Fitness Function Evaluation Number
    private int bestFoundFitness = Integer.MAX_VALUE;


    public GeneticAlgorithm() {

    }

    public void run(String dataFilename) {
        generationNumber = cacheUsageCount = FFENumber = 0;
        bestFoundFitness = Integer.MAX_VALUE;
        fitnessCache.clear();

        createPopAndLoadMatrices(Constants.MODULE_PATH + dataFilename);
        evaluatePopulation();

        BufferedWriter writer = null;
        String outputFilename = new SimpleDateFormat("yyyyMMddHHmmss'.txt'").format(new Date());
        try {
            writer = new BufferedWriter(new FileWriter(Constants.MODULE_PATH + outputFilename));
            writer.write(" FFENumber \t bestFitness \t avgFitness \t worstFitness \n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (FFENumber < Constants.FITNESS_EVALS) {
            if (Constants.SELECTION_TYPE == 0)
                tournamentSelection();
            else if (Constants.SELECTION_TYPE == 1)
                rouletteSelection();
            else if (Constants.SELECTION_TYPE == 2)
                optimizedRouletteSelection();
            else
                rankingSelection();

            crossover();
            mutation();
            evaluatePopulation();

            generationNumber++;
            printAndSaveToFile(writer);
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

    private void printPopulation() {
        int i = 0;
        for (Individual ind : population) {
            System.out.println("\t[" + i + "] - " + ind.toString() + " ( fit: " + ind.getFitness() + " )");
            i++;
        }
    }

    private void printStats() {
        System.out.println(bestFoundFitness + " \t" + generationNumber + " \t" + cacheUsageCount);
    }

    private void printAndSaveToFile(BufferedWriter writer) {
        int bestFitness = Integer.MAX_VALUE;
        int worstFitness = 0;
        int fitnessSum = 0;
        int currentFitness;
        for (Individual ind : population) {
            currentFitness = ind.getFitness();
            fitnessSum += currentFitness;
            if (currentFitness < bestFitness)
                bestFitness = currentFitness;
            if (currentFitness > worstFitness)
                worstFitness = currentFitness;
        }
        int avarageFitness = fitnessSum / population.size();
        System.out.println("Gen: " + generationNumber + "\t FFE: " + FFENumber
                + "\t < " + bestFitness + " | " + avarageFitness + " | " + worstFitness + " >"
                + " Cache hit count: " + cacheUsageCount);

        if (writer != null) {
            try {
                writer.write(FFENumber + "\t" + bestFitness + "\t"
                        + avarageFitness + "\t" + worstFitness + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createPopAndLoadMatrices(String dataFilename) {
        try {
            Scanner sc = new Scanner(new File(dataFilename));

            genesQuantity = sc.nextInt();
            population.clear();
            for (int i = 0; i < Constants.POPULATION_SIZE; i++) {
                population.add(new Individual(genesQuantity, rnd));
            }

            wholeSetOfGenes.clear();
            for (int i = 0; i < genesQuantity; i++) {
                wholeSetOfGenes.add(i);
            }

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

    private void evaluatePopulation() {
        for (Individual ind : population) {
            if (ind.hasGenotypeChanged())
                ind.setFitness(evaluate(ind));
        }
    }

    private int evaluate(Individual ind) {
        List<Integer> genesList = ind.getGenesList();

        if (Constants.USE_CACHE) {
            if (fitnessCache.containsKey(genesList)) {
                cacheUsageCount++;
                return fitnessCache.get(genesList);
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
        return fitness;
    }

    private void tournamentSelection() {
        List<Individual> newGeneration = new ArrayList<>();
        List<Integer> indexList = new ArrayList<>();
        int individualQuantity = population.size();
        int bestIndividualIndex = 0;
        int newIndex, bestFitness;

        for (int i = 0; i < individualQuantity; i++) {
            indexList.clear();

            for (int j = 0; j < Constants.TOURNAMENT_SIZE; j++) {
                do {
                    newIndex = rnd.random(individualQuantity);
                } while (indexList.contains(newIndex));
                indexList.add(newIndex);
            }

            bestFitness = Integer.MAX_VALUE;
            for (Integer index : indexList) {
                Individual individual = population.get(index);
                if (individual.getFitness() < bestFitness) {
                    bestFitness = individual.getFitness();
                    bestIndividualIndex = index;
                }
            }

            newGeneration.add(new Individual(population.get(bestIndividualIndex)));
        }
        population = newGeneration;
    }

    private void rouletteSelection() {
        List<Individual> newGeneration = new ArrayList<>();

        float fitnessFactorSum = 0;
        for (Individual ind : population) {
            fitnessFactorSum += 1 / ((float) ind.getFitness() + 1);
        }

        float drawnNumber;
        int index;
        for (int i = 0; i < population.size(); i++) {
            drawnNumber = rnd.random() * fitnessFactorSum;
            for (index = 0; drawnNumber > 0; index++) {
                drawnNumber -= 1 / ((float) population.get(index).getFitness() + 1);
            }
            newGeneration.add(new Individual(population.get(--index)));
        }
        population = newGeneration;
    }

    private void optimizedRouletteSelection() {
        List<Individual> newGeneration = new ArrayList<>();

        int bestFitness = Integer.MAX_VALUE;
        for (Individual ind : population) {
            if (ind.getFitness() < bestFitness)
                bestFitness = ind.getFitness();
        }

        float fitnessFactorSum = 0;
        for (Individual ind : population) {
            fitnessFactorSum += 1 / ((float) ind.getFitness() - bestFitness + 1);
        }

        float drawnNumber;
        int index;
        for (int i = 0; i < population.size(); i++) {
            drawnNumber = rnd.random() * fitnessFactorSum;
            for (index = 0; drawnNumber > 0; index++) {
                drawnNumber -= 1 / ((float) population.get(index).getFitness() - bestFitness + 1);
            }
            newGeneration.add(new Individual(population.get(--index)));
        }
        population = newGeneration;
    }

    private void rankingSelection() {
        List<Individual> newGeneration = new ArrayList<>();

        int popSize = population.size();
        int ranksSum = (popSize + 1) * popSize / 2;

        Collections.sort(population);

        int drawnNumber, index;
        for (int i = 0; i < popSize; i++) {
            drawnNumber = rnd.random(ranksSum) + 1;

            index = 0;
            while (drawnNumber > 0) {
                drawnNumber -= ++index;
            }

            newGeneration.add(new Individual(population.get(--index)));
        }
        population = newGeneration;
    }

    private void crossover() {
        Individual firstIndividual, secondIndividual;
        int crossPoint;
        for (int i = 0; i + 1 < population.size(); i += 2) {
            if (Constants.CROSS_PROB > rnd.random()) {
                crossPoint = rnd.random(genesQuantity - 1) + 1;

                firstIndividual = population.get(i);
                secondIndividual = population.get(i + 1);
                firstIndividual.cross(secondIndividual, crossPoint);

                if (!firstIndividual.getGenesList().containsAll(wholeSetOfGenes))
                    firstIndividual.repair(rnd);
                if (!secondIndividual.getGenesList().containsAll(wholeSetOfGenes))
                    secondIndividual.repair(rnd);
            }
        }
    }

    private void mutation() {
        int indexToSwap;
        for (Individual ind : population) {
            for (int i = 0; i < genesQuantity; i++) {
                if (Constants.MUTAT_PROB > rnd.random()) {
                    do {
                        indexToSwap = rnd.random(genesQuantity);
                    } while (i == indexToSwap);
                    ind.swapGenes(i, indexToSwap);
                }
            }
        }
    }
}

