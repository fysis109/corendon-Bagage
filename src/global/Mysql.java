/*
 * Geeft de informatie om een connectie te maken bij de database
 */
package global;

public class Mysql {
 
    private final String USENAME = "VUL HIER UW USERNAME IN";
    private final String PASSWORD = "VUL HIER UW WACHTWOORD IN";
    private final String IPADDRESS = "localhost";
    private final String POORT = "3306";
    private final String DATABASENAAM = "corendonbagagesystem";
    private final boolean AUTORECONNECT = true;
    private final boolean SSL = false;
  
    public String getUsername(){
        return USENAME;
    };
        
    public String getPassword(){
        return PASSWORD;
    };
    
    public String getUrlmysql(){
        return "jdbc:mysql://"+IPADDRESS+":"+POORT+"/"+DATABASENAAM+"?autoReconnect="+AUTORECONNECT+"&useSSL="+SSL;
    };


}