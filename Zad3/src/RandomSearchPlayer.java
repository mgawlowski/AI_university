import java.util.List;

public class RandomSearchPlayer extends Player {

    public RandomSearchPlayer(RandomGenerator rnd, String name, int boardSize) {
        super(rnd, name, boardSize);
    }

    @Override
    int makeMove(List<Field> takenFields, List<Field> freeFields) {
        int index = rnd.random(freeFields.size());
        //todo owner
        freeFields.get(index).setOwner('X');
        addPoints(evaluateMove(freeFields.get(index), takenFields));
        return index;
    }
}
