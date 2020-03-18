
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
public class DatabaseModel {
    DBConnectionController connectionController;
    Connection dbConnection;
    
    public DatabaseModel() throws SQLException{
        connectionController = new DBConnectionController(new DBConnectionModel());
        dbConnection = connectionController.getConnection();
    }
    
    //this gets a list of saved games from the database
    public ArrayList<String> getSavedGamesList() {
        ArrayList<String> savedGames;
        savedGames = new ArrayList<>();
        try {
        ResultSet rs;
        Statement getSavedData = dbConnection.createStatement();
        rs = getSavedData.executeQuery("select NICKNAME from GAMESAVES");
        
        while(rs.next()) {
            savedGames.add(rs.getString("NICKNAME"));
        }
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null, e + " Unable to access saved games, please contact developer");
            Logger.errorLog(e);
        }
        return savedGames;
    }
    
    
    public boolean checkNicknameUsed(String nickName) {
        boolean used = false;
        ArrayList existingNames = getSavedGamesList();
        
        if(nickName.matches("")) {
            return true;
        }
        for(int i = 0; i < existingNames.size(); ++i) {
            if(nickName.matches((String)existingNames.get(i))) {
                return true;
            }
        
        }
            
        return used;
    }
    
    private void updateSaveGame(PetController petToSave) throws SQLException{ //Method to update an existing saved game entry
        Statement updateSave = dbConnection.createStatement();
        updateSave.executeUpdate("update GAMESAVES set AGE=" + petToSave.getAge() + ",SLEEP=" + petToSave.getSleepMeter() + ",MOOD=" + petToSave.getMoodMeter() + ",CLEAN="+petToSave.getCleanMeter()+",HUNGER="+petToSave.getHungryMeter()+" where NICKNAME='"+petToSave.getNickname()+"'");
    }
    
    public boolean removeSaveGame(String nickname) throws SQLException{ //Method to remove existing saved game
        if(checkNicknameUsed(nickname)) {
            Statement statement = dbConnection.createStatement();
            statement.executeUpdate("delete from GAMESAVES where NICKNAME = '" + nickname + "'");
        }
        else return false;
        
        return true;
    }
    
    private boolean saveNewGame(PetController petToSave) throws SQLException{ //Method to save a new game
        int id = -1;
        boolean isSaved = false;
            Statement getPetId = dbConnection.createStatement();
            ResultSet characterIdRs = getPetId.executeQuery("select ID,CHARACTERNAME from PETCHARACTER");
            while(characterIdRs.next()) {
                if(characterIdRs.getString("CHARACTERNAME").equalsIgnoreCase(petToSave.getCharacterName())) {
                    id = characterIdRs.getInt("ID");
                }
            }
            if(id != -1) {
            Statement savePetData = dbConnection.createStatement();
            savePetData.executeUpdate("Insert Into GAMESAVES (CHARACTERID,NICKNAME,AGE,SLEEP,MOOD,CLEAN,HUNGER) VALUES (" + id + ",'" + petToSave.getNickname() + "'," + petToSave.getAge() + "," + petToSave.getSleepMeter() + "," + petToSave.getMoodMeter() + "," + petToSave.getCleanMeter() + "," + petToSave.getHungryMeter() + ")");
            isSaved = true;
            }
        return isSaved;
    }
    
    public Boolean saveGame(PetController petToSave) { //Method called by program to save a game, this uses other methods internally for logic on to save or update.
        Boolean gameSaved = false;
        try {
        if(checkNicknameUsed(petToSave.getNickname())) {
            if(!petToSave.isAlive()) { //Logic to remove pet from the DB if the pet has died
                removeSaveGame(petToSave.getNickname());
            }
            else {
            updateSaveGame(petToSave); //Update existing saved game
            }
            gameSaved = true;
        }
        else {
            saveNewGame(petToSave); //Create a new save game record
            gameSaved = true;
        }
        }
        catch(SQLException e) {
            Logger.errorLog(e);
        }
        return gameSaved;
    }
    
    public PetModel openGame(String nickName) {
        int characterId = -1;
        String characterName = null;
        PetModel pet = null;
        try {
            Statement getCharacterId = dbConnection.createStatement();
            ResultSet characterRs = getCharacterId.executeQuery("select CHARACTERID from GAMESAVES where NICKNAME = '" + nickName+ "'");
            while(characterRs.next()) {
                characterId = characterRs.getInt("CHARACTERID");
            }
            if(characterId != -1) {
                Statement getCharacterName = dbConnection.createStatement();
                ResultSet characterNameRs = getCharacterName.executeQuery("select CHARACTERNAME from PETCHARACTER where ID = " + characterId);
                while(characterNameRs.next()) {
                    characterName = characterNameRs.getString("CHARACTERNAME");
                }
                pet = new PetModel(nickName, PetModel.getCharacterType(characterName));
                Statement getAllPetData = dbConnection.createStatement();
                ResultSet petData = getAllPetData.executeQuery("select * from GAMESAVES where NICKNAME = '" + nickName + "'");
                while(petData.next()) {
                    pet.setAge(petData.getInt("AGE"));
                    pet.setSleepMeter(petData.getInt("SLEEP"));
                    pet.setMoodMeter(petData.getInt("MOOD"));
                    pet.setCleanMeter(petData.getInt("CLEAN"));
                    pet.setHungryMeter(petData.getInt("HUNGER"));
                }
            }
        }
        catch(SQLException e) {
            System.out.println("openGame failed");
            System.out.println(e.toString());
            return null;
        }
        return pet;
    }
}
