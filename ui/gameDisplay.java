package ui;

import combat.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

java.util.List<Move> playerMoves = new ArrayList<>();
  private Map<Move, JButton> playerMoveMap = new HashMap<>();
  private JPanel playerMovePanel;

  private final Object lock = new Object(); // Used to synchronize the wait/notify
    private Move chosenMove = null;
  
  public gameDisplay(Battlefield field, Unit player){
    this.field = field;
    this.player= player;
    this.allies = field.getAllies();
    this.enemies = field.getEnemies();
    this.playerMoves = player.getMoveSet();
    
    setTitle("Game Display");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
        setLayout(new BorderLayout(10, 10));

    displayHP();
    displayMoves();
    setVisible(true);
  }

  public Move getPlayerMoveChoice() {
        synchronized (lock) {
            chosenMove = null; 
            
            
            updateMoves(); 

            
            while (chosenMove == null) {
                try {
                    lock.wait(); 
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return null;
                }
            }
            return chosenMove;
        }
    }

  public void displayMoves(){
    playerMovePanel = new JPanel();
    
    playerMovePanel.setLayout(new BoxLayout(playerMovePanel, BoxLayout.X_AXIS)); 
    playerMovePanel.setBackground(Color.LIGHT_GRAY);
    playerMovePanel.add(new JLabel("Your Moves:"));

    for(Move mov: playerMoves){
      JButton but = createMoveButton(mov);
      but.addActionListener(new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e){
          synchronized(lock){
            chosenMove = mov;
            lock.notify();
          }
        }
      });
      playerMoveMap.put(mov,but);
        playerMovePanel.add(but);
      
    }
    updateMoves();
    JScrollPane movelist = new JScrollPane(playerMovePanel);

    add(movelist, BorderLayout.SOUTH);
    
  }

  

  private JButton createMoveButton(Move mov){
    JButton but = new JButton(mov.getName());
    return but;
  }

  public void updateMoves() {
    SwingUtilities.invokeLater(() -> {
        for (Map.Entry<Move, JButton> entry : playerMoveMap.entrySet()) {
            Move mov = entry.getKey();
            JButton button = entry.getValue();
            
            // Set whether the button is clickable based on usability
            button.setEnabled(mov.isUsable(field, player));
        }
    });
}
  
  public void displayHP(){
    allyContentPanel = new JPanel();
    enemContentPanel = new JPanel();

    allyContentPanel.setLayout(new BoxLayout(allyContentPanel, BoxLayout.Y_AXIS)); 
    allyContentPanel.setBackground(Color.LIGHT_GRAY);

    enemContentPanel.setLayout(new BoxLayout(enemContentPanel, BoxLayout.Y_AXIS));
    enemContentPanel.setBackground(Color.LIGHT_GRAY);
    
    
    allyContentPanel.add(new JLabel("Your Side"));

    enemContentPanel.add(new JLabel("Enemies"));

    playerHPBar = createHealthBar(player.getName(), player.getHP(), player.maxHP);
    allyContentPanel.add(playerHPBar);
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
