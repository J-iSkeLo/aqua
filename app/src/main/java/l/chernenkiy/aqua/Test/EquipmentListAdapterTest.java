package l.chernenkiy.aqua.Test;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import l.chernenkiy.aqua.Equipment.Items.ItemEquipment;
import l.chernenkiy.aqua.R;

public class EquipmentListAdapterTest extends BaseAdapter {

    private Context mContext;
    private ArrayList<ItemEquipmentTest> mItemEquipmentList;

    public EquipmentListAdapterTest(Context mContext, ArrayList<ItemEquipmentTest> mItemEquipmentList) {
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

           ItemEquipmentTest itemEquipmentTest = mItemEquipmentList.get(i);

           TextView tvArticle = convertView.findViewById(R.id.vendor_code);
           TextView tvName = convertView.findViewById(R.id.name_equip);
           TextView tvProducer = convertView.findViewById(R.id.txt_manufacturer);
           TextView tvDescription = convertView.findViewById(R.id.txt_description);
           TextView tvPrice = convertView.findViewById(R.id.price_equip);

           tvName.setText(itemEquipmentTest.getName());

           if (itemEquipmentTest.getArticle ().equals("null")) {
                itemEquipmentTest.setArticle (" - ");
                tvArticle.setText(itemEquipmentTest.getArticle());
           } else {
                tvArticle.setText("Артикул: " + itemEquipmentTest.getArticle());
           }

           if (itemEquipmentTest.getGeneralColKey ().equals("null")) {
                itemEquipmentTest.setGeneralColKey (" - ");
                tvProducer.setText( itemEquipmentTest.getGeneralColKey());
           } else {
                tvProducer.setText(itemEquipmentTest.getGeneralColKey ());
           }

           if (itemEquipmentTest.getDescription().equals("null")) {
                    itemEquipmentTest.setDescription ("Без описания");
                    tvDescription.setText( itemEquipmentTest.getDescription());
           } else {
                tvDescription.setText(itemEquipmentTest.getDescription());
           }

           if (itemEquipmentTest.getPrice().equals("null")) {
                itemEquipmentTest.setPrice ("0");
                tvPrice.setText(itemEquipmentTest.getPrice() + " грн.");
           } else {
                tvPrice.setText(itemEquipmentTest.getPrice() + " грн.");
           }

       return convertView;

    }

}
