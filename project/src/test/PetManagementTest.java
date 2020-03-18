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

/**
 *
 * @author User
 */
public class PetManagementTest {
    DatabaseController dbController;
    DatabaseModel dbModel;
    PetView petView;
    PetController petController;
    PetManagementModel petMgmtModel;
    PetManagementView petMgmtView;
    PetManagementController petMgmtController;
    String nickName;
    
    public PetManagementTest() {
    }
    
    @Before //Setup of all the view, models, controllers
    public void setUp() throws SQLException{
        nickName = "PetMgmtT";
        dbModel = new DatabaseModel();
        dbController = new DatabaseController(dbModel);
        petView = new PetView();
        petController = new PetController(petView, dbController);
        petMgmtModel = new PetManagementModel(petController);
    }
    
    @Test //Test the thread will kill the pet if stats reach zero
    public void testAutoDeath() throws InterruptedException{
        petController.createNewGame(nickName);
        petController.setHungryMeter(0);
        petMgmtModel.startGame();
        Thread.sleep(6000);
        Assert.assertEquals(petMgmtModel.isGameRunning(), false);
        petMgmtModel.stopGame();
        petController.removeGame();
        }
    
    @Test
    public void testPetHealthy() {
        petController.createNewGame(nickName);
        Assert.assertEquals(true, petMgmtModel.isPetHealthy());
        petController.removeGame();
    }
        
    @Test
    public void testStartStats() { //check the start stats are 50%
        petController.createNewGame(nickName);
        Assert.assertEquals(true, petController.getHungryMeter() == 50 && petController.getCleanMeter() == 50 && petController.getMoodMeter() == 50 && petController.getSleepMeter() == 50);
        petController.removeGame();
    }
    
    @After
    public void cleanUp() throws SQLException{ //in case the assert fails and game isn't removed
        if(dbController.checkNicknameUsed(nickName)) {
            dbController.removeSaveGame(nickName);
        }
    }
}
