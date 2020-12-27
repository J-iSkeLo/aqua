package l.chernenkiy.aqua.ShoppingBasket;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import l.chernenkiy.aqua.Helpers.CartHelper;
import l.chernenkiy.aqua.R;

import static l.chernenkiy.aqua.MainActivity.cartEquipmentItem;

public class ShopBaskEquipAdapter extends BaseAdapter {

    private ArrayList<HashMap> cartEquipmentItem;
    private Context context;


    public ShopBaskEquipAdapter(Context context, ArrayList<HashMap> cartEquipmentItem){
        this.cartEquipmentItem = cartEquipmentItem;
        this.context = context;
    }

    @Override
    public int getCount() {
        return (cartEquipmentItem == null) ? 0 : cartEquipmentItem.size();
    }

    @Override
    public Object getItem(int i) {
        return cartEquipmentItem.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        convertView = mInflater.inflate(R.layout.cart_equip_basket, null);

        HashMap cartGetPosition = cartEquipmentItem.get(i);

        String finalSum = bigDecimal(cartGetPosition);

        TextView tvName = convertView.findViewById(R.id.name_equip);
        TextView tvPrice = convertView.findViewById(R.id.price_equip);
        TextView tvDescription = convertView.findViewById(R.id.txt_description);
        TextView tvProducer = convertView.findViewById(R.id.txt_manufacturer);
        TextView tvArticle = convertView.findViewById(R.id.vendor_code);
        TextView quantity=convertView.findViewById(R.id.quantity_equip);

        quantity.setText((String) cartGetPosition.get("quantity"));
        tvName.setText((String) cartGetPosition.get("name"));
        tvDescription.setText((String) cartGetPosition.get("description"));
        tvProducer.setText((String) cartGetPosition.get("producer"));
        tvPrice.setText(finalSum + " грн.");
        tvArticle.setText("Артикул: " + cartGetPosition.get("article"));

        convertView.setTag(i);


        return convertView;
    }

    @NotNull
    private String bigDecimal(HashMap cartGetPosition) {
        double sumEquip = CartHelper.itemSum(cartGetPosition);
        BigDecimal bigDecimal = new BigDecimal(sumEquip);
        bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_UP);
        return String.valueOf(bigDecimal);
    }

}
