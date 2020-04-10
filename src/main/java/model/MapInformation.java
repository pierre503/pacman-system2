package model;

import java.util.Hashtable;

public class MapInformation {
    int[][] board;
    Hashtable tile;

    public MapInformation(int[][]board, Hashtable tile){
        this.board = board;
        this.tile = tile;
    }
}
