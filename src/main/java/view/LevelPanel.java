package view;

import javax.swing.*;
import java.awt.*;
import java.io.File;

//import static view.MainGui.getLastLevelUnlocked;

public class LevelPanel extends JPanel {

    private JLabel lblBackground2;
    private JButton Play;
    public LevelPanel(){

    }
    public void generateLevelButtons(JPanel jpanel){
        /*JPanel buttonList = new JPanel();
        Play = new JButton("Play");
        Play.setFont(new Font("Agency FB", Font.PLAIN, 22));
        File file = new File("/home/geckoow/Bureau/UMONS/MASTER 1/SOFTWARE EVOLUTION/Project/pacman-system2/src/main/resources/maps/levels");
        lblBackground2 = new JLabel(new ImageIcon(this.getClass().getResource("/graphics/background/main_background_middle.jpg")));
        lblBackground2.setLayout(new FlowLayout());
        for(int i = 0; i <file.list().length;i++){
            LevelButton button = new LevelButton("level" + (i+1), (i+1));
            button.setFont(new Font("Agency FB", Font.PLAIN, 22));
            if((i+1) > getLastLevelUnlocked()) {
                button.setText("Locked");
                button.setLocked(true);
            }
            //button.setLocation(i*jpanel.getWidth(), i*jpanel.getHeight());
            buttonList.add(button);
        }
        //buttonList.add(level1);
        lblBackground2.add(buttonList);
        jpanel.add(lblBackground2);
        jpanel.add(Play, BorderLayout.SOUTH);*/
    }
}
