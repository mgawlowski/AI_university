import java.util.List;
import java.util.Stack;

public abstract class Player {

    private RandomGenerator rnd;

    private String name;
    private char indicator;
    private int points = 0;
    private Stack<Integer> indexStack;


    public Player(String name, char indicator, RandomGenerator rnd) {
        this.name = name;
        this.indicator = indicator;
        this.rnd = rnd;
        indexStack = new Stack<>();
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
            if (currentPoints != 0) {
                indexStack.push(i);
            }
            pointsGained += currentPoints;
        }
        while (!indexStack.empty()) {
            scoringLines.remove((int) indexStack.pop());
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
