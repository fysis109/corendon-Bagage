/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

/**
 *
 * @author tim
 */
public class VernietigBagage {
    
    
    private Mysql mysql = new Mysql();
    private final String USERNAME = mysql.getUsername();
    private final String PASSWORD = mysql.getPassword();
    private final String CONN_STRING = mysql.getUrlmysql();
    
    public void start(Stage primaryStage){
        int count = 0;
        ArrayList<Integer> bagageToDeleteVerloren = new ArrayList<>();
        ArrayList<Integer> bagageToDeleteGevonden = new ArrayList<>();
        
        Connection conn;
        
        try {
            conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
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
                count++;
            }
            ResultSet rs1 = stm.executeQuery(selectStringGev);
            while(rs1.next()){
                bagageToDeleteGevonden.add(rs1.getInt("gevondenkofferID"));
            }
            for(int i = 0; i < bagageToDeleteGevonden.size(); i++){
                stm.execute("INSERT INTO nietopgelost (gevondenkofferID, datum) VALUES ('"+bagageToDeleteGevonden.get(i)+"', CURRENT_DATE())");
                stm.execute("UPDATE gevondenbagage SET status = 'deleted' WHERE gevondenkofferID = '"+bagageToDeleteGevonden.get(i)+"'");
                count++;
            }
            
        }catch(SQLException ed){
            System.out.println(ed);
        }
        
        Home home = new Home();
        
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        VBox dialogVbox = new VBox(20);
        Button test = new Button("Home");
        Text text = new Text(count+" Luggage have been removed /nfrom the database.");
        dialogVbox.getChildren().addAll(text, test);
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();

        test.setOnAction(new EventHandler<ActionEvent>() { public void handle(ActionEvent e){
            home.start(primaryStage);
            dialog.close();
        }});
    }
}
