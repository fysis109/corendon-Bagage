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
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 *
 * @author Joljin Verwest
 */
public class KlantenAanpassen extends Application {
    
        Mysql mysql = new Mysql();

    //private mqsql
    private final String USERNAME = mysql.getUsername();
    private final String PASSWORD = mysql.getPassword();
    private final String CONN_STRING = mysql.getUrlmysql();
    
    public SimpleStringProperty firstName;
    private SimpleStringProperty lastName;
    
    public static void main( String[] args )
    {
        launch( args );
    }
    public class Person
    {
        private Person( String fName, String lName )
        {
            firstName = new SimpleStringProperty(fName);
            lastName = new SimpleStringProperty(lName);
            
        }


        public String getFirstName()
        {
            return firstName.get();
        }


        public void setFirstName( String fName )
        {
            firstName.set(fName);
        }


        public String getLastName()
        {
            return lastName.get();
        }


        public void setLastName( String lName )
        {
            lastName.set(lName);
        }

    }
    
    private final TableView<Person> table = new TableView<>();
    private final ObservableList<Person> data
            = FXCollections.observableArrayList(
                    
            );
    
     @Override
    public void start( Stage stage )
    {
        stage.setWidth( 450 );
        stage.setHeight( 500 );

        TableColumn firstNameCol = new TableColumn( "First Name" );
        firstNameCol.setCellValueFactory( new PropertyValueFactory<>( "firstName" ) );

        TableColumn lastNameCol = new TableColumn( "Last Name" );
        lastNameCol.setCellValueFactory( new PropertyValueFactory<>( "lastName" ) );

        TableColumn actionCol = new TableColumn( "Action" );
        actionCol.setCellValueFactory( new PropertyValueFactory<>( "DUMMY" ) );

        Callback<TableColumn<Person, String>, TableCell<Person, String>> cellFactory = //
                new Callback<TableColumn<Person, String>, TableCell<Person, String>>()
                {
                    @Override
                    public TableCell call( final TableColumn<Person, String> param )
                    {
                        Connection conn;

                                                try {
                                                                conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
                                                                Statement stmt = (Statement) conn.createStatement();
                                                                ResultSet rs = stmt.executeQuery("SELECT * FROM customers");
                                                                while(rs.next()){
                                                                    String test1 = rs.getString("voornaam");
                                                                    String test2= rs.getString("achternaam");
                                                                    data.add(new Person(test1, test2));
                                                                    table.setItems(data);
                                                                }
                                                }
                                                catch (SQLException ed) {
                                                                System.out.println(ed);
                                                            }
                        
                        final TableCell<Person, String> cell = new TableCell<Person, String>()
                        {
                                

                            final Button btn = new Button( "Just Do It" );
                            
                            
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
                                                //Person person = getTableView().getItems().get( getIndex() );
                                                
                                                
                                                
                                                /*String f = "Tim";
                                                String l = "Vlaar";
                                                Person person = new Person(f, l);
                                                System.out.println( person.getFirstName() + "   " + person.getLastName() );*/
                                    } );
                                    setGraphic( btn );
                                    setText( null );
                                }
                            }
                        };
                        return cell;
                    }
                };

        actionCol.setCellFactory( cellFactory );

        table.setItems( data );
        table.getColumns().addAll( firstNameCol, lastNameCol, actionCol );

        Scene scene = new Scene( new Group() );

        (( Group ) scene.getRoot()).getChildren().addAll( table );

        stage.setScene( scene );
        stage.show();
    }
   
}