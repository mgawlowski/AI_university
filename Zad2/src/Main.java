public class Main {

    public static void main(String args[]) {
        QueenCSP csp = new QueenCSP();
        for (int i = 0; i < 5; i++) {
            csp.runRandom(20);
        }
    }
}
