/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
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
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 *
 * @author Joljin Verwest
 */
public class KlantenAanpassen extends Application {

    private AanpassenKlanten aanpassenKlanten = new AanpassenKlanten();
    private Mysql mysql = new Mysql();
    private Connection conn;
    private final String USERNAME = mysql.getUsername();
    private final String PASSWORD = mysql.getPassword();
    private final String CONN_STRING = mysql.getUrlmysql();
    private String firstName, lastName, tussenvoegsel, mail, gebDatum, telefoon, customersID;
    private int idcounter;
    private TableView<Person> table = new TableView<Person>();
      
    
   
   
 
   
    public void start(Stage stage) {
        
        //Menubar aan de bovenkant
        MenuB menuB = new MenuB();
        MenuBar menuBar = menuB.createMenuB(stage);
        BorderPane root = new BorderPane();
        //menuBar.prefWidthProperty().bind(stage.widthProperty());
        root.setTop(menuBar);
        root.setCenter(table);
        
     
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
        }
        
        
        Scene scene = new Scene(new Group());
        stage.setTitle("Table View Sample");
        stage.setWidth(800);
        stage.setHeight(800);
 
        
        final Label label = new Label("Adjust customers");
        label.setFont(new Font("Arial", 20));
        
        table.setEditable(true);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
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
        
        TableColumn gevondenkofferIDcol = new TableColumn("Firstname");
        gevondenkofferIDcol.setCellValueFactory(
                new PropertyValueFactory<>("firstName"));
        
        TableColumn bagagelabelcol = new TableColumn("Lastname");
        bagagelabelcol.setCellValueFactory(
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
        
        table.getColumns().addAll(gevondenkofferIDcol,bagagelabelcol,tussenvoegselCol, gebdatumcol,
                mailcol,telefooncol, actionCol);
        
        final VBox vbox = new VBox(root);
        
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table);
        ((Group) scene.getRoot()).getChildren().addAll(vbox);
        
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        //set Stage boundaries to visible bounds of the main screen
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());
        
        scene.getStylesheets().add("global/Style2.css");
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

