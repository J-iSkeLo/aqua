package l.chernenkiy.aqua.Equipment.Adapters;

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

import l.chernenkiy.aqua.Equipment.Items.ItemEquipment;
import l.chernenkiy.aqua.Helpers.Support;
import l.chernenkiy.aqua.R;

public class SearchListAdapter extends BaseAdapter {

    private final Context mContext;
    private final ArrayList<ItemEquipment> mItemEquipmentList;
    Support support = new Support();

    public SearchListAdapter(Context mContext, ArrayList<ItemEquipment> mItemEquipmentList) {
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

    @SuppressLint({"ViewHolder", "SetTextI18n", "InflateParams"})
    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {

       LayoutInflater mInflater = (LayoutInflater) mContext
               .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
           convertView = mInflater.inflate(R.layout.cart_accessories, null);

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
           }
           tvProducer.setText( itemEquipment.getGeneralColKey());

           if (itemEquipment.getDescription().equals("null")) {
                        itemEquipment.setDescription ("Без описания");
           }
           tvDescription.setText( itemEquipment.getDescription());

           if (itemEquipment.getPrice().equals("null")) {
                    itemEquipment.setPrice ("0");
           }
           tvPrice.setText(itemEquipment.getPrice() + " грн.");

           String urlImage = itemEquipment.getImage();
           support.loadImage(image, urlImage, mContext);

       return convertView;
    }
}
