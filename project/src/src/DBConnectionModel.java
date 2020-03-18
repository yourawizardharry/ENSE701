
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author User
 */
public class DBConnectionModel {
    private static Connection dbConnection = null;
    private static boolean schemaCreated = false;
    //public static String dbURL = ("jdbc:derby:PETDB;create=true");
    public static String dbURL = ("jdbc:derby://localhost:1527/PETDB;create=true");
    public static String dbUser = ("southpark");
    public static String dbPass = ("tamagotchi");
    
    
    public boolean isConnected() throws SQLException{
        return !dbConnection.isClosed();
    }
    
    //static method for other classes to get the dbConnection, shared so multiple connections arn't created
    public Connection getConnection() throws SQLException{
        return dbConnection;
    }
    
    //close the connection to the database
    public void closeConnection(){
        try {
            if(!dbConnection.isClosed()) {
        dbConnection.close();
            }
        }
        catch(SQLException e) {
            //Logger.errorLog(e);
        }
    }
    
    //database connnection establisher
    public boolean connectDatabase() throws SQLException{
        try {
        dbConnection = DriverManager.getConnection(dbURL, dbUser, dbPass);
        return true;
        }
        catch(SQLException e) {
            //Logger.errorLog(e);
            System.out.println(e);
            throw new SQLException("Unable to connect to the database, please ensure SQL is running.");
        }
    }
    
    //method to check if a table alreday exists pre creation
    public boolean checkTableExists(String tableName) {
        Boolean tableExists = false;
        
        try{
        DatabaseMetaData dbMeta = dbConnection.getMetaData();
        ResultSet rs = dbMeta.getTables(null, null, tableName, null);
        if(rs.next()) {
            tableExists = true;
        }
        }
        catch(SQLException e) {
        }
        
        return tableExists;
    }
    
    //used to create the tables/schema if the game is running for the first time
    public void createPetDefinitions() throws SQLException{
        try{
        Statement characterImgTableCreate = dbConnection.createStatement();
        Statement characterTableCreate = dbConnection.createStatement();
        Statement gameDataTableCreate = dbConnection.createStatement();
        //creating the pet database table
        if(checkTableExists("PETCHARACTER") != true) {
            characterTableCreate.executeUpdate("create table PETCHARACTER (id int not null primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), CHARACTERNAME varchar(50))");
            characterTableCreate.executeUpdate("insert into PETCHARACTER (CHARACTERNAME) values ('Cartman')");
            characterTableCreate.executeUpdate("insert into PETCHARACTER (CHARACTERNAME) values ('Butters')");
            characterTableCreate.executeUpdate("insert into PETCHARACTER (CHARACTERNAME) values ('Kyle')");
        }
        //creating the table to hold the character images
        if(checkTableExists("CHARACTERIMAGES") != true) {
            characterImgTableCreate.executeUpdate("create table CHARACTERIMAGES (cId int references PETCHARACTER(id), Path varchar(100))");
        }
        //creating table to hold the pet instances
        if(checkTableExists("GAMESAVES") != true) {
        gameDataTableCreate.executeUpdate("create table GAMESAVES (petId int not null primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), characterId int references PETCHARACTER(id), Nickname varchar(30), age int, sleep int, mood int, clean int, hunger int)");
        }
        
        }
        catch(SQLException e) {
            //Logger.errorLog(e);
            throw new SQLException("Unable to create database schema, please contact developer for help");
        }
    }
}
