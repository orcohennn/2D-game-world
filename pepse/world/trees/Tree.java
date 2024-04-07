package pepse.world.trees;

import danogl.GameObject;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.world.Block;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;

/**
 * Represents a tree in the game world.
 */
public class Tree {

    /**
     * The number of leaves generated per tree. It is a constant (static final).
     */
    public static final int NUM_LEAVES = 3;

    /**
     * The threshold probability for generating leaves. It is a constant (static final).
     */
    public static final float NUM_OF_LEAVES_THRESHOLD = 0.7f;

    /**
     * The threshold probability for generating fruits. It is a constant (static final).
     */
    private static final float NUM_OF_FRUITS_THRESHOLD = 0.05f;

    /**
     * The upper bound for probability generation. It is a constant (static final).
     */
    public static final float PROB_BOUND = 1f;

    /**
     * The minimum height of the tree stump. It is a constant (static final).
     */
    public static final int STUMP_MIN_HEIGHT = 120;

    /**
     * The factor used for determining the height of the stump. It is a constant (static final).
     */
    public static final float HALF_FACTOR = 0.5f;

    // Attributes
    private final GameObject stump;
    private ArrayList<GameObject> leavesAndFruitsArray;

    /**
     * Constructs a tree object with the specified parameters.
     * @param dayTimeCycle   The duration of a day-night cycle.
     * @param stumpBottomPosition The bottom position of the stump.
     * @param increaseEnergy The function to increase energy.
     */
    public Tree(float dayTimeCycle, Vector2 stumpBottomPosition, Consumer<Double> increaseEnergy) {
        this.stump = new Stump(stumpBottomPosition, new Random().nextInt(STUMP_MIN_HEIGHT,
                (int) (stumpBottomPosition.y() * HALF_FACTOR)));
        Vector2 leavesCenter = new Vector2(this.stump.getCenter().x(), stump.getTopLeftCorner().y());
        createLeavesAndFruits(dayTimeCycle, leavesCenter, increaseEnergy);
    }

    /**
     * Retrieves the stump of the tree.
     * @return The stump of the tree.
     */
    public GameObject getStump() {
        return this.stump;
    }

    /**
     * Retrieves the list of leaves and fruits associated with the tree.
     * @return The list of leaves and fruits.
     */
    public ArrayList<GameObject> getLeavesAndFruitsArray() {
        return this.leavesAndFruitsArray;
    }

    /*
     * Creates leaves and fruits for the tree.
     * @param dayTimeCycle   The duration of a day-night cycle.
     * @param leavesCenter   The center position for leaves.
     * @param increaseEnergy The function to increase energy.
     */
    private void createLeavesAndFruits(float dayTimeCycle,
                                       Vector2 leavesCenter, Consumer<Double> increaseEnergy) {
        Random random = new Random();
        this.leavesAndFruitsArray = new ArrayList<>();
        float minXPos = leavesCenter.x() - NUM_LEAVES * Block.SIZE;
        float maxXPos = leavesCenter.x() + NUM_LEAVES * Block.SIZE;
        float minYPos = leavesCenter.y() - NUM_LEAVES * Block.SIZE;
        float maxYPos = leavesCenter.y() + NUM_LEAVES * Block.SIZE;
        for (float i = minXPos; i < maxXPos; i += Block.SIZE) {
            for (float j = minYPos; j < maxYPos; j += Block.SIZE) {
                Vector2 curLocation = new Vector2(i, j);
                if (random.nextFloat(PROB_BOUND) <= NUM_OF_LEAVES_THRESHOLD) {
                    this.leavesAndFruitsArray.add(new Leaf(curLocation));
                }
                if (random.nextFloat(PROB_BOUND) <= NUM_OF_FRUITS_THRESHOLD) {
                    this.leavesAndFruitsArray.add(new Fruit(curLocation,
                            Vector2.ONES.mult(Block.SIZE),
                            new OvalRenderable(Color.RED), dayTimeCycle, increaseEnergy));
                }
            }
        }
    }
}

