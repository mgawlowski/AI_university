import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String args[]) {
        QueenCSP csp = new QueenCSP();

        //method: 1 - backtracking; 2 - forward-checking
        //heuristic: 1 - random; 2 - lowest cardinality
        csp.run(100, 2, 1);
    }
}
