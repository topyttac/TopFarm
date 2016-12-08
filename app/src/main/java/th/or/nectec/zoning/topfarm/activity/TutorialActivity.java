package th.or.nectec.zoning.topfarm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import th.or.nectec.zoning.topfarm.R;
import th.or.nectec.zoning.topfarm.fragment.LastPageFragment;
import th.or.nectec.zoning.topfarm.fragment.TutorialFragment;

public class TutorialActivity extends AppCompatActivity implements LastPageFragment.PageFiveListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer, TutorialFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public void onStartMainActivity() {
        Intent i = new Intent(TutorialActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
