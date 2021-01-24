package l.chernenkiy.aqua.ShoppingBasket;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
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
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import l.chernenkiy.aqua.Helpers.CartHelper;
import l.chernenkiy.aqua.Helpers.ConnectionDetector;
import l.chernenkiy.aqua.R;


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

        ConnectionDetector cd = new ConnectionDetector (context);
        boolean isInternetPresent = cd.ConnectingToInternet ( );

        convertView = mInflater.inflate(R.layout.basket_cart_equip, null);

        HashMap cartGetPosition = cartEquipmentItem.get(i);

        String finalSum = bigDecimal(cartGetPosition);

        TextView tvName = convertView.findViewById(R.id.name_equip);
        TextView tvPrice = convertView.findViewById(R.id.price_equip);
        TextView tvPriceOneItem = convertView.findViewById(R.id.price_one_item);
        TextView tvDescription = convertView.findViewById(R.id.txt_description);
        TextView tvProducer = convertView.findViewById(R.id.txt_manufacturer);
        TextView tvArticle = convertView.findViewById(R.id.vendor_code);
        TextView quantity = convertView.findViewById(R.id.quantity_equip);
        ImageView image = convertView.findViewById (R.id.image_shop_bask);

        quantity.setText((String) cartGetPosition.get("quantity"));
        tvPriceOneItem.setText (cartGetPosition.get ("price") + " грн." + " x  ");
        tvName.setText((String) cartGetPosition.get("name"));
        tvDescription.setText((String) cartGetPosition.get("description"));
        tvProducer.setText((String) cartGetPosition.get("producer"));
        tvPrice.setText(finalSum + " грн.");
        tvArticle.setText("Арт: " + cartGetPosition.get("article"));

        String urlImage = (String) cartGetPosition.get ("image");
        if(isInternetPresent) {
            loadImage(image, urlImage);
        }
        else{
            showToastInternetPresent("Нет интернет соединения для загрузки изображения!");
        }


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
