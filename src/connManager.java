
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
import java.util.HashMap;

public class connManager {
    private Connection con = null;

    public connManager() throws SQLException {
        //Set this to your directory as it does not want to work with relative path yet
        con = DriverManager.getConnection("jdbc:h2:C:\\Users\\ShawnsPC\\OneDrive - Florida Gulf Coast University\\H2Test\\src\\Database\\productDB");
    }

    //Creates a new customer
    public void insertCustomer(String[] insertValues) throws SQLException {
        String insertCustomer = "INSERT INTO customers (" +
                "    email," +
                "    firstName," +
                "    lastName," +
                "    phoneNumber," +
                "    lastFourCC" +
                ")" +
                "VALUES(?,?,?,?,?)";
        PreparedStatement pstmt = con.prepareStatement(insertCustomer);
        pstmt.setString(1, insertValues[0]);
        pstmt.setString(2, insertValues[1]);
        pstmt.setString(3, insertValues[2]);
        pstmt.setInt(4, Integer.parseInt(insertValues[3]));
        pstmt.setInt(5, Integer.parseInt(insertValues[4]));
        pstmt.executeUpdate();
    }

    //Creating a new employee
    public void insertEmployee(String[] insertValues) throws SQLException {
        String insertEmployee = "INSERT INTO employees (" +
                "    employeeID," +
                "    firstName," +
                "    lastName," +
                "    accessID" +
                ")" +
                "VALUES(?,?,?,?)";
        PreparedStatement pstmt = con.prepareStatement(insertEmployee);
        pstmt.setInt(1, Integer.parseInt(insertValues[0]));
        pstmt.setString(2, insertValues[1]);
        pstmt.setString(3, insertValues[2]);
        pstmt.setInt(4, Integer.parseInt(insertValues[3]));
        pstmt.executeUpdate();
    }

    //Creating access levels
    public void insertAccessID(String[] insertValues) throws SQLException {
        String insertAccessID = "INSERT INTO accessID (" +
                "    employeeID" +
                ")" +
                "VALUES(?)";
        PreparedStatement pstmt = con.prepareStatement(insertAccessID);
        pstmt.setInt(1, Integer.parseInt(insertValues[0]));
        pstmt.executeUpdate();
    }

    //Creating a new reservation
    public void setReservation(String start, String end, String[] insertValues) throws SQLException {
        Calendar d1 = Calendar.getInstance();
        Calendar d2 = Calendar.getInstance();

        String[]startStr = start.split("/");
        String[]endStr = end.split("/");

        //Since date object is base off index starting at 0 we need to take 1 off the month
        int startMonth = Integer.parseInt(startStr[1])-1;
        int endMonth = Integer.parseInt(endStr[1])-1;

        d1.set(Integer.parseInt(startStr[0]), startMonth, Integer.parseInt(startStr[2]));
        d2.set(Integer.parseInt(endStr[0]), endMonth, Integer.parseInt(endStr[2]));
        Date startDate = d1.getTime();
        Date endDate = d2.getTime();
        long startTime = startDate.getTime();
        long endTime = endDate.getTime();
        long diffTime = endTime - startTime;
        long diffDays = diffTime / (1000 * 60 * 60 * 24);

        ResultSet rs = null;

        int nextID = 0;

        try {
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT TOP 1 * FROM RESERVATIONS ORDER BY resID DESC");

            while(rs.next()) {
                System.out.printf("resID = %d%n", rs.getInt("resID"));
                nextID = rs.getInt("resID");
                nextID++;
                System.out.println(nextID);
            }

        } catch (SQLException e) {
            sqlExceptionHandler(e);
        }

        for(int i = 0; i<=diffDays; i++){
            int newDay = d1.get(Calendar.DATE);
            if(i!=0) {
                newDay = d1.get(Calendar.DATE) + 1;
            }
            //System.out.println(newDay);
            d1.set(Calendar.DATE, newDay);

            //Put back the one we took away as SQL is not index based
            int currMonth = d1.get(Calendar.MONTH)+1;
            String dateToAdd = d1.get(Calendar.YEAR) + "-" + currMonth + "-" + d1.get(Calendar.DATE);
            System.out.println(dateToAdd);
            String insertReservation = "INSERT INTO RESERVATIONS (" +
                    "    resDate," +
                    "    payCollected," +
                    "    total," +
                    "    roomID," +
                    "    resID," +
                    "    customerEmail" +
                    ")" +
                    "VALUES(?,?,?,?,?,?)";
            PreparedStatement pstmt = con.prepareStatement(insertReservation);
            pstmt.setDate(1, java.sql.Date.valueOf(dateToAdd));
            pstmt.setInt(2, Integer.parseInt(insertValues[0]));
            pstmt.setFloat(3, Float.parseFloat(insertValues[1]));
            pstmt.setInt(4, Integer.parseInt(insertValues[2]));
            pstmt.setInt(5, Integer.parseInt(String.valueOf(nextID)));
            pstmt.setString(6, insertValues[3]);
            pstmt.executeUpdate();
        }
    }

    //Creating a table not really useful for this
    public void createTable(String SQL_CREATE) throws SQLException {
        System.out.println(SQL_CREATE);
        PreparedStatement preparedStatement = con.prepareStatement(SQL_CREATE);
        try {
            preparedStatement.execute();
        }catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Selecting all Customers
    public void selectAllCustomer() {
        ResultSet rs = null;

        try {
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT * FROM customers;");

            while(rs.next()) {
                System.out.printf("email = %s%n", rs.getString("email"));
                System.out.printf("FirstName = %s%n", rs.getString("firstName"));
                System.out.printf("LastName = %s%n", rs.getString("lastName"));
                System.out.printf("PhoneNumber = %d%n", rs.getInt("phoneNumber"));
                System.out.printf("Last 4 = %s%n", rs.getString("lastFourCC"));
                System.out.println("\n");
            }

        } catch (SQLException e) {
            sqlExceptionHandler(e);
        }
    }
    public void selectAllEmployees() {
        ResultSet rs = null;

        try {
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT * FROM EMPLOYEES");

            while(rs.next()) {
                System.out.printf("employeeID = %s%n", rs.getString("employeeID"));
                System.out.printf("FirstName = %s%n", rs.getString("firstName"));
                System.out.printf("LastName = %s%n", rs.getString("lastName"));
                System.out.printf("accessID = %s%n", rs.getInt("accessID"));
                System.out.println("\n");
            }

        } catch (SQLException e) {
            sqlExceptionHandler(e);
        }
    }

    //Selecting all Reservations
    public void selectAllReservations(String resDate, int payCollected, float total, int roomID, String customerEmail) {
        ResultSet rs = null;

        System.out.printf("resDate "+resDate);
        System.out.printf("payCollected "+payCollected);
        System.out.printf("total "+total);
        System.out.printf("roomID "+roomID);
        System.out.printf("customerEmail "+customerEmail);

        try {
            Statement stmt = con.createStatement();
            String query = "";
            if(resDate == ""){

            }
            rs = stmt.executeQuery("SELECT * FROM RESERVATIONS;");

            while(rs.next()) {
                System.out.printf("resDate = %s%n", rs.getDate("resDate"));
                System.out.printf("payCollected = %s%n", rs.getInt("payCollected"));
                System.out.printf("total = %s%n", rs.getFloat("total"));
                System.out.printf("roomID = %d%n", rs.getInt("roomID"));
                System.out.printf("resID = %s%n", rs.getString("resID"));
                System.out.printf("customerEmail = %s%n", rs.getString("customerEmail"));
                System.out.println("\n");
            }

        } catch (SQLException e) {
            sqlExceptionHandler(e);
        }
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