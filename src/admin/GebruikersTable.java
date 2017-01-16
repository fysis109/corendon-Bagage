/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin;

import balie.AanpassenKlanten;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static javafx.application.Application.launch;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;
import global.MenuB;
import global.Mysql;

/**
 *
 * @author Joljin Verwest
 */
public class GebruikersTable {
 
GebruikerAanpassen gebruikerAanpassen = new GebruikerAanpassen();
AanpassenKlanten aanpassenKlanten = new AanpassenKlanten();
      Mysql mysql = new Mysql();
      Connection conn;
      private final String USERNAME = mysql.getUsername();
      private final String PASSWORD = mysql.getPassword();
      private final String CONN_STRING = mysql.getUrlmysql();
      private String userName;
      private String password;
      private String role;
      private String mail;
      private String gebDatum;
      private String telefoon;
      private String userID;
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
        
        int rij = 1;
        
        try{           
                        //maak connectie met het database
                        conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
                        // querry voor aantal koffers
                        String query1 = "select count(*) AS count from users";
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
                        
                       
                        
                            String query2 =  "select * from users";
                        System.out.println(query2);
                            Statement st2 = conn.createStatement();
                            ResultSet databaseResponse2 = st2.executeQuery(query2);
                            
                        ObservableList<Person> data = FXCollections.observableArrayList();
                        
                        while (databaseResponse2.next())
                        {   
                            
                                
                            //database response verwerken
                            
                            this.userName = databaseResponse2.getString("username");
                            this.password = databaseResponse2.getString("wachtwoord");
                            this.role = databaseResponse2.getString("rol");
                            this.mail = databaseResponse2.getString("email");
                            this.userID = databaseResponse2.getString("userID");
                            
                             data.add(new Person(userName, password, role, mail, userID));
                             
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
 
        
        final Label label = new Label("Adjust users");
        label.setFont(new Font("Arial", 20));
        table.setMinSize(690, 645);
        table.setMaxSize(700, 1000);
        table.setEditable(true);
 
        TableColumn gevondenkofferIDcol = new TableColumn("Username");
        gevondenkofferIDcol.setMinWidth(20);
        gevondenkofferIDcol.setCellValueFactory(
                new PropertyValueFactory<>("userName"));
        
        TableColumn bagagelabelcol = new TableColumn("Password");
        bagagelabelcol.setMinWidth(100);
        bagagelabelcol.setCellValueFactory(
                new PropertyValueFactory<>("wachtwoord"));
        
        TableColumn tussenvoegselCol = new TableColumn("Role");
        bagagelabelcol.setMinWidth(100);
        tussenvoegselCol.setCellValueFactory(
                new PropertyValueFactory<>("role"));
        
        TableColumn mailcol = new TableColumn("Mailadress");
        mailcol.setMinWidth(100);
        mailcol.setMaxWidth(200);
        mailcol.setCellValueFactory(
                new PropertyValueFactory<>("mail"));
        
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

                            Button btn = new Button( "Adjust user" );

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
                                                gebruikerAanpassen.star(stage,person.getUserName(),person.getWachtwoord(),person.getRole(),person.getMail(),person.getCustomersID());
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
        table.getColumns().addAll(gevondenkofferIDcol,bagagelabelcol,tussenvoegselCol,
                mailcol, actionCol);
        
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
        
        stage.setScene(scene);
        stage.show();
    }
 
    public static class Person {
 
        private final SimpleStringProperty username;
        private final SimpleStringProperty wachtwoord;
        private final SimpleStringProperty role;
        private final SimpleStringProperty mail;
        private final SimpleStringProperty userID;
        //private final Button btn = new Button();
 
        private Person(String username, String wachtwoord, String role, String mail, String userID) {
            this.username = new SimpleStringProperty(username);
            this.wachtwoord = new SimpleStringProperty(wachtwoord);
            this.role = new SimpleStringProperty(role);
            this.mail = new SimpleStringProperty(mail);
            this.userID = new SimpleStringProperty(userID);
            
        }
        public String getCustomersID(){
            return userID.get();
        }
        public void setCustomersID(String userID){
            this.userID.set(userID);
        }
        public String getMail(){
            return mail.get();
        }
        public void setMail(String mail){
            this.mail.set(mail);
        }
        public String getRole(){
            return role.get();
        }
        public void setRole(String role){
            this.role.set(role);
        }
        public String getUserName() {
            return username.get();
        }
 
        public void setUserName(String username) {
            this.username.set(username);
        }
 
        public String getWachtwoord() {
            return wachtwoord.get();
        }
 
        public void setWachtwoord(String wachtwoord) {
            this.wachtwoord.set(wachtwoord);
        }
        
    }
}
