package l.chernenkiy.aqua;

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

public class ProductListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Product> mProductList;
    private ArrayList<Product> filteredData;

    public ProductListAdapter(Context mContext, List<Product> mProductList) {
        this.mContext = mContext;
        this.mProductList = mProductList;
        this.filteredData = new ArrayList<Product>();
        this.filteredData.addAll(mProductList);
    }

    @Override
    public int getCount() {
        return mProductList.size();
    }

    @Override
    public Object getItem(int i) {
        return mProductList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        LayoutInflater mInflater = (LayoutInflater) mContext
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (hasNotTitle(mProductList, i)) {
            convertView = mInflater.inflate(R.layout.fish_price_list, null);

            TextView tvNumber = convertView.findViewById(R.id.number_pn);
            TextView tvName = convertView.findViewById(R.id.name_pn);
            TextView tvSize = convertView.findViewById(R.id.size_pn);
            TextView tvPrice = convertView.findViewById(R.id.price_pn);

            tvNumber.setText("№" + mProductList.get(i).getNumber());
            tvName.setText(mProductList.get(i).getName());
            tvSize.setText(mProductList.get(i).getSize() + " см.");
            tvPrice.setText(mProductList.get(i).getPrice() + " грн.");
        } else {
            convertView = mInflater.inflate(R.layout.title_list, null);

            TextView tvTitle = convertView.findViewById(R.id.title_pn);
            tvTitle.setText(mProductList.get(i).getTitle());
        }

        convertView.setTag(mProductList.get(i).getNumber());

        return convertView;
    }

    public boolean hasNotTitle(List<Product> mProductList, int i) {
        return mProductList.get(i).getTitle().isEmpty();
    }

    public void myFilter (String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mProductList.clear();
        if (charText.length() == 0) {
            mProductList.addAll(filteredData);
        } else {
            for (Product product : filteredData) {
                if (product.getName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    mProductList.add(product);
                }
            }
        }
        notifyDataSetChanged();
    }
}
