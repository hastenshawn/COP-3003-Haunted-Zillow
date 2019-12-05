package sample;
// Add logging & change try/catches to throws
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class connManager {
    private Connection con = null;
    private ArrayList<String> listOfStates = new ArrayList<String>();
    private ArrayList<String> listOfCities = new ArrayList<String>();
    private ArrayList<String> listOfAddresses = new ArrayList<String>();
    private  ArrayList<String> listOfZipcodes = new ArrayList<String>();
    private  ArrayList<String> listOfZPIDS = new ArrayList<String>();

    public connManager() throws SQLException {
        //Set this to your directory as it does not want to work with relative path yet
        con = DriverManager.getConnection("jdbc:h2:C:\\Users\\ShawnsPC\\OneDrive - Florida Gulf Coast University\\Object Oriented Pro\\Zillow - Haunted\\DB\\productDB");
    }

    public void selectProperties(String[] searchValues) throws SQLException {
        String query = "SELECT * FROM PROPERTIES WHERE ";
        String queryValues = "";
        if(searchValues[0] != ""){
            queryValues += "ADDRESS LIKE '%"+ searchValues[0] +"%'";
        }
        if(searchValues[1] != ""){
            if(queryValues != ""){
                queryValues += " AND ";
            }
            queryValues += "CITY LIKE '%"+ searchValues[1] +"%'";
        }
        if(searchValues[2] != ""){
            if(queryValues != ""){
                queryValues += " AND ";
            }
            queryValues += "STATE LIKE '%"+ searchValues[2] +"%'";
        }
        if(searchValues[3] != ""){
            if(queryValues != ""){
                queryValues += " AND ";
            }
            queryValues += "ZIPCODE LIKE '%"+ searchValues[3] +"%'";
        }
        query += queryValues;
        System.out.println(query);
        ResultSet rs = null;

        try {
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery(query);

            listOfAddresses.clear();
            listOfCities.clear();
            listOfStates.clear();
            listOfZipcodes.clear();
            listOfZPIDS.clear();
            while(rs.next()) {
                this.listOfAddresses.add(rs.getString("ADDRESS"));
                this.listOfCities.add(rs.getString("CITY"));
                this.listOfStates.add(rs.getString("STATE"));
                this.listOfZipcodes.add(rs.getString("ZIPCODE"));
                this.listOfZPIDS.add(rs.getString("ZPID"));
                /*System.out.printf("ADDRESS = %s%n", rs.getString("ADDRESS"));
                System.out.printf("CITY = %s%n", rs.getString("CITY"));
                System.out.printf("STATE = %s%n", rs.getString("STATE"));
                System.out.printf("ZIPCODE = %s%n", rs.getString("ZIPCODE"));
                System.out.println("\n");*/
            }

        } catch (SQLException e) {
            sqlExceptionHandler(e);
        }
    }

    public ArrayList<String> getStates(){
        return this.listOfStates;
    }

    public ArrayList<String> getAddresses(){
        return this.listOfAddresses;
    }

    public ArrayList<String> getCities(){
        return this.listOfCities;
    }

    public ArrayList<String> getZips(){
        return this.listOfZipcodes;
    }

    public ArrayList<String> getZPIDS(){
        return this.listOfZPIDS;
    }

    public void closeCon() {
        try {
            con.close();
        } catch (SQLException e) {
            sqlExceptionHandler(e);
        }
    }

    public void sqlExceptionHandler(SQLException error) {
        // add logging, could make into a wrapper function
        System.out.println("Standard Failure: " + error.getMessage());
    }
}