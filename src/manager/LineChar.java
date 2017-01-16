/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import global.MenuB;
import global.Mysql;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LineChar extends Application {

    //mysql connectie
    Mysql mysql = new Mysql();  
    
    //private mqsql
    private final String USERNAME = mysql.getUsername();
    private final String PASSWORD = mysql.getPassword();
    private final String CONN_STRING = mysql.getUrlmysql();
    
    //int
    private int tempJaar, tempMaand, tempQueryMaand, tempQueryJaar, count = 0;
    
    //strings
    private String eindDatum, eindDatumString;
    
    public void start(Stage primaryStage, String beginJaarAlleStatsString, String beginMaandAlleStatsString, 
                    String eindJaarAlleStatsString, String eindMaandAlleStatsString, ArrayList AirportList) {
        
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
        
        //defining the axes
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();    
        xAxis.setLabel("Month");

        //creating the chart
        final LineChart<String,Number> lineChart = 
            new LineChart<String,Number>(xAxis,yAxis);
        
        primaryStage.setTitle("Line Chart Sample");
        lineChart.setTitle("Stock Monitoring, 2010");
        
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("My portfolio");
        
        //mysql
        Connection conn;
        try {
            
            //connect to mysql
            conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
            Statement st = conn.createStatement();
            
            //temp strings vullen
            tempMaand = Integer.parseInt(beginMaandAlleStatsString);
            tempJaar = Integer.parseInt(beginJaarAlleStatsString);
            
            //kijken in welke maanden er op het scherm moeten komen
            while(true){
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
                
                String test1 = eindDatumString;
                String test2 = eindDatum;
                
                if(test1.equals(test2)){
                    break;
                }
            }
        }catch (SQLException ex) {
            System.out.println("Code kan geen connectie maken met het database.");
        }

        //scence
        //Scene scene = new Scene(new Group());
        primaryStage.setTitle("");
        primaryStage.setWidth(1200);
        primaryStage.setHeight(920);
        
        Scene scene  = new Scene(lineChart,800,600);
        lineChart.getData().add(series);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
 
    @Override
    public void start(Stage primaryStage) throws Exception {
       
    }
}