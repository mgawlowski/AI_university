import java.util.Random;

public class RandomGenerator {

    private final Random random;

    public RandomGenerator() {
        random = new Random();
        if(Constants.USE_SEED)
            random.setSeed(Constants.SEED);
    }

    public int random(int i){
        return random.nextInt(i);
    }

    public float random(){
        return random.nextFloat();
    }

    public Random getRandomEngine() {
        return random;
    }
}
