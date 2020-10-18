package com.app.sammy;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();
        LogoLauncher logoLauncher = new LogoLauncher();
        logoLauncher.start();

    }

    private class LogoLauncher extends Thread{
        public void run(){
            try{
                sleep(1500);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
            Intent i = new Intent( SplashScreenActivity.this, MainActivity.class);
            startActivity(i);
            SplashScreenActivity.this.finish();
        }

    }
}
