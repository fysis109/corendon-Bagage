/*
 * Dit programma returnd een lineChart die in een andere klasse wordt toegevoegd
 * aan de gridd.
*/
package manager;

import global.Mysql;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class LineChar {

    //mysql connectie
    private final Mysql MYSQL = new Mysql();
    private final String USERNAME = MYSQL.getUsername();
    private final String PASSWORD = MYSQL.getPassword();
    private final String CONN_STRING = MYSQL.getUrlmysql();
    
    public LineChart start(Stage primaryStage, String beginJaarAlleStatsString, String beginMaandAlleStatsString, 
                    String eindJaarAlleStatsString, String eindMaandAlleStatsString, ArrayList AirportList) {
        
        int tempJaar, tempMaand, tempQueryMaand, tempQueryJaar, count = 0;
        String eindDatum, eindDatumString;
       
        //defining the axes
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();    
        xAxis.setLabel("Month");

        //creating the chart
        final LineChart<String,Number> lineChart = 
            new LineChart<String,Number>(xAxis,yAxis);
        
        primaryStage.setTitle("Lost bagage");
        lineChart.setTitle("Lost bagage");
        
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("Lost babage");
        
        //mysql
        Connection conn;
        try {
            
            //connect to mysql
            conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
            Statement st = conn.createStatement();
            
            //temp strings vullen
            tempMaand = Integer.parseInt(beginMaandAlleStatsString);
            tempJaar = Integer.parseInt(beginJaarAlleStatsString);
            
            while(true){
                
                //Einddatum word gevuld tot hoe ver de while loop moet gaan
                eindDatum = eindJaarAlleStatsString +"-"+ eindMaandAlleStatsString;
                
                //temp vullen voor begin data voor de query
                tempQueryMaand = tempMaand;
                tempQueryJaar = tempJaar;
                
                //datum uitrekenen
                tempMaand += 1;
                if(tempMaand == 13){
                    tempMaand = 1;
                    tempJaar += 1;
                }
                
                String query = "SELECT count(verlorenkofferID) as count FROM corendonbagagesystem.verlorenbagage where datum between '"+tempQueryJaar+"-"+tempQueryMaand+"-01' and '"+tempJaar+"-"+tempMaand+"-01'";
                
                //0 voor de maand zetten
                if(tempMaand < 10){
                    eindDatumString = tempJaar+"-0"+tempMaand;
                } else {
                    eindDatumString = tempJaar+"-"+tempMaand;
                }
                
                ResultSet databaseResponse = st.executeQuery(query);
                while (databaseResponse.next()) {
                   
                    //tel de nieuwe hoeveel koffers op
                    count += 1;
                    
                    //add data 
                    series.getData().add(new XYChart.Data(eindDatumString, databaseResponse.getInt("count")));
                }
               
                //kijk of eindDatum string het zelfde is al eindDatumString en als dat het zelfde is stop met de while loop
                if(eindDatumString.equals(eindDatum)){
                    break;
                }
            }
        }catch (SQLException ex) {
            System.out.println(ex);
        }
        
        lineChart.getData().add(series);
        return lineChart;
    }

}