package l.chernenkiy.aqua.ShoppingBasket;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import l.chernenkiy.aqua.Helpers.CartHelper;
import l.chernenkiy.aqua.R;

public class ShopBaskFishAdapter extends BaseAdapter {

    public static ArrayList<HashMap> cartItems;
    private Context context;
    private ArrayList<HashMap> filteredData;


    public ShopBaskFishAdapter(Context context, ArrayList<HashMap> cartItems){
        this.cartItems = cartItems;
        this.context = context;
//        this.filteredData = new ArrayList<HashMap>();
//        this.filteredData.addAll(cartItems);
    }

    @Override
    public int getCount() {
        return (cartItems == null) ? 0 : cartItems.size();
    }

    @Override
    public Object getItem(int i) {
        return cartItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        convertView = mInflater.inflate(R.layout.cart, null);

        TextView tvName = convertView.findViewById(R.id.name_cart);
        TextView tvSize = convertView.findViewById(R.id.size_cart);
        TextView tvPrice = convertView.findViewById(R.id.price_cart);
        TextView tvNumber = convertView.findViewById(R.id.number_cart);
        TextView quantity=convertView.findViewById(R.id.quantity);

        quantity.setText((String) cartItems.get(i).get("quantity"));
        tvName.setText((String) cartItems.get(i).get("name"));
        tvSize.setText(cartItems.get(i).get("size") + " см.");
        tvPrice.setText(CartHelper.itemSum(cartItems.get(i))+ " грн.");
        tvNumber.setText("№ " + cartItems.get(i).get("number"));

        convertView.setTag(i);

        return convertView;
    }

    public void myFilter (String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        cartItems.clear();
        if (charText.length() == 0) {
            cartItems.addAll(filteredData);
        } else {
            for (HashMap HM : filteredData) {
                String strName = String.valueOf(HM.get("name"));
                if (strName.toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    cartItems.add(HM);
                }
            }
        }
        notifyDataSetChanged();
    }
}
