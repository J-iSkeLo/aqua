package l.chernenkiy.aqua.Fish;

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
import java.util.Locale;

import l.chernenkiy.aqua.Helpers.ConnectionDetector;
import l.chernenkiy.aqua.Helpers.Support;
import l.chernenkiy.aqua.R;

import static l.chernenkiy.aqua.MainActivity.listFish;

public class FishListAdapter extends BaseAdapter {

    private final Context mContext;
    private final ArrayList<Product> mProductList;
    Support support = new Support();

    private final ArrayList<Product> filteredData = new ArrayList<>();

    public FishListAdapter(Context mContext, ArrayList<Product> mProductList) {
        this.mContext = mContext;
        this.mProductList = mProductList;
        this.filteredData.clear();
        this.filteredData.addAll(listFish);
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

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        LayoutInflater mInflater = (LayoutInflater) mContext
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        ConnectionDetector cd = new ConnectionDetector (mContext);
        boolean isInternetPresent = cd.ConnectingToInternet ( );
        Product product = mProductList.get(i);

        if (hasNotTitle(mProductList, i)) {
            convertView = mInflater.inflate(R.layout.cart_fish_price_list, null);

            TextView tvName = convertView.findViewById(R.id.name_pn);
            TextView tvSize = convertView.findViewById(R.id.size_pn);
            TextView tvPrice = convertView.findViewById(R.id.price_pn);
            ImageView image = convertView.findViewById(R.id.imageFish_pn);

            tvName.setText(listFish.get(i).getName());
            tvSize.setText(product.getSize() + " см.");
            tvPrice.setText(product.getPrice() + " грн.");

            String urlImage = product.getImage();
            if(isInternetPresent) {
                support.loadImage(image, urlImage, mContext);
            }
            else{
                support.showToast(mContext,"Нет интернет соединения для загрузки изображения!");
            }

        } else {
            convertView = mInflater.inflate(R.layout.cart_fish_title_category, null);

            TextView tvTitle = convertView.findViewById(R.id.title_pn);
            tvTitle.setText(product.getTitle());
        }


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
                if (product.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mProductList.add(product);
                }
            }
        }
        notifyDataSetChanged();
    }
}
