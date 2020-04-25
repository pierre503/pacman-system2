package model;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.util.Hashtable;
import java.util.Scanner;

import javax.xml.parsers.*;
import java.io.*;

public class MapParser {

    public static MapInformation ParseMap(String file){
        if(Settings.getInstance().getExtension(file).equals(".tmx"))
            return ParseTmxMap(file + ".tmx");
        else if(Settings.getInstance().getExtension(file).equals(".txt")) {
            System.out.println("test");
            return ParseTxtMap(file + ".txt");
        }
        else
            return null;
    }

    public static MapInformation ParseTmxMap(String file) {
        try {
            File outFile = new File(file);

            Hashtable tile = new Hashtable();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(outFile);

            Element root = doc.getDocumentElement();
            Node layer = root.getElementsByTagName("layer").item(0);
            int height = Integer.parseInt(root.getAttributes().getNamedItem("height").getTextContent());
            int width = Integer.parseInt(root.getAttributes().getNamedItem("width").getTextContent());
            NodeList tileset = root.getElementsByTagName("tile");
            for(int n =0; n < tileset.getLength() ; n++){
                int id = Integer.parseInt(tileset.item(n).getAttributes().getNamedItem("id").getTextContent());
                String type = tileset.item(n).getAttributes().getNamedItem("type").getTextContent();
                System.out.println(id + " / " + type);
                tile.put(type,id);
            }
            Scanner scan = new Scanner(layer.getTextContent());
            scan.useDelimiter(",|\n");



            int[][] board = new int[width][height];
            int i = 0;
            int j = 0;
            int n = 1;
            String out = null;
            while(scan.hasNext()){
                out = scan.next();
                n++;
                if(!(out.contains(" ")||out.length()==0)) {
                    if (i == width) {
                        i = 0;
                        j = j + 1;
                    }
                    board[i][j] = Integer.parseInt(out);
                    i += 1;
                }
            }

            return new MapInformation(board, tile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static MapInformation ParseTxtMap(String file){
        try{
            Hashtable tile = generateTile();

            InputStream flux = new FileInputStream(file);
            InputStreamReader lecture = new InputStreamReader(flux);
            BufferedReader buff = new BufferedReader(lecture);
            String ligne;
            int index = 0;
            int i = 0;
            int[][] board = null;
            while ((ligne = buff.readLine()) != null) {
                String[] mots = ligne.split(",");
                if(index == 0){
                    System.out.println(mots[0] + " " + mots[1]);
                    board = new int[Integer.parseInt(mots[0])][Integer.parseInt(mots[1])];
                }
                else{
                    System.out.println(mots.length);
                    for(int j = 0; j < mots.length; j++){
                        board[j][i] = Integer.parseInt(mots[j]);
                    }
                    i++;
                }
                index++;
            }
            return new MapInformation(board, tile);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Hashtable generateTile(){
        Hashtable tile = new Hashtable();
        tile.put("Wall", 0);
        tile.put("Holder", 1);
        tile.put("Red-Ghost", 2);
        tile.put("Orange-Ghost", 3);
        tile.put("Blue-Ghost", 4);
        tile.put("Pink-Ghost", 5);
        tile.put("Pacman_Male", 6);
        tile.put("SuperGums", 7);
        tile.put("Key", 8);
        tile.put("Apple", 9);
        tile.put("Orange", 10);
        tile.put("Cherry", 11);
        tile.put("Grapes", 12);
        tile.put("Strawberry", 13);
        tile.put("Gums", 14);
        tile.put("Pacman_Female", 15);
        return tile;
    }
}