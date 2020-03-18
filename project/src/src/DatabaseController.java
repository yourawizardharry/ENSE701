
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author User
 */
public class DatabaseController {
    DatabaseModel model;
    
    public DatabaseController(DatabaseModel model) {
        this.model = model;
    }
    
    public ArrayList<String> getSavedGamesList() { //gets a list of saved games
        return model.getSavedGamesList();
    }
    
    public boolean checkNicknameUsed(String nickName) { //checks if game has been saved with same nickname already
        return model.checkNicknameUsed(nickName);
    }
    
    public boolean removeSaveGame(String nickname) throws SQLException{ //Method to remove existing saved game
        return model.removeSaveGame(nickname);
    }
    
    public Boolean saveGame(PetController petToSave) { //Method called by program to save a game, this uses other methods internally for logic on to save or update.
        return model.saveGame(petToSave);
    }
    
    public PetModel openGame(String nickName) {
        return model.openGame(nickName);
    }
}
