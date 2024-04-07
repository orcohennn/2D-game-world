package pepse.world;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.trees.OnJumpObserver;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the player's avatar in the game world.
 */
public class Avatar extends GameObject {

    /**
     * The initial image path for the avatar. It is a constant (static final).
     */
    private static final String AVATAR_INITIAL_IMAGE_PATH = "src/assets/idle_0.png";

    /**
     * The horizontal velocity of the avatar. It is a constant (static final).
     */
    private static final float VELOCITY_X = 400;

    /**
     * The vertical velocity of the avatar for jumping. It is a constant (static final).
     */
    private static final float VELOCITY_Y = -650;

    /**
     * The gravitational force applied to the avatar. It is a constant (static final).
     */
    private static final float GRAVITY = 350;

    /**
     * The height of the avatar. It is a constant (static final).
     */
    private static final float AVATAR_HEIGHT = 78;

    /**
     * The width of the avatar. It is a constant (static final).
     */
    private static final float AVATAR_WIDTH = 50;

    /**
     * The maximum energy level of the avatar. It is a constant (static final).
     */
    private static final double MAX_ENERGY = 100;

    /**
     * The energy required for a jump. It is a constant (static final).
     */
    private static final double JUMP_ENERGY = 10;

    /**
     * The energy consumed when the avatar moves horizontally. It is a constant (static final).
     */
    private static final double MOVE_ENERGY = 0.5;

    /**
     * The energy consumed when the avatar is idle. It is a constant (static final).
     */
    private static final double IDLE_ENERGY = 1;

    /**
     * The time between animation clips for the avatar. It is a constant (static final).
     */
    private static final double TIME_BETWEEN_CLIPS = 0.05;

    /**
     * The number of animations for the avatar. It is a constant (static final).
     */
    private static final int NUM_OF_ANIMATIONS = 3;

    /**
     * The name of the idle animation. It is a constant (static final).
     */
    private static final String IDLE = "idle";

    /**
     * The name of the jump animation. It is a constant (static final).
     */
    private static final String JUMP = "jump";

    /**
     * The name of the run animation. It is a constant (static final).
     */
    private static final String RUN = "run";

    /**
     * The number of images in the idle animation. It is a constant (static final).
     */
    private static final int NUM_OF_IDLE_IMAGES = 4;

    /**
     * The number of images in the jump animation. It is a constant (static final).
     */
    private static final int NUM_OF_JUMP_IMAGES = 4;

    /**
     * The number of images in the run animation. It is a constant (static final).
     */
    private static final int NUM_OF_RUN_IMAGES = 6;

    /**
     * The index of the idle animation in the animations array. It is a constant (static final).
     */
    private static final int IDLE_INDEX = 0;

    /**
     * The index of the jump animation in the animations array. It is a constant (static final).
     */
    private static final int JUMP_INDEX = 1;

    /**
     * The index of the run animation in the animations array. It is a constant (static final).
     */
    private static final int RUN_INDEX = 2;

    /**
     * The tag assigned to avatar objects. It is a constant (static final).
     */
    private static final String AVATAR_TAG = "avatar";

    /**
     * The format for animation image paths. It is a constant (static final).
     */
    private static final String ANIMATION_PATH_FORMAT = "src/assets/%s_%d.png";

    private final UserInputListener inputListener;
    private final Renderable[] animationsRenderable;
    private final List<OnJumpObserver> onJumpObservers;
    private double energy;

    /**
     * Constructs an Avatar object with the specified position, input listener, and image reader.
     * @param pos           The initial position of the avatar.
     * @param inputListener The input listener for user controls.
     * @param imageReader   The image reader for loading avatar images.
     */
    public Avatar(Vector2 pos, UserInputListener inputListener, ImageReader imageReader) {
        super(pos, new Vector2(AVATAR_WIDTH, AVATAR_HEIGHT),
                imageReader.readImage(AVATAR_INITIAL_IMAGE_PATH, false));
        this.onJumpObservers = new ArrayList<>();
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);

