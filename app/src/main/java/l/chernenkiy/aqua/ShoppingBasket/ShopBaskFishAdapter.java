package l.chernenkiy.aqua.ShoppingBasket;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.renderscript.Sampler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import l.chernenkiy.aqua.Helpers.CartHelper;
import l.chernenkiy.aqua.Helpers.ConnectionDetector;
import l.chernenkiy.aqua.R;

public class ShopBaskFishAdapter extends BaseAdapter {

    public static ArrayList<HashMap> cartItems;
    private Context context;

    Boolean isInternetPresent = false;
    ConnectionDetector cd;


    public ShopBaskFishAdapter(Context context, ArrayList<HashMap> cartItems){
        ShopBaskFishAdapter.cartItems = cartItems;
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

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        cd = new ConnectionDetector (context);
        isInternetPresent = cd.ConnectingToInternet();

        convertView = mInflater.inflate(R.layout.cart, null);

        HashMap getPosition = cartItems.get(i);

        TextView tvName = convertView.findViewById(R.id.name_cart);
        TextView tvSize = convertView.findViewById(R.id.size_cart);
        TextView tvPrice = convertView.findViewById(R.id.price_cart);
        TextView tvNumber = convertView.findViewById(R.id.number_cart);
        TextView quantity = convertView.findViewById(R.id.quantity);
        TextView priceOneItem = convertView.findViewById (R.id.priceFish_one_item);
        ImageView image = convertView.findViewById (R.id.imageFish_shop_bask);

        quantity.setText((String) getPosition.get("quantity"));
        tvName.setText((String) getPosition.get("name"));
        tvSize.setText(getPosition.get("size") + " см.");
        tvPrice.setText(CartHelper.itemSum(getPosition) + " грн.");
        String numberPosition = String.valueOf ((i + 1));

        tvNumber.setText("№ " + numberPosition);
        priceOneItem.setText (getPosition.get ("price") + " грн." + " x  ");

        String urlImage = (String) getPosition.get ("image");
        if(isInternetPresent) {
            loadImage(image, urlImage);
        }
        else{
            showToastInternetPresent("Нет интернет соединения для загрузки изображения!");
        }

        convertView.setTag(i);

        return convertView;
    }


    public void loadImage (ImageView image, String imageURL){

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(10f);
        circularProgressDrawable.setCenterRadius(60f);
        circularProgressDrawable.setColorSchemeColors(Color.rgb (155,155,155));
        circularProgressDrawable.start();



        Glide.with(context)
                .load(imageURL)
                .placeholder(circularProgressDrawable)
                .into(image);
    }

    private void showToastInternetPresent(String msg) {
        Toast toast = Toast.makeText
                (context,msg,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
}
