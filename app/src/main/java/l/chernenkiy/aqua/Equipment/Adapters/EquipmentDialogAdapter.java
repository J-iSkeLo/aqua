package l.chernenkiy.aqua.Equipment.Adapters;

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
import l.chernenkiy.aqua.Helpers.ConnectionDetector;
import l.chernenkiy.aqua.Helpers.Support;
import l.chernenkiy.aqua.R;

public class EquipmentDialogAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<ItemEquipment> mItemEquipmentList;

    public EquipmentDialogAdapter(Context mContext, ArrayList<ItemEquipment> mItemEquipmentList) {
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
           convertView = mInflater.inflate(R.layout.cart_dialog_all_item, null);

           ItemEquipment itemEquipment = mItemEquipmentList.get(i);

           TextView tvArticle = convertView.findViewById(R.id.vendor_code_dialog);
           TextView tvName = convertView.findViewById(R.id.name_equip_dialog);
           TextView tvProducer = convertView.findViewById(R.id.txt_manufacturer_dialog);
           TextView tvDescription = convertView.findViewById(R.id.txt_description_dialog);
           TextView tvPrice = convertView.findViewById(R.id.price_equip_dialog);

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

       return convertView;
    }
}
