import java.util.List;

public class BruteForcePlayer extends Player {

    public BruteForcePlayer(RandomGenerator rnd, String name, int boardSize) {
        super(rnd, name, boardSize);
    }

    @Override
    int makeMove(List<Field> takenFields, List<Field> freeFields) {
        int point = 0;
        //todo ogarnac shit
        int indexToTake = -1;
        int currentIndex = 0;
        for (Field freeField: freeFields) {
            int currentPoints = evaluateMove(freeField, takenFields);
            if(currentPoints > point){
                point = currentPoints;
                indexToTake = currentIndex;
            }
            currentIndex++;
        }
        if(indexToTake == -1){
            indexToTake = rnd.random(freeFields.size());
        }
        freeFields.get(indexToTake).setOwner('O');
        addPoints(evaluateMove(freeFields.get(indexToTake), takenFields));
        return indexToTake;
    }
}
