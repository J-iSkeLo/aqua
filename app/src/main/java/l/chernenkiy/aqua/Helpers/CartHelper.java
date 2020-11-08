package l.chernenkiy.aqua.Helpers;


import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import static l.chernenkiy.aqua.Fish.Fish.cartAddItemText;
import static l.chernenkiy.aqua.MainActivity.cartEquipmentItem;
import static l.chernenkiy.aqua.MainActivity.cartItems;

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

    public static boolean findCartItem(String name, String price, ArrayList<HashMap> arrayList) {
        for (int i=0; i<arrayList.size(); i++) {
            if (arrayList.get(i).get("name").equals(name)&&arrayList.get(i).get("price").equals(price)) {
                return true;
            }

        }
        return false;
    }

    public static void calculateItemsCart() {
        cartAddItemText.setVisibility(View.VISIBLE);
        cartAddItemText.setText(String.valueOf(cartItems.size() + cartEquipmentItem.size()));
    }



}
