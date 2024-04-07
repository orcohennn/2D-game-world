package pepse.world.trees;

import danogl.util.Vector2;
import pepse.world.Block;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Represents the flora in the game world, including the creation of trees.
 */
public class Flora {

    /**
     * The probability bound for generating a tree. It is a constant (static final).
     */
    private static final float TREE_PROB_BOUND = 1f;

    /**
     * The threshold value for generating a tree. It is a constant (static final).
     */
    private static final float TREE_THRESHOLD = 0.06f;

    // Fields
    private final float dayTimeCycle;
    private final Function<Float, Float> groundHeightAt;
    private final Consumer<Double> increaseEnergy;

    /**
     * Constructs the flora with the specified parameters.
     * @param dayTimeCycle      The day time cycle.
     * @param groundHeightAt    The function for obtaining the ground height.
     * @param increaseEnergy    The consumer for increasing energy.
     */
    public Flora(float dayTimeCycle, Function<Float, Float> groundHeightAt,
                 Consumer<Double> increaseEnergy) {
        this.dayTimeCycle = dayTimeCycle;
        this.groundHeightAt = groundHeightAt;
        this.increaseEnergy = increaseEnergy;
    }

    /**
     * Creates trees within the specified range.
     * @param minX The minimum x-coordinate.
     * @param maxX The maximum x-coordinate.
     * @return An ArrayList of trees within the specified range.
     */
    public ArrayList<Tree> createInRange(int minX, int maxX) {
        Random random = new Random();
        ArrayList<Tree> trees = new ArrayList<>();
        for (float i = minX; i < maxX; i += Block.SIZE) {
            if (random.nextFloat(TREE_PROB_BOUND) <= TREE_THRESHOLD) {
                Tree tree = new Tree(this.dayTimeCycle, new Vector2(i,
                        groundHeightAt.apply(i)), this.increaseEnergy);
                trees.add(tree);
            }
        }
        return trees;
    }
}
