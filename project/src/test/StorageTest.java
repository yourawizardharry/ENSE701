/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.SQLException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author User
 */
public class StorageTest {
    DatabaseModel dbModel;
    DatabaseController dbController;
    PetModel petModel;
    PetView petView;
    PetController petController;
    String nickname = "StorageTestClass";
    
    public StorageTest() {
    }
    
    @Before
    public void setUp() throws SQLException {
        dbModel = new DatabaseModel();
        dbController = new DatabaseController(dbModel);
        petView = new PetView();
        petController = new PetController(petView, dbController);
    }
    
    @Test //Check game is able to save game data in DB
    public void testGameSave() {
        petController.createNewGame(nickname);
        petController.saveGame();
        Assert.assertEquals(dbController.checkNicknameUsed(nickname), true);
        petController.removeGame();
    }
    
    @Test //Test the petController class can remove a pet from the DB
    public void testExistingGameRemoval() {
        if(!dbController.checkNicknameUsed(nickname)) {
            petController.createNewGame(nickname);
        }
        petController.removeGame();
        Assert.assertEquals(dbController.checkNicknameUsed(nickname), false);
    }

    @Test //Make sure removing a non existing game doesn't cause failure
    public void testNonExistingSaveRemoval() { 
        try {
        Assert.assertFalse(dbController.removeSaveGame("TestClass324213244"));
        }
        catch(SQLException e) {
            Assert.fail(e.toString());
        }
    }
    
    @After
    public void cleanUp() throws SQLException{
        if(dbController.checkNicknameUsed(nickname)) {
        dbController.removeSaveGame(nickname);
    }
    }
}
