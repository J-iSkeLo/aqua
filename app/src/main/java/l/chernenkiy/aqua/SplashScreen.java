package l.chernenkiy.aqua;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.splash_screen);

        getWindow ().setStatusBarColor (getResources ().getColor (R.color.statBarColor));

        new Handler().postDelayed (() -> {
            Intent intent = new Intent (SplashScreen.this, MainActivity.class);
            startActivity (intent);
            finish ();
        }, 2000);

    }
}
