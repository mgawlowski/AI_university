import java.util.*;

public class QueenCSP {

    private ProblemObject problemObj;
    private int boardSize;
    private RandomGenerator rnd = new RandomGenerator();
    private int iterations = 0;
    private int reverts = 0;

    public QueenCSP() {

    }

    //method: 1 - backtracking; 2 - forward-checking
    //heuristic: 1 - random; 2 - lowest cardinality
    public void run(int boardSize, int method, int heuristic) {
        this.boardSize = boardSize;
        problemObj = new ProblemObject();
        populateProblemObject();
        iterations = 0;
        reverts = 0;
        if(method == 1){
            backtracking(heuristic);
        }
        else{
            forwardChecking(heuristic);
        }
        printSolution();
    }

    private void populateProblemObject() {
        for (int i = 1; i <= boardSize; i++) {
            List<Integer> domain = new ArrayList<>();
            for (int j = 1; j <= boardSize; j++) {
                domain.add(j);
            }
            problemObj.addVariable("col" + i, domain);
        }
    }

    private void forwardChecking(int heuristic) {
        while (!problemObj.getVariablesIndToGo().isEmpty()) {
            iterations++;
            int chosenIndex;
            if(heuristic == 1) {
                chosenIndex = getRandomVariableIndex();
            }
            else {
                chosenIndex = getVariableIndexWithLowestCardinality();
            }
            int chosenValue = getRandomValue(chosenIndex);
            problemObj.chooseValue(chosenIndex, chosenValue);

            int difference = 1;
            for (int i = chosenIndex + 1; i < problemObj.getVariablesNumber(); i++) {
                problemObj.deleteValue(i, chosenValue - difference);
                problemObj.deleteValue(i, chosenValue);
                problemObj.deleteValue(i, chosenValue + difference);
                difference++;
            }
            difference = 1;
            for (int i = chosenIndex - 1; i >= 0; i--) {
                problemObj.deleteValue(i, chosenValue - difference);
                problemObj.deleteValue(i, chosenValue);
                problemObj.deleteValue(i, chosenValue + difference);
                difference++;
            }

            while (problemObj.isAnyDomainEmpty()) {
                problemObj.revert();
                reverts++;
            }
        }
    }

    private void backtracking(int heuristic){

        while (!problemObj.getVariablesIndToGo().isEmpty()) {
            iterations++;
            int chosenIndex = getRandomVariableIndex();
            int chosenValue = getRandomValue(chosenIndex);
            problemObj.chooseValue(chosenIndex, chosenValue);

            while (problemObj.isAnyDomainEmpty() || !areConstraintsFulfilled()){
                problemObj.revert();
                reverts++;
            }
        }
    }

    private boolean areConstraintsFulfilled() {
        Map<Integer, Integer> chosenValues = problemObj.getChosenValues();
        for (int i = 0; i < boardSize; i++) {
            if(chosenValues.get(i) != null){
                int difference = 1;
                for (int j = i + 1; j < problemObj.getVariablesNumber(); j++) {
                    if(chosenValues.get(j) != null){
                        int firstValue = chosenValues.get(i);
                        int secondValue = chosenValues.get(j);
                        if(firstValue == secondValue - difference ||
                                firstValue == secondValue ||
                                firstValue == secondValue + difference){
                            return false;
                        }
                    }
                    difference++;
                }
            }
        }
        return true;
    }

    private int getRandomVariableIndex() {
        Set<Integer> varsIndexes = problemObj.getVariablesIndToGo();
        int draw = rnd.random(varsIndexes.size());
        int i = 0;
        for (Integer integer : varsIndexes) {
            if (i == draw) {
                return integer;
            }
            i++;
        }
        return -1;
    }

    private int getVariableIndexWithLowestCardinality(){
        Set<Integer> varsIndexes = problemObj.getVariablesIndToGo();
        List<Integer> indexesToConsider = new ArrayList<>();
        int minDomainCount = Integer.MAX_VALUE;
        for (Integer i : varsIndexes) {
            int domainSize = problemObj.getDomain(i).size();
            if (domainSize < minDomainCount){
                minDomainCount = domainSize;
                indexesToConsider.clear();
                indexesToConsider.add(i);
            }
            else if (domainSize == minDomainCount){
                indexesToConsider.add(i);
            }
        }
        return indexesToConsider.get(rnd.random(indexesToConsider.size()));
    }

    private int getRandomValue(int chosenIndex) {
        List<Integer> values = problemObj.getDomain(chosenIndex);
        return values.get(rnd.random(values.size()));
    }

    private void printSolution() {
        System.out.println();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (problemObj.getChosenValue(j) == i + 1) {
                    System.out.print("X   ");
                } else {
                    System.out.print(".   ");
                }
            }
            System.out.println();
            System.out.println();
        }
        System.out.println("Iterations: " + iterations + " Reverts: " + reverts);
    }

    public void runRandom(int boardSize) {
        List<Integer> queenPosList = new ArrayList<>();
        this.boardSize = boardSize;
        for (int i = 0; i < boardSize; i++) {
            queenPosList.add(i);
        }

        iterations = 0;
        do {
            Collections.shuffle(queenPosList, rnd.getRandomEngine());
            iterations++;
        } while (!checkIfValidRandom(queenPosList));

        for (int i = 0; i < boardSize; i++) {
            System.out.print(queenPosList.get(i) + ", ");
        }
        System.out.print(" (iterations: " + iterations + ")\n");
    }

    private boolean checkIfValidRandom(List<Integer> queenPosList) {
        int firstQueenPos, secondQueenPos, diagonalDifference;

        for (int i = 0; i < boardSize; i++) {
            firstQueenPos = queenPosList.get(i);
            diagonalDifference = 1;
            for (int j = i + 1; j < boardSize; j++) {
                secondQueenPos = queenPosList.get(j);
                if (firstQueenPos - diagonalDifference == secondQueenPos
                        || firstQueenPos + diagonalDifference == secondQueenPos
                        || firstQueenPos == secondQueenPos) {
                    return false;
                }
                diagonalDifference++;
            }
        }

        return true;
    }

}
