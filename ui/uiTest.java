package ui;

import combat.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class uiTest extends JFrame{
  List<Unit> allies = new ArrayList<>();
  List<Unit> enemies = new ArrayList<>();
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
    allyList = new JTextArea();
    allyList.setEditable(false);
    allyList.setText("Allies: ");

    for(Unit un : allies){
      allyList.append("\n"+ un.getName());
    }

    enemList = new JTextArea();
    enemList.setEditable(false);
    enemList.setText("\nEnemies: ");

    for(Unit un : enemies){
      enemList.append("\n"+ un.getName());
    }
  }
  
}
