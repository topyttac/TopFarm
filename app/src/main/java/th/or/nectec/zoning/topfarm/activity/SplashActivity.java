package th.or.nectec.zoning.topfarm.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import th.or.nectec.zoning.topfarm.R;

public class SplashActivity extends AppCompatActivity {

    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Boolean isFirstTime = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstTime", true);

        if (isFirstTime) {
            int secondsDelayed = 3;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    startActivity(new Intent(SplashActivity.this, TutorialActivity.class));
                    finish();
                }
            }, secondsDelayed * 1000);
            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                    .putBoolean("isFirstTime", false).commit();
        } else{
            int secondsDelayed = 3;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }, secondsDelayed * 1000);
        }

        Typeface gloria = Typeface.createFromAsset(SplashActivity.this.getAssets(), "fonts/GloriaHallelujah.ttf");

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setTypeface(gloria);
    }
}
