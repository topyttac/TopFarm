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
import th.or.nectec.zoning.topfarm.fragment.TimelineFragment;

public class TimelineActivity extends AppCompatActivity implements TimelineFragment.TimelineFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer, TimelineFragment.newInstance())
                    .commit();
        }

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            Intent i = new Intent(TimelineActivity.this, MainActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            return true;
        }
        switch (item.getItemId()) {
            case R.id.menu_tutorial:
                Intent i = new Intent(TimelineActivity.this, TutorialResultActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            case R.id.menu_credit:
                new MaterialDialog.Builder(this)
                        .typeface("Mitr-Regular.ttf", "Mitr-Regular.ttf")
                        .title("ขอขอบคุณ")
                        .content(Html.fromHtml("- ร้านชัยเจริญ (เมล็ดพันธุ์ข้าว เฮียใช้)" + "<br>"
                                + "- http://www.flaticon.com"))
                        .positiveText("ตกลง")
                        .show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void returnHome() {
        finish();
        Intent i = new Intent(TimelineActivity.this, CircularActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}
