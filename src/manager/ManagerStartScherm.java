package manager;

import java.sql.SQLException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
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
import global.MenuB;
import global.Mysql;

public class ManagerStartScherm extends Application{
    
    //mysql connectie
    Mysql mysql = new Mysql();  
    
    PieChar pieChar = new PieChar();
    LineChar lineChar = new LineChar();
    
    //private mqsql
    private final String USERNAME = mysql.getUsername();
    private final String PASSWORD = mysql.getPassword();
    private final String CONN_STRING = mysql.getUrlmysql();
    
    private String beginJaarString, beginMaandString, beginDagString,eindJaarString,
        eindMaandString,eindDagString, beginDatum, eindDatum,
        stringAmsterdamSelected, stringBarcelonaSelected, stringIstanbulSelected, zoekOpDracht;

    //ArrayList
    private ArrayList<String> airportList = new ArrayList<String>();
    
    private ObservableList<PieChart.Data> pieChartData;
    private boolean amsterdamSelected, barcelonaSelected, istanbulSelected;

    //finals
    private final String ZOEKENOPGELOSTBAGAGE= "opgelostBagage";
    private final String ZOEKENNIETTERUGGEVONDEN= "nietTerugGevonden";
    private final String ZOEKENNAARBAGAGEWATVERWIJDERDIS= "verwijderdBagage";
    
