import java.util.List;

public class BruteForcePlayer extends Player {

    public BruteForcePlayer(String name, char indicator, int boardSize, RandomGenerator rnd) {
        super(name, indicator, boardSize, rnd);
    }

    @Override
    int chooseMove(List<Field> freeFields, List<Line> scoringLines) {
        int bestPointsGain = 0;
        int indexToTake = 0;
        for (int i = 0; i < freeFields.size(); i++) {
            int currentPoints = evaluateMove(freeFields.get(i), scoringLines);
            if (currentPoints > bestPointsGain) {
                bestPointsGain = currentPoints;
                indexToTake = i;
            }
        }
        if (bestPointsGain == 0) {
            indexToTake = getRnd().random(freeFields.size());
        }
        return indexToTake;
    }
}