        this.inputListener = inputListener;
        this.energy = MAX_ENERGY;
        this.animationsRenderable = createAnimationsRenderable(imageReader);
        this.setTag(AVATAR_TAG);
    }

    /**
     * Adds an observer for jump events.
     * @param observer The observer to add.
     */
    public void addJumpObserver(OnJumpObserver observer) {
        onJumpObservers.add(observer);
    }

    /**
     * Updates the avatar's state based on user input and physics.
     * @param deltaTime The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = 0;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT) && this.energy >= MOVE_ENERGY) {
            xVel -= VELOCITY_X;
            this.energy -= MOVE_ENERGY;
            this.renderer().setRenderable(animationsRenderable[RUN_INDEX]);
            renderer().setIsFlippedHorizontally(true);
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT) && this.energy >= MOVE_ENERGY) {
            xVel += VELOCITY_X;
            this.energy -= MOVE_ENERGY;
            this.renderer().setRenderable(animationsRenderable[RUN_INDEX]);
            renderer().setIsFlippedHorizontally(false);
        }
        transform().setVelocityX(xVel);
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0
                && this.energy >= JUMP_ENERGY) {
            transform().setVelocityY(VELOCITY_Y);
            this.energy -= JUMP_ENERGY;
            this.renderer().setRenderable(animationsRenderable[JUMP_INDEX]);
            notifyOnJump();
        }
        if (getVelocity().equals(Vector2.ZERO)) {
            increaseEnergy(IDLE_ENERGY);
            this.renderer().setRenderable(animationsRenderable[IDLE_INDEX]);
        }
    }

    /**
     * Retrieves the current energy level of the avatar.
     * @return The current energy level.
     */
    public double getEnergy() {
        return energy;
    }

    /**
     * Increases the energy level of the avatar by the specified amount.
     * @param increaseAmount The amount by which to increase the energy level.
     */
    public void increaseEnergy(double increaseAmount) {
        this.energy = Math.min(MAX_ENERGY, this.energy + increaseAmount);
    }

    /*
     * Creates an array of renderables for avatar animations.
     * @param imageReader The image reader for loading animation images.
     * @return            An array of renderables for avatar animations.
     */
    private Renderable[] createAnimationsRenderable(ImageReader imageReader) {
        Renderable[] animationsRenderable = new Renderable[NUM_OF_ANIMATIONS];
        animationsRenderable[IDLE_INDEX] =
                new AnimationRenderable(createAnimImagePaths(IDLE, NUM_OF_IDLE_IMAGES),
                imageReader, false, TIME_BETWEEN_CLIPS);
        animationsRenderable[JUMP_INDEX] =
                new AnimationRenderable(createAnimImagePaths(JUMP, NUM_OF_JUMP_IMAGES),
                imageReader, false, TIME_BETWEEN_CLIPS);
        animationsRenderable[RUN_INDEX] =
                new AnimationRenderable(createAnimImagePaths(RUN, NUM_OF_RUN_IMAGES),
                imageReader, false, TIME_BETWEEN_CLIPS);
        return animationsRenderable;
    }

    /*
     * Creates an array of image paths for animation frames.
     * @param prefix      The prefix for animation image filenames.
     * @param numOfImages The number of images in the animation.
     * @return            An array of image paths for animation frames.
     */
    private String[] createAnimImagePaths(String prefix, int numOfImages) {
        String[] pathsArray = new String[numOfImages];
        for (int i = 0; i < numOfImages; i++) {
            pathsArray[i] = String.format(ANIMATION_PATH_FORMAT, prefix, i);
        }
        return pathsArray;
    }

    /*
     * Notifies all jump observers of a jump event.
     */
    private void notifyOnJump() {
        for (OnJumpObserver observer : onJumpObservers) {
            observer.onJump();
        }
    }
}

