public class Field {

    private final int x;
    private final int y;
    private char owner;

    public Field(int x, int y) {
        this.x = x;
        this.y = y;
        owner = '.';
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public char getOwner() {
        return owner;
    }

    public void setOwner(char owner) {
        this.owner = owner;
    }

    public String toString() {
        return String.valueOf(owner);
    }

    public boolean equals(int x, int y) {
        return this.x == x && this.y == y;
    }

    public boolean equals(Object o) {
        return this.x == ((Field)o).x && this.y == ((Field)o).y;
    }

    public int hashCode() {
        return x * 1000 + y;
    }
}
