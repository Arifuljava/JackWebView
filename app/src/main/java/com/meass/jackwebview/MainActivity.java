package com.meass.jackwebview;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.multidex.MultiDex;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.geniusforapp.fancydialog.FancyAlertDialog;

public class MainActivity extends AppCompatActivity {

    private ImageView appLogo, pattern1, pattern2;
    private TextView appSlogan, poweredBy, developerDepository;

    //Other Variables
    private Animation topAnimation, bottomAnimation, startAnimation, endAnimation;
    private SharedPreferences onBoardingPreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        MultiDex.install(this);
        // new CheckInternetConnection(this).checkConnection();
        Long tsLong = System.currentTimeMillis() / 1000;


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getColor(R.color.colorBackground));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        initViews();
        initAnimation();
    }

    private void initViews() {
        //Initialize Views
        appLogo = findViewById(R.id.app_logo);
        pattern1 = findViewById(R.id.pattern1);
        pattern2 = findViewById(R.id.pattern2);
        appSlogan = findViewById(R.id.app_slogan);
        poweredBy = findViewById(R.id.powered_by);
        developerDepository = findViewById(R.id.developer_depository);
    }

    private void initAnimation() {
        //Initialize Animations
        topAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.splash_top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.splash_bottom_animation);
        startAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.splash_start_animation);
        endAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.splash_end_animation);
    }

    @Override
    protected void onStart() {
        super.onStart();
        int SPLASH_TIMER = 3000;

        //Set Preferences
        onBoardingPreference = getSharedPreferences("onBoardingPreference", MODE_PRIVATE);
        final boolean isFirstTime = onBoardingPreference.getBoolean("firstTime", true);

        //Set Animation To Views
        appLogo.setAnimation(topAnimation);
        pattern1.setAnimation(startAnimation);
        pattern2.setAnimation(endAnimation);
        appSlogan.setAnimation(bottomAnimation);
        poweredBy.setAnimation(bottomAnimation);
        developerDepository.setAnimation(bottomAnimation);

        new Handler().postDelayed(() -> {
            if (InternetConnection.checkConnection(MainActivity.this)) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));

            } else {
                AlertDialog.Builder bb=new AlertDialog.Builder(MainActivity.this);
                bb.setTitle("Warning")
                        .setMessage("Kindly connect to a WIFI network or enable Mobile Data")
                        .setCancelable(false)
                        .setPositiveButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setNegativeButton("Connect Now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent dialogIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(dialogIntent);
                    }
                }).create();
                bb.show();

            }



        }, SPLASH_TIMER);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
    private boolean isInternetConnected() {
        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isConnectedOrConnecting();

    }
}