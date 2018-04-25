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

    public String getName() {
        return name;
    }

    public void add(Field field) {
        fields.add(field);
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
        String text = name + ": ";
        for (Field f : fields) {
            text += f;
        }
        return text;
    }
}
