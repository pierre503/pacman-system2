package view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LevelButton extends JButton implements ActionListener {

    private int level;

    private boolean locked;
    public LevelButton(String text, int level){
        super(text);
        this.level = level;
        this.locked = false;
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(locked == false) {
            System.out.println(level);
            MainGui.setLevelSelected(this.level);
        }
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
