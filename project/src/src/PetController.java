
import java.awt.Image;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author User
 */

//This class is for controlliing the Pet and inital startup of loading and saving the pet.
//This class deals with the db
public class PetController extends Observable{
    private PetModel petModel = null;
    private PetView petView;
    private DatabaseController dbController;
    private PetManagementController petMgmtController = null;
    
    public PetController(PetView petView, DatabaseController dbController) {
        this.petView = petView;
        this.dbController = dbController;
        petView.addController(this);
    }
    
    public void addPetManagementController(PetManagementController petMgmtController) {
        this.petMgmtController = petMgmtController;
    }
    
    public void loadGameFromDb(String nickname) {
        petModel = dbController.openGame(nickname);
        if(petModel == null) {
            JOptionPane.showMessageDialog(null, " There was an issue getting game data from the datase, please contact developer");
            petView.dispose();
        }
        else {
        petMgmtController.startGame();
        }
    }
    
    public boolean isGameSelected() {
        return !(petModel == null);
    }
    
    public ArrayList getSaveList() {
        return dbController.getSavedGamesList();
    }
    
    public boolean checkNameUsed(String nickName) {
        return dbController.checkNicknameUsed(nickName);
    }
    
    public void createNewGame(String nickname) {
        petModel = new PetModel(nickname);
        petModel.randomCharacterSelect();
        if(petMgmtController != null) {
        petMgmtController.startGame();
        }
    }
    
    public void saveGame() {
        if(petModel != null) {
        dbController.saveGame(this);
        }
    }
    
    public void removeGame() {
        if(petModel!= null) {
        if(dbController.checkNicknameUsed(petModel.getNickname())) {
            try {
                dbController.removeSaveGame(petModel.getNickname());
            }
            catch(SQLException e) {
                Logger.errorLog(e);
            }
        }
        }
    }
    
    public Image getCharacterImage() {
        return petModel.getCharacterImage();
    }
    
    public Image getCharacterUnhappyImage() {
        return petModel.getCharacterUnhappyImage();
    }
    
    
    public Image getSleepingBackgroundImage() {
        return petModel.getSleepingBackgroundImage();
    }
    
    public Image getBedImage() {
        return petModel.getBedImage();
    }
    
    public Image getBackgroundImage() {
        return petModel.getBackgroundImage();
    }
    
    public int getAgeLimit() {
        return petModel.getAgeLimit();
    }
    
    public String getNickname() {
        return petModel.getNickname();
    }
    
    public String getCharacterName() {
        return petModel.getCharacterName();
    }
    
    public int getAge() {
        return petModel.getAge();
    }
    
    public int getMoodMeter() {
        return petModel.getMoodMeter();
    }
    
    public int getHungryMeter() {
        return petModel.getHungryMeter();
    }
    
     public int getSleepMeter() {
        return petModel.getSleepMeter();
    }
     
     public int getCleanMeter() {
        return petModel.getCleanMeter();
    }
     
     public boolean isAlive() {
        return petModel.isAlive();
    }
     
     //Text for when the pet is performing actions..
    public String getEatingText() {
        return petModel.getEatingText();
    }
    
    //sleeping text for each character
    public String getSleepText() {
        return petModel.getSleepText();
    }
    
    //cleaning text for each character
    public String getCleanText() {
        return petModel.getCleanText();
    }
    
    @Override
    public String toString() {
        return petModel.toString();
    }
    
    public void setNickname(String nickName) {
        petModel.setNickname(nickName);
    }
    
    public void setAlive(boolean alive) {
        petModel.setAlive(alive);
    }
    
    public void setAge(int petAge) {
        petModel.setAge(petAge);
    }
    
    public void setMoodMeter(int moodMeter) {
        petModel.setMoodMeter(moodMeter);
    }
    
    public void setHungryMeter(int hungryMeter) {
        petModel.setHungryMeter(hungryMeter);
    }
    
    public void setSleepMeter(int sleepMeter) {
        petModel.setSleepMeter(sleepMeter);
    }
    
    public void setCleanMeter(int cleanMeter) {
        petModel.setCleanMeter(cleanMeter);
    }
    
    //This will load the game start options
    public void runPrompt() {
        
        boolean gameExited = false;
        boolean nameValid = false;
        while(!nameValid && !gameExited) {
        PetView.USER_ACTION action = petView.startPrompt();
        if(action == PetView.USER_ACTION.NEW) {
            nameValid = petView.newGamePrompt();
            if(nameValid) {
                System.out.print(petView.getChosenName());
                createNewGame(petView.getChosenName());
                }
        }
        else if(action == PetView.USER_ACTION.LOAD) {
            petView.generateLoadDialog();
            nameValid = true;
        }
        else {
            if(petMgmtController != null) {
                petMgmtController.gameExit();
            }
            gameExited = true;
            petView.dispose();
        }
        }
    }
}
