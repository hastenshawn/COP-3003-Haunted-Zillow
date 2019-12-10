import java.io.FileReader;
import java.io.IOException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException, SQLException {
        connManager pm = new connManager();

        String[] resValues = {"1","200","100","shawn@gmail.com"};

        //pm.setReservation("2019/11/28", "2019/12/2", resValues);
        pm.selectAllReservations();
        /*Calendar d1 = Calendar.getInstance();
        Calendar d2 = Calendar.getInstance();

        ArrayList<String> listOfDates = new ArrayList<String>();

        d1.set(2010, 7, 23);
        d2.set(2010, 8, 2);
        Date startDate = d1.getTime();
        Date endDate = d2.getTime();
        long startTime = startDate.getTime();
        long endTime = endDate.getTime();
        long diffTime = endTime - startTime;
        long diffDays = diffTime / (1000 * 60 * 60 * 24);

        for(int i = 0; i<=diffDays; i++){
            int newDay = d1.get(Calendar.DATE);
            if(i!=0) {
                newDay = d1.get(Calendar.DATE) + 1;
            }
            System.out.println(newDay);
            d1.set(Calendar.DATE, newDay);
            String dateToAdd = d1.get(Calendar.YEAR) + "-" + d1.get(Calendar.MONTH) + "-" + d1.get(Calendar.DATE);
            listOfDates.add(dateToAdd);
        }

        System.out.println(listOfDates);

        //pm.selectAllCustomer();
        //pm.selectAllEmployees();*/
        
        pm.closeCon();
    }
}