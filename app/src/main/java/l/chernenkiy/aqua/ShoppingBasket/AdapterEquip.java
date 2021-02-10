package l.chernenkiy.aqua.ShoppingBasket;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import l.chernenkiy.aqua.Helpers.CartHelper;
import l.chernenkiy.aqua.Helpers.Support;
import l.chernenkiy.aqua.R;


public class AdapterEquip extends BaseAdapter {

    private final ArrayList<HashMap<String, String>> cartEquipmentItem;
    private final Context context;
    Support support = new Support();


    public AdapterEquip(Context context, ArrayList<HashMap<String, String>> cartEquipmentItem){
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

    @SuppressLint({"ViewHolder", "InflateParams", "SetTextI18n"})
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        convertView = mInflater.inflate(R.layout.basket_cart_equip, null);

        HashMap<String, String> cartGetPosition = cartEquipmentItem.get(i);

        String finalSum = bigDecimal(cartGetPosition);

        TextView tvName = convertView.findViewById(R.id.name_equip);
        TextView tvPrice = convertView.findViewById(R.id.price_equip);
        TextView tvPriceOneItem = convertView.findViewById(R.id.price_one_item);
        TextView tvDescription = convertView.findViewById(R.id.txt_description);
        TextView tvProducer = convertView.findViewById(R.id.txt_manufacturer);
        TextView tvArticle = convertView.findViewById(R.id.vendor_code);
        TextView quantity = convertView.findViewById(R.id.quantity_equip);
        ImageView image = convertView.findViewById (R.id.image_shop_bask);

        quantity.setText(cartGetPosition.get("quantity"));
        tvPriceOneItem.setText (cartGetPosition.get ("price") + " грн." + " x  ");
        tvName.setText(cartGetPosition.get("name"));
        tvDescription.setText(cartGetPosition.get("description"));
        tvProducer.setText(cartGetPosition.get("producer"));
        tvPrice.setText(finalSum + " грн.");
        tvArticle.setText("Арт: " + cartGetPosition.get("article"));

        String urlImage = cartGetPosition.get ("image");
        support.loadImage(image, urlImage, context);

        convertView.setTag(i);
        return convertView;
    }

    @NotNull
    private String bigDecimal(HashMap<String, String> cartGetPosition) {
        double sumEquip = CartHelper.itemSum(cartGetPosition);
        BigDecimal bigDecimal = new BigDecimal(sumEquip);
        bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_UP);
        return String.valueOf(bigDecimal);
    }

}
