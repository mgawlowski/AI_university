import java.util.List;

public class RandomSearchPlayer extends Player {

    public RandomSearchPlayer(String name, char indicator, RandomGenerator rnd) {
        super(name, indicator, rnd);
    }

    @Override
    public int chooseMove(List<Field> freeFields, List<Line> scoringLines) {
        return getRnd().random(freeFields.size());
    }
}
