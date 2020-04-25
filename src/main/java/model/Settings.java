/******************************************************************************
 * This work is applicable to the conditions of the MIT License,              *
 * which can be found in the LICENSE file, or at                              *
 * https://github.com/philippwinter/pacman/blob/master/LICENSE                *
 *                                                                            *
 * Copyright (c) 2013 Philipp Winter, Jonas Heidecke & Niklas Kaddatz         *
 ******************************************************************************/

package model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Settings
 *
 * @author Philipp Winter
 */
public class Settings {

    private Game.Mode gameMode = Game.Mode.SINGLEPLAYER;

    private Game.Mode gameType = Game.Mode.NORMAL;

    private String levelPath = "src/main/resources/maps/levels";

    private static Settings instance = new Settings();

    public Settings() {
        // TODO Implement class
    }

    public static Settings getInstance() {
        return instance;
    }

    public static void setInstance(Settings instance) {
        Settings.instance = instance;
    }

    public Game.Mode getGameMode() {
        return gameMode;
    }

    public void setGameMode(Game.Mode gameMode) {
        this.gameMode = gameMode;
    }

    public Game.Mode getGameType() {
        return gameType;
    }

    public void setGameType(Game.Mode gameType) {
        this.gameType = gameType;
    }

    public String getLevelPath() {
        return levelPath;
    }

    public void setLevelPath(String levelPath) {
        this.levelPath = levelPath;
    }

    public String getExtension(String path){
        try {
            InputStream flux = new FileInputStream(levelPath + "/saveInfo.txt");
            InputStreamReader lecture = new InputStreamReader(flux);
            BufferedReader buff = new BufferedReader(lecture);
            String ligne;
            int index = 0;
            String extension = ".";
            while ((ligne = buff.readLine()) != null) {
                String[] mots = ligne.split(" ");
                if (index > 0) {
                    String[] ext = mots[0].split("\\.");
                    if(ext[0].equals(path))
                        extension = extension + ext[1];
                    }
                index++;
            }
            return extension;
        }catch (Exception e){}
        return null;
    }
}
