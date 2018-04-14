import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {


    public static void main(String args[]) {
        GeneticAlgorithm ga = new GeneticAlgorithm();
        RandomSearch rs = new RandomSearch();

        // had20, Kra32

//        ga.run("had20.dat");
        ga.run("Kra32.dat");

//        for (int i = 0; i < 10; i++) {
//            ga.run("Kra32.dat");
//        }


//        rs.run("had20.dat", 30000, true, true);

//        rs.run("Kra32.dat", 30000, true, false);

//        for (int i = 0; i < 10; i++) {
//            rs.run("Kra32.dat", 30000, false, true);
//        }
    }
}
