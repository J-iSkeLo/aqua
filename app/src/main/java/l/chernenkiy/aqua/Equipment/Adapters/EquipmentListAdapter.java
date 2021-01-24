package l.chernenkiy.aqua.Equipment.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import l.chernenkiy.aqua.Equipment.Items.ItemEquipment;
import l.chernenkiy.aqua.R;

public class EquipmentListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<ItemEquipment> mItemEquipmentList;
    private ArrayList<ItemEquipment> filteredData;

    public EquipmentListAdapter(Context mContext, ArrayList<ItemEquipment> mItemEquipmentList) {
        this.mContext = mContext;
        this.mItemEquipmentList = mItemEquipmentList;
        this.filteredData = new ArrayList<>();
        this.filteredData.addAll(mItemEquipmentList);
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

           ItemEquipment itemEquipment = mItemEquipmentList.get(i);

           TextView tvArticle = convertView.findViewById(R.id.vendor_code);
           TextView tvName = convertView.findViewById(R.id.name_equip);
           TextView tvProducer = convertView.findViewById(R.id.txt_manufacturer);
           TextView tvDescription = convertView.findViewById(R.id.txt_description);
           TextView tvPrice = convertView.findViewById(R.id.price_equip);

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


    public void myFilter (String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mItemEquipmentList.clear();
        if (charText.length() == 0) {
            mItemEquipmentList.addAll(filteredData);
        } else {
            for (ItemEquipment itemEquipment : filteredData) {
                if (itemEquipment.getName() != null && itemEquipment.getName ().contains(charText)) {
                    mItemEquipmentList.add(itemEquipment);
                }
            }
        }
        notifyDataSetChanged();
    }


}
