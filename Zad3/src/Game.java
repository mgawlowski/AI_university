import java.util.*;

public class Game {

    //todo 1,2... aPLayer, bPlayer
    //todo linia/przekatna to jakis byt (lista, ktore przynaleza do siebie)

    private int boardSize;
    private int currentMove;
    private RandomGenerator rnd;

    private List<Field> takenFields;
    private List<Field> freeFields;

    private Player playerA;
    private Player playerB;

    public Game(int boardSize) {
        this.boardSize = boardSize;
        currentMove = 0;
        rnd = new RandomGenerator();
        playerA = new BruteForcePlayer(rnd, "BruteForceA", boardSize);
        playerB = new BruteForcePlayer(rnd, "BruteForce", boardSize);
        takenFields = new ArrayList<>();
        freeFields = new ArrayList<>();

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                freeFields.add(new Field(j, i));
            }
        }


    }

    public void run() {
        while (freeFields.size() != 0) {
            int fieldIndex = currentMove % 2 == 0 ? playerA.makeMove(takenFields, freeFields)
                    : playerB.makeMove(takenFields, freeFields);
            takenFields.add(freeFields.remove(fieldIndex));
            currentMove++;
        }
        printBoard();
    }

    private void printBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                boolean found = false;
                for (Field field : takenFields) {
                    if (field.equals(j, i)) {
                        System.out.print(field);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    System.out.print(".");
                }
                System.out.print("   ");
            }
            System.out.println();
            System.out.println();
        }
        System.out.println("Move " + currentMove + " (" + (currentMove % 2 == 0 ? playerA.getName() : playerB.getName()) + ")");
        System.out.println(playerA.getName() + " " + playerA.getPoints()
                + " : " + playerB.getPoints() + " " + playerB.getName());
        System.out.println("\n-------------------------\n");
    }
}
