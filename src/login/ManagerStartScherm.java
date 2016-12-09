/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author tim
 */
public class ManagerStartScherm {
    
    //mysql connectie
    Mysql mysql = new Mysql();   
    //private mqsql
    private final String USERNAME = mysql.getUsername();
    private final String PASSWORD = mysql.getPassword();
    private final String CONN_STRING = mysql.getUrlmysql();
    
    public void start(Stage primaryStage) {
        
        // deze vijf regels om een homeknop aan te roepen
        MenuB menuB = new MenuB();
        MenuBar menuBar = menuB.createMenuB(primaryStage);        
        BorderPane root = new BorderPane();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        root.setTop(menuBar);
       
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        // deze regel moet ook aangemaakt worden voor de homeknop
        root.setCenter(grid);
        
        Text grafieken = new Text("Statistics");
        grafieken.setFont(Font.font("Tahoma", FontWeight.NORMAL, 25));
        grid.add(grafieken, 0, 0, 2, 1);
        
        Text selecteerData = new Text("Select data");
        selecteerData.setFont(Font.font("Tahoma", FontWeight.NORMAL, 18));
        grid.add(selecteerData, 0, 1, 2, 1);
        
        Label beginDataLabel = new Label("Begin date YYYY-MM-DD");
        grid.add(beginDataLabel, 0, 2);
        
        ComboBox beginJaar = new ComboBox();
        beginJaar.getItems().addAll(
                "2012", "2013", "2014", "2015", "2016", "2017"
        );
        grid.add(beginJaar, 2, 2 );
        
        ComboBox beginMaand = new ComboBox();
        beginMaand.getItems().addAll(
                "01", "02", "03" , "04", "05", "06", "07", "08", "09", "10", "11", "12"
        );
        grid.add(beginMaand, 3, 2);
        
        ComboBox beginDag = new ComboBox();
        beginDag.getItems().addAll(
                "01", "02", "03" , "04", "05", "06", "07", "08", "09", "10",
                "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
                "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"
        );
        grid.add(beginDag, 4, 2);
        
        Label eindDataLabel = new Label("End date YYYY-MM-DD");
        grid.add(eindDataLabel, 0, 3);
        
        ComboBox eindJaar = new ComboBox();
        eindJaar.getItems().addAll(
                "2012", "2013", "2014", "2015", "2016", "2017"
        );
        grid.add(eindJaar, 2, 3 );
        
        ComboBox eindMaand = new ComboBox();
        eindMaand.getItems().addAll(
                "01", "02", "03" , "04", "05", "06", "07", "08", "09", "10", "11", "12"
        );
        grid.add(eindMaand, 3, 3);
        
        ComboBox eindDag = new ComboBox();
        eindDag.getItems().addAll(
                "01", "02", "03" , "04", "05", "06", "07", "08", "09", "10",
                "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
                "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"
        );
        grid.add(eindDag, 4, 3);
        
        Text selectAirportLabel = new Text("Select airports");
        selectAirportLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 18));
        grid.add(selectAirportLabel, 9, 1, 2, 1);
        
        Label amsterdamAirportLabel = new Label("Schiphol, Amsterdam");
        grid.add(amsterdamAirportLabel, 9, 2);
        
        CheckBox amsterdamAirport = new CheckBox();
        grid.add(amsterdamAirport, 10 ,2 );
        
        Label barcelonaAirportLabel = new Label("El Prat, Barcelona");
        grid.add(barcelonaAirportLabel, 9 , 3);
        
        CheckBox barcelonaAirport = new CheckBox();
        grid.add(barcelonaAirport, 10,3 );
        
        Label istanbulAirportLabel = new Label("Atatürk, Istanbul");
        grid.add(istanbulAirportLabel,9,4);
        
        CheckBox istanbulAirport = new CheckBox();
        grid.add(istanbulAirport, 10 ,4 );
        
        
        
        
        
        
        // deze aanpassen van grid naar root..
        Scene scene = new Scene(root, 1200, 920);
        
        primaryStage.setTitle("Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    
    }
}
