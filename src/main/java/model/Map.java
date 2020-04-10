/******************************************************************************
 * This work is applicable to the conditions of the MIT License,              *
 * which can be found in the LICENSE file, or at                              *
 * https://github.com/philippwinter/pacman/blob/master/LICENSE                *
 *                                                                            *
 * Copyright (c) 2013 Philipp Winter, Jonas Heidecke & Niklas Kaddatz         *
 ******************************************************************************/

package model;

import java.util.Hashtable;

/**
 * @author Philipp Winter
 * @author Jonas Heidecke
 * @author Niklas Kaddatz
 */
public class Map {

    public static final PositionContainer positionsToRender = new PositionContainer(Map.getInstance().width, Map.getInstance().height);

    private static Map instance;

    private PositionContainer positionContainer;

    private int[][] board;

    private Hashtable tileInf;

    public final int width;

    public final int height;

    private boolean objectsPlaced = false;

    public static StartingPosition startingPositions;

    public static Map getInstance() {
        if (Map.instance == null) {
            Map.instance = new Map();
        }

        return Map.instance;
    }

    public static void reset() {
        Map.instance = new Map();
    }

    private Map() {
        this("src/main/resources/maps/Pacman.tmx");
    }

    private Map(String File) {
        MapInformation mapInf = MapParser.ParseMap(File);
        this.board = mapInf.board;
        this.tileInf = mapInf.tile;

        this.width = this.board.length;
        this.height = this.board[0].length;

        this.positionContainer = new PositionContainer(width, height);

        // Create all position instances for this map
        for (int actX = 0; actX < width; actX++) {
            for (int actY = 0; actY < height; actY++) {
                this.positionContainer.add(new Position(actX, actY));
            }
        }
    }

    public PositionContainer getPositionContainer() {
        return this.positionContainer;
    }

    public static int freeNeighbourFields(Position pos) {
        int count = 0;
        for (Direction d : Direction.values()) {
            if (getPositionByDirectionIfMovableTo(pos, d) != null) {
                count++;
            }
        }
        return count;
    }

    public static Position getPositionByDirectionIfMovableTo(Position prevPos, Direction movingTo) {
        Position p = null;
        if (prevPos == null) {
            throw new IllegalArgumentException("prevPos cannot be null.");
        }
        try {
            if (movingTo == Direction.NORTH) {
                p = Map.getInstance().getPositionContainer().get(prevPos.getX(), prevPos.getY() - 1);
            } else if (movingTo == Direction.EAST) {
                p = Map.getInstance().getPositionContainer().get(prevPos.getX() + 1, prevPos.getY());
            } else if (movingTo == Direction.WEST) {
                p = Map.getInstance().getPositionContainer().get(prevPos.getX() - 1, prevPos.getY());
            } else if (movingTo == Direction.SOUTH) {
                p = Map.getInstance().getPositionContainer().get(prevPos.getX(), prevPos.getY() + 1);
            }
            if (p != null && p.isMoveableTo()) {
                return p;
            } else {
                return null;
            }
        } catch (IllegalArgumentException ex) {
            // Just return null to signalize, that the point doesn't exist
            return null;
        }
    }

    public boolean equals(Object o) {
        if (o != null) {
            if (o instanceof Map) {
                return this.getPositionContainer().equals(((Map) o).getPositionContainer())
                        && this.objectsPlaced == ((Map) o).isObjectsPlaced();
            }
        }
        return false;
    }

    public int hashCode(){
        return this.getPositionContainer().hashCode();
    }

    public void placeObjects() {
        PositionContainer wallPositions = new PositionContainer(width, height);
        PositionContainer placeholderPositions = new PositionContainer(width, height);

        //Pacman
        Game g = Game.getInstance();
        PacmanContainer pacC = g.getPacmanContainer();
        GhostContainer gC = g.getGhostContainer();

        PointContainer pC = g.getPointContainer();
        CoinContainer cC = g.getCoinContainer();

        Position[] ghostAndPacman= new Position[6];

        cC.removeAll();
        pC.removeAll();
        for(int i = 0 ; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                Position newPosition = new Position(i,j);
                if(board[i][j]-1 == (int)tileInf.get("Wall")){
                    wallPositions.add(positionContainer.get(i,j));
                }else if(board[i][j]-1 == (int)tileInf.get("Holder")){
                    placeholderPositions.add(positionContainer.get(i,j));
                }else if(board[i][j]-1 == (int)tileInf.get("SuperGums")){
                    positionsToRender.add((positionContainer.get(i, j)));
                    cC.add(new Coin(positionContainer.get(i, j)));
                }else if(board[i][j]-1 == (int)tileInf.get("Pacman_Male")){
                    ghostAndPacman[0] = newPosition;
                    pacC.add(new Pacman(newPosition, Pacman.Sex.MALE));
                }else if((board[i][j]-1 == (int)tileInf.get("Pacman_Female"))){
                    ghostAndPacman[1] = newPosition;
                    if((Settings.getInstance().getGameMode() == Game.Mode.MULTIPLAYER)) {
                        pacC.add(new Pacman(newPosition, Pacman.Sex.FEMALE));
                    }
                }else if(board[i][j]-1 == (int)tileInf.get("Red-Ghost")){
                    ghostAndPacman[2] = newPosition;
                    gC.add(new Ghost(newPosition, Ghost.Colour.RED));
                    placeholderPositions.add(positionContainer.get(i,j));
                }else if(board[i][j]-1 == (int)tileInf.get("Orange-Ghost")){
                    ghostAndPacman[5] = newPosition;
                    gC.add(new Ghost(newPosition, Ghost.Colour.ORANGE));
                    placeholderPositions.add(positionContainer.get(i,j));
                }else if(board[i][j]-1 == (int)tileInf.get("Blue-Ghost")){
                    ghostAndPacman[4] = newPosition;
                    gC.add(new Ghost(newPosition, Ghost.Colour.BLUE));
                    placeholderPositions.add(positionContainer.get(i,j));
                }else if(board[i][j]-1 == (int)tileInf.get("Pink-Ghost")){
                    ghostAndPacman[3] = newPosition;
                    gC.add(new Ghost(newPosition, Ghost.Colour.PINK));
                    placeholderPositions.add(positionContainer.get(i,j));
                }
            }
        }
        this.startingPositions = new StartingPosition(ghostAndPacman[0],ghostAndPacman[1],ghostAndPacman[2],ghostAndPacman[3],ghostAndPacman[4],ghostAndPacman[5]);

