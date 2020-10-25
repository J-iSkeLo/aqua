package l.chernenkiy.aqua.Equipment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import l.chernenkiy.aqua.MainActivity;
import l.chernenkiy.aqua.R;

public class EquipmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment);

        Toolbar toolbar = findViewById(R.id.toolbarEquip);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Intent(EquipmentActivity.this, MainActivity.class);
                finish();
            }
        });

        Button btnEquipAcces = findViewById(R.id.btnEquipAcces);
        btnEquipAcces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent(EquipmentActivity.this, Equipment_accessories_Activity.class);
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        new Intent(EquipmentActivity.this, MainActivity.class);
        finish();
    }
}
