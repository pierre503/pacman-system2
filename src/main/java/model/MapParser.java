package model;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.util.Hashtable;
import java.util.Scanner;

import javax.xml.parsers.*;
import java.io.*;

public class MapParser {

    public static MapInformation ParseMap(String file) {
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
}