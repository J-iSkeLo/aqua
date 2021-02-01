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

import l.chernenkiy.aqua.Equipment.Items.ItemSubCategory;
import l.chernenkiy.aqua.R;

public class SubCategoryAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<ItemSubCategory> mItemCategory;


    public SubCategoryAdapter(Context mContext, ArrayList<ItemSubCategory> mItemCategory) {
        this.mContext = mContext;
        this.mItemCategory = mItemCategory;

    }



    @Override
    public int getCount() {
        return mItemCategory.size();
    }

    @Override
    public Object getItem(int i) {
        return mItemCategory.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {

        LayoutInflater mInflater = (LayoutInflater) mContext
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.cart_equipment_test, null);

        ImageView ivRightArrow = convertView.findViewById(R.id.imageRightArrow);
        ivRightArrow.setImageResource(R.drawable.right_arrow_equipment_list);

        TextView tvCategory = convertView.findViewById(R.id.name_category);
        TextView tvNumbEquip = convertView.findViewById(R.id.numb_category);

        ItemSubCategory itemSubCategory = mItemCategory.get(i);

        tvCategory.setText(itemSubCategory.getName());
        tvNumbEquip.setText(itemSubCategory.getItems().size() + " позиций");

        return convertView;
    }
}
