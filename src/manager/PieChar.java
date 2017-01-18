/*
 * Dit programma maakt een piechar die in een andere klasse wordt aangeroepen
*/
package manager;

import global.Mysql;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.*;
import javafx.stage.Stage;

public class PieChar {
    
    //mysql connectie
    Mysql mysql = new Mysql();  
    
    private final String USERNAME = mysql.getUsername();
    private final String PASSWORD = mysql.getPassword();
    private final String CONN_STRING = mysql.getUrlmysql();
    
    private int total = 0;
    private int totalKoffer = 0;
    
    HashMap<Integer, String> TempArray= new HashMap<>();
     
    public PieChart start(Stage primaryStage, String beginDatum, String eindDatum, String zoekOpDracht ,ArrayList AirportList) throws SQLException {
        
        PieChart chart = new PieChart();
        
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
                    pieChartData.add(new PieChart.Data(pieCharStringArray[b] +" Bagage found back: "+ (int) tempkofferLuchthaven,berekening));
                }

                //vul de chart met de piechart data
                chart.setData(pieChartData);
                chart.setTitle("Luggage found back");
                
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
                    pieChartData.add(new PieChart.Data(pieCharStringArray[b] +" Luggage lost: "+ (int) tempkofferLuchthaven ,berekening));
                }

                //vul de chart met de piechart data
                chart.setData(pieChartData);
                chart.setTitle("Luggage that is still lost");
               
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
                    pieChartData.add(new PieChart.Data(pieCharStringArray[b]+" Luggage deleted: "+ (int) tempkofferLuchthaven,berekening));
                }

                //piechart
                chart.setData(pieChartData);
                chart.setTitle("Luggage deleted.");
                
            }
        }catch (SQLException ex) {
            System.out.println("Code kan geen connectie maken met het database.");
        }
    
    return(chart);
    }
    
}