    public void start(Stage primaryStage) {

        //airport list
        stringAmsterdamSelected = "Schiphol, Amsterdam";
        stringBarcelonaSelected = "El Prat, Barcelona";
        stringIstanbulSelected = "Atatürk, Istanbul";
        
        // deze vijf regels om de menubar aan te roepen
        MenuB menuB = new MenuB();
        MenuBar menuBar = menuB.createMenuB(primaryStage);        
        BorderPane root = new BorderPane();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        root.setTop(menuBar);
       
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        root.setCenter(grid);
        
        //labels en comboBoxen en checkboxen
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
        
        //Label Amsterdamairport
        Label amsterdamAirportLabel = new Label("Schiphol, Amsterdam");
        grid.add(amsterdamAirportLabel, 9, 2);
        
        //CheckBox Amsterdamairport
        CheckBox amsterdamAirport = new CheckBox();
        grid.add(amsterdamAirport, 10 ,2 );
        
        //Label Barcelona airport
        Label barcelonaAirportLabel = new Label("El Prat, Barcelona");
        grid.add(barcelonaAirportLabel, 9 , 3);
        
        //CheckBox barcelonaairport
        CheckBox barcelonaAirport = new CheckBox();
        grid.add(barcelonaAirport, 10,3 );
        
        Label istanbulAirportLabel = new Label("Atatürk, Istanbul");
        grid.add(istanbulAirportLabel,9,4);
        
        CheckBox istanbulAirport = new CheckBox();
        grid.add(istanbulAirport, 10 ,4 );
        
        Label opgelostLabel = new Label("Luggage found back.");
        grid.add(opgelostLabel, 11, 2);
        
        Button opgelostBagage = new Button("Show");
        grid.add(opgelostBagage, 12, 2);
        
        Label nietopgelostLabel = new Label("Luggage not found back.");
        grid.add(nietopgelostLabel, 11, 3);
        
        Button nietopgelostBagage = new Button("Show");
        grid.add(nietopgelostBagage, 12, 3);

        Label bagageVernietigdLabel = new Label("Luggage destroyed.");
        grid.add(bagageVernietigdLabel, 11, 4);
        
        Button bagageVernietigd = new Button("Show");
        grid.add(bagageVernietigd, 12, 4);
        
        Label bagagewWtIsZoekGeraaktLabel = new Label("Luggage that is lost.");
        grid.add(bagagewWtIsZoekGeraaktLabel, 11, 5);
        
        Button bagagewWtIsZoekGeraakt = new Button("Show");
        grid.add(bagagewWtIsZoekGeraakt, 12, 5); 
        
        //eventhandlers voor de begindatum
        beginJaar.setOnAction((event) -> {
            beginJaarString = (String) beginJaar.getSelectionModel().getSelectedItem();
        });
        
        beginMaand.setOnAction((event) -> {
           beginMaandString = (String) beginMaand.getSelectionModel().getSelectedItem();
        });
        
        beginDag.setOnAction((event) -> {
            beginDagString = (String) beginDag.getSelectionModel().getSelectedItem();
        });
        
        //eventhandler voor de einddatum
        eindJaar.setOnAction((event) -> {
            eindJaarString = (String) eindJaar.getSelectionModel().getSelectedItem();
        });
        
        eindMaand.setOnAction((event) -> {
            eindMaandString = (String) eindMaand.getSelectionModel().getSelectedItem();
        });
        
        eindDag.setOnAction((event) -> {
            eindDagString = (String) eindDag.getSelectionModel().getSelectedItem();
        });
        
        //eventhandlers voor de checkboxes
        amsterdamAirport.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
            amsterdamSelected = new_val;
            if(amsterdamSelected == true){
                airportList.add(stringAmsterdamSelected);
                System.out.println(airportList.size());
            } else {
                airportList.remove(stringAmsterdamSelected);
            }
        });
        
        barcelonaAirport.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
            barcelonaSelected = new_val;
            if(barcelonaSelected == true){
                airportList.add(stringBarcelonaSelected);
                System.out.println("barcelona"+airportList.size());
            } else {
                airportList.remove(stringBarcelonaSelected);
                System.out.println("barcelona"+airportList.size());
            }
        });        
        
        istanbulAirport.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
            istanbulSelected = new_val;
            if(istanbulSelected == true){
                airportList.add(stringIstanbulSelected);
            } else {
                airportList.remove(stringIstanbulSelected);
            }
        });
            
        opgelostBagage.setOnAction((ActionEvent e) -> {
            
            beginDatum = beginJaarString + "-" +beginMaandString + "-" +beginDagString;
            eindDatum = eindJaarString + "-" + eindMaandString + "-" + eindDagString;
            
            zoekOpDracht = ZOEKENOPGELOSTBAGAGE;
            try {
                pieChar.start(primaryStage, beginDatum, eindDatum, zoekOpDracht, airportList);
            } catch (SQLException ex) {    
            
            }
        });
        
        nietopgelostBagage.setOnAction((ActionEvent e) -> {
            
            //Bagage wat niet is opgelost
            beginDatum = beginJaarString + "-" +beginMaandString + "-" +beginDagString;
            eindDatum = eindJaarString + "-" + eindMaandString + "-" + eindDagString;
            
            zoekOpDracht = ZOEKENNIETTERUGGEVONDEN;
            
            try {
                pieChar.start(primaryStage, beginDatum, eindDatum, zoekOpDracht, airportList);
            } catch (SQLException ex) {    
            
            }
        });        
        
        bagageVernietigd.setOnAction((ActionEvent e) -> {
            
            //Bagage wat niet is opgelost
            beginDatum = beginJaarString + "-" +beginMaandString + "-" +beginDagString;
            eindDatum = eindJaarString + "-" + eindMaandString + "-" + eindDagString;
            
            zoekOpDracht = ZOEKENNAARBAGAGEWATVERWIJDERDIS;
            
            try {
                pieChar.start(primaryStage, beginDatum, eindDatum, zoekOpDracht, airportList);
            } catch (SQLException ex) {    
            
            }
        });

        /** =========== lineChar =========== */
        bagagewWtIsZoekGeraakt.setOnAction((ActionEvent e) -> {
            lineChar.start(primaryStage, beginJaarString, beginMaandString, eindJaarString, eindMaandString, airportList);
        });
        
        // deze aanpassen van grid naar root..
        Scene scene = new Scene(root, 1200, 920);
        
        primaryStage.setTitle("Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}