/*
 * Dit programma maakt een piechar de statistieken laat zien.
*/
package manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.geometry.Insets;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.chart.*;
import global.MenuB;
import global.Mysql;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class PieChar extends Application {
    
    //mysql connectie
    Mysql mysql = new Mysql();  
    MenuB menubar = new MenuB();
    
    //private mqsql
    private final String USERNAME = mysql.getUsername();
    private final String PASSWORD = mysql.getPassword();
    private final String CONN_STRING = mysql.getUrlmysql();
    
    private int total = 0;
    private int totalKoffer = 0;
    
    HashMap<Integer, String> TempArray= new HashMap<Integer, String>();
     
    public void start(Stage primaryStage, String beginDatum, String eindDatum, String zoekOpDracht ,ArrayList AirportList) throws SQLException {
        
        
        ManagerStartScherm managerstartscherm = new ManagerStartScherm();
        
        // deze vijf regels om de menubar aan te roepen
        MenuB menuB = new MenuB();
        MenuBar menuBar = menuB.createMenuB(primaryStage);        
        BorderPane root = new BorderPane();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        root.setTop(menuBar);
       
        //Grid 
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        root.setCenter(grid);
        
        //scence
        Scene scene = new Scene(root);
        primaryStage.setTitle("");
        primaryStage.setWidth(1200);
        primaryStage.setHeight(920);
        
        Connection conn;
        try {
            
            //connect to mysql
            conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
            Statement st = conn.createStatement();
       
            //Maak een array aan
            int[]pieCharDoubleArray = new int[AirportList.size()];
            String[]pieCharStringArray = new String[AirportList.size()];
            
            //laat piechar zien als de bagage is terug gevonnden
            if("opgelostBagage".equals(zoekOpDracht)) {
                
                for (int i = 0; i < AirportList.size(); i++) {
                    System.out.println("AirportList "+AirportList.size());


                    //query
                    String query = "select count(luchthavengevonden) as count from gevondenbagage a inner join opgelost b on a.gevondenkofferID = b.gevondenkofferID" +
                        " where luchthavengevonden = '"+AirportList.get(i)+"' AND b.datum between '"+beginDatum+"' and '"+eindDatum+"'" +
                        " group by luchthavengevonden";

                    System.out.println(query);
                    ResultSet databaseResponse = st.executeQuery(query);
                    while (databaseResponse.next()) {

                        System.out.println(databaseResponse.getInt("count"));
                        pieCharDoubleArray[i] = databaseResponse.getInt("count");
                        
                        //tel de nieuwe hoeveel koffers op
                        totalKoffer = totalKoffer + databaseResponse.getInt("count");
                        pieCharStringArray[i]= (String) AirportList.get(i);
                    }
                }
                
                //fxcollection
                ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

                //loop om alles data in pieChart te zetten
                for (int b = 0; b < pieCharDoubleArray.length; b++) {


                    double tempkofferLuchthaven = pieCharDoubleArray[b];
                    double berekening = tempkofferLuchthaven/totalKoffer*100;

                    //Gooi data in Piechar    
                    pieChartData.add(new PieChart.Data(pieCharStringArray[b] +" Bagage found back "+ tempkofferLuchthaven,berekening));
                }

                //piechart
                final PieChart chart = new PieChart(pieChartData);
                chart.setTitle("Found bagage back");
                root.setCenter(chart);
                
                //maak het beschikbaar in scene
                primaryStage.setScene(scene);
                primaryStage.show();
            }
            
            //kijken naar niet terug gevonden bagage
            if("nietTerugGevonden".equals(zoekOpDracht)){
                
                for (int i = 0; i < AirportList.size(); i++) {

                    //query
                    String query = "SELECT count(bagagelabel) as count FROM corendonbagagesystem.verlorenbagage where luchthavenvertrokken = '"+AirportList.get(i)+"' AND status='notSolved';";

                    System.out.println(query);
                    ResultSet databaseResponse = st.executeQuery(query);
                    while (databaseResponse.next()) {
                        
                        pieCharDoubleArray[i] = databaseResponse.getInt("count");
                        
                        //tel de nieuwe hoeveel koffers op
                        totalKoffer = totalKoffer + databaseResponse.getInt("count");
                        pieCharStringArray[i]= (String) AirportList.get(i);
                    }
                }
                
                //fxcollection
                ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

                //loop om alles data in pieChart te zetten
                for (int b = 0; b < pieCharDoubleArray.length; b++) {


                    double tempkofferLuchthaven = pieCharDoubleArray[b];
                    double berekening = tempkofferLuchthaven/totalKoffer*100;

                    //Gooi data in Piechar    
                    pieChartData.add(new PieChart.Data(pieCharStringArray[b] +" Bagage not found back "+ tempkofferLuchthaven ,berekening));
                }

                //piechart
                final PieChart chart = new PieChart(pieChartData);
                chart.setTitle("Not found bagage back");
                root.setCenter(chart);
                
                //maak het beschikbaar in scene
                primaryStage.setScene(scene);
                primaryStage.show();
            }
            
            //bagage wat naar de sloop is gegaan
            if("verwijderdBagage".equals(zoekOpDracht)){
                
                for (int i = 0; i < AirportList.size(); i++) {

                    //query
                    String query = "SELECT count(bagagelabel) as count FROM corendonbagagesystem.verlorenbagage where luchthavenvertrokken = '"+AirportList.get(i)+"' AND status='deleted';";

                    ResultSet databaseResponse = st.executeQuery(query);
                    while (databaseResponse.next()) {
                        
                        pieCharDoubleArray[i] = databaseResponse.getInt("count");
                        
                        //tel de nieuwe hoeveel koffers op
                        totalKoffer = totalKoffer + databaseResponse.getInt("count");
                        pieCharStringArray[i]= (String) AirportList.get(i);
                    }
                }
                
                //fxcollection
                ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

                //loop om alles data in pieChart te zetten
                for (int b = 0; b < pieCharDoubleArray.length; b++) {


                    double tempkofferLuchthaven = pieCharDoubleArray[b];
                    double berekening = tempkofferLuchthaven/totalKoffer*100;

                    //Gooi data in Piechar    
                    pieChartData.add(new PieChart.Data(pieCharStringArray[b]+" Bagage delete "+ tempkofferLuchthaven,berekening));
                }

                //piechart
                final PieChart chart = new PieChart(pieChartData);
                chart.setTitle("Bagage that not found back.");
                root.setCenter(chart);
                
                
                primaryStage.setScene(scene);
                primaryStage.show();

            }
        }catch (SQLException ex) {
            System.out.println("Code kan geen connectie maken met het database.");
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
       
    }
}