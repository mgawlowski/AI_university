import java.util.List;

public abstract class Player {

    private RandomGenerator rnd;
    private int boardSize;

    private String name;
    private char indicator;
    private int points = 0;


    public Player(String name, char indicator, int boardSize, RandomGenerator rnd) {
        this.name = name;
        this.indicator = indicator;
        this.boardSize = boardSize;
        this.rnd = rnd;
    }

    public RandomGenerator getRnd() {
        return rnd;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    abstract int chooseMove(List<Field> freeFields, List<Line> scoringLines);

    public void move(List<Field> takenFields, List<Field> freeFields, List<Line> scoringLines) {
        Field chosenField = freeFields.remove(chooseMove(freeFields, scoringLines));
        chosenField.setTakenBy(indicator);

        int pointsGained = 0;
        int currentPoints;
        for (int i = 0; i < scoringLines.size(); i++) {
            currentPoints = scoringLines.get(i).makeMove(chosenField);
            if(currentPoints != 0){
                scoringLines.remove(i);
            }
            pointsGained += currentPoints;
        }
        points += pointsGained;

        takenFields.add(chosenField);
    }

    public int evaluateMove(Field move, List<Line> scoringLines) {
        int pointsToGain = 0;
        for (Line line : scoringLines) {
            pointsToGain += line.movePoints(move);
        }
        return pointsToGain;
    }

    public String toString() {
        return name + " - " + indicator;
    }
}
