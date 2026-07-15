package ui;

import combat.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class gameDisplay extends JFrame{
  java.util.List<Unit> allies = new ArrayList<>();
  java.util.List<Unit> enemies = new ArrayList<>();
  Battlefield field;
  Unit player;

  private Map<Unit, JProgressBar> allyBarsMap = new HashMap<>();
  private Map<Unit, JProgressBar> enemyBarsMap = new HashMap<>();

  JProgressBar playerHPBar;

  private JPanel allyContentPanel;
    private JPanel enemContentPanel;


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


    allyContentPanel.setLayout(new BoxLayout(allyContentPanel, BoxLayout.Y_AXIS)); 
    allyContentPanel.setBackground(Color.LIGHT_GRAY);

    enemContentPanel.setLayout(new BoxLayout(enemContentPanel, BoxLayout.Y_AXIS));
    enemContentPanel.setBackground(Color.LIGHT_GRAY);
    
    
    allyContentPanel.add(new JLabel("Your Side"));

    enemContentPanel.add(new JLabel("Enemies"));

    playerHPBar = createHealthBar(player.getName(), player.getHP(), player.maxHP);

    for (Unit un : allies) {
            JProgressBar bar = createHealthBar(un.getName(), un.getHP(), un.maxHP);
            allyBarsMap.put(un, bar);
            allyContentPanel.add(bar);
        }

    for (Unit un : enemies) {
            JProgressBar bar = createHealthBar(un.getName(), un.getHP(), un.maxHP);
            enemyBarsMap.put(un, bar);
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
            if (player.getHP() <= 0) {
                playerHPBar.setValue(0);
                playerHPBar.setString(player.getName() + " is DEAD");
                playerHPBar.setForeground(Color.DARK_GRAY);
            } else {
                updateBarValue(playerHPBar, player.getName(), player.getHP(), player.maxHP);
            }

            // 2. Process Allies and safely remove dead ones
            updateSide(allyBarsMap, allyContentPanel);

            // 3. Process Enemies and safely remove dead ones
            updateSide(enemyBarsMap, enemContentPanel);
        });
    }
  private void updateBarValue(JProgressBar bar, String name, int current, int max) {
        bar.setMaximum(max);
        bar.setValue(current);
        bar.setString(name + ": " + current + "/" + max + " HP");
    }

  private void updateSide(Map<Unit, JProgressBar> barMap, JPanel containerPanel) {
        Iterator<Map.Entry<Unit, JProgressBar>> iterator = barMap.entrySet().iterator();
        boolean structureChanged = false;

        while (iterator.hasNext()) {
            Map.Entry<Unit, JProgressBar> entry = iterator.next();
            Unit unit = entry.getKey();
            JProgressBar bar = entry.getValue();

            if (unit.getHP() <= 0) {
                // Remove visually from the GUI panel
                containerPanel.remove(bar);
                // Remove tracking entry from our map
                iterator.remove(); 
                structureChanged = true;
            } else {
                // Otherwise, just update its layout figures
                updateBarValue(bar, unit.getName(), unit.getHP(), unit.maxHP);
            }
        }

        // If components were removed, tell Swing to recalculate layout positions
        if (structureChanged) {
            containerPanel.revalidate();
            containerPanel.repaint();
        }
    }

}
