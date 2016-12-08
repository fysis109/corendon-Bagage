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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
    private String kleur;
    private String merk;
    private String hoogte;
    private String lengte;
    private String breedte;
    private String luchthavenVertrekEntry;
    private String luchthavenAankomstEntry;
    
            
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
 
        int rij = 1;
        
        Text scenetitle = new Text("Customer Information");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);
        
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
                            customerID = rs2.getInt("customersID");
                        }
                        System.out.println(customerID);
                        
                        HBox bwvbox1 = new HBox(10);
                        bwvbox1.setAlignment(Pos.BOTTOM_RIGHT);
                        bwvbox1.getChildren().add(verderNaarBagage);
                        grid.add(bwvbox1, 1, 10);
                        
                        } catch (SQLException ed) { 
                            System.out.println(ed);
                    }                  
                }
            }
        });
        
        verderNaarBagage.setOnAction((ActionEvent event) -> {
            BagageToevoegen(primaryStage);
        });
        
        Scene scene = new Scene(root,1200,920);        
        primaryStage.setTitle("Register Customer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
   public void BagageToevoegen(Stage primaryStage) {
        
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

        int rij = 1;
        
        Text scenetitle = new Text("Register lost bagage");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);
        
        Label bagageLabel = new Label("Bagagelabelnumber:");
        grid.add(bagageLabel, 0, rij);
        
        TextField bagageLabelEntry = new TextField();
        grid.add(bagageLabelEntry,1,rij++);
        
        Label kleurKoffer = new Label("Color:");
        grid.add(kleurKoffer, 0, rij);
        
        ComboBox kleurKofferComboBox = new ComboBox();
         kleurKofferComboBox.getItems().addAll(
            "Blauw",
            "Geel",
            "Zwart",
            "Grijs",
            "Bruin",
            "Overig"
        );
        kleurKofferComboBox.setPrefWidth(200);
        grid.add(kleurKofferComboBox, 1, rij++);
        
        //eventhandler kleurKofferComboBox
        kleurKofferComboBox.valueProperty().addListener(new ChangeListener<String>(){
            @Override public void changed(ObservableValue ov, String t, String t1) {                
                kleur = t1;                
            } 
        });
        
        Label merkKoffer = new Label("Brand:");
        grid.add(merkKoffer, 0, rij);
        
        ComboBox merkKofferComboBox = new ComboBox();
        merkKofferComboBox.getItems().addAll(
            "American Tourister",
            "Bric's",
            "Eastpak ",
            "March",
            "Porsche Design",
            "Rimowa",
            "Samsonite",
            "Swiss Wenger",
            "Ted Baker",
            "The North Face",
            "Tumi",
            "Victorinox",
            "Other"
        );     
        merkKofferComboBox.setPrefWidth(200);
        grid.add(merkKofferComboBox, 1, rij++);
        
        //eventhandler merkkoffercombobox
        merkKofferComboBox.valueProperty().addListener(new ChangeListener<String>(){
            @Override public void changed(ObservableValue ov, String t, String t1) {                
                merk = t1;                
            } 
        });
        
        Label hoogteKoffer = new Label("Height of bagage:");
        grid.add(hoogteKoffer, 0, rij);
        
        ComboBox hoogteKofferComboBox = new ComboBox();
        hoogteKofferComboBox.getItems().addAll(
            "10cm-15cm",
            "15cm-20cm",
            "20cm-25cm",
            "25cm-30cm",
            "35cm-40cm",
            "40cm-45cm",
            "Unknown"
        );
        hoogteKofferComboBox.setPrefWidth(200);
        grid.add(hoogteKofferComboBox,1, rij++);
        
        //eventhandler hoogtekoffercombobox
        hoogteKofferComboBox.valueProperty().addListener(new ChangeListener<String>(){
            @Override public void changed(ObservableValue ov, String t, String t1) {                
                hoogte = t1;                
            } 
        });
        
        Label lengteKoffer = new Label("Length of bagage:");
        grid.add(lengteKoffer,0,rij);
        
        ComboBox lengteKofferComboBox = new ComboBox();
        lengteKofferComboBox.getItems().addAll(
            "30cm-40cm",
            "40cm-50cm",
            "50cm-60cm",
            "60cm-70cm",
            "70cm-80cm",
            "Unknown"
        ); 
        lengteKofferComboBox.setPrefWidth(200);
        grid.add(lengteKofferComboBox,1,rij++);
        
        lengteKofferComboBox.valueProperty().addListener(new ChangeListener<String>(){
            @Override public void changed(ObservableValue ov, String t, String t1) {                
                lengte = t1;                
            } 
        });
        
        Label breedteKoffer = new Label("Width of bagage:");
        grid.add(breedteKoffer, 0 , rij);
        
        ComboBox breedteKofferComboBox = new ComboBox();
        breedteKofferComboBox.getItems().addAll(
            "20cm-30cm",
            "30cm-40cm",
            "40cm-50cm",
            "50cm-60cm",
            "Unknown"
        );
        breedteKofferComboBox.setPrefWidth(200);
        grid.add(breedteKofferComboBox, 1, rij++);
        
        //eventhandler breedtekoffercombobox
        breedteKofferComboBox.valueProperty().addListener(new ChangeListener<String>(){
            @Override public void changed(ObservableValue ov, String t, String t1) {                
                breedte = t1;                
            } 
        });
        
        Label luchthavenVertrek = new Label("Airport of departure:"); 
        grid.add(luchthavenVertrek, 0, rij);
        
        ComboBox vliegveldVertrekComboBox = new ComboBox();
        vliegveldVertrekComboBox.getItems().addAll(
            "Schiphol, Amsterdam",
            "El Prat, Barcelona",
            "Atatürk, Istanbul" 
        );
        vliegveldVertrekComboBox.setPrefWidth(200);
        grid.add(vliegveldVertrekComboBox, 1 , rij++);
        
        //eventhandler luchthavenvertrek
        vliegveldVertrekComboBox.valueProperty().addListener(new ChangeListener<String>(){
            @Override public void changed(ObservableValue ov, String t, String t1) {                
                luchthavenVertrekEntry = t1;                
            } 
        });
        
        Label luchthavenAankomst = new Label("Airport of arrival:"); 
        grid.add(luchthavenAankomst, 0, rij);
        
        ComboBox vliegveldAankomstComboBox = new ComboBox();
        vliegveldAankomstComboBox.getItems().addAll(
            "Schiphol, Amsterdam",
            "El Prat, Barcelona",
            "Atatürk, Istanbul" 
        );
        vliegveldAankomstComboBox.setPrefWidth(200);
        grid.add(vliegveldAankomstComboBox, 1 , rij++);
        
        //eventhandler vliegveld aankomst
        vliegveldAankomstComboBox.valueProperty().addListener(new ChangeListener<String>(){
            @Override public void changed(ObservableValue ov, String t, String t1) {                
                luchthavenVertrekEntry = t1;                
            } 
        });
        
        Text scenetitle1 = new Text("Delivery address");
        scenetitle1.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle1, 0, rij++, 2, 1);
        
        
        
        
        
        
        
        
        Scene scene = new Scene(root,1200,920);        
        primaryStage.setTitle("Register Found Bagage");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        
    }
    
}
