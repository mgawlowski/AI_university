public class Main {

    public static void main(String args[]) {
//        QueenCSP csp = new QueenCSP();
        LatinSquareCSP csp = new LatinSquareCSP();

        long startTime = System.nanoTime();

        //method: 1 - backtracking; 2 - forward-checking
        //heuristic: 1 - random; 2 - lowest cardinality

        csp.run(5, 1, 1);
//        csp.runRandom(5);

        long endTime = System.nanoTime();
        long duration = (endTime - startTime)/1000000; //1ms = 0,001s
        System.out.println("Execution: " + duration + " ms");
    }
}
