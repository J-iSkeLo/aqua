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

import java.util.ArrayList;
import java.util.HashMap;

import l.chernenkiy.aqua.Helpers.CartHelper;
import l.chernenkiy.aqua.Helpers.Support;
import l.chernenkiy.aqua.R;

public class AdapterFish extends BaseAdapter {

    private final ArrayList<HashMap> cartItems;
    private final Context context;
    Support support = new Support();


    public AdapterFish(Context context, ArrayList<HashMap> cartItems){
        this.cartItems = cartItems;
        this.context = context;
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

    @SuppressLint({"SetTextI18n", "ViewHolder", "InflateParams"})
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);


        convertView = mInflater.inflate(R.layout.basket_cart_fish, null);

        HashMap getPosition = cartItems.get(i);

        TextView tvName = convertView.findViewById(R.id.name_cart);
        TextView tvSize = convertView.findViewById(R.id.size_cart);
        TextView tvPrice = convertView.findViewById(R.id.price_cart);
        TextView quantity = convertView.findViewById(R.id.quantity);
        TextView priceOneItem = convertView.findViewById (R.id.priceFish_one_item);
        ImageView image = convertView.findViewById (R.id.imageFish_shop_bask);

        quantity.setText((String) getPosition.get("quantity"));
        tvName.setText((String) getPosition.get("name"));
        tvSize.setText(getPosition.get("size") + " см.");
        tvPrice.setText(CartHelper.itemSum(getPosition) + " грн.");
        priceOneItem.setText (getPosition.get ("price") + " грн." + " x  ");

        String urlImage = (String) getPosition.get ("image");
        support.loadImage(image, urlImage, context);

        convertView.setTag(i);

        return convertView;
    }
}
