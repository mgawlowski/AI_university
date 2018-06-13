import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MinMaxPlayer extends Player {

    private int depth;

    private MinMaxPlayer(String name, char indicator, RandomGenerator rnd) {
        super(name, indicator, rnd);
    }

    public MinMaxPlayer(String name, int depth, char indicator, RandomGenerator rnd) {
        this(name, indicator, rnd);
        this.depth = depth;
    }

    @Override
    public int chooseMove(ArrayList<Field> freeFields, ArrayList<Line> scoringLines) {
        return minMax(freeFields, scoringLines, depth, true);
    }

    private ArrayList<Field> deepCopyFields(ArrayList<Field> arrayList) {
        ArrayList<Field> newArrayList = new ArrayList<>();
        for (Field field : arrayList) {
            newArrayList.add(new Field(field));
        }
        return newArrayList;
    }

    private ArrayList<Line> deepCopyScorableLines(ArrayList<Line> scoringLines, int depth) {
        ArrayList<Line> scorableLines = new ArrayList<>();
        for (Line line : scoringLines) {
            if (line.isScorableInXMoves(depth)) {
                scorableLines.add(new Line(line));
            }
        }
        return scorableLines;
    }

    @SuppressWarnings("Duplicates")
    private int minMax(ArrayList<Field> freeFields, ArrayList<Line> scoringLines, int depth, boolean isMax) {
        if (depth > 1 && !freeFields.isEmpty()) {
            if (isMax) {
                int maxValue = -1;
                int indexOfMax = 0;
                int currentValue;
                for (int i = 0; i < freeFields.size(); i++) {
                    ArrayList<Field> newFreeFields = deepCopyFields(freeFields);
                    newFreeFields.remove(freeFields.get(i));
                    ArrayList<Line> newScoringLines = deepCopyScorableLines(scoringLines, depth);
                    applyMove(freeFields.get(i), newScoringLines);
                    currentValue = minMax(newFreeFields, newScoringLines, depth - 1, !isMax);
                    if (currentValue > maxValue) {
                        maxValue = currentValue;
                        indexOfMax = i;
                    }
                }
                return indexOfMax;
            } else {
                int minValue = Integer.MAX_VALUE;
                int indexOfMin = 0;
                int currentValue;
                for (int i = 0; i < freeFields.size(); i++) {
                    ArrayList<Field> newFreeFields = deepCopyFields(freeFields);
                    newFreeFields.remove(freeFields.get(i));
                    ArrayList<Line> newScoringLines = deepCopyScorableLines(scoringLines, depth);
                    applyMove(freeFields.get(i), newScoringLines);
                    currentValue = minMax(newFreeFields, newScoringLines, depth - 1, !isMax);
                    if (currentValue < minValue) {
                        minValue = currentValue;
                        indexOfMin = i;
                    }
                    return indexOfMin;
                }
            }
        } else {
            ArrayList<Line> scorableLines = new ArrayList<>();
            for (Line line : scoringLines) {
                if (line.isScorable()) {
                    scorableLines.add(line);
                }
            }
            if (isMax) {
                int maxValue = -1;
                int indexOfMax = 0;
                int currentValue;
                for (int i = 0; i < freeFields.size(); i++) {
                    currentValue = evaluateMove(freeFields.get(i), scorableLines);
                    if (currentValue > maxValue) {
                        maxValue = currentValue;
                        indexOfMax = i;
                    }
                }
                return indexOfMax;
            } else {
                int minValue = Integer.MAX_VALUE;
                int indexOfMin = 0;
                int currentValue;
                for (int i = 0; i < freeFields.size(); i++) {
                    currentValue = evaluateMove(freeFields.get(i), scorableLines);
                    if (currentValue < minValue) {
                        minValue = currentValue;
                        indexOfMin = i;
                    }
                }
                return indexOfMin;
            }
        }
        return -1;
    }

    private int slimMinMax(ArrayList<Field> freeFields, ArrayList<Line> scoringLines, int depth, boolean isMax) {
        if (depth > 1 && !freeFields.isEmpty()) {
            int minMaxValue = isMax ? -1 : Integer.MAX_VALUE;
            int index = 0;
            int currentValue;
            for (int i = 0; i < freeFields.size(); i++) {
                ArrayList<Field> newFreeFields = deepCopyFields(freeFields);
                newFreeFields.remove(freeFields.get(i));

                ArrayList<Line> newScoringLines = deepCopyScorableLines(scoringLines, depth);
                applyMove(freeFields.get(i), newScoringLines);

                currentValue = slimMinMax(newFreeFields, newScoringLines, depth - 1, !isMax);

                if (isMax ? currentValue > minMaxValue : currentValue < minMaxValue) {
                    minMaxValue = currentValue;
                    index = i;
                }
            }
            return index;
        } else {
            ArrayList<Line> scorableLines = new ArrayList<>();
            for (Line line : scoringLines) {
                if (line.isScorable()) {
                    scorableLines.add(line);
                }
            }

            int minMaxValue = isMax ? -1 : Integer.MAX_VALUE;
            int index = 0;
            int currentValue;
            for (int i = 0; i < freeFields.size(); i++) {
                currentValue = evaluateMove(freeFields.get(i), scorableLines);
                if (isMax ? currentValue > minMaxValue : currentValue < minMaxValue) {
                    minMaxValue = currentValue;
                    index = i;
                }
            }
            return index;
        }
    }

    @SuppressWarnings("Duplicates")
    private int newMinMax(ArrayList<Field> freeFields, ArrayList<Field> takenFields, ArrayList<Line> scoringLines, int depth, boolean isMax) {
        List<Field> currentFreeFields = freeFields.stream().filter(u -> !takenFields.contains(u)).collect(Collectors.toList());
        if (depth > 1 && !currentFreeFields.isEmpty()) {
            if (isMax) {
                int maxValue = -1;
                int indexOfMax = 0;
                int currentValue;
                for (int i = 0; i < currentFreeFields.size(); i++) {
                    ArrayList<Field> newTakenFields = deepCopyFields(takenFields);
                    newTakenFields.add(currentFreeFields.get(i));
                    ArrayList<Line> newScoringLines = deepCopyScorableLines(scoringLines, depth);
                    applyMove(currentFreeFields.get(i), newScoringLines);
                    currentValue = newMinMax(freeFields, newTakenFields, newScoringLines, depth - 1, !isMax);
                    if (currentValue > maxValue) {
                        maxValue = currentValue;
                        indexOfMax = i;
                    }
                }
                return indexOfMax;
            } else {
                int minValue = Integer.MAX_VALUE;
                int indexOfMin = 0;
                int currentValue;
                for (int i = 0; i < currentFreeFields.size(); i++) {
                    ArrayList<Field> newTakenFields = deepCopyFields(takenFields);
                    newTakenFields.add(currentFreeFields.get(i));
                    ArrayList<Line> newScoringLines = deepCopyScorableLines(scoringLines, depth);
                    applyMove(currentFreeFields.get(i), newScoringLines);
                    currentValue = newMinMax(freeFields, newTakenFields, newScoringLines, depth - 1, !isMax);
                    if (currentValue < minValue) {
                        minValue = currentValue;
                        indexOfMin = i;
                    }
                    return indexOfMin;
                }
            }
        } else {
            ArrayList<Line> scorableLines = new ArrayList<>();
            for (Line line : scoringLines) {
                if (line.isScorable()) {
                    scorableLines.add(line);
                }
            }
            if (isMax) {
                int maxValue = -1;
                int indexOfMax = 0;
                int currentValue;
                for (int i = 0; i < currentFreeFields.size(); i++) {
                    currentValue = evaluateMove(currentFreeFields.get(i), scorableLines);
                    if (currentValue > maxValue) {
                        maxValue = currentValue;
                        indexOfMax = i;
                    }
                }
                return indexOfMax;
            } else {
                int minValue = Integer.MAX_VALUE;
                int indexOfMin = 0;
                int currentValue;
                for (int i = 0; i < currentFreeFields.size(); i++) {
                    currentValue = evaluateMove(currentFreeFields.get(i), scorableLines);
                    if (currentValue < minValue) {
                        minValue = currentValue;
                        indexOfMin = i;
                    }
                }
                return indexOfMin;
            }
        }
        return -1;
    }
}
