package l.chernenkiy.aqua.Helpers;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import l.chernenkiy.aqua.Equipment.Aquariums;
import l.chernenkiy.aqua.Equipment.Chemistry;
import l.chernenkiy.aqua.Equipment.EquipmentAccessActivity;
import l.chernenkiy.aqua.Equipment.Feed;
import l.chernenkiy.aqua.Fish.Fish;
import l.chernenkiy.aqua.R;

public class NavigationBar {


    static public void itemSelected(BottomNavigationView navigation, final Context context, final int current) {
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {


            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();


                if (itemId == R.id.fish && R.id.fish != current) {
                    Intent intent = new Intent (context, Fish.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity (intent);
                    return false;
                } else if (itemId == R.id.equipment_accessories_Activity && R.id.equipment_accessories_Activity != current) {
                    Intent intent = new Intent (context, EquipmentAccessActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity (intent);
                    return false;
                } else if (itemId == R.id.feed && R.id.feed != current) {
                    Intent intent = new Intent (context, Feed.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity (intent);
                    return false;
                } else if (itemId == R.id.chemistry2 && R.id.chemistry2 != current) {
                    Intent intent = new Intent (context, Chemistry.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity (intent);
                    return false;
                } else if (itemId == R.id.aquariums && R.id.aquariums != current ) {
                    Intent intent = new Intent (context, Aquariums.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity (intent);
                    return true;
                }

                return false;
            }
        });
    }
}
