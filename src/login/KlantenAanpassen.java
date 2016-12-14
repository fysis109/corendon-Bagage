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
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 *
 * @author Joljin Verwest
 */
public class KlantenAanpassen extends Application {
      
      AanpassenKlanten aanpassenKlanten = new AanpassenKlanten();
      Mysql mysql = new Mysql();
      Connection conn;
      private final String USERNAME = mysql.getUsername();
      private final String PASSWORD = mysql.getPassword();
      private final String CONN_STRING = mysql.getUrlmysql();
      private String firstName;
      private String lastName;
      private String tussenvoegsel;
      private String mail;
      private String gebDatum;
      private String telefoon;
      private String customersID;
      private int idcounter;
      private TableView<Person> table = new TableView<Person>();
      
    
   
    public static void main(String[] args) {
        
        launch(args);
    }
 
    @Override
    public void start(Stage stage) {
        
        //Menubar aan de bovenkant
        MenuB menuB = new MenuB();
        MenuBar menuBar = menuB.createMenuB(stage);
        BorderPane root = new BorderPane();
        //menuBar.prefWidthProperty().bind(stage.widthProperty());
        root.setTop(menuBar);
        root.setCenter(table);
        
        int rij = 1;
        
        try{           
                        //maak connectie met het database
                        conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
                        // querry voor aantal koffers
                        String query1 = "select count(*) AS count from customers";
                        System.out.println(query1);
                        
                        // create the java statement
                        Statement st1 = conn.createStatement();
                        
                        // execute the query, and get a java resultset
                        ResultSet databaseResponse = st1.executeQuery(query1);
                        while (databaseResponse.next())
                        { 
                             this.idcounter = databaseResponse.getInt("count");
                            
                        }
                        Person[] person = new Person[this.idcounter];
                        
                       
                        
                            String query2 =  "select * from customers";
                        System.out.println(query2);
                            Statement st2 = conn.createStatement();
                            ResultSet databaseResponse2 = st2.executeQuery(query2);
                            
                        ObservableList<Person> data = FXCollections.observableArrayList();
                        
                        while (databaseResponse2.next())
                        {   
                            
                                
                            //database response verwerken
                            this.customersID = databaseResponse2.getString("customersID");
                            this.firstName = databaseResponse2.getString("voornaam");
                            this.lastName = databaseResponse2.getString("achternaam");
                            this.tussenvoegsel = databaseResponse2.getString("tussenvoegsel");
                            this.mail = databaseResponse2.getString("email");
                            this.gebDatum = databaseResponse2.getString("geb_datum");
                            this.telefoon = databaseResponse2.getString("telefoonnummer");
                            
                             data.add(new Person(firstName, lastName, tussenvoegsel, mail, gebDatum, telefoon, customersID));
                             
                             table.setItems(data);
                            
                        }
        }
                        catch (SQLException ed) {
                        System.err.println(ed);
                        }
        
        
        Scene scene = new Scene(new Group());
        stage.setTitle("Table View Sample");
        stage.setWidth(800);
        stage.setHeight(800);
 
        
        final Label label = new Label("Adjust customers");
        label.setFont(new Font("Arial", 20));
        table.setMinSize(690, 645);
        table.setMaxSize(700, 1000);
        table.setEditable(true);
 
        TableColumn gevondenkofferIDcol = new TableColumn("firstname");
        gevondenkofferIDcol.setMinWidth(20);
        gevondenkofferIDcol.setCellValueFactory(
                new PropertyValueFactory<>("firstName"));
        
        TableColumn bagagelabelcol = new TableColumn("lastname");
        bagagelabelcol.setMinWidth(100);
        bagagelabelcol.setCellValueFactory(
                new PropertyValueFactory<>("lastName"));
        
        TableColumn tussenvoegselCol = new TableColumn("insertion");
        bagagelabelcol.setMinWidth(100);
        tussenvoegselCol.setCellValueFactory(
                new PropertyValueFactory<>("tussenvoegsel"));
        
        TableColumn mailcol = new TableColumn("mailadress");
        mailcol.setMinWidth(100);
        mailcol.setMaxWidth(200);
        mailcol.setCellValueFactory(
                new PropertyValueFactory<>("mail"));
        
        TableColumn gebdatumcol = new TableColumn("date of birth");
        bagagelabelcol.setMinWidth(100);
        gebdatumcol.setCellValueFactory(
                new PropertyValueFactory<>("gebDatum"));
        
        TableColumn telefooncol = new TableColumn("phone number");
        bagagelabelcol.setMinWidth(100);
        telefooncol.setCellValueFactory(
                new PropertyValueFactory<>("telefoon"));
        
        TableColumn actionCol = new TableColumn( "Action" );
        actionCol.setMinWidth(125);
        actionCol.setCellValueFactory( 
                new PropertyValueFactory<>( "" ));
        
        Callback<TableColumn<Person, String>, TableCell<Person, String>> cellFactory = //
                new Callback<TableColumn<Person, String>, TableCell<Person, String>>()
                {
                    @Override
                    public TableCell call( final TableColumn<Person, String> param )
                    {
                        final TableCell<Person, String> cell = new TableCell<Person, String>()
                        {

                            Button btn = new Button( "Adjust customer" );

                            @Override
                            public void updateItem( String item, boolean empty )
                            {
                                super.updateItem( item, empty );
                                if ( empty )
                                {
                                    setGraphic( null );
                                    setText( null );
                                }
                                else
                                {
                                    btn.setOnAction( ( ActionEvent event ) ->
                                            {
                                                Person person = getTableView().getItems().get( getIndex() );
                                                aanpassenKlanten.AanpassenKlanten(stage,person.getFirstName(), person.getLastName(), person.getTussenvoegsel(),
                                                        person.getTelefoon(), person.getMail(),person.getGebDatum(),person.getCustomersID()); 
                                                System.out.println( person.getFirstName() + "   " + person.getLastName() );
                                    } );
                                    setGraphic( btn );
                                    btn.setPrefWidth(125);
                                    setText( null );
                                }
                            }
                        };
                        cell.setAlignment(Pos.CENTER);
                        return cell;
                    }
                };

        actionCol.setCellFactory( cellFactory );
        //table.prefWidthProperty().bind(scene.widthProperty());
        table.getColumns().addAll(gevondenkofferIDcol,bagagelabelcol,tussenvoegselCol, gebdatumcol,
                mailcol,telefooncol, actionCol);
        
        final VBox vbox = new VBox(root);
        
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table);
        ((Group) scene.getRoot()).getChildren().addAll(vbox);
 
        
        stage.setScene(scene);
        stage.show();
    }
 
    public static class Person {
 
        private final SimpleStringProperty firstName;
        private final SimpleStringProperty lastName;
        private final SimpleStringProperty tussenvoegsel;
        private final SimpleStringProperty mail;
        private final SimpleStringProperty gebDatum;
        private final SimpleStringProperty telefoon;
        private final SimpleStringProperty customersID;
        //private final Button btn = new Button();
 
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

