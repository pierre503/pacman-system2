package view;

import model.Game;
import model.Settings;

import java.io.*;
import java.util.Arrays;

public class LoadSave {

    private final MainGui gui;

    public LoadSave(MainGui gui){
        this.gui = gui;
    }

    public int getLastUnlocked(){
        InputStream flux= null;
        String filePath = null;
        int maxLevel = 0;
        try {
            filePath = "src/main/resources/maps/levels/saveInfo.txt";
            if(Settings.getInstance().getGameType() == Game.Mode.CAMPAIGN)
                filePath = "src/main/resources/maps/campaign/campaign"+gui.getCampaignIndex()+"/saveInfo.txt";
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
                pathing = "src/main/resources/maps/campaign/campaign"+gui.getCampaignIndex();
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
            copy(source, destination, levelPath, levelStar, unlockLevel);
        }
        catch (Exception e){
            System.out.println("save ERROR on save "+e);
        }
    }

    private static void copy(String source, String destination, String levelPath, int levelStar, int unlockLevel) throws IOException {
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
                filePath = "src/main/resources/maps/campaign/campaign"+gui.getCampaignIndex()+"/saveInfo.txt";
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
        String pathing = "src/main/resources/maps/campaign/campaign"+gui.getCampaignIndex();
        File[] p = (new File(pathing)).listFiles();
        Arrays.sort(p);
        int nbrlevel = p.length -2;
        for(int i = 0; i < nbrlevel; i++){
            total = total + getLevelStar(i+1);
        }
        return total;

    }
    public File getCampaignPath(){
        return new File("src/main/resources/maps/campaign/campaign" + gui.getCampaignIndex());
    }
}
