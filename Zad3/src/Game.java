import java.util.ArrayList;

public class Game {

    private RandomGenerator rnd;
    private Player playerA;
    private Player playerB;
    private Player currentPlayer;

    private int boardSize;
    private ArrayList<Field> takenFields;
    private ArrayList<Field> freeFields;
    private ArrayList<Line> scoringLines;
    private int currentMove;


    public Game(int boardSize) {
        this.boardSize = boardSize;
        takenFields = new ArrayList<>();
        freeFields = new ArrayList<>();
        scoringLines = new ArrayList<>();
        rnd = new RandomGenerator();
    }

    public void runMatch(int playerAType, int playerBType) {
        playerA = buildPlayer(playerAType, Constants.PLAYER_A_INDICATOR, rnd);
        playerB = buildPlayer(playerBType, Constants.PLAYER_B_INDICATOR, rnd);

        match(playerA);
    }

    public void runGame(int playerAType, int playerBType) {
        playerA = buildPlayer(playerAType, Constants.PLAYER_A_INDICATOR, rnd);
        playerB = buildPlayer(playerBType, Constants.PLAYER_B_INDICATOR, rnd);
        Player winner;
        int playerAWinsCount = 0;
        int playerBWinsCount = 0;

        for (int i = 0; i < 6; i++) {
            winner = match(i < 3 ? playerA : playerB);
            if (winner == playerA) {
                playerAWinsCount++;
            }
            if (winner == playerB) {
                playerBWinsCount++;
            }
        }
        System.out.println("GAME SCORE - " + playerA.getName() + "(A) " + playerAWinsCount
                + " : " + playerBWinsCount + " " + playerB.getName() + "(B)");
    }

    private Player match(Player startingPlayer) {
        initializeBoard();
        playerA.resetPoints();
        playerB.resetPoints();
        currentMove = 1;
        currentPlayer = startingPlayer;
        while (freeFields.size() != 0) {
            currentPlayer.move(takenFields, freeFields, scoringLines);

//            printStateAndMove();

            switchPlayers();
            currentMove++;
        }
        printScore();

        if (playerA.getPoints() > playerB.getPoints()) {
            return playerA;
        } else if (playerA.getPoints() < playerB.getPoints()) {
            return playerB;
        }
        return null;
    }

    private void switchPlayers() {
        if (currentPlayer == playerA) {
            currentPlayer = playerB;
        } else {
            currentPlayer = playerA;
        }
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
            newLine = new Line(Constants.HORIZONTAL_LINE_NAME + row);
            for (int col = 0; col < boardSize; col++) {
                newLine.add(new Field(row, col));
            }
            scoringLines.add(newLine);
        }

        //populating vertical lines
        for (int col = 0; col < boardSize; col++) {
            newLine = new Line(Constants.VERTICAL_LINE_NAME + col);
            for (int row = 0; row < boardSize; row++) {
                newLine.add(new Field(row, col));
            }
            scoringLines.add(newLine);
        }

        //populating diagonals (left down)
        for (int i = 0; i < boardSize - 1; i++) {
            newLine = new Line(Constants.DIAGONAL_DOWN_NAME + i + Constants.LEFT_SIDE);
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
            newLine = new Line(Constants.DIAGONAL_UP_NAME + i + Constants.LEFT_SIDE);
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
            newLine = new Line(Constants.DIAGONAL_DOWN_NAME + i + Constants.RIGHT_SIDE);
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
            newLine = new Line(Constants.DIAGONAL_UP_NAME + i + Constants.RIGHT_SIDE);
            int row = i;
            int col = boardSize - 1;
            while (row >= 0 && col >= 0) {
                newLine.add(new Field(row, col));
                row--;
                col--;
            }
            scoringLines.add(newLine);
        }
    }

    private Player buildPlayer(int playerType, char indicator, RandomGenerator rnd) {
        if (playerType == 0) {
            return new RandomSearchPlayer("RandomSearch", indicator, rnd);
        } else if (playerType == 1) {
            return new BruteForcePlayer("BruteForce", indicator, rnd);
        } else if (playerType == 2) {
            return new MinMaxPlayer("MinMax5", 5, indicator, rnd);
        } else if (playerType == 3) {
            return new MinMaxPlayer("MinMax10", 10, indicator, rnd);
        } else if (playerType == 4) {
            return new MinMaxPlayer("MinMax15", 15, indicator, rnd);
        } else {
            return null;
        }
    }

    private void printStateAndMove() {
        System.out.println("Move " + currentMove + " (" + currentPlayer + ")");
        printScore();
        System.out.println();

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
                System.out.print(" " + fieldChar);
            }
            System.out.println();
        }
        System.out.println("\n-------------------------\n");
    }

    private void printScore() {
        System.out.println(playerA.getName() + "(A) " + playerA.getPoints()
                + " : " + playerB.getPoints() + " " + playerB.getName() + "(B)");
    }
}
