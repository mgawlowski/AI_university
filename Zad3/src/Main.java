public class Main {

    public static void main(String args[]) {

        Game game = new Game(10);

        long startTime = System.nanoTime();

        // 0 - random, 1 - brute, 2 - minMax5, 3 - minMax10, 4 - minMax15
        game.runGame(0, 2);

        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 100000000; //0,1s
        System.out.println("Execution: " + ((double) duration) / 10 + " s");
    }
}
