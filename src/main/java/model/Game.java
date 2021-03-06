/******************************************************************************
 * This work is applicable to the conditions of the MIT License,              *
 * which can be found in the LICENSE file, or at                              *
 * https://github.com/philippwinter/pacman/blob/master/LICENSE                *
 *                                                                            *
 * Copyright (c) 2013 Philipp Winter, Jonas Heidecke & Niklas Kaddatz         *
 ******************************************************************************/

package model;

import controller.MainController;
import model.event.RendererProcess;
import model.event.Timer;
import model.event.WorkerProcess;
import view.MainGui;

/**
 * The Game class is kind of a <i>master</i>-class, organizing all other business logic objects.
 *
 * @author Philipp Winter
 * @author Jonas Heidecke
 * @author Niklas Kaddatz
 */
public class Game {
    private GameContainer gameProduct = new GameContainer();

    static {
        Game.reset();
    }

    public static final double BASIC_REFRESH_RATE = 4.;

    public final static Settings settings = Settings.getInstance();

    /**
     * The singleton instance.
     */
    private static Game instance;

    /**
     * Whether the game was initialized already.
     */
    private static boolean initialized;

    /**
     * A container of all ghosts.
     */
    private GhostContainer ghostContainer;

    /**
     * A container of all pacmans.
     */
    private PacmanContainer pacmanContainer;

    /**
     * The event handler reacts on events happening in the game.
     */
    private Timer eventHandlerManager;

    /**
     * The map is like a two dimensional array of positions, containing all map objects
     */
    private Map map;

    /**
     * The amount of time, our UI will be repainted.
     * Also how often the user is able to interact with it's character, e.g. by pressing a key.
     */
    private double refreshRate = BASIC_REFRESH_RATE;

    /**
     * The level of the game.
     */
    private Level level;

    public boolean isOver = false;

    private int playerLifes = 3;

    private MainGui gui;

    public MainGui getGui() {
        return gui;
    }

    public void setGui(MainGui gui) {
        this.gui = gui;
    }

    /**
     * Reset the game, for instance necessary when the user wants to start a new try.
     */
    public synchronized static void reset() {
        Game.initialized = true;
        Game.instance = new Game();
        // Initialization work must be done in a new method in order to retrieve the game object during the following work
        Game.instance.initializeInternal();
    }

    /**
     * The internal initialization method.
     */
    private synchronized void initializeInternal() {
        Map.reset(1);
        Coin.resetActiveSeconds();
        Level.reset();

        this.map = Map.getInstance(1);

        gameProduct.resetContainer(this);

        this.level = Level.getInstance();

        this.eventHandlerManager = new Timer();
        this.eventHandlerManager.register(new WorkerProcess());
        this.eventHandlerManager.register(new RendererProcess());

        this.map.placeObjects();
    }

    public void nextMap(int nextMap) {
        this.map.getNextMap(nextMap);
        this.level.setLevel(nextMap);
        this.level.star = 3;
        gameProduct.resetContainer(this);
        this.map.placeObjects();
    }

    public void resetContainer() {
        gameProduct.resetContainer(this);
    }

    public void respawn() {
        int i = (int) (Math.random() * map.getPositionContainer().getAll().size());
        MapObjectContainer p = map.getPositionContainer().getAll().get(i).getOnPosition();
        if (p.size() == 1 && p.get(0) instanceof Point) {
            map.setRespawnPosition(map.getPositionContainer().getAll().get(i));
        } else
            respawn();
    }

    public boolean isAGhost(Position pos) {
        int i = 0;
        boolean b = false;
        while (i < ghostContainer.max) {
            if (pos.equals(ghostContainer.get(i).position)) {
                b = true;
                i = 5;
            }
            i++;
        }
        return b;
    }

    public boolean ghostOnPosition(MapObjectContainer p, int x, int y) {
        try {
            Position pos = map.getPositionContainer().get(p.get(0).getPosition().getX() + x, p.get(0).getPosition().getY() + y);
            if (isAGhost(pos))
                return false;
            else
                return true;
        } catch (Exception e) {
            System.out.println("exception");
            return true;
        }
    }

    public boolean ghostInRangeOf(MapObjectContainer p, int range) {
        int i = 1;
        boolean b = true;
        while (i <= range) {
            if (ghostOnPosition(p, i, 0) == false || ghostOnPosition(p, -i, 0) == false || ghostOnPosition(p, 0, i) == false || ghostOnPosition(p, 0, -i) == false) {
                b = false;
                i = range + 1;
            }
            i++;
        }
        return b;
    }

