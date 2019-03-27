package com.yondevstudio.colortapfun;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.yondevstudio.colortapfun.widget.FlowLayout;
import com.yondevstudio.colortapfun.utils.Constant;
import com.yondevstudio.colortapfun.utils.Shared;

import org.w3c.dom.Text;

import java.security.SecureRandom;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener{
    private int currentbest = 0;
    private int currentScore = 0;
    private int currentblock = Constant.BLOCK_2;
    private float currentlight = Constant.LIGHT_EASY;

    private TextView txtBest;
    private TextView txtScore;

    private ProgressBar viewTimer;
    private CountDownTimer timer;
    private FlowLayout flow;
    private MediaPlayer mMediaPlayer;
    private MediaPlayer mMediaPlayerFail;

    private boolean isFirstimeTouch = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mMediaPlayerFail = MediaPlayer.create(getApplicationContext(), R.raw.fail);

        TextView t5 = (TextView)findViewById(R.id.textView5);
        TextView t6 = (TextView)findViewById(R.id.textView6);

        txtBest = (TextView)findViewById(R.id.txtbest);
        txtScore = (TextView)findViewById(R.id.txtscore);

        viewTimer = (ProgressBar)findViewById(R.id.timer);
        flow = (FlowLayout)findViewById(R.id.flowLayout);

        t5.setTypeface(Shared.appfont);
        t6.setTypeface(Shared.appfont);

        txtBest.setTypeface(Shared.appfontBold);
        txtScore.setTypeface(Shared.appfontBold);

        txtScore.setText(String.valueOf(currentScore));

        currentbest = Integer.valueOf(Shared.read(Constant.HIGHSCORE, "0"));
        txtBest.setText(String.valueOf(currentbest));

        flow.post(new Runnable() {
            @Override
            public void run() {
                int width = flow.getWidth();
                ViewGroup.LayoutParams params = flow.getLayoutParams();
                params.width = width;
                params.height = params.width;
                flow.setLayoutParams(params);
                gameStart();
            }
        });

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        viewTimer.setProgress(100);

    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void gameOver()
    {
        mMediaPlayerFail.start();
        Intent intent = new Intent(GameActivity.this,GameOverActivity.class);
        intent.putExtra(Constant.CURRENT_SCORE,currentScore);
        intent.putExtra(Constant.CURRENT_BEST,currentbest);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    private int counter = 100;
    private void gameStart()
    {
        flow.removeAllViews();
        if(timer != null)
            timer.cancel();

        if(currentScore >= 3 && currentScore < 6)
        {
            currentblock = Constant.BLOCK_3;
        }
        else if(currentScore >= 6 && currentScore < 9)
        {
            currentblock = Constant.BLOCK_4;
        }
        else if(currentScore >= 9 && currentScore <  12)
        {
            currentblock = Constant.BLOCK_5;
            currentlight = Constant.LIGHT_MEDIUM;
        }
        else if(currentScore >= 12 && currentScore <  15)
        {
            currentblock = Constant.BLOCK_6;
        }
        else if(currentScore >= 15 && currentScore <  18)
        {
            currentblock = Constant.BLOCK_7;
            currentlight = Constant.LIGHT_HARD;
        }
        else if(currentScore >= 18 )
        {
            currentblock = Constant.BLOCK_8;
        }

        Random rand = new Random();
        int n = rand.nextInt(Constant.colors.length);
        int nl = rand.nextInt(currentblock*currentblock);
        int color = Color.parseColor(Constant.colors[n]);
        int blockWidth = (flow.getWidth() - (currentblock*2)) / currentblock ;

        int Clcounter = 0;
        for(int i = 0; i < currentblock;i++)
        {
            for(int j = 0; j < currentblock;j++)
            {
                FlowLayout.LayoutParam params = new FlowLayout.LayoutParam(blockWidth,blockWidth);
                View v = new View(this);
                v.setBackgroundResource(R.drawable.border_view);
                GradientDrawable drawable = (GradientDrawable) v.getBackground();

                v.setTag(Clcounter == nl);
                if(Clcounter == nl)
                {
                    drawable.setColor(lighter(color, currentlight));
                }
                else
                    drawable.setColor(color);

                params.setMargins(1,1,1,1);
                v.setLayoutParams(params);
                flow.addView(v);

                v.setOnClickListener(this);

                Clcounter++;
            }
        }

        final int count = 3000;
        final int tick = 30;
        viewTimer.setProgress(100);
        counter = 100;
        timer = new CountDownTimer(count,tick) {
            @Override
            public void onTick(long millisUntilFinished) {
                counter--;
                viewTimer.setProgress(counter);
            }
            @Override
            public void onFinish() {
                gameOver();
                viewTimer.setProgress(0);
            }
        };

        if(!isFirstimeTouch)
        {
            timer.start();
        }
        else
            isFirstimeTouch = false;
    }


    public static int lighter(int color, float factor) {
        int red = (int) ((Color.red(color) * (1 - factor) / 255 + factor) * 255);
        int green = (int) ((Color.green(color) * (1 - factor) / 255 + factor) * 255);
        int blue = (int) ((Color.blue(color) * (1 - factor) / 255 + factor) * 255);
        return Color.argb(Color.alpha(color), red, green, blue);
    }

    @Override
    public void onClick(View v) {

        if(v.getTag() != null)
        {
            if((boolean)v.getTag() == false)
            {
                if(timer != null)
                    timer.cancel();

                gameOver();
            }
            else
            {
                if(mMediaPlayer != null)
                {
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                    mMediaPlayer = null;
                }

                mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.btn_sound);
                mMediaPlayer.start();
                currentScore++;
                txtScore.setText(String.valueOf(currentScore));
                if(currentScore >= currentbest)
                {
                    currentbest = currentScore;
                    txtBest.setText(String.valueOf(currentScore));
                }
                gameStart();
            }
        }
    }
}