        for(Position p: wallPositions){
            new Wall(p);
        }

        for(Position p: placeholderPositions){
            new Placeholder(p);
        }


        for(Position p : positionContainer){
            if(p.getOnPosition().size() == 0){
                pC.add(new Point(p));
                positionsToRender.add(p);
            }
        }

        Map.positionsToRender.add(wallPositions);
        Map.positionsToRender.add(placeholderPositions);

        this.markAllForRendering();
    }

    public void onNextLevel() {
        this.replaceDynamicObjects();
        Game g = Game.getInstance();

        FruitContainer fC = g.getFruitsContainer();
        fC.removeAll();

        PointContainer pC = g.getPointContainer();
        pC.removeAll();

        for(Position p : positionContainer){
            if(p.getOnPosition().size() == 0){
                pC.add(new Point(p));
                positionsToRender.add(p);
            }
        }

        for(Coin c : Game.getInstance().getCoinContainer()){
            if(c.getState() == StaticTarget.State.EATEN) {
                c.changeState(StaticTarget.State.AVAILABLE);
            }
        }

        this.markAllForRendering();
    }

    public void onPacmanGotEaten() {
        this.replaceDynamicObjects();
    }

    public static class StartingPosition {

        public final Position GHOST_RED;
        public final Position GHOST_PINK;
        public final Position GHOST_BLUE;
        public final Position GHOST_ORANGE;

        public final Position PACMAN_MALE;
        public final Position PACMAN_FEMALE;

        public StartingPosition(Position male, Position female, Position red, Position pink, Position blue, Position orange){
            GHOST_RED = Map.getInstance().positionContainer.get(red.getX(), red.getY());
            GHOST_PINK = Map.getInstance().positionContainer.get(pink.getX(), pink.getY());
            GHOST_BLUE = Map.getInstance().positionContainer.get(blue.getX(), blue.getY());
            GHOST_ORANGE = Map.getInstance().positionContainer.get(orange.getX(), orange.getY());

            PACMAN_MALE = Map.getInstance().positionContainer.get(male.getX(), male.getY());
            PACMAN_FEMALE = Map.getInstance().positionContainer.get(female.getX(), female.getY());
        }


    }

    private void replaceDynamicObjects() {
        GhostContainer gC = Game.getInstance().getGhostContainer();

        for(Ghost g : gC) {
            switch(g.getColour()) {
                case RED: g.move(startingPositions.GHOST_RED);
                    break;
                case PINK: g.move(startingPositions.GHOST_PINK);
                    break;
                case BLUE: g.move(startingPositions.GHOST_BLUE);
                    break;
                case ORANGE: g.move(startingPositions.GHOST_ORANGE);
                    break;
                default:
                    throw new RuntimeException("Bla");
            }
        }

        PacmanContainer pC = Game.getInstance().getPacmanContainer();

        for(Pacman p : pC) {
            switch(p.getSex()) {
                case MALE:
                    p.move(startingPositions.PACMAN_MALE);
                    break;
                case FEMALE:
                    p.move(startingPositions.PACMAN_FEMALE);
                    break;
            }
            positionsToRender.add(p.getPosition());
        }

    }

    public boolean isObjectsPlaced() {
        return objectsPlaced;
    }

    public void markAllForRendering() {
        positionsToRender.add(positionContainer);
    }

    public enum Direction {

        NORTH, WEST, EAST, SOUTH;

        public static Direction guessDirection(MapObject mO) {
            Direction[] directions = Direction.values();
            Position guessedPosition = null;
            Direction guessedDirection = null;

            Helper.shuffle(directions);

            for (Direction direction : directions) {
                guessedPosition = Map.getPositionByDirectionIfMovableTo(mO.getPosition(), direction);
                if (guessedPosition != null) {
                    guessedDirection = direction;
                    break;
                }
            }
            if (guessedPosition == null) {
                throw new RuntimeException("Couldn't find any free position :(");
            } else {
                return guessedDirection;
            }
        }

    }

}
