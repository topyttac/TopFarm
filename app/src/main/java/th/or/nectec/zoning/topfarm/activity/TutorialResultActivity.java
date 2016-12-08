package th.or.nectec.zoning.topfarm.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import th.or.nectec.zoning.topfarm.R;
import th.or.nectec.zoning.topfarm.fragment.ComfirmPageFragment;
import th.or.nectec.zoning.topfarm.fragment.TutorialResultFragment;

public class TutorialResultActivity extends AppCompatActivity implements ComfirmPageFragment.ComfirmPageFragmentListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turorial_result);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer, TutorialResultFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public void onResumeResultFragment() {
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
