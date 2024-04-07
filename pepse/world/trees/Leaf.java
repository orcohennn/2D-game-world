package pepse.world.trees;

import danogl.GameObject;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.Random;
import java.util.function.Consumer;

/**
 * Represents a leaf object in the game world.
 */
public class Leaf extends GameObject implements OnJumpObserver {

    /**
     * The base color of the leaf. It is a constant (static final).
     */
    private static final Color LEAF_BASE_COLOR = new Color(50, 200, 30);

    /**
     * The transition time for leaf animations. It is a constant (static final).
     */
    private static final int TRANSITION_TIME = 1;

    /**
     * The minimum angle for leaf animation. It is a constant (static final).
     */
    private static final float MIN_ANGLE = -8f;

    /**
     * The maximum angle for leaf animation. It is a constant (static final).
     */
    private static final float MAX_ANGLE = 8f;

    /**
     * The minimum width of the leaf. It is a constant (static final).
     */
    private static final float MIN_WIDTH = 28f;

    /**
     * The maximum width of the leaf. It is a constant (static final).
     */
    private static final float MAX_WIDTH = 32f;

    /**
     * The start angle for leaf transition animation. It is a constant (static final).
     */
    private static final float START_ANGLE = 0f;

    /**
     * The end angle for leaf transition animation. It is a constant (static final).
     */
    private static final float END_ANGLE = 90f;

    /**
     * The transition time for leaf transformation. It is a constant (static final).
     */
    private static final float LEAF_TRANSITION_TIME = 2f;

    /**
     * Constructs a leaf object with the specified parameters.
     * @param topLeftCorner The top-left corner position of the leaf.
     */
    public Leaf(Vector2 topLeftCorner) {
        super(topLeftCorner, Vector2.ONES.mult(Block.SIZE),
                new RectangleRenderable(ColorSupplier.approximateColor(LEAF_BASE_COLOR)));
        Random random = new Random();
        createAngleTask(this, random);
        createWidthChangerTask(this, random);
    }

    /*
     * Creates a task to change the width of the leaf.
     * @param leaf   The leaf object.
     * @param random The random number generator.
     */
    private static void createWidthChangerTask(GameObject leaf, Random random) {
        Consumer<Float> leafWidthChanger =
                (x) -> leaf.setDimensions(new Vector2(x, leaf.getDimensions().y()));
        Runnable widthRunnable = () -> new Transition<>(leaf, leafWidthChanger,
                MIN_WIDTH, MAX_WIDTH, Transition.CUBIC_INTERPOLATOR_FLOAT, TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        new ScheduledTask(leaf, random.nextFloat(), false, widthRunnable);
    }

    /*
     * Creates a task to change the angle of the leaf.
     * @param leaf   The leaf object.
     * @param random The random number generator.
     */
    private static void createAngleTask(GameObject leaf, Random random) {
        Runnable angleRunnable = () -> new Transition<>(leaf, leaf.renderer()::setRenderableAngle,
                MIN_ANGLE, MAX_ANGLE, Transition.CUBIC_INTERPOLATOR_FLOAT, TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        new ScheduledTask(leaf, random.nextFloat(), false, angleRunnable);
    }

    /**
     * Handles the jump event for the leaf.
     */
    @Override
    public void onJump() {
        new Transition<>(this, this.renderer()::setRenderableAngle,
                START_ANGLE, END_ANGLE, Transition.LINEAR_INTERPOLATOR_FLOAT, LEAF_TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_ONCE, null);
    }
}

