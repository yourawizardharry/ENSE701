
import java.sql.SQLException;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author harry
 */
public class main {
        public static void main(String[] args) {
        try {
        DatabaseModel dbModel = new DatabaseModel();
        DatabaseController dbController = new DatabaseController(dbModel);
        PetView petView = new PetView();
        PetController petController = new PetController(petView,dbController);
        petView.addController(petController);
        PetManagementView gameView = new PetManagementView();
        PetManagementModel gameModel = new PetManagementModel(petController);
        PetManagementController gameController = new PetManagementController(gameModel, gameView, petController);
        petController.addPetManagementController(gameController);
        petController.runPrompt();
        }
        catch(SQLException ex) { //This will execute if the data derby connect cannot be established 
            JOptionPane.showMessageDialog(null, ex + " Please restart the program and try again. Make sure SQL server is running");
        }
    }
}
