package model;

public class GameContainer {
    /**
     * A container of all fruits.
     */
    private FruitContainer fruitsContainer;
    /**
     * A container of all coins.
     */
    private CoinContainer coinContainer;
    /**
     * A container of all points.
     */
    private PointContainer pointContainer;

    public FruitContainer getFruitsContainer() {
        return fruitsContainer;
    }

    public CoinContainer getCoinContainer() {
        return coinContainer;
    }

    public PointContainer getPointContainer() {
        return pointContainer;
    }

    public void resetContainer(Game game) {
        game.setGhostContainer(new GhostContainer());
        this.coinContainer = new CoinContainer();
        this.pointContainer = new PointContainer();
        game.setPacmanContainer(new PacmanContainer());
        this.fruitsContainer = new FruitContainer();
    }
}