package l.chernenkiy.aqua.Helpers;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;

public class Support {

    public void showToast(Context context, String msg) {
        Toast toast = Toast.makeText
                (context,msg,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    public void loadImage (ImageView image, String imageURL, Context context){

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
}
