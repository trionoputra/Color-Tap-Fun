package com.yondevstudio.colortapfun;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.yondevstudio.colortapfun.utils.Constant;
import com.yondevstudio.colortapfun.utils.Shared;

public class GameOverActivity extends AppCompatActivity implements View.OnClickListener {
    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.btn_sound);
        findViewById(R.id.btnRestart).setOnClickListener(this);

        TextView t4 = (TextView)findViewById(R.id.textView4);
        TextView t5 = (TextView)findViewById(R.id.textView5);
        TextView t7 = (TextView)findViewById(R.id.textView7);
        TextView t8 = (TextView)findViewById(R.id.textView8);
        TextView t9 = (TextView)findViewById(R.id.textView9);
        TextView t10 = (TextView)findViewById(R.id.textView10);

        t4.setTypeface(Shared.appfontLight);
        t5.setTypeface(Shared.appfontLight);

        t7.setTypeface(Shared.appfont);
        t9.setTypeface(Shared.appfont);

        t8.setTypeface(Shared.appfontLight);
        t10.setTypeface(Shared.appfontLight);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            t8.setText(String.valueOf(extras.getInt(Constant.CURRENT_SCORE,0)));

            int currentBest = extras.getInt(Constant.CURRENT_BEST,Integer.valueOf(Shared.read(Constant.HIGHSCORE, "0")));
            Shared.write(Constant.HIGHSCORE, String.valueOf(currentBest));
            t10.setText(String.valueOf(currentBest));
        }

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onClick(View v) {
        mMediaPlayer.start();
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
