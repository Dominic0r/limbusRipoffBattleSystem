package ui;

import combat.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class uiTest extends JFrame{
  java.util.List<Unit> allies = new ArrayList<>();
  java.util.List<Unit> enemies = new ArrayList<>();
  Battlefield field;

  public uiTest(Battlefield field){
    this.field = field;
    allies = field.getAllies();
    enemies = field.getEnemies();

    setTitle("Test UI");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
        setLayout(new BorderLayout(10, 10));
    
    displayAlliesAndEnemies();

    
  }

  public void displayAlliesAndEnemies(){
    JTextArea allyList = new JTextArea();
    allyList.setEditable(false);
    allyList.setText("Allies: ");
    allyList.setBackground(new Color(25, 25, 25));
    allyList.setForeground(Color.CYAN);

    for(Unit un : allies){
      allyList.append("\n"+ un.getName());
    }

    JScrollPane scrollPaneAlly = new JScrollPane(allyList);
    add(scrollPaneAlly, BorderLayout.CENTER);

    JTextArea enemList = new JTextArea();
    enemList.setEditable(false);
    enemList.setText("\nEnemies: ");
    enemList.setBackground(new Color(25, 25, 25));
    enemList.setForeground(Color.CYAN);

    for(Unit un : enemies){
      enemList.append("\n"+ un.getName());
    }
    JScrollPane scrollPaneEnemy = new JScrollPane(enemList);
    add(scrollPaneEnemy, BorderLayout.CENTER);
  }
  
}
