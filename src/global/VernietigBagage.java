/**
 * Zet de status op delete bij bagage ouder dan 1 jaar en vernietigd automatisch
 * permanent bagage ouder dan 5 jaar
 */
package global;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class VernietigBagage {
    
    //conneciie met database
    private Mysql mysql = new Mysql();
    private final String USERNAME = mysql.getUsername();
    private final String PASSWORD = mysql.getPassword();
    private final String CONN_STRING = mysql.getUrlmysql();
    
    public void start(Stage primaryStage){
        int countDestroyed = 0;
        int countPermantlyVerwijderd = 0;
        ArrayList<Integer> bagageToDeleteVerloren = new ArrayList<>();
        ArrayList<Integer> bagageToDeleteGevonden = new ArrayList<>();
        ArrayList<Integer> bagageToDeleteVerlorenPermantly = new ArrayList<>();
        ArrayList<Integer> bagageToDeleteGevondenPermantly = new ArrayList<>();
        
        
        //checked welke bagage ouder is dan 1 jaar en zet de status op delete
        //checked welke bagage ouder is dan 5 jaar en verwijdert deze permanent
        try {
            Connection conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
            Statement stm = conn.createStatement();
            String selectStringVer = "SELECT verlorenkofferID FROM verlorenbagage WHERE datum <= DATE_SUB(CURRENT_DATE(), INTERVAL 1 YEAR) AND status = 'notSolved' ";
            String selectStringGev = "SELECT gevondenkofferID FROM gevondenbagage WHERE datum <= DATE_SUB(CURRENT_DATE(), INTERVAL 1 YEAR) AND status = 'notSolved' ";
            ResultSet rs = stm.executeQuery(selectStringVer); 
            while(rs.next()){
                bagageToDeleteVerloren.add(rs.getInt("verlorenkofferID"));
            }
            for (int i = 0; i < bagageToDeleteVerloren.size(); i++) {
                stm.execute("INSERT INTO nietopgelost (verlorenkofferID, datum) VALUES ('"+bagageToDeleteVerloren.get(i)+"', CURRENT_DATE())");
                stm.execute("UPDATE verlorenbagage SET status = 'deleted' WHERE verlorenkofferID = '"+bagageToDeleteVerloren.get(i)+"'");
                countDestroyed++;
            }
            ResultSet rs1 = stm.executeQuery(selectStringGev);
            while(rs1.next()){
                bagageToDeleteGevonden.add(rs1.getInt("gevondenkofferID"));
            }
            for(int i = 0; i < bagageToDeleteGevonden.size(); i++){
                stm.execute("INSERT INTO nietopgelost (gevondenkofferID, datum) VALUES ('"+bagageToDeleteGevonden.get(i)+"', CURRENT_DATE())");
                stm.execute("UPDATE gevondenbagage SET status = 'deleted' WHERE gevondenkofferID = '"+bagageToDeleteGevonden.get(i)+"'");
                countDestroyed++;
            }
            
            String selectDeletePermantlyVerl = "SELECT verlorenkofferID FROM verlorenbagage WHERE datum <= DATE_SUB(CURRENT_DATE(), INTERVAL 5 YEAR) ";
            String selectDeletePermantlyGevo = "SELECT gevondenkofferID from gevondenbagage WHERE datum <= DATE_SUB(CURRENT_DATE(), INTERVAL 5 YEAR) ";
            ResultSet rs2 = stm.executeQuery(selectDeletePermantlyVerl);
            while(rs2.next()){
                bagageToDeleteVerlorenPermantly.add(rs2.getInt("verlorenkofferID"));
            }
            for(int i = 0; i < bagageToDeleteVerlorenPermantly.size(); i++){
                stm.execute("DELETE FROM verlorenbagage WHERE verlorenkofferID = '"+bagageToDeleteVerlorenPermantly.get(i)+"'");
                stm.execute("DELETE FROM nietopgelost WHERE verlorenkofferID = '"+bagageToDeleteVerlorenPermantly.get(i)+"'");
                stm.execute("DELETE FROM afleveradres WHERE verlorenkofferID = '"+bagageToDeleteVerlorenPermantly.get(i)+"'");  
                stm.execute("DELETE FROM opgelost WHERE verlorenkofferID = '"+bagageToDeleteVerlorenPermantly.get(i)+"'");
                countPermantlyVerwijderd ++;
            }
            
            ResultSet rs3 = stm.executeQuery(selectDeletePermantlyGevo);
            while(rs3.next()){
                bagageToDeleteGevondenPermantly.add(rs3.getInt("gevondenkofferID"));
            }
            for(int i = 0; i < bagageToDeleteGevondenPermantly.size(); i++){
                stm.execute("DELETE FROM gevondenbagage WHERE gevondenkofferID = '"+bagageToDeleteGevondenPermantly.get(i)+"'");
                stm.execute("DELETE FROM nietopgelost WHERE gevondenkofferID = '"+bagageToDeleteGevondenPermantly.get(i)+"'");
                stm.execute("DELETE FROM opgelost WHERE gevondenkofferID = '"+bagageToDeleteGevondenPermantly.get(i)+"'");
                countPermantlyVerwijderd ++;
            }
        }catch(SQLException ed){
            System.out.println(ed);
        }
        
        //buttons en scene
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        VBox dialogVbox = new VBox(20);
        Button test = new Button("Home");
        Text text = new Text(countDestroyed+" Luggage has been destroyd");
        Text text1 = new Text(countPermantlyVerwijderd+" Luggage has been permantly \ndeleted from the database ");
        dialogVbox.getChildren().addAll(text, text1, test);
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        
        dialog.setScene(dialogScene);
        dialog.show();

        test.setOnAction(new EventHandler<ActionEvent>() { public void handle(ActionEvent e){
            Home home = new Home();
            home.start(primaryStage);
            dialog.close();
        }});
    }
}
