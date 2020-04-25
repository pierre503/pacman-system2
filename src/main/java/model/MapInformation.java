package model;

import java.util.Hashtable;

public class MapInformation {
    int[][] board;
    Hashtable tile;

    public MapInformation(int[][]board, Hashtable tile){
        this.board = board;
        this.tile = tile;
    }

    private Position CheckingPlayerGhostsPower(){
        int nbrPlayer = 0;
        int nbrGhosts = 0;
        int nbrPower = 0;
        Position playerPosition = null;
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[0].length; j++){
                if(board[i][j]-1 == (int)tile.get("Wall"))
                    nbrGhosts++;
                else if(board[i][j]-1 == (int)tile.get("Pacman_Male")) {
                    nbrPlayer++;
                    playerPosition = new Position(i, j);
                }
                else if(board[i][j]-1 == (int)tile.get("SuperGums"))
                    nbrPower++;
            }
        }
        if(nbrPlayer == 1 && nbrGhosts == 4 && nbrPower <= 4)
            return playerPosition;
        else
            return null;
    }

    private boolean canEatCoin(Position player){
        boolean[][]
    }
    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public Hashtable getTile() {
        return tile;
    }

    public void setTile(Hashtable tile) {
        this.tile = tile;
    }

    private static class Position{
        public int x;
        public int y;
        public Position(int x, int y){
            this.x = x;
            this.y = y;
        }
    }
}
