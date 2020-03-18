
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
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


public class PetView extends JFrame implements ActionListener{
    enum USER_ACTION {
    LOAD,
    QUIT,
    NEW
}    
    private PetController controller;
    private JButton openFileButton, cancelButton;
    private ActionListener openFileButtonListener, cancelButtonListener;
    private String chosenName = null;
    private JList list;
    private JPanel loadGamePanel = null;
    private JPanel buttonPanel = null;

    
    public PetView() {
        super();
        this.setVisible(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void addController(PetController controller) {
        this.controller = controller;
    }

    public USER_ACTION startPrompt() {
        int userPromptOption;
        USER_ACTION action;
        
        Object startGameOptions[] = {"Start New Game", "Open Existing Game"}; 
        userPromptOption = JOptionPane.showOptionDialog(this,"How do you want to start","Start Game",JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE, null, startGameOptions, startGameOptions[1]);

        switch (userPromptOption) {
            case JOptionPane.OK_OPTION:
                action = USER_ACTION.NEW;
                break;
            case JOptionPane.CLOSED_OPTION:
                action = USER_ACTION.QUIT;
                break;
            default:
                action = USER_ACTION.LOAD;
                break;
        }
        
        return action;
    }
    
    public String getChosenName() {
        return chosenName;
    }
    
    public synchronized boolean newGamePrompt() {
        boolean nameValid = false;
        boolean exit = false;
        String userChosenName;
        String nickNameBoxText = "Enter a Nickname";
        
        while(!nameValid && !exit) { //continue to enter a nickname until it is valid or the user cancels 
            userChosenName = JOptionPane.showInputDialog(this, nickNameBoxText , "Enter a Nickname", JOptionPane.QUESTION_MESSAGE);
            if(userChosenName != null) {
                if(userChosenName.length() >10) { //Check to see if nickname is too long
                    nickNameBoxText = "Please enter a nickname 10 or less characters";
                }
                else if(controller.checkNameUsed(userChosenName) == false) { //check if the nickname already exists
                    nameValid = true;
                    chosenName = userChosenName;
                }
                else {
                    nickNameBoxText = "Nickname has already been used, please try again";
                }
            }
            else exit = true; //executed when user cancels the prompt
            }
            return nameValid;
        }
    
    public synchronized void loadGameDialog() {
        generateLoadDialog();
        if(chosenName == null) {
            try {
                wait();
            }
            catch(Exception e){
            }
            
            System.out.println("Game loaded");
        }
    }
    
    public synchronized void generateLoadDialog() {
        if(loadGamePanel!=null) {
            loadGamePanel.setVisible(true);
        }
        this.setVisible(true);
        loadGamePanel = new JPanel();
        buttonPanel = new JPanel();
        openFileButton = new JButton("Open Save");
        cancelButton = new JButton("Go Back");
        openFileButton.addActionListener(e -> {
                setVisible(false);
                chosenName = (String)list.getSelectedValue();
                controller.loadGameFromDb(chosenName);
        });
        cancelButton.addActionListener(e -> {
            this.setVisible(false);
            controller.runPrompt();
        });
        JOptionPane optionPane = new JOptionPane();
        optionPane.setOptions(new JButton[] {openFileButton,cancelButton});
        
        loadGamePanel.add(generateListPane());
        buttonPanel.add(openFileButton);
        buttonPanel.add(cancelButton);
        
        
        add(loadGamePanel);
        add(buttonPanel, BorderLayout.SOUTH);
        pack();
    }
    
    private JPanel generateListPane() { 
        ArrayList savedGameList;
        
            JPanel pane = new JPanel();
            savedGameList = controller.getSaveList();
            list = new JList((String[])savedGameList.toArray(new String[savedGameList.size()]));
            list.setPreferredSize(new Dimension(250,250));
            pane.add(list);
            
            return pane;
        }

    //This is not used as action listeners have been created with lambda expression defining the behaviour/action
    @Override
    public void actionPerformed(ActionEvent ae) {}
}