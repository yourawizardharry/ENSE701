
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author User
 */
public class PetManagementController implements Observer{
    PetManagementModel model;
    PetManagementView view;
    PetController petController;
    DatabaseController dbController;
    
    
    public PetManagementController(PetManagementModel model, PetManagementView view, PetController petController) {
        this.model = model;
        this.view = view;
        try {
        this.petController = petController;
        }
        catch (Exception ex) {}
        view.addController(this);
        model.addMeterObserver(this);
        model.addSleepObserver(this);
        //model.startGame(); --need to start the threads and stuff at some point
    }
    
    //Will kill the application
    public void gameExit() {
        view.dispose();
    }
    
    public void stopGame() {
        model.stopGame();
        view.killPet();
    }
    
    public void saveGame() {
        if(model != null) {
            petController.saveGame();
        }
    }
    
    public void startGame() {
        view.startGame();
        model.startGame();
    }

    @Override
    public void update(Observable o, Object o1) {
        if(!petController.isAlive()) {
            stopGame();
            petController.runPrompt();
        }
        else {
            view.repaint();
        }
        }
    
    public boolean isPetHealthy() {
        return model.isPetHealthy();
    }
    
    public HashMap getMessageQueue() {
        return model.getMessageQueue();
    }
    
    public void feedPet() {
        model.feedPet();
        view.repaint();
    }
    
    public void playPet() {
        model.playPet();
        view.repaint();
    } 
    
    public void sleepPet() {
        model.sleepPet();
        view.repaint();
    }
    
    public void cleanPet() {
        model.cleanPet();
        view.repaint();
    }
    
    public boolean isPetSleeping() {
        return model.isPetSleeping();
    }
    }
