import java.util.List;

public abstract class Player {

    //todo not public
    public RandomGenerator rnd;
    private String name;
    private int points = 0;
    private int boardSize;

    public Player(RandomGenerator rnd, String name, int boardSize) {
        this.rnd = rnd;
        this.name = name;
        this.boardSize = boardSize;
    }

    public int getPoints() {
        return points;
    }

    public String getName() {
        return name;
    }

    public void addPoints(int points){
        //todo ogarnac punkty
        this.points += points;
    }

    public int evaluateMove(Field move, List<Field> takenFields){
        int x = move.getX();
        int y = move.getY();
        int point = 0;
        boolean emptyField;

        // -
        emptyField = false;
        for (int i = x+1; i < boardSize && !emptyField; i++) {
            if(!takenFields.contains(new Field(i,y))){
                emptyField = true;
            }
        }
        for (int i = x-1; i >=0 && !emptyField; i--) {
            if(!takenFields.contains(new Field(i,y))){
                emptyField = true;
            }
        }
        if(!emptyField){
            point += boardSize;
        }

        // |
        emptyField = false;
        for (int i = y+1; i < boardSize && !emptyField; i++) {
            if(!takenFields.contains(new Field(x,i))){
                emptyField = true;
            }
        }
        for (int i = y-1; i >=0 && !emptyField; i--) {
            if(!takenFields.contains(new Field(x,i))){
                emptyField = true;
            }
        }
        if(!emptyField){
            point += boardSize;
        }



        return point;
    }

    abstract int makeMove(List<Field> takenFields, List<Field> freeFields);
}
