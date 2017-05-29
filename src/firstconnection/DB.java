
package firstconnection;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DB {
    final String JDBC_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    final String URL = "jdbc:derby:sampleDB;create=true";
    final String USERNAME = "";
    final String PASSWORD = "";
    
    //Létrehozzuk a kapcsolatot (hidat)
    Connection conn = null;
    Statement createStatement = null;
    DatabaseMetaData dbmd = null;
    
    
    public DB() {
        //Megpróbáljuk életre kelteni
        try {
            conn = DriverManager.getConnection(URL);
            System.out.println("A híd létrejött");
        } catch (SQLException ex) {
            System.out.println("Valami baj van a connection (híd) létrehozásakor.");
            System.out.println(""+ex);
        }
        
        //Ha életre kelt, csinálunk egy megpakolható teherautót
        if (conn != null){
            try {
                createStatement = conn.createStatement();
            } catch (SQLException ex) {
                System.out.println("Valami baj van van a createStatament (teherautó) létrehozásakor.");
                System.out.println(""+ex);
            }
        }
        
        //Megnézzük, hogy üres-e az adatbázis? Megnézzük, létezik-e az adott adattábla.
        try {           
            dbmd = conn.getMetaData();
        } catch (SQLException ex) {
            System.out.println("Valami baj van a DatabaseMetaData (adatbázis leírása) létrehozásakor..");
            System.out.println(""+ex);
        }
        
        try {
            ResultSet rs = dbmd.getTables(null, "APP", "USERS", null);
            if(!rs.next())
            {
             createStatement.execute("create table users(name varchar(20), address varchar(20))");
            }
        } catch (SQLException ex) {
            System.out.println("Valami baj van az adattáblák létrehozásakor.");
            System.out.println(""+ex);
        }       
    }
    
    
    public void addUser(String name, String address){
        try {
//            String sql = "insert into users values ('" + name + "','" + address + "')";
//            createStatement.execute(sql);
              String sql = "insert into users values (?,?)";
              PreparedStatement preparedStatement = conn.prepareStatement(sql);
              preparedStatement.setString(1, name);
              preparedStatement.setString(2, address);
              preparedStatement.execute();
        } catch (SQLException ex) {
            System.out.println("Valami baj van a user hozzáadásakor");
            System.out.println(""+ex);
        }
    }
    
    
    public void showAllUsers(){
        String sql = "select * from users";
        try {
            ResultSet rs = createStatement.executeQuery(sql);
            while (rs.next()){
                String name = rs.getString("name");
                String address = rs.getString("address");
                System.out.println(name + " | " + address);
            }
        } catch (SQLException ex) {
            System.out.println("Valami baj van a userek kiolvasásakor");
            System.out.println(""+ex);
        }
    }
    
    public void showUsersMeta(){
        String sql = "select * from users";
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        try {
            rs = createStatement.executeQuery(sql);
            rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int x = 1; x <= columnCount; x++){
                System.out.print(rsmd.getColumnName(x) + " | ");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ArrayList<User> getAllUsers(){
        String sql = "select * from users";
        ArrayList<User> users = null;
        try {
            ResultSet rs = createStatement.executeQuery(sql);
            users = new ArrayList<>();
            
            while (rs.next()){
                User actualUser = new User(rs.getString("name"),rs.getString("name"));
                users.add(actualUser);
            }
        } catch (SQLException ex) {
            System.out.println("Valami baj van a userek kiolvasásakor");
            System.out.println(""+ex);
        }
      return users;
    }
    
    public void addUser(User user){
      try {
        String sql = "insert into users values (?,?)";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, user.getName());
        preparedStatement.setString(2, user.getAddress());
        preparedStatement.execute();
        } catch (SQLException ex) {
            System.out.println("Valami baj van a user hozzáadásakor");
            System.out.println(""+ex);
        }
    }
    
}
