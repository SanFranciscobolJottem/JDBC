
package firstconnection;

import java.util.ArrayList;

public class FirstConnection {


    public static void main(String[] args) {
        DB db = new DB();
//        db.addUser("Gyula", "Pécs");
//        db.showAllUsers();
//        db.showUsersMeta();
          ArrayList<User> users = db.getAllUsers();
          for (User u : users){
              System.out.println(u.getName());
          }
    }
    
}
