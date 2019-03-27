package com.yondevstudio.colortapfun;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.yondevstudio.colortapfun.utils.Shared;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private InterstitialAd interstitialAd;
    private boolean isFirstime = true;
    private MediaPlayer mMediaPlayer;
    private AdRequest adRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Shared.initialize(getBaseContext());
        mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.btn_sound);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnPlay).setOnClickListener(this);

        TextView t1 = (TextView)findViewById(R.id.textView);
        TextView t2 = (TextView)findViewById(R.id.textView2);
        TextView t3 = (TextView)findViewById(R.id.textView3);

        t1.setTypeface(Shared.appfontLight);
        t2.setTypeface(Shared.appfontLight);
        t3.setTypeface(Shared.appfontLight);

        subscribeToPushService();

        AdView mAdView = findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    @Override
    public void onClick(View v) {
        mMediaPlayer.start();
        Intent intent = null;
        switch (v.getId())
        {
            case R.id.btnPlay:
                intent = new Intent(MainActivity.this,GameActivity.class);
                break;
        }

        if(intent != null)
        {
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    private void subscribeToPushService() {
        FirebaseMessaging.getInstance().subscribeToTopic("update");
        String token = FirebaseInstanceId.getInstance().getToken();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadInterstitialAd();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        displayInterstitial();
    }

    public void displayInterstitial() {
        // If Ads are loaded, show Interstitial else show nothing.
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        }
    }

    private void loadInterstitialAd() {

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_id));
        interstitialAd.loadAd(adRequest);
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                if(isFirstime)
                {
                    displayInterstitial();
                    isFirstime = false;
                }
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
            }
        });
    }

}
