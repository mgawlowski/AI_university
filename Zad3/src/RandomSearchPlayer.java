import java.util.ArrayList;

public class RandomSearchPlayer extends Player {

    public RandomSearchPlayer(String name, char indicator, RandomGenerator rnd) {
        super(name, indicator, rnd);
    }

    @Override
    public int chooseMove(ArrayList<Field> freeFields, ArrayList<Line> scoringLines) {
        return getRnd().random(freeFields.size());
    }
}
