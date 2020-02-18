
    //Name: Elvis Presley Gene (217304338)
   //Date: 18 - 02 - 2020

/*
    First Maven project.

    Used the ucanaccess dependency in the pom.xml file
    not to have to download ucanacess and add it to the project.

    Branches:
    1- Master
    2- isograms (implementing a new feature to check for names that are isograms)
    3- serializeNames (feature to add the names into a serialized file)
 */

package org.elvis.gene;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class App
{
   private Connection conn = null;
   private Statement statement = null;
   private BufferedReader br = null;
   private PreparedStatement ps = null;

   // ucanaccess is in the list of dependencies in the pom.xml file.
   public void setConn(){
       try {
           String url = "jdbc:ucanaccess://names_db.accdb";
           conn = DriverManager.getConnection(url);
           System.out.println("Connection established");

           statement = conn.createStatement();
       } catch (Exception error) {
           System.out.println("Error: " + error.getMessage());
       }
   }

   public void readNames(){
       try {
           br = new BufferedReader(new FileReader("fam_list.txt"));
           String line = br.readLine();

           while (line != null){
                fillTable(line);
               line = br.readLine();
           }
       }catch ( /*FileNotFoundException*/ IOException e){
           System.out.println(e.getMessage());
       }
   }

   public void createTable() {
       String createTableQuery = "CREATE TABLE NAMES (id AUTOINCREMENT PRIMARY KEY, name VARCHAR(15))";
       String fillTableQuery = "INSERT INTO NAMES (name) VALUES (?)";

       try {
           //Check if a table with the same name already exists
           DatabaseMetaData dm = conn.getMetaData();
           ResultSet tableExist = dm.getTables(null, null, "Names", null);
           if (tableExist.next()) {
               statement.executeUpdate("DROP TABLE NAMES");
           }

           statement.executeUpdate(createTableQuery);
           ps = conn.prepareStatement(fillTableQuery);

       } catch (SQLException e) {
           System.out.println(e.getMessage());
       }
   }

       public void fillTable(String name){
       try {
           ps.setString(1,name);
           ps.executeUpdate();

       }catch (SQLException e){
           System.out.println(e.getMessage());
       }
   }

   public void closeResources(){
       try {
           if (ps != null)
               ps.close();
           if (statement != null)
               statement.close();
           if (br != null)
               br.close();
           if (conn != null)
               conn.close();
       }catch (SQLException | IOException e){
           System.out.println(e.getMessage());
       }
   }

    public static void main(String[] args) {
        App app = new App();

        app.setConn();
        app.createTable();
        app.readNames();
        app.closeResources();
    }
}
