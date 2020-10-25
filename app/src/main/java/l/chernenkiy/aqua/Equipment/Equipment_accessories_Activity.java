package l.chernenkiy.aqua.Equipment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import l.chernenkiy.aqua.R;

public class Equipment_accessories_Activity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equipment_accessories);

        Toolbar toolbar = findViewById(R.id.toolbarEquipAccess);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Intent(Equipment_accessories_Activity.this, EquipmentActivity.class);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        new Intent(Equipment_accessories_Activity.this, EquipmentActivity.class);
        finish();
    }
}
