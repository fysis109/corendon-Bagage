package login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class zoekBagage {
    
    //Strings
    private final String bagageNummer; 
    private final String kofferKleur;
    private final String merkKoffer;
    private final String breedteKoffer;
    private final String lengteKoffer;
    private final String dikteKoffer;
    private final String locatieKoffer, softHardCase;
    
    //mysql connectie
    Mysql mysql = new Mysql();
    
    //private mqsql
    private final String USERNAME = mysql.getUsername();
    private final String PASSWORD = mysql.getPassword();
    private final String CONN_STRING = mysql.getUrlmysql();
    public static String rol;    
    
    zoekBagage (String bagageNummer, String kofferKleur, String merkKoffer, String breedteKoffer, String lengteKoffer, String dikteKoffer, String locatieKoffer, String softHardCase){
    
        //variable verwerken
        this.bagageNummer = bagageNummer;
        this.kofferKleur = kofferKleur;
        this.merkKoffer = merkKoffer;
        this.breedteKoffer = breedteKoffer;
        this.lengteKoffer = lengteKoffer;
        this.dikteKoffer = dikteKoffer;
        this.locatieKoffer = locatieKoffer;
        this.softHardCase = softHardCase;
    }
    
    public int[] check(){
        int [] zoekenOpkenmerkenCallback;
        Connection conn;
        try {

            //maak connectie met het database
            conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
            
            //create the java statement
            Statement st = conn.createStatement();
            
            //if else
            
            if (!bagageNummer.trim().isEmpty()){
                
                // execute the query, and get a java resultset
                ResultSet databaseResponse = st.executeQuery("SELECT * FROM verlorenbagage WHERE bagagelabel ='"+bagageNummer+"' AND status = 'notSolved'");
                while (databaseResponse.next()){
                    
                    //database response verwerken
                    String DBBagageNummer = databaseResponse.getString("bagagelabel");
                    
                    //kijk of het bagage nummer het zelfde is
                    if(bagageNummer.equals(DBBagageNummer)){
                        
                        zoekenOpkenmerkenCallback = new int[1];
                        //database response verwerken
                        zoekenOpkenmerkenCallback [0] = databaseResponse.getInt("verlorenkofferID");

                        //koffer id terug geven
                        return zoekenOpkenmerkenCallback;
                            //SELECT * FROM verlorenbagage WHERE kleur = '"+kofferKleur+"' AND merk = '"+merkKoffer+"' AND breedte='"+breedteKoffer+"' AND lengte ='"+lengteKoffer+"' AND dikte = '"+dikteKoffer+"'" 
                    }
                }            
            } else {
                
                //quary string maken
                //String ZoekenKenmerkenQuary = "SELECT * FROM verlorenbagage WHERE";

                //kijk of er 1 of meer kenmerken zijn ingevuld
                if ("Other".equals(kofferKleur) && "Other".equals(merkKoffer) && "Unknown".equals(breedteKoffer) && "Unknown".equals(lengteKoffer) && "Unknown".equals(dikteKoffer)){
                    System.out.println("Vul aub speciekere informatie");
                } else {
                    zoekenOpkenmerkenCallback = zoekenOpKenmerken();
                            
                    return zoekenOpkenmerkenCallback;
                }
            }
        } catch (SQLException ed) {
            System.err.println(ed);
        }
        
        zoekenOpkenmerkenCallback = new int[1];
        zoekenOpkenmerkenCallback[0] = 0;
        return zoekenOpkenmerkenCallback;
    };
    
    
    int[] zoekenOpKenmerken(){
        
        
        
        //
        Connection conn;
        try {
            //maak connectie met het database
            conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
            
            //create the java statement
            Statement st = conn.createStatement();
            
            //quary string maken
            String ZoekenKenmerkenQuary = "SELECT * FROM verlorenbagage WHERE";
            
            //kijk of er 1 of meer kenmerken zijn ingevuld
            if ("Other".equals(kofferKleur) && "Other".equals(merkKoffer) && "Unknown".equals(breedteKoffer) && "Unknown".equals(lengteKoffer) && "Unknown".equals(dikteKoffer)){
                int [] geenResultaat = new int [1];
                geenResultaat[0] = 0;
                System.out.println("Vul aub speciekere informatie");
                return geenResultaat;
                
            } else {
                //alle kenmerken door gaan om te kijken we info handig is om te gebruiken.
                
                //koffer kleur
                if(!"Other".equals(kofferKleur)){
                    
                    //kijken of quary nog het zelfde
                    if(!ZoekenKenmerkenQuary.equals("SELECT * FROM verlorenbagage WHERE")){
                        ZoekenKenmerkenQuary += " AND (kleur = '"+kofferKleur+"' OR kleur = 'Other')";
                    } else {
                        ZoekenKenmerkenQuary += " (kleur = '"+kofferKleur+"' OR kleur = 'Other')";
                    }
                }
                
                
                
                //merkKoffer
                if(!"Other".equals(merkKoffer)){
                    
                    //kijken of quary nog het zelfde
                    if(!ZoekenKenmerkenQuary.equals("SELECT * FROM verlorenbagage WHERE")){
                        ZoekenKenmerkenQuary += " AND (merk='"+merkKoffer+"' OR merk = 'Other')";
                    } else {
                        ZoekenKenmerkenQuary += " (merk='"+merkKoffer+"' OR merk = 'Other')";
                    }
                }
                
                //breedteKoffer
                if(!"Unknown".equals(breedteKoffer)){
                    
                    //kijken of quary nog het zelfde
                    if(!ZoekenKenmerkenQuary.equals("SELECT * FROM verlorenbagage WHERE")){
                        ZoekenKenmerkenQuary += " AND (breedte='"+breedteKoffer+"' OR breedte = 'Unknown')";
                    } else {
                        ZoekenKenmerkenQuary += " (breedte='"+breedteKoffer+"' OR breedte = 'Unknown')";
                    }
                }

                //lengteKoffer
                if(!"Unknown".equals(lengteKoffer)){
                    
                    //kijken of quary nog het zelfde
                    if(!ZoekenKenmerkenQuary.equals("SELECT * FROM verlorenbagage WHERE")){
                        ZoekenKenmerkenQuary += " AND (lengte='"+lengteKoffer+"' OR lengte = 'Unknown')";
                    } else {
                        ZoekenKenmerkenQuary += " (lengte='"+lengteKoffer+"' OR lengte = 'Unknown')";
                    }
                }
                
                //dikteKoffer
                if(!dikteKoffer.equals("Unknown")){
                    
                    //kijken of quary nog het zelfde
                    if(!ZoekenKenmerkenQuary.equals("SELECT * FROM verlorenbagage WHERE")){
                        ZoekenKenmerkenQuary += " AND (dikte='"+dikteKoffer+"' OR dikte = 'Unknown')";
                    } else {
                        ZoekenKenmerkenQuary += " (dikte='"+dikteKoffer+"' OR dikte = 'Unknown')";
                    }
                }
                
                if(!ZoekenKenmerkenQuary.equals("SELECT * FROM verlorenbagage WHERE")){
                        ZoekenKenmerkenQuary += " AND softhard = '"+softHardCase+"'";
                    } else {
                        ZoekenKenmerkenQuary += " softhard '"+softHardCase+"'";
                        
                    }
                
                ZoekenKenmerkenQuary += " AND (luchthavenvertrokken = '"+locatieKoffer+"' OR luchthavenaankomst = '"+locatieKoffer+"')";
                ZoekenKenmerkenQuary += " AND status = 'notSolved'";
                
             
                
                System.out.println(ZoekenKenmerkenQuary);
                
                ResultSet databaseResponse = st.executeQuery(ZoekenKenmerkenQuary);
                ArrayList<Integer> list = new ArrayList<Integer>();
                //
                while (databaseResponse.next()){
                    list.add(databaseResponse.getInt("verlorenkofferID"));
                }
                if(list.isEmpty()){
                    int [] resultIfNoMatches = new int[1];
                    resultIfNoMatches [0] = 0;
                    return resultIfNoMatches;
                }
                int [] result = new int[list.size()];
                result = list.stream().mapToInt(i->i).toArray();
                return result;
            }
        } catch (SQLException ed) {
            System.err.println(ed);
        }
        
        int [] test = new int[1];
        test[0] = 0;
        return test;
    };
}