import java.util.HashSet;

public class Line {

    private String name;
    private HashSet<Field> fields;
    private int takenFields;

    public Line(String name) {
        this.name = name;
        fields = new HashSet<>();
        takenFields = 0;
    }

    public Line(Line line) {
        this.name = line.name;
        fields = new HashSet<>();
        for (Field field : fields) {
            fields.add(new Field(field));
        }
        this.takenFields = line.takenFields;
    }

    public void add(Field field) {
        fields.add(field);
    }

    public boolean isScorable(){
        return fields.size() == takenFields + 1;
    }

    public boolean isScorableInXMoves(int xMoves){
        return fields.size() <= takenFields + xMoves;
    }

    public int movePoints(Field fieldToCheck) {
        if (fields.contains(fieldToCheck)) {
            if (fields.size() == takenFields - 1) {
                return fields.size();
            }
        }
        return 0;
    }

    public int makeMove(Field fieldToTake) {
        if (fields.contains(fieldToTake)) {
            takenFields++;
            if (fields.size() == takenFields) {
                return fields.size();
            }
        }
        return 0;
    }

    public String toString() {
        StringBuilder text = new StringBuilder(name + ": ");
        for (Field f : fields) {
            text.append(f).append(" ");
        }
        text.append("= ").append(takenFields).append(" / ").append(fields.size());
        return text.toString();
    }
}
