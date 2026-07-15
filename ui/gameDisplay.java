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

    setVisible(true);
  }

  public void displayHP(){


    JPanel allyContentPanel = new JPanel();
    allyContentPanel.setLayout(new BoxLayout(allyContentPanel, BoxLayout.Y_AXIS)); 
    allyContentPanel.setBackground(Color.LIGHT_GRAY);

    JPanel enemContentPanel = new JPanel();
    enemContentPanel.setLayout(new BoxLayout(enemContentPanel, BoxLayout.Y_AXIS));
    enemContentPanel.setBackground(Color.LIGHT_GRAY);
    
    
    allyContentPanel.add(new JLabel("Your Side"));

    enemContentPanel.add(new JLabel("Enemies"));

    playerHPBar = createHealthBar(player.getName(), player.getHP(), player.maxHP);

    allyContentPanel.add(playerHPBar);
    for(Unit un : allies){
      JProgressBar bar = createHealthBar(un.getName(), un.getHP(), un.maxHP);
      allyHPBar.add(bar);
      allyContentPanel.add(bar);
    }

    for(Unit un : enemies){
      JProgressBar bar = createHealthBar(un.getName(), un.getHP(), un.maxHP);
      enemyHPBar.add(bar);
      enemContentPanel.add(bar);
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

  public void updateHP() {
        // SwingUtilities.invokeLater ensures this runs safely on the UI thread
        SwingUtilities.invokeLater(() -> {
            // Update Player
            updateBarValue(playerHPBar, player.getName(), player.getHP(), player.maxHP);

            // Update Allies (assuming the lists stay in the same order)
            for (int i = 0; i < allies.size(); i++) {
                Unit unit = allies.get(i);
                JProgressBar bar = allyHPBar.get(i);
                updateBarValue(bar, unit.getName(), unit.getHP(), unit.maxHP);
            }

            // Update Enemies
            for (int i = 0; i < enemies.size(); i++) {
                Unit unit = enemies.get(i);
                JProgressBar bar = enemyHPBar.get(i);
                updateBarValue(bar, unit.getName(), unit.getHP(), unit.maxHP);
            }
        });
    }
  private void updateBarValue(JProgressBar bar, String name, int current, int max) {
        bar.setMaximum(max);
        bar.setValue(current);
        bar.setString(name + ": " + current + "/" + max + " HP");
    }

}
