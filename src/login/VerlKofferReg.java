/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author tim
 */
public class VerlKofferReg {
    
    Mysql mysql = new Mysql();
    private final String USERNAME = mysql.getUsername();
    private final String PASSWORD = mysql.getPassword();
    private final String CONN_STRING = mysql.getUrlmysql();
    private int customerID;
    
    public void start(Stage primaryStage){
        
        MenuB menuB = new MenuB();
        MenuBar menuBar = menuB.createMenuB(primaryStage);        
        BorderPane root = new BorderPane();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        root.setTop(menuBar);
        
        GridPane grid = new GridPane(); 
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        root.setCenter(grid);
        
        
        
        int rij = 0;
        
        Label customerInfo = new Label("Customer Information");
        grid.add(customerInfo, 0,rij++);

        Label lastName = new Label("Last name:");
        grid.add(lastName, 0, rij);
  
        TextField lastNameEntry = new TextField();
        grid.add(lastNameEntry, 1, rij++);
        
        Label tussenVoegsel = new Label("Insertion:");
        grid.add(tussenVoegsel, 0, rij);
        
        TextField tussenVoegselEntry = new TextField();
        grid.add(tussenVoegselEntry, 1, rij++);
        
        Label firstName = new Label("First name:");
        grid.add(firstName,0,rij);
        
        TextField firstNameEntry = new TextField();
        grid.add(firstNameEntry, 1, rij++);
        
        Label geboorteDatum = new Label("Date of birth yyyy-mm-dd:");
        grid.add(geboorteDatum,0, rij);
        
        TextField geboorteDatumEntry = new TextField();
        grid.add(geboorteDatumEntry, 1, rij++);
        
        Label phonenumber = new Label("Phonenumber:");
        grid.add(phonenumber, 0, rij);
        
        TextField phonenumberEntry = new TextField();
        grid.add(phonenumberEntry, 1, rij++);
        
        Label email = new Label("Email:");
        grid.add(email, 0, rij);
        
        TextField emailEntry = new TextField();
        grid.add(emailEntry, 1, rij++);
        
        Button registreerKlant = new Button("Register customer");
        HBox bwvbox = new HBox(10);
        bwvbox.setAlignment(Pos.BOTTOM_RIGHT);
        bwvbox.getChildren().add(registreerKlant);
        grid.add(bwvbox, 1, rij++);
        
        Text actiontarget = new Text();
        actiontarget.setFill(Color.FIREBRICK);
        grid.add(actiontarget, 1, 10);
        
        Button verderNaarBagage = new Button("Register Bagage");
        
       
        
        registreerKlant.setOnAction(new EventHandler<ActionEvent>() {
            private String[] test;
            @Override
            public void handle(ActionEvent e) {
                String lastName = lastNameEntry.getText();
                String firstName = firstNameEntry.getText();
                String phoneNumber = phonenumberEntry.getText();
                String email = emailEntry.getText();
                String tussenvoegsel = tussenVoegselEntry.getText();
                String geb_datum = geboorteDatumEntry.getText();
                
                if(lastNameEntry.getText().trim().isEmpty() ||firstNameEntry.getText().trim().isEmpty() || geboorteDatumEntry.getText().trim().isEmpty() || 
                        emailEntry.getText().trim().isEmpty() || phonenumberEntry.getText().trim().isEmpty()){
                    actiontarget.setText("Lastname, Firstname,\ndate of birth, phonenumber\n and email can't\n be left open");
                } else {
                    Connection conn;
                    try {
                        conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
                        System.out.println("Connected!");
                        Statement stmt = (Statement) conn.createStatement();
                        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM customers WHERE voornaam = '"+firstName+"' AND achternaam = '"+ lastName +"' AND tussenvoegsel = '"+ tussenvoegsel+"' AND geb_datum = '"+ geb_datum+"'");
                        System.out.println("test");
                        int count = 0;
                        while(rs.next()){
                            count = rs.getInt("total");
                            System.out.println(count);
                        }
                        if(count == 0){
                            String insert= "insert into customers (voornaam, achternaam, tussenvoegsel, telefoonnummer, email, geb_datum) "
                                    + "VALUES ('"+ firstName+"','"+ lastName+"', '"+ tussenvoegsel+"' , '"+ phoneNumber+"', '"+ email+"','"+geb_datum+"' )";
                            stmt.execute(insert);
                            actiontarget.setFill(Color.GREEN);
                            actiontarget.setText("Klant Toegevoegd");
                        }else{
                            actiontarget.setFill(Color.FIREBRICK);
                            actiontarget.setText("Klant bestaat al,\nklik op ga verder\nom aan deze klant\neen koffer toe\nte voegen");
                        }
                        
                        ResultSet rs2 = stmt.executeQuery("SELECT customersID FROM customers WHERE voornaam = '"+firstName+"' AND achternaam = '"+ lastName +"' AND tussenvoegsel = '"+ tussenvoegsel+"' AND geb_datum = '"+ geb_datum+"'");
                        while(rs2.next()){
                            customerID = rs.getInt("customersID");
                        }
                        System.out.println(customerID);
                        
                        HBox bwvbox1 = new HBox(10);
                        bwvbox1.setAlignment(Pos.BOTTOM_RIGHT);
                        bwvbox1.getChildren().add(verderNaarBagage);
                        grid.add(bwvbox1, 1, 9);
                        
                        } catch (SQLException ed) { 
                            System.out.println(ed);
                    }                  
                }
            }
        });
        
        verderNaarBagage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BagageToevoegen(primaryStage);
            }
        });
        
        Scene scene = new Scene(root,1200,920);        
        primaryStage.setTitle("Register Found Bagage");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void BagageToevoegen(Stage primaryStage) {
        
    }
    
}
