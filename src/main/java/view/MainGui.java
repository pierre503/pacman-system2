/******************************************************************************
 * This work is applicable to the conditions of the MIT License,              *
 * which can be found in the LICENSE file, or at                              *
 * https://github.com/philippwinter/pacman/blob/master/LICENSE                *
 *                                                                            *
 * Copyright (c) 2013 Philipp Winter, Jonas Heidecke & Niklas Kaddatz         *
 ******************************************************************************/

package view;

import controller.MainController;
import model.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.Vector;

public class MainGui extends JFrame {

    private static final long serialVersionUID = 6812319670817799344L;

    private final MainController controller;

    private final ImageOrganizer imgOrganizer;

    private final Renderer renderer;
    private final File nbrcampaign;
    private final File[] nbrcampaignF;

    private JPanel pnlPreGame;

    private GamePanel pnlGame;

    private JPanel pnlButtons;

    private JLabel lblBackground;

    private JButton btnPlaySingleplayer;

    private JButton btnPlayMultiplayer;

    private JButton btnPause;

    private ActionListener toggleGameStateListener;

    private boolean initialized;

    private JPanel levelSelection;

    private JPanel levelButtons;

    private JButton Play;

    private JLabel lblBackground2;

    private ActionListener testListener;

    private static boolean gameRunning;

    private static String path;

    private File file;

    private static File[] f;

    public static int levelSelected;

    private JPanel GameMode;

    private JLabel lblBackground3;

    private JButton normal;
    private JButton campaign;

    private JPanel modeButton;
    private ActionListener test2Listener;

    private JPanel CampaignSelection;
    private JButton campaignPlay;
    private JButton nextCampaign;
    private JButton previousCampaign;

    private File campaignFile;
    private int campaignIndex = 1;
    private ActionListener nextLister;
    private File[] campaignF;

    private Font fontBtn = new Font("Agency FB", Font.PLAIN, 22);

    private LoadSave loadSave;

    public MainGui() {
        loadSave = new LoadSave(this);
        file = new File("src/main/resources/maps/levels");
        nbrcampaign = new File("src/main/resources/maps/campaign");
        campaignFile = loadSave.getCampaignPath();
        f = file.listFiles();
        campaignF = campaignFile.listFiles();
        nbrcampaignF = nbrcampaign.listFiles();
        Arrays.sort(f);
        Arrays.sort(campaignF);
        Arrays.sort(nbrcampaignF);
        levelSelected = 1;
        controller = MainController.getInstance();
        imgOrganizer = ImageOrganizer.getInstance();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        renderer = new Renderer(this);

        this.initialize();
    }

    private synchronized void initialize() {
        if (!this.initialized) {
            setTitle("Pacman");

            try {
                setIconImage(ImageIO.read(this.getClass().getResource("/graphics/resized/pacman/4_east.png")));
            } catch (IOException e) {
                MainController.uncaughtExceptionHandler.uncaught(e);
            }

            int windowWidth = renderer.mapWidth + (renderer.leftSpace * 2);
            int windowHeight = renderer.mapHeight + (renderer.topSpace * 3);

            System.out.println(windowWidth + "x" + windowHeight);

            setSize(windowWidth, windowHeight);
            setLocationRelativeTo(null);
            setResizable(false);
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            getContentPane().setLayout(new BorderLayout());

            initializeComponents();
            initializeListeners();

            setVisible(true);

            this.initialized = true;
        }
    }