    /**
     * Is the Game already initialized?
     */
    public static boolean isInitialized() {
        return Game.initialized;
    }

    public int getPlayerLifes() {
        return playerLifes;
    }

    public void reducePLayerLifes() {
        this.playerLifes -= 1;
        if (this.level.star > 0)
            this.level.star -= 1;
    }

    public Level getLevel() {
        return level;
    }

    /**
     * Gets the ghost container.
     *
     * @return The container used to manage all instance of {@link Ghost}'s in the object tree.
     */
    public GhostContainer getGhostContainer() {
        return ghostContainer;
    }

    /**
     * Gets the coin container.
     *
     * @return The container used to manage all instance of {@link Coin}'s in the object tree.
     */
    public CoinContainer getCoinContainer() {
        return gameProduct.getCoinContainer();
    }

    /**
     * Gets the point container.
     *
     * @return The container used to manage all instance of {@link Point}'s in the object tree.
     */
    public PointContainer getPointContainer() {
        return gameProduct.getPointContainer();
    }

    /**
     * Gets the pacman container.
     *
     * @return The container used to manage all instance of {@link Pacman}'s in the object tree.
     */
    public PacmanContainer getPacmanContainer() {
        return pacmanContainer;
    }


    /**
     * Gets the fruits container.
     *
     * @return The container used to manage all instance of {@link Fruit}'s in the object tree.
     */
    public FruitContainer getFruitsContainer() {
        return gameProduct.getFruitsContainer();
    }

    /**
     * Gets the map of the game.
     *
     * @return The map.
     */
    public Map getMap() {
        return map;
    }

    /**
     * Changes the refresh rate depending on the level.
     * Can be expressed by the equation <code>RefreshRate(level) = (level^5)^(1/7)</code>.
     *
     * @param l The level which is used as a parameter in the mathematical equation to generate a new refresh rate.
     */
    public void changeRefreshRate(Level l) {
        // f(x) = (x^5)^(1/7) or "The refresh rate per second is the 7th root of the level raised to 5"
        this.refreshRate = Math.pow(Math.pow(l.getLevel(), 5), 1 / 7) + BASIC_REFRESH_RATE;
    }

    /**
     * Gets the refresh rate.
     *
     * @return The refresh rate.
     */
    public double getRefreshRate() {
        return this.refreshRate;
    }

    /**
     * Starts the game, in detail it causes all {@link WorkerProcess}'s to start working.
     *
     * @see Timer#startExecution()
     */
    public void start(int level) {
        if (MainGui.isGameRunning() == false)
            nextMap(level);
        if (gameProduct.getPointContainer().size() == 0) {
            this.map.placeObjects();
        }
        this.eventHandlerManager.startExecution();
    }

    /**
     * Pauses the game, by stopping/pausing all {@link WorkerProcess}'s.
     *
     * @see Timer#pauseExecution()
     */
    public void pause() {
        this.eventHandlerManager.pauseExecution();
    }

    /**
     * Compares two objects for equality.
     *
     * @param o The other object.
     * @return Whether both objects are equal.
     */
    public boolean equals(Object o) {
        if (o != null) {
            if (o instanceof Game) {
                // As it is a singleton, checking for reference equality is enough
                return this == o;
            }
        }
        return false;
    }


    public void gameOver() {
        this.isOver = true;
        Game.getInstance().getEventHandlerManager().pauseExecution();
        MainController.getInstance().getGui().onGameOver();
        MainController.getInstance().getGui().getRenderer().markReady();
    }

    public Timer getEventHandlerManager() {
        return eventHandlerManager;
    }

    /**
     * Returns the singleton instance.
     *
     * @return The game singleton.
     */
    public static Game getInstance() {
        return Game.instance;
    }

    public boolean isGameOver() {
        return this.isOver;
    }

    public void onPacmanGotEaten() {
        Map.getInstance().onPacmanGotEaten();
    }

    public void increasePlayerLifes() {
        this.playerLifes++;
    }

    public enum Mode {
        SINGLEPLAYER, MULTIPLAYER, NORMAL, CAMPAIGN
    }

    public void setGhostContainer(GhostContainer ghostContainer) {
        this.ghostContainer = ghostContainer;
    }

    public void setPacmanContainer(PacmanContainer pacmanContainer) {
        this.pacmanContainer = pacmanContainer;
    }
}
