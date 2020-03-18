
import java.awt.event.ActionEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author User
 */
public class PetManagementModel {
    private boolean isSleeping;
    private boolean gameRunning;
    //current instance of the pet
    public PetController pet; 
    //thread instances
    private AgeThread ageThread;
    private MeterThread meterThread;
    private SleepThread sleepThread;
    private HashMap petMessages;
    
    
    public PetManagementModel (PetController pet) {
        this.pet = pet;
        petMessages = new HashMap();
        isSleeping = false;
        ageThread = new AgeThread();
        meterThread = new MeterThread();
        sleepThread = new SleepThread();
    }
    
    public boolean isPetSleeping() {
        return isSleeping;
    }
    
    public void addMeterObserver(Observer o) {
        meterThread.addObserver(o);
    }
    
    public void addSleepObserver(Observer o) {
        sleepThread.addObserver(o);
    }
    
    
    public void startGame() { //method to start various threads to simulate gameplay.
        gameRunning = true;
        ageThread.start();
        meterThread.start();
    }
    
    public void stopGame() { //method to stop the game from running.
        sleepThread.stop();
        meterThread.stop();
        ageThread.stop();
        gameRunning = false;
    }
    
    public boolean isGameRunning() {
        return gameRunning;
    }
    
    public HashMap getMessageQueue() {
        return petMessages;
    }
    
    public boolean isPetHealthy() {
        return (pet.getCleanMeter() > 20 && pet.getHungryMeter() > 20 && pet.getMoodMeter() > 20 && pet.getSleepMeter() > 20);
    }
    
    public void killPet(String reason) { //Kills the pet, stopping the game.
        pet.setAlive(false);
        stopGame();
    }
    
    private class AgeThread implements Runnable{ //Thread used to age the current pet throughout the game.
        private Thread ageThread;
        
        @Override
        public void run() { //Logic to increase the age of the pet.
            try{
                while(gameRunning) {
                    ageThread.sleep(100000); //thread will sleep before incrementing age, to make aging realistic.
                    pet.setAge(pet.getAge()+1);
                    if(pet.getAge() == pet.getAgeLimit()) {
                        pet.setAlive(false);
                    }
                }
            }
            catch(InterruptedException e) {
                //Logger.errorLog(e); //Will log any exceptions to the log file, for later developer investigation.
            }
        }
        
        public void stop() { //kills the thread, if age needs to stop increasing.
                if(ageThread.isAlive()) {
                ageThread.interrupt();
                }
        }
        
        
        public void start() { 
            ageThread = new Thread(this);
            ageThread.start();
        }
    }
    
        private class MeterThread extends Observable implements Runnable{ //Thread to deincrement meters thoughout gameplay.
        private Thread meterThread;
        private Random rand;
        
        public MeterThread(){
            rand = new Random();
        }
        
        @Override
        public void run() {
            try{
                while(gameRunning) {
                    meterThread.sleep(5000); //thread to sleep before adjustiing meters.
                    pet.setCleanMeter(pet.getCleanMeter() - (rand.nextInt(2) + 1));
                    pet.setHungryMeter(pet.getHungryMeter() - (rand.nextInt(2) + 1));
                    pet.setMoodMeter(pet.getMoodMeter() - (rand.nextInt(2) + 1));
                    pet.setSleepMeter(pet.getSleepMeter() - (rand.nextInt(2) + 1));
                    
                    //warn user of low meters.
                    if(pet.getCleanMeter() < 10) {
                        petMessages.put("Clean", pet.getNickname() + " is dirty, clean your pet before it dies from sickness.");
                    }
                    else {
                        if(petMessages.containsKey("Clean")) {
                            petMessages.remove("Clean");
                        }
                    }
                    if(pet.getHungryMeter() < 10) {
                        petMessages.put("Hungry", pet.getNickname() + " is hungry, feed your pet before it dies from hunger."); 
                    }
                    else {
                        if(petMessages.containsKey("Hungry")) {
                            petMessages.remove("Hungry");
                        }
                    }
                    if(pet.getMoodMeter() < 10) {
                        petMessages.put("Mood", pet.getNickname() + " is unhappy, play your pet.");
                    }
                    else {
                        if(petMessages.containsKey("Mood")) {
                            petMessages.remove("Mood");
                        }
                    }
                    if(pet.getSleepMeter() < 10) {
                        petMessages.put("Sleep",pet.getNickname() + " is tired, put your pet to sleep before it dies of sleep deprivation.");
                    }
                    else {
                        if(petMessages.containsKey("Sleep")) {
                            petMessages.remove("Sleep");
                        }
                    }
                    //Check meters to see if they have fallen to 0, then kill pet if this is the case.
                    if(pet.isAlive() == false) {
                        killPet("neglect");
                    }
                    
                    this.setChanged();
                    this.notifyObservers("meter");
                }
            }
            catch(InterruptedException e) {
                //Logger.errorLog(e); //Exception stack sent to file for further investigation.
            }
            finally {
                
            }
        }
        //stop the thread when pet is dead.
        public void stop() {
            if(meterThread.isAlive()) {
            meterThread.interrupt();
            }
        }
        //for starting meter thread, when game is running.
        public void start() {
            meterThread = new Thread(this);
            meterThread.start();
        }
    }
    
        
    private class SleepThread extends Observable implements Runnable{ //thread to simulate sleepling of the pet, to be started upon gameplay.
        private Thread sleepThread;
        
