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


  public gameDisplay(Battlefield field, Unit player){
    this.field = field;
    this.player= player;
    this.allies = field.getAllies();
    this.enemies = field.getEnemies();
    setTitle("Game Display");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
        setLayout(new BorderLayout(10, 10));

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


    JPanel allyContentPanel = new JPanel();
    // BoxLayout.Y_AXIS stacks components vertically
    allyContentPanel.setLayout(new BoxLayout(allyContentPanel, BoxLayout.Y_AXIS)); 
    allyContentPanel.setBackground(Color.LIGHT_GRAY);

    JPanel enemContentPanel = new JPanel();
    enemContentPanel.setLayout(new BoxLayout(enemContentPanel, BoxLayout.Y_AXIS));
    enemContentPanel.setBackground(Color.LIGHT_GRAY);
    
    
    allyContentPanel.add(new JLabel("Your Side"));

    enemContentPanel.add(new JLabel("Enemies"));

    allyContentPanel.add(createHealthBar(player.getName(), player.getHP(), player.maxHP));
    for(Unit un : allies){
      allyContentPanel.add(createHealthBar(un.getName(), un.getHP(), un.maxHP));
    }

    for(Unit un : enemies){
      enemContentPanel.add(createHealthBar(un.getName(), un.getHP(), un.maxHP));
    }

    JScrollPane allyScroll = new JScrollPane(allyContentPanel);
    JScrollPane enemScroll = new JScrollPane(enemContentPanel);

    add(allyScroll, BorderLayout.WEST);
    add(enemScroll, BorderLayout.EAST);
    
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