    private void initializeComponents() {
        pnlPreGame = new JPanel();
        pnlPreGame.setLayout(new BorderLayout());

        pnlGame = new GamePanel();
        pnlGame.setLayout(new FlowLayout());

        levelSelection = new JPanel();
        levelSelection.setLayout(new BorderLayout());

        GameMode = new JPanel();
        GameMode.setLayout(new BorderLayout());

        CampaignSelection = new JPanel();
        CampaignSelection.setLayout(new BorderLayout());

        lblBackground3 = new JLabel(new ImageIcon(this.getClass().getResource("/graphics/background/main_background_middle.jpg")));
        lblBackground3.setLayout(new FlowLayout());

        modeButton = new JPanel();
        modeButton.setLayout(new FlowLayout());
        modeButton.setOpaque(false);

        lblBackground = new JLabel(new ImageIcon(this.getClass().getResource("/graphics/background/main_background_middle.jpg")));
        lblBackground.setLayout(new FlowLayout());

        pnlButtons = new JPanel();
        pnlButtons.setLayout(new FlowLayout());
        pnlButtons.setOpaque(false);

        levelButtons = new JPanel();
        levelButtons.setLayout(new FlowLayout());
        levelButtons.setOpaque(false);

        btnPlaySingleplayer = new JButton("Singleplayer");
        btnPlaySingleplayer.setFont(fontBtn);

        btnPlayMultiplayer = new JButton("Multiplayer");
        btnPlayMultiplayer.setFont(fontBtn);

        btnPause = new JButton("Pause");
        btnPause.setFont(fontBtn);

        Play = new JButton("Play");
        Play.setFont(fontBtn);

        normal = new JButton("Normal");
        normal.setFont(fontBtn);

        campaign = new JButton("Campaign");
        campaign.setFont(fontBtn);

        campaignPlay = new JButton("Play");
        campaignPlay.setFont(fontBtn);

        pnlGame.add(btnPause);

        pnlButtons.add(btnPlaySingleplayer);
        Dimension dim = new Dimension(200, 0);
        pnlButtons.add(new Box.Filler(dim, dim, dim));
        pnlButtons.add(btnPlayMultiplayer);

        modeButton.add(normal);
        modeButton.add(new Box.Filler(dim, dim, dim));
        modeButton.add(campaign);

        lblBackground.add(pnlButtons);

        lblBackground3.add(modeButton);

        pnlPreGame.add(lblBackground);

        generateLevelButtons(levelSelection, file, Play);

        CampaignSelection = generateCampaignPanel();

        GameMode.add(lblBackground3);

        getContentPane().add(pnlPreGame);
        gameRunning = false;
    }

