package com.opm.saitama.bms;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v4.app.FragmentActivity;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class SplashActivity extends FragmentActivity {

    MediaPlayer splashSong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        LinearLayout ll = (LinearLayout)findViewById(R.id.layout_splash);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        splashSong = MediaPlayer.create(SplashActivity.this,R.raw.faded);
        splashSong.start();

        Thread timer = new Thread(){
            public void run(){
                try
                {
                    sleep(10000);
                }
                catch (Exception e )
                {
                    e.printStackTrace();
                }
                finally
                {
                    Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            }
        };
        timer.start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        splashSong.release();
        finish();
    }
}
