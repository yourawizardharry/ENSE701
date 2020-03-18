
import java.sql.Connection;
import java.sql.SQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author User
 */
public class DBConnectionController {
    DBConnectionModel model;
    
    public DBConnectionController(DBConnectionModel model) throws SQLException{
        this.model = model;
        model.connectDatabase();
        model.createPetDefinitions();
        
    }
    
    public Connection getConnection() throws SQLException{
        if(!model.isConnected()) {
            model.connectDatabase();
        }
        return model.getConnection();
    }
    
    //close the connection to the database
    public void closeConnection(){
        model.closeConnection();
    }
}
