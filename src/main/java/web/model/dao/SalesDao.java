package web.model.dao;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;

@Component
public class SalesDao {

    public ArrayList<Map<String, String>> yearlySales(String year) {
        try{

        }catch(Exception e){
            System.out.println("yearlySales() : " + e );
        }
        return null;
    }

    public ArrayList<Map<String, String>> monthlySales(String year, String month) {
        try{

        }catch(Exception e){
            System.out.println("monthlySales() : " + e );
        }
        return null;
    }

}
