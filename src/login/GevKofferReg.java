/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 *
 * @author tim
 */
public class GevKofferReg {
    
    public void start(Stage primaryStage) {
       
        GridPane grid = new GridPane(); 
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        int rij = 0;
        
        Label customerInfo = new Label("Customer Information");
        grid.add(customerInfo, 0,rij++);

        Label lastName = new Label("Last name:");
        grid.add(lastName, 0, rij);
  
        TextField lastNameEntry = new TextField();
        grid.add(lastNameEntry, 1, rij++);
        
        Label firstName = new Label("First name:");
        grid.add(firstName,0,rij);
        
        TextField firstNameEntry = new TextField();
        grid.add(firstNameEntry, 1, rij++);
        
        
        Scene scene = new Scene(grid,1200,920);        
        primaryStage.setTitle("Register Found Bagage");
        primaryStage.setScene(scene);
        primaryStage.show();
         
    }
    
}
