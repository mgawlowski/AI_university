import java.util.ArrayList;
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

    public void resetPoints() {
        points = 0;
    }

    public void move(ArrayList<Field> takenFields, ArrayList<Field> freeFields, ArrayList<Line> scoringLines) {
        Field chosenField = freeFields.remove(chooseMove(freeFields, scoringLines));
        chosenField.setTakenBy(indicator);

        points += applyMove(chosenField, scoringLines);

        takenFields.add(chosenField);
    }

    protected int applyMove(Field chosenField, ArrayList<Line> scoringLines) {
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
        return pointsGained;
    }

    protected int evaluateMove(Field move, ArrayList<Line> scoringLines) {
        int pointsToGain = 0;
        for (Line line : scoringLines) {
            pointsToGain += line.movePoints(move);
        }
        return pointsToGain;
    }

    public String toString() {
        return name + " - " + indicator;
    }

    public abstract int chooseMove(ArrayList<Field> freeFields, ArrayList<Line> scoringLines);
}
