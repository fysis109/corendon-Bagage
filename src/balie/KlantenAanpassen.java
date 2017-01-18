/*
 * Deze klasse zorgt voor een table met alle informatie over klanten
 */
package balie;

import global.MenuB;
import global.Mysql;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class KlantenAanpassen extends Application {

    private Mysql mysql = new Mysql();
    private final String USERNAME = mysql.getUsername();
    private final String PASSWORD = mysql.getPassword();
    private final String CONN_STRING = mysql.getUrlmysql();
    private TableView<Person> table = new TableView<Person>();
    
    
    @Override
    public void start(Stage stage) {
        
        //Menubar aan de bovenkant
        MenuB menuB = new MenuB();
        MenuBar menuBar = menuB.createMenuB(stage);
        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(table);
        
        try{
            Connection conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
            String selectQuery =  "SELECT * FROM customers";
            Statement stmt = conn.createStatement();
            ResultSet databaseResponse = stmt.executeQuery(selectQuery);
            ObservableList<Person> data = FXCollections.observableArrayList();
            while (databaseResponse.next())
            {
                String customersID = databaseResponse.getString("customersID");
                String firstName = databaseResponse.getString("voornaam");
                String lastName = databaseResponse.getString("achternaam");
                String tussenvoegsel = databaseResponse.getString("tussenvoegsel");
                String mail = databaseResponse.getString("email");
                String gebDatum = databaseResponse.getString("geb_datum");
                String telefoon = databaseResponse.getString("telefoonnummer");
                
                data.add(new Person(firstName, lastName, tussenvoegsel, mail, gebDatum, telefoon, customersID));
                table.setItems(data);
            }
        }catch (SQLException ed) {
            System.out.println(ed);
        }
        
        Scene scene = new Scene(new Group());
        stage.setTitle("All customers");
        
        Label adjustCustomersLabel = new Label("Adjust customers");
        adjustCustomersLabel.setFont(new Font("Arial", 20));
        
        table.setEditable(true);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        //veranderd de grootte van de tableview aan de hand van de grootte van het scherm
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                table.setMinWidth(((double)newSceneWidth - 10));
                table.setMaxWidth(((double)newSceneWidth - 10));
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                table.setMinHeight((double)newSceneHeight - 200);
                table.setMaxHeight((double)newSceneHeight - 200);
            }
        });
        
        //alle tablecolum
        TableColumn firstNameCol = new TableColumn("Firstname");
        firstNameCol.setCellValueFactory(
                new PropertyValueFactory<>("firstName"));
        
        TableColumn lastNameCol = new TableColumn("Lastname");
        lastNameCol.setCellValueFactory(
                new PropertyValueFactory<>("lastName"));
        
        TableColumn tussenvoegselCol = new TableColumn("Insertion");
        tussenvoegselCol.setCellValueFactory(
                new PropertyValueFactory<>("tussenvoegsel"));
        
        TableColumn mailcol = new TableColumn("Mailadress");
        mailcol.setCellValueFactory(
                new PropertyValueFactory<>("mail"));
        
        TableColumn gebdatumcol = new TableColumn("Date of birth");
        gebdatumcol.setCellValueFactory(
                new PropertyValueFactory<>("gebDatum"));
        
        TableColumn telefooncol = new TableColumn("Phone number");
        telefooncol.setCellValueFactory(
                new PropertyValueFactory<>("telefoon"));
        
        TableColumn actionCol = new TableColumn( "Adjust" );
        actionCol.setCellValueFactory( 
                new PropertyValueFactory<>( "" ));
        
        Button adjustCustomer = new Button("Adjust customer");
            
        //als er een klant is geselecteerd en er wordt gedrukt op adjsut customer,
        //dan wordt dat scherm geopend.
        adjustCustomer.setOnAction((ActionEvent e ) -> {
            if(table.getSelectionModel().isEmpty() == false){
                Person person = table.getSelectionModel().getSelectedItem();
                AanpassenKlanten aanpassenKlanten = new AanpassenKlanten();
                                        aanpassenKlanten.AanpassenKlanten(stage,person.getFirstName(), person.getLastName(), person.getTussenvoegsel(),
                                                person.getTelefoon(), person.getMail(),person.getGebDatum(),person.getCustomersID());
            }    
        });
        
        //voeg alle columns toe aan de table
        table.getColumns().addAll(lastNameCol,firstNameCol,tussenvoegselCol, gebdatumcol,
                mailcol,telefooncol);
        
        final VBox vbox = new VBox(root);
        
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(adjustCustomersLabel, table, adjustCustomer);
        ((Group) scene.getRoot()).getChildren().addAll(vbox);
        
        scene.getStylesheets().add("global/Style2.css");
        stage.setScene(scene);
        stage.show();
    }
 
    //deze klasse is nodig om de tableview te kunnen maken
    public static class Person {
 
        private final SimpleStringProperty firstName;
        private final SimpleStringProperty lastName;
        private final SimpleStringProperty tussenvoegsel;
        private final SimpleStringProperty mail;
        private final SimpleStringProperty gebDatum;
        private final SimpleStringProperty telefoon;
        private final SimpleStringProperty customersID;
 
        private Person(String firstName, String lastName, String tussenvoegsel, String mail, String gebDatum, String telefoon,
                String customersID) {
            this.firstName = new SimpleStringProperty(firstName);
            this.lastName = new SimpleStringProperty(lastName);
            this.tussenvoegsel = new SimpleStringProperty(tussenvoegsel);
            this.mail = new SimpleStringProperty(mail);
            this.gebDatum = new SimpleStringProperty(gebDatum);
            this.telefoon = new SimpleStringProperty(telefoon);
            this.customersID = new SimpleStringProperty(customersID);
            
        }
        public String getCustomersID(){
            return customersID.get();
        }
        public void setCustomersID(String customersID){
            this.customersID.set(customersID);
        }
        public String getTelefoon(){
            return telefoon.get();
        }
        public void setTelefoon(String telefoon){
            this.telefoon.set(telefoon);
        }
        public String getMail(){
            return mail.get();
        }
        public void setMail(String mail){
            this.mail.set(mail);
        }
        public String getGebDatum(){
            return gebDatum.get();
        }
        public void setGebDatum(String gebDatum){
            this.gebDatum.set(gebDatum);
        }
        public String getTussenvoegsel(){
            return tussenvoegsel.get();
        }
        public void setTussenvoegsel(String tussenvoegsel){
            this.tussenvoegsel.set(tussenvoegsel);
        }
        public String getFirstName() {
            return firstName.get();
        }
 
        public void setFirstName(String firstName) {
            this.firstName.set(firstName);
        }
 
        public String getLastName() {
            return lastName.get();
        }
 
        public void setLastName(String lastName) {
            this.lastName.set(lastName);
        }
        
    }
}

