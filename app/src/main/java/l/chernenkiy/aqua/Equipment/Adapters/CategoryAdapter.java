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
import java.util.List;

import l.chernenkiy.aqua.Equipment.Items.ItemCategory;
import l.chernenkiy.aqua.Equipment.Items.ItemSubCategory;
import l.chernenkiy.aqua.R;

public class CategoryAdapter extends BaseAdapter {

    private Context mContext;
    private List<ItemCategory> mItemCategory;



    public CategoryAdapter(Context mContext, List<ItemCategory> mItemCategory) {
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

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {

        LayoutInflater mInflater = (LayoutInflater) mContext
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.cart_equipment, null);

        ImageView ivRightArrow = convertView.findViewById(R.id.imageRightArrow);
        ivRightArrow.setImageResource(R.drawable.right_arrow_equipment_list);

        TextView tvCategory = convertView.findViewById(R.id.name_category);
        TextView tvNumbEquip = convertView.findViewById(R.id.numb_category);

        tvCategory.setText(mItemCategory.get(i).getName());

        ItemCategory item = mItemCategory.get(i);

        int size = item.getItemSubCategories().isEmpty()
            ? item.getItems().size()
            : item.getSubCategoryItems().size();

        tvNumbEquip.setText(size + " позиций");

        return convertView;
    }
}
