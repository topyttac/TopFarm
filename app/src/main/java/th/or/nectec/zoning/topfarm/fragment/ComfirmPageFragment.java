package th.or.nectec.zoning.topfarm.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;

import th.or.nectec.zoning.topfarm.R;

/**
 * Created by nuuneoi on 11/16/2014.
 */
@SuppressWarnings("unused")
public class ComfirmPageFragment extends Fragment {

    public interface ComfirmPageFragmentListener{
        void onResumeResultFragment();
    }

    private TextView tv_start;

    public ComfirmPageFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static ComfirmPageFragment newInstance() {
        ComfirmPageFragment fragment = new ComfirmPageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page_confirm, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        Typeface mitr_light = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Mitr-Light.ttf");
        Typeface mitr_regular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Mitr-Regular.ttf");

        tv_start = (TextView) rootView.findViewById(R.id.tv_start);
        tv_start.setTypeface(mitr_regular);
        tv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
                BounceInterpolator interpolator = new BounceInterpolator();
                anim.setInterpolator(interpolator);
                tv_start.startAnimation(anim);

                ComfirmPageFragmentListener comfirmPageFragmentListener = (ComfirmPageFragmentListener) getActivity();
                comfirmPageFragmentListener.onResumeResultFragment();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /*
     * Save Instance State Here
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
    }

    /*
     * Restore Instance State Here
     */
    @SuppressWarnings("UnusedParameters")
    private void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore Instance State here
    }

}
