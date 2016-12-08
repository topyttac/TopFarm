package th.or.nectec.zoning.topfarm.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.afollestad.materialdialogs.MaterialDialog;

import th.or.nectec.zoning.topfarm.R;
import th.or.nectec.zoning.topfarm.fragment.MainFragment;

public class MainActivity extends AppCompatActivity implements MainFragment.MainFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.contentContainer, MainFragment.newInstance())
                    .add(R.id.fragment_main, new MainFragment())
                    .commit();
        }

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void showCircular() {
        finish();
        Intent i = new Intent(MainActivity.this, CircularActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    public void showTimeline() {
        finish();
        Intent i = new Intent(MainActivity.this, TimelineActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_plant:
                MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_main);
                mainFragment.selectPlant();
                return true;
            case R.id.menu_tutorial:
                Intent i = new Intent(MainActivity.this, TutorialActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            case R.id.menu_credit:
                new MaterialDialog.Builder(this)
                        .typeface("Mitr-Regular.ttf", "Mitr-Regular.ttf")
                        .title("ขอขอบคุณ")
                        .content(Html.fromHtml("\t \t ร้านชัยเจริญ (เมล็ดพันธุ์ข้าว เฮียใช้)" + "<br>"
                                + "\t \t \t http://www.flaticon.com"))
                        .positiveText("ตกลง")
                        .show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
