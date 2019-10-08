package l.chernenkiy.aqua;


import java.util.ArrayList;
import java.util.HashMap;

public class CartHelper {

    public static double countFinalSum(ArrayList<HashMap> cartItems){
      double result = 0;

      for (int i=0; i<cartItems.size(); i++){
          result += itemSum(cartItems.get(i));
      }

      return result;
    }

    public static double itemSum(HashMap<String, String> cartItem){
        Double quantity = convertDoublePoint((String) cartItem.get("quantity"));
        Double price = convertDoublePoint((String) cartItem.get("price"));
        return price * quantity;

    }

    public static Double convertDoublePoint(String number){
        if (number.contains(",")){
            number = number.replace(",",".");
        }
        if (number.isEmpty()){
            number = "0";
        }
        return Double.valueOf(number);
    }



}
