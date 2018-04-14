import java.util.Random;

public class RandomNumbers {

    private final Random random;

    public RandomNumbers() {
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
}
