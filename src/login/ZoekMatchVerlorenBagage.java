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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.Stage;


class ZoekMatchVerlorenBagage {
    
    private final Mysql mysql = new Mysql();
    private final String USERNAME = mysql.getUsername();
    private final String PASSWORD = mysql.getPassword();
    private final String CONN_STRING = mysql.getUrlmysql();
    
    public void maakZoekString(Stage primaryStage, String bagagelabel, String kleur, String hoogte, 
            String lengte, String breedte, String luchthavenVertrek, 
            String luchthavenAankomst, String bijzonderheden, String merk, String hardSoftCase) {
        
        String zoekCriteria = "SELECT * FROM gevondenbagage WHERE (bagagelabel = '"+bagagelabel+"' OR bagagelabel = '' )";
        if(!kleur.equals("Other")){
            zoekCriteria += " AND (kleur = '"+kleur+"' OR kleur = 'Other' )";
        }else{
            zoekCriteria += " AND kleur = 'Other'";
        }
        if(!hoogte.equals("Unknown")){
            zoekCriteria += " AND (dikte = '"+hoogte+"' OR dikte = 'Unknown' )"; 
        }else{
            zoekCriteria += " AND dikte = 'Unknown'";
        }
        if(!lengte.equals("Unknown")){
            zoekCriteria += " AND (lengte = '"+lengte+"' OR lengte = 'Unknown' )";
        }else{
            zoekCriteria += " AND lengte = 'Unknown'";
        }
        if(!breedte.equals("Unknown")){
            zoekCriteria += " AND (breedte = '"+breedte+"' OR breedte = 'Unknown' )";
        }else{
            zoekCriteria += " AND breedte = 'Unknown'";
        }
        if(!merk.equals("Other")){
            zoekCriteria += " AND (merk = '"+merk+"' OR merk = 'Other' )"; 
        }else{
            zoekCriteria += " AND merk = 'Other'";
        }
        zoekCriteria += " AND softhard = '"+hardSoftCase+"'"; 
        zoekCriteria += " AND (luchthavengevonden = '"+luchthavenAankomst+"' OR luchthavengevonden = '"+luchthavenVertrek+"' )"; 
        
        System.out.println(zoekCriteria);
        zoekOpCriteria(zoekCriteria);        
    }
    
    private void zoekOpCriteria(String zoekString){
        Connection conn;
        try {
            conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
            Statement stmt = (Statement) conn.createStatement();
            ResultSet matchendeKoffersResult = stmt.executeQuery(zoekString);
            while(matchendeKoffersResult.next()){
                System.out.println(matchendeKoffersResult.getInt("gevondenkofferID"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZoekMatchVerlorenBagage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