    private void initializeListeners() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Highscore.getInstance().writeToFile();
                super.windowClosing(e);
            }
        });

        toggleGameStateListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(isGameRunning());
                if (controller.isGameActive()) {
                    controller.pauseGame();
                } else {
                    if(e.getSource() == btnPlaySingleplayer){
                        Settings.getInstance().setGameMode(Game.Mode.SINGLEPLAYER);
                    } else if(e.getSource() == btnPlayMultiplayer){
                        Settings.getInstance().setGameMode(Game.Mode.MULTIPLAYER);
                    }
                    //getContentPane().removeAll();
                    //getContentPane().add(levelSelection);
                    //setContentPane(levelSelection);
                    MapInformation m = MapParser.ParseTxtMap("src/main/resources/maps/levels/Pacman3.txt");
                    MapInformation m2 = MapParser.ParseTmxMap("src/main/resources/maps/levels/Pacman2.tmx");
                    System.out.println(m.getBoard()[0].length);
                    System.out.println(m2.getBoard()[0].length);
                    if(gameRunning == false) {
                        btnPlaySingleplayer.setText("Pause");
                        getContentPane().removeAll();
                        getContentPane().add(GameMode);
                    }
                    else
                        controller.startGame(levelSelected);
                    //controller.startGame();
                    //btnPlaySingleplayer.setText("Pause");
                    //getContentPane().removeAll();
                    //getContentPane().add(GameMode);
                }
                repaint();
            }

        };

        testListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                    if (e.getSource() == Play) {
                        controller.startGame(levelSelected);
                        gameRunning = true;
                    }
                    else if(e.getSource() == campaignPlay){
                        campaignPlay.setText("pause");
                        controller.startGame(levelSelected);
                        gameRunning = true;
                    }
                repaint();
            }

        };

        test2Listener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                    if (e.getSource() == normal) {
                        Settings.getInstance().setGameType(Game.Mode.NORMAL);
                        Settings.getInstance().setLevelPath("src/main/resources/maps/levels");
                        normalPlay();
                    }
                    else if (e.getSource() == campaign){
                        Settings.getInstance().setGameType(Game.Mode.CAMPAIGN);
                        Settings.getInstance().setLevelPath("src/main/resources/maps/campaign/campaign" + campaignIndex);
                        campaign.setText("Pause");
                        getContentPane().removeAll();
                        getContentPane().add(generateCampaignPanel());
                    }
                repaint();
            }

        };

        nextLister = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                    if (e.getSource() == nextCampaign) {
                        if(campaignIndex < nbrcampaignF.length) {
                            campaignIndex++;
                            Settings.getInstance().setLevelPath("src/main/resources/maps/campaign/campaign" + campaignIndex);
                            switchCampaign(nextCampaign);
                        }
                    }
                    else if (e.getSource() == previousCampaign){
                        if(campaignIndex > 1) {
                            campaignIndex--;
                            Settings.getInstance().setLevelPath("src/main/resources/maps/campaign/campaign" + campaignIndex);
                            switchCampaign(previousCampaign);
                        }
                    }
                repaint();
            }

        };
        
        btnPause.addActionListener(toggleGameStateListener);
        Play.addActionListener(testListener);
        campaignPlay.addActionListener(testListener);
        normal.addActionListener(test2Listener);
        campaign.addActionListener(test2Listener);
        nextCampaign.addActionListener(nextLister);
        previousCampaign.addActionListener(nextLister);
        btnPlaySingleplayer.addActionListener(toggleGameStateListener);
        btnPlayMultiplayer.addActionListener(toggleGameStateListener);

        KeyListener keyEventListener = new KeyboardProcesser();
        this.addKeyListener(keyEventListener);
        pnlGame.addKeyListener(keyEventListener);
    }

    public void showPreGame() {
        btnPause.setText("Play");
        setTitle("Pacman: Game paused");
    }

    public void generateLevelButtons(JPanel jpanel, File file, JButton Playbutton){
        JPanel buttonList = new JPanel();
        lblBackground2 = new JLabel(new ImageIcon(this.getClass().getResource("/graphics/background/main_background_middle.jpg")));
        lblBackground2.setLayout(new FlowLayout());
        int lengthList = file.list().length - 1;
        if(Settings.getInstance().getGameType() == Game.Mode.CAMPAIGN)
            lengthList = file.list().length -2;
        for(int i = 0; i <lengthList; i++){
            LevelButton button = new LevelButton("level" + (i+1), (i+1));
            button.setFont(new Font("Agency FB", Font.PLAIN, 22));
            if((i+1) > loadSave.getLastUnlocked() && i > 0) {
                button.setText("Locked");
                button.setLocked(true);
            }
            //button.setLocation(i*jpanel.getWidth(), i*jpanel.getHeight());
            button.setStars(loadSave.getLevelStar(i+1));
            buttonList.add(button);
        }
        //buttonList.add(level1);
        lblBackground2.add(buttonList);
        jpanel.add(lblBackground2);
        jpanel.add(Playbutton, BorderLayout.SOUTH);
    }
    public JPanel generateCampaignPanel(){
        JPanel panel = new JPanel(new BorderLayout());
        generateLevelButtons(panel, campaignFile, campaignPlay);

        nextCampaign = new JButton("->");
        nextCampaign.setFont(fontBtn);
        nextCampaign.addActionListener(nextLister);

        previousCampaign = new JButton("<-");
        previousCampaign.setFont(fontBtn);
        previousCampaign.addActionListener(nextLister);

        panel.add(nextCampaign, BorderLayout.EAST);
        panel.add(previousCampaign, BorderLayout.WEST);

        if(loadSave.getCampaignTotalStars() >= 6)
            nextCampaign.setVisible(true);
        else
            nextCampaign.setVisible(false);

        return panel;
    }
    /*public int getLastUnlocked(){
        InputStream flux= null;
        String filePath = null;
        int maxLevel = 0;
        try {
            filePath = "src/main/resources/maps/levels/saveInfo.txt";
            if(Settings.getInstance().getGameType() == Game.Mode.CAMPAIGN)
                filePath = "src/main/resources/maps/campaign/campaign"+campaignIndex+"/saveInfo.txt";
            flux = new FileInputStream(filePath);
            InputStreamReader lecture=new InputStreamReader(flux);
            BufferedReader buff=new BufferedReader(lecture);
            String ligne =buff.readLine();
            maxLevel = Integer.parseInt(ligne);
        } catch (FileNotFoundException e) {
            initSave(1, filePath);
        }
        catch (IOException ie){
            System.out.println("saveIOException "+ie);
        }
        return maxLevel;
    }
    public void initSave(int level, String path){
        try {
            String pathing = "src/main/resources/maps/levels";
            File[] p = new File(pathing).listFiles();
            Arrays.sort(p);
            int nbrlevel = p.length;
            if(Settings.getInstance().getGameType() == Game.Mode.CAMPAIGN){
                pathing = "src/main/resources/maps/campaign/campaign"+campaignIndex;
                p = (new File(pathing)).listFiles();
                Arrays.sort(p);
                nbrlevel = p.length -1;
            }
            FileWriter fw = new FileWriter(path);
            fw.write(level + "\r\n");
            for(int i = 0; i < nbrlevel; i++){
                fw.write(p[i].getPath() + " " + 0);
                fw.write("\r\n");
            }
            fw.close();
        }catch (IOException e){
            System.out.println("saveIOException "+ e);
        }
    }

    public static void saveCopy(String source, String destination, String levelPath, int levelStar, int unlockLevel){
        try{
            InputStream flux=new FileInputStream(source);
            InputStreamReader lecture=new InputStreamReader(flux);
            BufferedReader buff=new BufferedReader(lecture);
            FileWriter fw = new FileWriter(destination);
            String ligne;
            while ((ligne=buff.readLine())!=null){
                String[] mots = ligne.split(" ");
                if(mots.length == 1){
                    if(Integer.parseInt(mots[0]) < unlockLevel && unlockLevel < 999)
                        fw.write("" + unlockLevel + "\r\n");
                    else
                        fw.write(mots[0] + "\r\n");
                }
                else {
                    fw.write(mots[0] + " ");
                    System.out.println((mots[0]) + "  " + levelPath+"klm");
                    if((mots[0]).equals(levelPath)){
                        if(Integer.parseInt(mots[1]) < levelStar)
                            fw.write(levelStar + "\r\n");
                        else
                            fw.write(mots[1] + "\r\n");
                    }
                    else{
                        fw.write(mots[1] + "\r\n");
                    }
                }
            }
            fw.close();
            buff.close();
        }
        catch (Exception e){
            System.out.println("save ERROR on save "+e);
        }
    }

    public static void save(String levelPath, int levelStar, int unlockLevel){
        String destination = "save1.save";
        saveCopy(Settings.getInstance().getLevelPath()+ "/saveInfo.txt", destination, levelPath, levelStar, unlockLevel);
        saveCopy(destination, Settings.getInstance().getLevelPath()+ "/saveInfo.txt", levelPath, levelStar, unlockLevel);
        File delete = new File(destination);
        delete.delete();
    }

    public int getLevelStar(int level) {
        int nbrStar = 0;
        try {
            String filePath = "src/main/resources/maps/levels/saveInfo.txt";
            if(Settings.getInstance().getGameType() == Game.Mode.CAMPAIGN)
                filePath = "src/main/resources/maps/campaign/campaign"+campaignIndex+"/saveInfo.txt";
            InputStream flux = new FileInputStream(filePath);
            InputStreamReader lecture = new InputStreamReader(flux);
            BufferedReader buff = new BufferedReader(lecture);
            String ligne;
            int index = 0;
            while ((ligne = buff.readLine()) != null) {
                String[] mots = ligne.split(" ");
                if(index == level){
                    nbrStar = Integer.parseInt(mots[1]);
                }
                index++;
            }
        } catch (Exception e) {
            System.out.println("error");
        }
        return nbrStar;
    }
    public int getCampaignTotalStars(){
        int total = 0;
        String pathing = "src/main/resources/maps/campaign/campaign"+campaignIndex;
        File[] p = (new File(pathing)).listFiles();
        Arrays.sort(p);
        int nbrlevel = p.length -2;
        for(int i = 0; i < nbrlevel; i++){
            total = total + getLevelStar(i+1);
        }
        return total;

    }
    public File getCampaignPath(){
        return new File("src/main/resources/maps/campaign/campaign" + campaignIndex);
    }*/

    public static void setLevelSelected(int newLevel) {
        levelSelected = newLevel;
    }

    public static boolean isGameRunning() {
        return gameRunning;
    }

    public static void setGameRunning(boolean running){
        gameRunning = running;
    }

    public static File[] getF() {
        return f;
    }

    public void normalPlay(){
        normal.setText("Pause");
        normal.setText("Normal");
        getContentPane().removeAll();
        getContentPane().add(levelSelection);
        //Map.getInstance().markAllForRendering();
        //renderer.markReady();
    }

    public void switchCampaign(JButton button){
        button.setText("pause");
        getContentPane().removeAll();
        campaignFile = loadSave.getCampaignPath();
        getContentPane().add(generateCampaignPanel());
    }

    public void showGame() {
        Play.setText("Pause");
        btnPause.setText("Pause");
        getContentPane().removeAll();
        getContentPane().add(pnlGame);
        setTitle("Pacman: Game running");

        Map.getInstance().markAllForRendering();
        renderer.markReady();
        //repaint();
    }

    public JPanel getPnlGame() {
        return pnlGame;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public void onFinishScreen(String text){
        btnPause.setText(text);
        btnPause.removeActionListener(toggleGameStateListener);
        btnPause.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                getContentPane().removeAll();
                getContentPane().add(pnlPreGame);
                btnPause.removeActionListener(this);
                btnPause.addActionListener(toggleGameStateListener);
                btnPlaySingleplayer.setText("Singleplayer");
                dispose();
                MainController.reset();
                repaint();
            }
        });
    }

    public void endCampaign(){
        onFinishScreen("BACK");
    }

    public void onGameOver() {
        onFinishScreen("GAME OVER");
    }

    public int getCampaignIndex() {
        return campaignIndex;
    }

    public LoadSave getLoadSave() {
        return loadSave;
    }

    private class GamePanel extends JPanel {

        private static final long serialVersionUID = -6138420780196252730L;

        @Override
        public void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            JPanel pnl = this;
            Graphics2D g = (Graphics2D) graphics;

            g.clearRect(
                    0,
                    0,
                    pnl.getWidth(),
                    pnl.getHeight());

            for (Position pos : Map.positionsToRender) {
                final int paintX = pos.getX() * renderer.multiplier + renderer.leftSpace;
                final int paintY = pos.getY() * renderer.multiplier + renderer.topSpace;

                g.clearRect(
                        paintX,
                        paintY,
                        renderer.multiplier,
                        renderer.multiplier
                );

                Vector<MapObject> mapObjects = pos.getOnPosition().getAll();

                for (MapObject mO : mapObjects) {
                    if (mO.isVisible()) {
                        BufferedImage img = imgOrganizer.get(mO);
                        g.drawImage(
                                img,
                                null,
                                paintX,
                                paintY
                        );
                    }
                }
            }

            int i = 0;

            for (Pacman p : Game.getInstance().getPacmanContainer()) {
                renderer.drawString(g, "Highscore of " + p.getName() + ":\t" + p.getScore().getScore(), ++i);
            }

            renderer.drawString(g, "Player Lifes: " + Game.getInstance().getPlayerLifes(), ++i);
            renderer.drawString(g, "Level: " + Game.getInstance().getLevel().getLevel(), ++i);

            int j = 0;
            for (Score s : Highscore.getInstance().getScores()) {
                renderer.drawString(g, "Score #" + (++j) + " " + s.toString(), ++i);
            }

            requestFocusInWindow();
        }
    }
    private class LevelButton extends JButton implements ActionListener{
        private int level;

        private boolean locked;

        private int stars;

        private JPanel panel;
        public LevelButton(String text, int level){
            super(text);
            this.level = level;
            this.locked = false;
            this.panel = panel;
            addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if(locked == false) {
                setText(showStars());
                setLevelSelected(this.level);
            }
        }

        public String showStars(){
            if(stars == 0)
                return "       ";
            else if(stars == 1)
                return "   *   ";
            else if(stars == 2)
                return "  **   ";
            else
                return "  ***  ";
        }

        public boolean isLocked() {
            return locked;
        }

        public void setLocked(boolean locked) {
            this.locked = locked;
        }

        public void setStars(int stars) {
            this.stars = stars;
        }
    }
}
