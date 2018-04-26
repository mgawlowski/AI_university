import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

@SuppressWarnings("WeakerAccess")
public class Constants {

    private static final String CONFIG_FILENAME = "constants.txt";
    public static final String MODULE_PATH = "Zad1\\";

    public static final int POPULATION_SIZE;
    public static final int FITNESS_EVALS;

    public static final int SELECTION_TYPE;
    public static final int TOURNAMENT_SIZE;

    public static final float CROSS_PROB;
    public static final float MUTAT_PROB;

    public static final boolean USE_SEED;
    public static final long SEED;

    public static final boolean USE_CACHE;

    static {
        HashMap<String, String> keyValues = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(MODULE_PATH + CONFIG_FILENAME));
            String line;
            while ((line = br.readLine()) != null) {
                // omits empty lines
                if(line.trim().length() != 0)
                    //config file should have "<NAME> <VALUE>" structure with any whitespaces as delimiter
                    keyValues.put(line.trim().split("\\s+")[0], line.trim().split("\\s+")[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        POPULATION_SIZE = Integer.parseInt(keyValues.get("POPULATION_SIZE"));
        FITNESS_EVALS = Integer.parseInt(keyValues.get("FITNESS_EVALS"));

        SELECTION_TYPE = Integer.parseInt(keyValues.get("SELECTION_TYPE"));
        TOURNAMENT_SIZE = Integer.parseInt(keyValues.get("TOURNAMENT_SIZE"));

        CROSS_PROB = Float.parseFloat(keyValues.get("CROSS_PROB"));
        MUTAT_PROB = Float.parseFloat(keyValues.get("MUTAT_PROB"));

        USE_SEED = Boolean.parseBoolean(keyValues.get("USE_SEED"));
        SEED = Long.parseLong(keyValues.get("SEED"));

        USE_CACHE = Boolean.parseBoolean(keyValues.get("USE_CACHE"));
    }

    private Constants() {

    }
}