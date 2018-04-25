import java.util.*;

public class Game {

    private static final char PLAYER_A_INDICATOR = 'X';
    private static final char PLAYER_B_INDICATOR = 'O';
    private static final String HORIZONTAL_LINE_NAME = "horizontal-";
    private static final String VERTICAL_LINE_NAME = "vertical-";
    private static final String DIAGONAL_UP_NAME = "diagonal(up)-";
    private static final String DIAGONAL_DOWN_NAME = "diagonal(down)-";
    private static final String LEFT_SIDE = "L";
    private static final String RIGHT_SIDE = "R";


    private RandomGenerator rnd; //todo seed
    private Player playerA; //todo think about attaching players at Main
    private Player playerB;

    private int boardSize;
    private List<Field> takenFields;
    private List<Field> freeFields;
    private List<Line> scoringLines;
    private int currentMove;


    public Game(int boardSize) {
        this.boardSize = boardSize;
        takenFields = new ArrayList<>();
        freeFields = new ArrayList<>();
        scoringLines = new ArrayList<>();
        rnd = new RandomGenerator();
    }

    public void run(int playerAType, int playerBType) {
        initializeBoard();
        initializePlayers(playerAType, playerBType);

        currentMove = 1;
        while (freeFields.size() != 0) {
            if (currentMove % 2 == 0) {
                playerA.move(takenFields, freeFields, scoringLines);
            } else {
                playerB.move(takenFields, freeFields, scoringLines);
            }
            printBoard();
            currentMove++;
        }
//        printBoard();
    }

    @SuppressWarnings("Duplicates")
    private void initializeBoard() {
        takenFields.clear();
        freeFields.clear();
        scoringLines.clear();
        Line newLine;

        //populating free fields
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                freeFields.add(new Field(i, j));
            }
        }

        //populating horizontal lines
        for (int row = 0; row < boardSize; row++) {
            newLine = new Line(HORIZONTAL_LINE_NAME + row);
            for (int col = 0; col < boardSize; col++) {
                newLine.add(new Field(row, col));
            }
            scoringLines.add(newLine);
        }

        //populating vertical lines
        for (int col = 0; col < boardSize; col++) {
            newLine = new Line(VERTICAL_LINE_NAME + col);
            for (int row = 0; row < boardSize; row++) {
                newLine.add(new Field(row, col));
            }
            scoringLines.add(newLine);
        }

        //populating diagonals (left down)
        for (int i = 0; i < boardSize - 1; i++) {
            newLine = new Line(DIAGONAL_DOWN_NAME + i + LEFT_SIDE);
            int row = i;
            int col = 0;
            while (row < boardSize && col < boardSize) {
                newLine.add(new Field(row, col));
                row++;
                col++;
            }
            scoringLines.add(newLine);
        }

        //populating diagonals (left up)
        for (int i = 1; i < boardSize; i++) {
            newLine = new Line(DIAGONAL_UP_NAME + i + LEFT_SIDE);
            int row = i;
            int col = 0;
            while (row >= 0 && col < boardSize) {
                newLine.add(new Field(row, col));
                row--;
                col++;
            }
            scoringLines.add(newLine);
        }

        //populating diagonals (right down)
        for (int i = 1; i < boardSize - 1; i++) {
            newLine = new Line(DIAGONAL_DOWN_NAME + i + RIGHT_SIDE);
            int row = i;
            int col = boardSize - 1;
            while (row < boardSize && col >= 0) {
                newLine.add(new Field(row, col));
                row++;
                col--;
            }
            scoringLines.add(newLine);
        }

        //populating diagonals (right up)
        for (int i = 1; i < boardSize - 1; i++) {
            newLine = new Line(DIAGONAL_UP_NAME + i + RIGHT_SIDE);
            int row = i;
            int col = boardSize - 1;
            while (row >= 0 && col >= 0) {
                newLine.add(new Field(row, col));
                row--;
                col--;
            }
            scoringLines.add(newLine);
        }

        //todo del
//        for (Line l: scoringLines) {
//            System.out.println(l);
//        }
    }

    private void initializePlayers(int playerAType, int playerBType) {
        if (playerAType == 0) {
            playerA = new RandomSearchPlayer("RandomSearchA", PLAYER_A_INDICATOR, boardSize, rnd);
        } else {
            playerA = new BruteForcePlayer("BruteForceA", PLAYER_A_INDICATOR, boardSize, rnd);
        }

        if (playerBType == 0) {
            playerB = new RandomSearchPlayer("RandomSearchB", PLAYER_B_INDICATOR, boardSize, rnd);
        } else {
            playerB = new BruteForcePlayer("BruteForceB", PLAYER_B_INDICATOR, boardSize, rnd);
        }
    }

    private void printBoard() {
        char fieldChar;
        boolean found;
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                fieldChar = '.';
                found = false;
                for (int i = 0; i < takenFields.size() && !found; i++) {
                    if (takenFields.get(i).equals(row, col)) {
                        fieldChar = takenFields.get(i).getTakenBy();
                        found = true;
                    }
                }
                System.out.print("   " + fieldChar);
            }
            System.out.print("\n\n");
        }
        System.out.println("Move " + currentMove + " (" + (currentMove % 2 == 0 ? playerA : playerB) + ")");
        System.out.println(playerA.getName() + " " + playerA.getPoints()
                + " : " + playerB.getPoints() + " " + playerB.getName());
        System.out.println("\n-------------------------\n");
    }
}