        @Override
        public void run() {
            try {
                System.out.println(pet.getSleepText());
                while(pet.getSleepMeter() < 100) {
                sleepThread.sleep(1500);
                if (pet.getSleepMeter() >90) {
                    pet.setSleepMeter(100);
                }
                else {
                    pet.setSleepMeter(pet.getSleepMeter()+10);
                }
                setChanged();
                notifyObservers();
                } 
            }
            catch (InterruptedException e) {
                //Logger.errorLog(e);
            }
            finally {
                isSleeping = false;
            }
        }
        
        public void stop() {
            if(sleepThread != null) {
                if(sleepThread.isAlive()) {
                sleepThread.interrupt();
            }
        }
        }
        
        public void start() {
            sleepThread = new Thread(this);
            isSleeping = true;
            sleepThread.start();
        }  
    }
    
    public String checkStatus() { //helper method to check if pet is in a state that it cannot perform actions.
        if(isSleeping) {
            return "Sleeping";
        }
        if(pet.isAlive() == false) {
            return "Dead";
        }
        else return "Ready";
    }
   
    
    //all of the meter methods return a string to the caller. This was planned in expectation of future GUI.
    public String feedPet() {
        if(checkStatus().equalsIgnoreCase("ready")) {//checks if pet is doing some other action.
            if(pet.getHungryMeter() > 70) { //if pet has more than 70 on the meter, it will full it up.
                if(pet.getHungryMeter() > 90) {
                return "Your pet is not hungry";
            }
            else {
                pet.setHungryMeter(100);
            }
        }
            else {
            pet.setHungryMeter(pet.getHungryMeter() + 30); //adds 30 to the meter.
        }
            return pet.getEatingText();
        }
        else return "Cannot perform this action, your pet is currently: " + checkStatus();
    }
    
    
    public String playPet() {
        if(checkStatus().equalsIgnoreCase("ready")) {//checks if pet is doing some other action.
        if(pet.getMoodMeter() > 90) {
            return "Your pet is not in the mood to play";
        }
        else {
            pet.setMoodMeter(100);
            return "You have played with your pet";
        }
        }
        else return "Cannot perform this action, your pet is currently: " + checkStatus(); //will return reason that pet is busy.
    }
    
    public String sleepPet() {
    if(checkStatus().equalsIgnoreCase("ready")) { // checks if pet is busy.
    if(pet.getSleepMeter() > 90) { //if meter is over 90, pet doesn't need to sleep.
        return "Your pet is not sleepy";
    }
    else {
        isSleeping = true;
        sleepThread.start(); //starts sleep thread
        return "Your pet has been sent to sleep";
    }
    }
    else return "Cannot perform this action, your pet is currently: " + checkStatus();
    }
    
    public String cleanPet() {
        if(checkStatus().equalsIgnoreCase("ready")) {
        if(pet.getCleanMeter() > 90) { //if meter is over 90, pet doesn't need to be cleaned.
            return "Your pet is not dirty";
        }
        else {
            pet.setCleanMeter(100);
            return "Your pet is now clean";
        }
    }
        else return "Cannot perform this action, your pet is currently: " + checkStatus();
    }
    
    @Override
    public String toString() { //returns pet stats from pet class.
        return pet.toString();
    }
}
