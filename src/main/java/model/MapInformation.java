package model;

import java.util.Hashtable;

public class MapInformation {
    int[][] board;
    Hashtable tile;

    public MapInformation(int[][]board, Hashtable tile){
        this.board = board;
        this.tile = tile;
    }

    public void isMapValid(){
        Position player = CheckingPlayerGhostsPower();
        if(player == null){
            try {
                throw new Exception("Invalid Map, no pacman found");
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        else
            if(canEatCoin(player) == false){
                try {
                    throw new Exception("Invalid Map, collectible inaccessible");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            };
    }

    private Position CheckingPlayerGhostsPower(){
        int nbrPlayer = 0;
        int nbrGhosts = 0;
        int nbrPower = 0;
        Position playerPosition = null;
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[0].length; j++){
                if(isCaseGhost(i, j))
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
        boolean[][] virtualBoard = new boolean[board.length][board[0].length];
        virtualBoard[player.x][player.y] = true;
        travelBoard(virtualBoard, player);
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j<board[0].length; j++){
                if(board[i][j]-1 == (int)tile.get("Gums") || isCaseFruit(i, j) || board[i][j]-1 == (int)tile.get("SuperGums")){
                    if(!virtualBoard[i][j]){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void caseChecking(boolean[][] virtualBoard, int x, int y){
        if(virtualBoard[x][y] != true){
            virtualBoard[x][y] = true;
            if(board[x][y]-1 != (int)tile.get("Wall")){
                travelBoard(virtualBoard, new Position(x,y));
            }
        }
    }

    private void travelBoard(boolean[][] virtualBoard, Position position){
        int right = position.x + 1;
        int left = position.x - 1;
        int up = position.y + 1;
        int down = position.y -1;
        if(right >= board.length)
            right = 0;
        if(left < 0)
            left = board.length - 1;
        if(up > board[0].length)
            up = 0;
        if(down < 0)
            down = board[0].length - 1;
        caseChecking(virtualBoard, right, position.y);
        caseChecking(virtualBoard, left, position.y);
        caseChecking(virtualBoard, position.x, up);
        caseChecking(virtualBoard, position.x, down);
    }

    public boolean isCaseGhost(int i, int j){
        if(board[i][j]-1 == (int)tile.get("Red-Ghost"))
            return true;
        else if(board[i][j]-1 == (int)tile.get("Orange-Ghost"))
            return true;
        else if(board[i][j]-1 == (int)tile.get("Blue-Ghost"))
            return true;
        else if(board[i][j]-1 == (int)tile.get("Pink-Ghost"))
            return true;
        else
            return false;
    }
    public boolean isCaseFruit(int i, int j){
        if(board[i][j]-1 == (int)tile.get("Apple"))
            return true;
        else if(board[i][j]-1 == (int)tile.get("Orange"))
            return true;
        else if(board[i][j]-1 == (int)tile.get("Cherry"))
            return true;
        else if(board[i][j]-1 == (int)tile.get("Grapes"))
            return true;
        else if(board[i][j]-1 == (int)tile.get("Strawberry"))
            return true;
        else
            return false;
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
