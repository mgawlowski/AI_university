import java.util.*;

@SuppressWarnings({"Duplicates", "unused", "WeakerAccess"})
public class LatinSquareCSP {

    private ProblemObject problemObj;
    private int boardSize;
    private RandomGenerator rnd = new RandomGenerator();
    private int iterations = 0;
    private int reverts = 0;

    public LatinSquareCSP() {

    }

    //method: 1 - backtracking; 2 - forward-checking
    //heuristic: 1 - random; 2 - lowest cardinality
    public void run(int boardSize, int method, int heuristic) {
        this.boardSize = boardSize;
        problemObj = new ProblemObject();
        populateProblemObject();
        iterations = 0;
        reverts = 0;
        if (method == 1) {
            backtracking(heuristic);
        } else {
            forwardChecking(heuristic);
        }
//        printSolution();
        System.out.println("Iterations: " + iterations + " Reverts: " + reverts);
    }

    private void populateProblemObject() {
        for (int i = 1; i <= boardSize; i++) {
            for (int j = 1; j <= boardSize; j++) {
                List<Integer> domain = new ArrayList<>();
                for (int k = 1; k <= boardSize; k++) {
                    domain.add(k);
                }
                problemObj.addVariable("field" + i + "." + j, domain);
            }
        }
    }

    private void forwardChecking(int heuristic) {
        while (!problemObj.getVariablesIndToGo().isEmpty()) {
            iterations++;
            int chosenIndex;
            if (heuristic == 1) {
                chosenIndex = getRandomVariableIndex();
            } else {
                chosenIndex = getVariableIndexWithLowestCardinality();
            }
            int chosenValue = getRandomValue(chosenIndex);
            problemObj.chooseValue(chosenIndex, chosenValue);

            int row, col;
            row = chosenIndex / boardSize;
            col = chosenIndex % boardSize;
            for (int i = col + 1; i < boardSize; i++) {
                problemObj.deleteValue(i + row * boardSize, chosenValue);
            }
            for (int i = col - 1; i >= 0; i--) {
                problemObj.deleteValue(i + row * boardSize, chosenValue);
            }
            for (int j = row + 1; j < boardSize; j++) {
                problemObj.deleteValue(col + j * boardSize, chosenValue);
            }
            for (int j = row - 1; j >= 0; j--) {
                problemObj.deleteValue(col + j * boardSize, chosenValue);
            }

            while (problemObj.isAnyDomainEmpty()) {
                problemObj.revert();
                reverts++;
            }
        }
    }

    private void backtracking(int heuristic) {

        while (!problemObj.getVariablesIndToGo().isEmpty()) {
            iterations++;
            int chosenIndex;
            if (heuristic == 1) {
                chosenIndex = getRandomVariableIndex();
            } else {
                chosenIndex = getVariableIndexWithLowestCardinality();
            }
            int chosenValue = getRandomValue(chosenIndex);
            problemObj.chooseValue(chosenIndex, chosenValue);

            while (problemObj.isAnyDomainEmpty() || !areConstraintsFulfilled()) {
                problemObj.revert();
                reverts++;
            }
        }
    }

    private boolean areConstraintsFulfilled() {
        Map<Integer, Integer> chosenValues = problemObj.getChosenValues();
        for (int i = 0; i < boardSize * boardSize; i++) {
            if (chosenValues.get(i) != null) {
                for (int j = i + 1; j < boardSize * boardSize; j++) {
                    if (chosenValues.get(j) != null) {
                        int firstValue = chosenValues.get(i);
                        int secondValue = chosenValues.get(j);
                        if (firstValue == secondValue) {
                            if (i / boardSize == j / boardSize ||
                                    i % boardSize == j % boardSize) {
                                return false;
                            }
                        }
                    }
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

    private int getVariableIndexWithLowestCardinality() {
        Set<Integer> varsIndexes = problemObj.getVariablesIndToGo();
        List<Integer> indexesToConsider = new ArrayList<>();
        int minDomainCount = Integer.MAX_VALUE;
        for (Integer i : varsIndexes) {
            int domainSize = problemObj.getDomain(i).size();
            if (domainSize < minDomainCount) {
                minDomainCount = domainSize;
                indexesToConsider.clear();
                indexesToConsider.add(i);
            } else if (domainSize == minDomainCount) {
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
                System.out.print(problemObj.getChosenValue(j + i * boardSize) + "   ");
            }
            System.out.println();
            System.out.println();
        }
    }

    public void runRandom(int boardSize) {
        this.boardSize = boardSize;
        int[][] board;
        int iterations = 0;
        do {
            board = new int[boardSize][boardSize];
            List<Integer> numbersToAttach = new ArrayList<>();
            int indexOfNumber;
            for (int row = 0; row < boardSize; row++) {
                for (int i = 0; i < boardSize; i++) {
                    numbersToAttach.add(i);
                }
                for (int col = 0; col < boardSize; col++) {
                    indexOfNumber = rnd.random(numbersToAttach.size());
                    board[row][col] = numbersToAttach.remove(indexOfNumber);
                }
            }
            iterations++;
        } while (!isRandomValid(board));

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                System.out.print("   " + board[row][col]);
            }
            System.out.print("\n\n");
        }
        System.out.println("Iterations: " + iterations);
    }

    private boolean isRandomValid(int[][] board) {
        List<Integer> currentLineNumbers = new ArrayList<>();

        for (int col = 0; col < boardSize; col++) {
            currentLineNumbers.clear();
            for (int row = 0; row < boardSize; row++) {
                if (currentLineNumbers.contains(board[row][col])) {
                    return false;
                }
                currentLineNumbers.add(board[row][col]);
            }
        }
        return true;
    }
}
