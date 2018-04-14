import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QueenCSP {

    //todo backtracking, forwardchecking
    private List<Integer> queenPosList = new ArrayList<>();
    private int boardSize = 0;
    private int iterations = 0;

    public QueenCSP() {

    }

    public void runRandom(int boardSize) {
        queenPosList.clear();
        this.boardSize = boardSize;
        for (int i = 0; i < boardSize; i++) {
            queenPosList.add(i);
        }

        iterations = 0;
        do {
            Collections.shuffle(queenPosList);
            iterations++;
        } while (!checkIfValid());

        printResult();
    }

    private void printResult(){
        for (int i = 0; i < boardSize; i++) {
            System.out.print(queenPosList.get(i) + ", ");
        }
        System.out.print(" (iterations: " + iterations + ")\n");
    }

    private boolean checkIfValid() {
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
