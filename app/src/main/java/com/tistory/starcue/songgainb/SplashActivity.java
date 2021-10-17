package com.tistory.starcue.songgainb;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.agrawalsuneet.dotsloader.loaders.TrailingCircularDotsLoader;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class SplashActivity extends AppCompatActivity {

    InterstitialAd interstitialAd;
    TrailingCircularDotsLoader trailingCircularDotsLoaders;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
//        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//        startActivity(intent);
//        finish();

        trailingCircularDotsLoaders = (TrailingCircularDotsLoader) findViewById(R.id.aniview);

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-4304457330328945/4891853237");
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("7AF578A575ED8BAF31AD452E5DDA9609")
                .addTestDevice("7FFE3DAE12F890AAC183AC7B92E9AFAC")
                .addTestDevice("D6B81045BE65B144F3717CAD87F0E43E")
                .addTestDevice("587B0E954BC444EFCEBDEBFE6C587952").build();
        interstitialAd.loadAd(adRequest);

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        });

//        if (interstitialAd.isLoaded()) {
//            interstitialAd.show();
//        } else {
//            Log.d("interstitialAd is = ", "fail");
//        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
            }
        }, 5000);






//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        super.onBackPressed();
    }
}
