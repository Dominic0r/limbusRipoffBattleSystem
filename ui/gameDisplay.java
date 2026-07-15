package ui;

import combat.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class gameDisplay extends JFrame{
  java.util.List<Unit> allies = new ArrayList<>();
  java.util.List<Unit> enemies = new ArrayList<>();
  Battlefield field;
  Unit player;

  java.util.List<JProgressBar> allyHPBar = new ArrayList<>();
  java.util.List<JProgressBar> enemyHPBar = new ArrayList<>();

  JProgressBar playerHPBar;

  public JFrame frame;

  public gameDisplay(Battlefield field, Unit player){
    this.field = field;
    this.player= player;
    this.allies = field.getAllies();
    this.enemies = field.getEnemies();
    frame = new JFrame("Game Display");
    frame.setLayout(new BorderLayout());

    displayHP();
  }

  public void displayHP(){
    /*JTextArea allyList = new JTextArea();
    JTextArea enemList = new JTextArea();
    allyList.setEditable(false);
    allyList.setText("Allies: ");
    allyList.setBackground(Color.WHITE);
    allyList.setForeground(Color.BLACK);

    enemList.setEditable(false);
    enemList.setText("Enemies: ");
    enemList.setBackground(Color.WHITE);
    enemList.setForeground(Color.BLACK);*/

    JPanel allyPanel = new JPanel();
    JPanel enemPanel = new JPanel();
    
    allyPanel.setBackground(Color.LIGHT_GRAY);
    allyPanel.add(new JLabel("Your Side"));

    enemPanel.setBackground(Color.LIGHT_GRAY);
    enemPanel.add(new JLabel("Enemies"));

    allyPanel.add(createHealthBar(player.getName(), player.getHP(), player.maxHP));
    for(Unit un : allies){
      allyPanel.add(createHealthBar(un.getName(), un.getHP(), un.maxHP, Color.RED));
    }

    for(Unit un : enemies){
      enemPanel.add(createHealthBar(un.getName(), un.getHP(), un.maxHP, Color.RED));
    }

    frame.add(allyPanel, BorderLayout.WEST);
    frame.add(enemPanel, BorderLayout.EAST);
    
  }

  private JProgressBar createHealthBar(String name, int current, int max) {
        JProgressBar bar = new JProgressBar(0, max);
        bar.setValue(current);
        bar.setStringPainted(true);
        bar.setString(name + ": " + current + "/" + max + " HP");
        bar.setForeground(Color.RED);
        return bar;
    }
}
