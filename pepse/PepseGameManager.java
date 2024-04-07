package pepse;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import pepse.userinterface.UiEnergy;
import pepse.world.*;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;
import pepse.world.trees.OnJumpObserver;
import pepse.world.trees.Tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Manages the game logic for Pepse, a 2D platformer game.
 */
public class PepseGameManager extends GameManager{

    /**
     * The duration of a day-night cycle in seconds. It is a constant (static final).
     */
    private static final float DAY_TIME_CYCLE = 30;
    /**
     * The starting position on the x-axis. It is a constant (static final).
     */
    private static final int START_X = 0;

    /**
     * Entry point for the game. Instantiates and runs the game manager.
     * @param args Command-line arguments (unused).
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    /**
     * Initializes the game with necessary components.
     * @param imageReader       Reads images for game assets.
     * @param soundReader       Reads sounds for game assets.
     * @param inputListener     Listens for user input.
     * @param windowController  Controls the game window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        Vector2 windowDimensions = windowController.getWindowDimensions();

        GameObject sky = Sky.create(windowDimensions);
        gameObjects().addGameObject(sky, Layer.BACKGROUND);

        createSun(windowDimensions);

        Terrain terrain = createTerrain(windowDimensions);

        GameObject night = Night.create(windowDimensions,DAY_TIME_CYCLE);
        gameObjects().addGameObject(night,Layer.FOREGROUND);

        Avatar avatar = new Avatar(Vector2.ZERO,inputListener,imageReader);
        createFlora(terrain, avatar, windowDimensions);
        gameObjects().addGameObject(avatar, Layer.DEFAULT);

        GameObject uiEnergy = UiEnergy.create(avatar::getEnergy);
        gameObjects().addGameObject(uiEnergy, Layer.UI);
    }

    /*
     * Creates and adds the sun and its halo to the game.
     * @param windowDimensions The dimensions of the game window.
     */
    private void createSun(Vector2 windowDimensions) {
        GameObject sun = Sun.create(windowDimensions, DAY_TIME_CYCLE);
        GameObject sunHalo = SunHalo.create(sun);
        gameObjects().addGameObject(sunHalo,Layer.STATIC_OBJECTS);
        gameObjects().addGameObject(sun,Layer.STATIC_OBJECTS);
    }


    /*
     * Creates flora objects in the game world.
     * @param terrain           The terrain of the game world.
     * @param avatar            The player's avatar.
     * @param windowDimensions  The dimensions of the game window.
     */
    private void createFlora(Terrain terrain, Avatar avatar, Vector2 windowDimensions) {
        Flora flora = new Flora(DAY_TIME_CYCLE,terrain::groundHeightAt, avatar::increaseEnergy);
        createTrees(flora, windowDimensions, avatar);
    }

    /*
     * Creates trees for the specified flora and adds them to the game world.
     * @param flora             The flora for which trees are created.
     * @param windowDimensions  The dimensions of the game window.
     * @param avatar            The player's avatar.
     */
    private void createTrees(Flora flora, Vector2 windowDimensions, Avatar avatar) {
        ArrayList<Tree> treesArray = flora.createInRange(START_X, (int)windowDimensions.x());
        for (Tree tree: treesArray){
            gameObjects().addGameObject(tree.getStump(), Layer.STATIC_OBJECTS);
            avatar.addJumpObserver((OnJumpObserver) tree.getStump());
            for (GameObject object: tree.getLeavesAndFruitsArray()){
                avatar.addJumpObserver((OnJumpObserver) object);
                gameObjects().addGameObject(object, Layer.DEFAULT);
            }
        }
    }

    /*
     * Creates terrain blocks for the game world and adds them to the game.
     * @param windowDimensions  The dimensions of the game window.
     * @return                  The generated terrain.
     */
    private Terrain createTerrain(Vector2 windowDimensions) {
        Terrain terrain = new Terrain(windowDimensions, new Random().nextInt());
        List<Block> blockList = terrain.createInRange(START_X, (int) (windowDimensions.x()));
        for (Block block:blockList){
            gameObjects().addGameObject(block,Layer.STATIC_OBJECTS);
        }
        return terrain;
    }
}
