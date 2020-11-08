package l.chernenkiy.aqua.Helpers;


import java.util.ArrayList;
import java.util.HashMap;

public class CartHelper {

    public static double countFinalSum(ArrayList<HashMap> arrayList){
      double result = 0;

      for (int i=0; i<arrayList.size(); i++){
          result += itemSum(arrayList.get(i));
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

    public static boolean findCartItem(String name, String price, ArrayList<HashMap> cartItems) {
        for (int i=0; i<cartItems.size(); i++) {
            if (cartItems.get(i).get("name").equals(name)&&cartItems.get(i).get("price").equals(price)) {
                return true;
            }

        }
        return false;
    }



}
