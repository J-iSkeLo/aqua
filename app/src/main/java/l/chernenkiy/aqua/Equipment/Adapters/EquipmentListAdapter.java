package l.chernenkiy.aqua.Equipment.Adapters;

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

import java.util.ArrayList;

import l.chernenkiy.aqua.Equipment.Items.ItemEquipment;
import l.chernenkiy.aqua.Helpers.ConnectionDetector;
import l.chernenkiy.aqua.Helpers.Support;
import l.chernenkiy.aqua.R;

public class EquipmentListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<ItemEquipment> mItemEquipmentList;
    Support support = new Support();

    public EquipmentListAdapter(Context mContext, ArrayList<ItemEquipment> mItemEquipmentList) {
        this.mContext = mContext;
        this.mItemEquipmentList = mItemEquipmentList;

    }



    @Override
    public int getCount() {
        return mItemEquipmentList.size();
    }

    @Override
    public Object getItem(int i) {
        return mItemEquipmentList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {

       LayoutInflater mInflater = (LayoutInflater) mContext
               .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
           convertView = mInflater.inflate(R.layout.cart_accessories, null);

            ConnectionDetector cd = new ConnectionDetector (mContext);
            boolean isInternetPresent = cd.ConnectingToInternet ( );

           ItemEquipment itemEquipment = mItemEquipmentList.get(i);

           TextView tvArticle = convertView.findViewById(R.id.vendor_code);
           TextView tvName = convertView.findViewById(R.id.name_equip);
           TextView tvProducer = convertView.findViewById(R.id.txt_manufacturer);
           TextView tvDescription = convertView.findViewById(R.id.txt_description);
           TextView tvPrice = convertView.findViewById(R.id.price_equip);
           ImageView image = convertView.findViewById(R.id.image_equip);

           tvName.setText(itemEquipment.getName());

           if (itemEquipment.getArticle ().equals("null")) {
                itemEquipment.setArticle (" - ");
                tvArticle.setText(itemEquipment.getArticle());
           } else {
                tvArticle.setText("Артикул: " + itemEquipment.getArticle());
           }

           if (itemEquipment.getGeneralColKey ().equals("null")) {
                itemEquipment.setGeneralColKey (" - ");
                tvProducer.setText( itemEquipment.getGeneralColKey());
           } else {
                tvProducer.setText(itemEquipment.getGeneralColKey ());
           }

           if (itemEquipment.getDescription().equals("null")) {
                    itemEquipment.setDescription ("Без описания");
                    tvDescription.setText( itemEquipment.getDescription());
           } else {
                tvDescription.setText(itemEquipment.getDescription());
           }

           if (itemEquipment.getPrice().equals("null")) {
                itemEquipment.setPrice ("0");
                tvPrice.setText(itemEquipment.getPrice() + " грн.");
           } else {
                tvPrice.setText(itemEquipment.getPrice() + " грн.");
           }

        String urlImage = itemEquipment.getImage();
        if(isInternetPresent) {
            support.loadImage(image, urlImage, mContext);
        }
        else{
            support.showToast(mContext,"Нет интернет соединения для загрузки изображения!");
        }

       return convertView;
    }
}
