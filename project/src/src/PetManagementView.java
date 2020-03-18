
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author User
 */
public class PetManagementView extends JFrame{
    
    private PetManagementController controller;
    private JPanel centerPanel, southPanel, eastPanel, westPanel;
    private JButton cleanButton,playButton,sleepButton,eatButton;
    public boolean startGame = false;

    
    
    public PetManagementView() {

            super("Tamagotichi");
            this.setVisible(false);
            southPanel = new JPanel();
            eastPanel = new JPanel();
            westPanel = new JPanel();
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent we) { //overriding this method, so the game can be saved when the window is closed.
                    if(controller != null) {
                        controller.saveGame();
                    }
                    System.exit(0);
                }
            });
            
        }
   

    public void addController(PetManagementController controller) {
        this.controller = controller;
    } 
    
    public void startGame() {
        //initiating game start
        startGame = true;
        
        //createPanels
        centerPanel = new AnimationPanel();
        eastPanel = new StatsPanel();
        southPanel = new JPanel();
        
        //sizes
        centerPanel.setPreferredSize(new Dimension(960,540));
        eastPanel.setPreferredSize(new Dimension(320,540));
        
        //instantiate buttons
        eatButton = new JButton("Feed");
        cleanButton = new JButton("Clean");
        playButton = new JButton("Play");
        sleepButton = new JButton("Sleep");
        
        //add listeners
        eatButton.addActionListener(e -> controller.feedPet());
        cleanButton.addActionListener(e -> controller.cleanPet());
        playButton.addActionListener(e -> controller.playPet());
        sleepButton.addActionListener(e -> controller.sleepPet());
        
        //add buttons to panels
        southPanel.add(eatButton);
        southPanel.add(cleanButton);
        southPanel.add(playButton);
        southPanel.add(sleepButton);
        
        //add panels to frame
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(eastPanel, BorderLayout.EAST);
        this.add(southPanel, BorderLayout.SOUTH);
        
        //set panel visability
        centerPanel.setVisible(true);
        eastPanel.setVisible(true);
        this.setVisible(true);
        pack();
        
        //repaint
        repaint();
    }
    
    public void killPet() {
        this.setVisible(false);
        JOptionPane.showMessageDialog(this, "Your pet has died");
    }
    
    //inner class to display the game animiations/images
    private class AnimationPanel extends JPanel{
        
        public AnimationPanel() {
            super();
        }
        
        
        @Override
        public void paintComponent(Graphics g) {
            if(startGame) { //check if game has started so will only display if game is running
            if(controller.isPetSleeping()) {//checks if the pet is sleeping to display different backround
                g.drawImage(controller.petController.getSleepingBackgroundImage(), 0, 0, null);
                g.drawImage(controller.petController.getBedImage(), 385, 310, null);
                g.drawImage(controller.petController.getCharacterImage(), 400, 330, null);
                g.setColor(Color.WHITE);
                g.fillOval(290, 200, controller.petController.getSleepText().length() * 6, 50);
                g.fillOval(320, 245, 30, 30);
                g.fillOval(345, 270, 25, 25);
                g.fillOval(370, 290, 20, 20);
                g.setColor(Color.BLACK);
                g.drawString(controller.petController.getSleepText(), 305, 230); //character text to be displayed when sleeping
            }
            else if(!controller.isPetHealthy()) { //displays normal pet state
            g.drawImage(controller.petController.getBackgroundImage(), 0, 0, null);
            g.drawImage(controller.petController.getCharacterUnhappyImage(), 400, 330, null);
            }
            else { //displays pet unhealthy pet state when meters are low
            g.drawImage(controller.petController.getBackgroundImage(), 0, 0, null);
            g.drawImage(controller.petController.getCharacterImage(), 400, 330, null);
            }
            
        } 
        }
    }
    
    //inner class to display stats and meters
    public class StatsPanel extends JPanel{
        
        public StatsPanel() {
            super();
        }
        
        @Override
        public void paintComponent(Graphics g) {
            if(startGame) {
                 //displays pet age
                g.drawString("Pet Age: " + controller.petController.getAge(), 10, 15);
            int hunger = controller.petController.getHungryMeter();
            //hunger meter
            paintMeter(g, 10, 30, hunger);
            g.drawString("Hunger: " + hunger + "%", hunger + 20, 35);
            //sleep meter
            int sleep = controller.petController.getSleepMeter();
            paintMeter(g, 10, 50, sleep);
            g.drawString("Sleep: " + sleep + "%", sleep + 20, 55);
            //mood meter
            int mood = controller.petController.getMoodMeter();
            paintMeter(g, 10, 70, mood);
            g.drawString("Mood: " + mood + "%", mood + 20, 75);
            //clean meter
            int clean = controller.petController.getCleanMeter();
            paintMeter(g, 10, 90, clean);
            g.drawString("Clean: " + clean + "%", clean + 20, 95);
            //displays any meter warning messages
            paintPetMessages(g,10,120); 
        }
        }
        
        
        public void paintPetMessages(Graphics g, int x, int y) {
            HashMap messages = controller.getMessageQueue();
            Set set = messages.entrySet(); //converting messages into set
            Iterator iterator = messages.values().iterator(); //iterator for messages
            int i = 1; //used as multiplier for y axis
            g.setColor(Color.BLACK);
            while(iterator.hasNext()) {
                //iterates through set and displayes messages
                g.drawString(iterator.next().toString(), x, y+(i * 20));
                ++i;
            }
        }
        
        //method used for painting the meter bars
        public void paintMeter(Graphics g, int x, int y, int meter) {
            if(meter <= 20) {
                g.setColor(Color.RED);
            }
            else {g.setColor(Color.BLACK);}
            g.fillRect(x, y, meter, 5);
        }
}
   
}