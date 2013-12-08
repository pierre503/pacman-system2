/******************************************************************************
 * This work is applicable to the conditions of the MIT License,              *
 * which can be found in the LICENSE file, or at                              *
 * https://github.com/philippwinter/pacman/blob/master/LICENSE                *
 *                                                                            *
 * Copyright (c) 2013 Philipp Winter, Jonas Heidecke & Niklas Kaddatz         *
 ******************************************************************************/

package controller;

import model.Game;
import view.MainGui;

/**
 * The main controller that launches the Game and shows the main view.
 *
 * @author Philipp Winter
 * @author Jonas Heidecke
 * @author Niklas Kaddatz
 */
public class MainController {

    /**
     * The game instance.
     */
    private Game game;

    /**
     * The main window.
     */
    private MainGui gui;

    /**
     * The main method, the entry point of our user to the game.
     *
     * @param args The command line arguments given to the program.
     */
    public static void main(String[] args) {
        new MainController().start();
    }

    public MainGui getGui() {
        return gui;
    }

    /**
     * Start the application.
     */
    public void start() {
        this.game.start();
        // TODO Do something in the GUI as well
    }

    public void pause() {
        this.game.pause();
        // TODO Do something in the GUI as well
    }

    public MainController() {
        // Initialize and instantiate objects
        Game.initializeGame();

        this.game = Game.getInstance();
        this.gui = new MainGui();
    }

}