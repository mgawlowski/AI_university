import java.util.ArrayList;

public class BruteForcePlayer extends Player {

    public BruteForcePlayer(String name, char indicator, RandomGenerator rnd) {
        super(name, indicator, rnd);
    }

    @Override
    public int chooseMove(ArrayList<Field> freeFields, ArrayList<Line> scoringLines) {
        int bestPointsGain = 0;
        int indexToTake = 0;
        ArrayList<Line> scorableLines = new ArrayList<>();
        for (Line line : scoringLines) {
            if (line.isScorable()) {
                scorableLines.add(line);
            }
        }
        for (int i = 0; i < freeFields.size(); i++) {
            int currentPoints = evaluateMove(freeFields.get(i), scorableLines);
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
