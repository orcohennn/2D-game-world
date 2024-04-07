package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.function.Consumer;

/**
 * Represents a fruit object in the game world.
 */
public class Fruit extends GameObject implements OnJumpObserver {

    /**
     * The energy value provided by consuming the fruit. It is a constant (static final).
     */
    private static final int FRUIT_ENERGY = 10;

    /**
     * The tag for the avatar object. It is a constant (static final).
     */
    private static final String AVATAR_TAG = "avatar";

    /**
     * The index for the yellow color in the color renderable array. It is a constant (static final).
     */
    private static final int YELLOW_INDEX = 0;

    /**
     * The index for the red color in the color renderable array. It is a constant (static final).
     */
    private static final int RED_INDEX = 1;

    /**
     * The number of colors in the color renderable array. It is a constant (static final).
     */
    private static final int NUM_OF_COLORS = 2;

    /**
     * The full transparency value. It is a constant (static final).
     */
    private static final float FULL_TRANSPARENCY = 1f;

    /**
     * The full opaqueness value. It is a constant (static final).
     */
    private static final float FULL_OPAQUENESS = 0f;

    /**
     * Tag of fruit Object.
     */
    private static final String FRUIT_TAG = "fruit";

    private final float dayTimeCycle;
    private final Consumer<Double> increaseEnergyConsumer;
    private final Renderable[] colorRenderable;

    /**
     * Constructs a fruit object with the specified parameters.
     * @param topLeftCorner          The top-left corner position of the fruit.
     * @param dimensions             The dimensions of the fruit.
     * @param renderable             The renderable representing the fruit.
     * @param dayTimeCycle           The day time cycle.
     * @param increaseEnergyConsumer The consumer for increasing energy.
     */
    public Fruit(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 float dayTimeCycle, Consumer<Double> increaseEnergyConsumer) {
        super(topLeftCorner, dimensions, renderable);
        this.dayTimeCycle = dayTimeCycle;
        this.increaseEnergyConsumer = increaseEnergyConsumer;
        this.colorRenderable = new Renderable[NUM_OF_COLORS];
        this.colorRenderable[YELLOW_INDEX] = new OvalRenderable(Color.YELLOW);
        this.colorRenderable[RED_INDEX] = renderable;
        this.setTag(FRUIT_TAG);
    }

    /**
     * Handles the collision event when the fruit collides with another object.
     * @param other     The GameObject with which the fruit collides.
     * @param collision The collision information.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals(AVATAR_TAG)) {
            if (this.renderer().getOpaqueness() == FULL_TRANSPARENCY) {
                this.increaseEnergyConsumer.accept((double) FRUIT_ENERGY);
                this.renderer().setOpaqueness(FULL_OPAQUENESS);
                new ScheduledTask(this, this.dayTimeCycle, false,
                        () -> this.renderer().setOpaqueness(FULL_TRANSPARENCY));
            }
        }
    }

    /**
     * Handles the jump event for the fruit.
     */
    @Override
    public void onJump() {
        if (this.renderer().getRenderable() == this.colorRenderable[YELLOW_INDEX]) {
            this.renderer().setRenderable(this.colorRenderable[RED_INDEX]);
        } else {
            this.renderer().setRenderable(this.colorRenderable[YELLOW_INDEX]);
        }
    }
}

