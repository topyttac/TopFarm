package th.or.nectec.zoning.topfarm.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crystal.crystalpreloaders.widgets.CrystalPreloader;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import th.or.nectec.zoning.topfarm.R;
import th.or.nectec.zoning.topfarm.database.DatabaseHelper;
import th.or.nectec.zoning.topfarm.datatype.Event;
import th.or.nectec.zoning.topfarm.manager.OnSwipeTouchListener;
import th.or.nectec.zoning.topfarm.manager.SelectedOption;

/**
 * Created by nuuneoi on 11/16/2014.
 */
@SuppressWarnings("unused")
public class TimelineFragment extends Fragment {

    public interface TimelineFragmentListener{
        void returnHome();
    }
    /************************
     * VARIABLE
     ***********************/
    private DatabaseHelper databaseHelper;
    private SelectedOption selectedOption;
    private TextView tv_selected_type, tv_selected_solution, tv_result_start, tv_result_end;
    private String[] EventName, EventDesc;
    private CircleImageView circle_image_1, circle_image_2, circle_image_3, circle_image_4;
    private CircleImageView profile_image, profile_image_weather, profile_image_plant;
    private View line_1, line_2, line_3, line_4, line_5;
    private LinearLayout ll_up_field, ll_content_timeline;
    private CrystalPreloader timeline_progress_bar;
    private RelativeLayout fragment_timeline;
    private Animation slide_in, slide_out;
    private int check = 0;

    public TimelineFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static TimelineFragment newInstance() {
        TimelineFragment fragment = new TimelineFragment();
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

        databaseHelper = new DatabaseHelper(getContext());
        try {
            databaseHelper.createDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_timeline, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
        selectedOption = SelectedOption.getInstance();
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        ll_up_field = (LinearLayout) rootView.findViewById(R.id.ll_up_field);
        ll_content_timeline = (LinearLayout) rootView.findViewById(R.id.ll_content_timeline);
        timeline_progress_bar = (CrystalPreloader) rootView.findViewById(R.id.timeline_progress_bar);
        tv_selected_type = (TextView) rootView.findViewById(R.id.tv_selected_type);
        tv_selected_solution = (TextView) rootView.findViewById(R.id.tv_selected_solution);
        tv_result_start = (TextView) rootView.findViewById(R.id.tv_result_start);
        tv_result_end = (TextView) rootView.findViewById(R.id.tv_result_end);
        circle_image_1 = (CircleImageView) rootView.findViewById(R.id.circle_image_1);
        circle_image_2 = (CircleImageView) rootView.findViewById(R.id.circle_image_2);
        circle_image_3 = (CircleImageView) rootView.findViewById(R.id.circle_image_3);
        circle_image_4 = (CircleImageView) rootView.findViewById(R.id.circle_image_4);
        line_1 = (View) rootView.findViewById(R.id.line_1);
        line_2 = (View) rootView.findViewById(R.id.line_2);
        line_3 = (View) rootView.findViewById(R.id.line_3);
        line_4 = (View) rootView.findViewById(R.id.line_4);
        line_5 = (View) rootView.findViewById(R.id.line_5);
        fragment_timeline = (RelativeLayout) rootView.findViewById(R.id.fragment_timeline);
        profile_image = (CircleImageView) rootView.findViewById(R.id.profile_image);
        profile_image_weather = (CircleImageView) rootView.findViewById(R.id.profile_image_weather);
        profile_image_plant = (CircleImageView) rootView.findViewById(R.id.profile_image_plant);

        slide_in = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_right_season);
        slide_out = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_right_season);

        ll_content_timeline.setVisibility(View.INVISIBLE);

        Typeface mitr_light = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Mitr-Light.ttf");
        Typeface mitr_regular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Mitr-Regular.ttf");
        tv_selected_type.setTypeface(mitr_regular);
        tv_selected_solution.setTypeface(mitr_regular);
        tv_result_start.setTypeface(mitr_light);
        tv_result_end.setTypeface(mitr_light);

        fragment_timeline.setOnTouchListener(swipeRight);

        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                ll_content_timeline.setVisibility(View.VISIBLE);
                timeline_progress_bar.setVisibility(View.INVISIBLE);
                Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.up_from_buttom);
                ll_content_timeline.startAnimation(anim);
            }
        }, secondsDelayed * 500);

        setCircleImage();
        if(selectedOption.getSelectedPlantId() == 1){
            profile_image_plant.setImageResource(R.drawable.rice);
        } else if(selectedOption.getSelectedPlantId() == 2){
            profile_image_plant.setImageResource(R.drawable.corn);
        }
        tv_selected_type.setText(selectedOption.getSelectedType());
        tv_selected_solution.setText(selectedOption.getSelectedSolution());

        configSelected();
        showResult();
    }

    /************************
     * ADD-ON FUNCTION
     ***********************/
    private void showResult() {
        if (selectedOption.getSelectedSolutionDuration() == 20 && selectedOption.getSelectedPlantId() == 1) {
            circle_image_1.setImageResource(R.drawable.noleaf);
            tv_result_start.setText(Html.fromHtml("<b>เริ่มปลูก : </b>" + "<b>" + selectedOption.getSelectedDate() + "</b>" + "<br>"
                    + "<b>ระยะเวลาปลูก : </b>" + "<b>"  + (selectedOption.getSelectedSolutionDuration() + selectedOption.getSelectedTypeDuration()) + " วัน" + "</b>" + "<br>"
                    + "<b>หมายเหตุ : </b>ปลูกข้าวช่วงเข้าฤดูหนาว 20 ก.ย. - 1 ธ.ค. ทำให้ใช้เวลาปลูกมากขึ้น"));
        } else {
            circle_image_1.setImageResource(R.drawable.noleaf);
            tv_result_start.setText(Html.fromHtml("<b>เริ่มปลูก : </b>" + "<b>" + selectedOption.getSelectedDate() + "</b>" + "<br>"
                    + "<b>ระยะเวลาปลูก : </b>" + "<b>"  + (selectedOption.getSelectedSolutionDuration() + selectedOption.getSelectedTypeDuration()) + " วัน" + "</b>"));
        }

        if (EventName.length >  0) {
            if (EventName[0].equals("ไม่แนะนำให้ปลูก")) {
                circle_image_4.setImageResource(R.drawable.noleaf);
                ll_up_field.setBackgroundResource(R.drawable.background_rec_gadiant_red_orange);
                tv_result_end.setBackgroundResource(R.drawable.text_box_red);
                tv_result_start.setBackgroundResource(R.drawable.text_box_red);
                circle_image_1.setBorderColorResource(R.color.colorRed);
                circle_image_2.setBorderColorResource(R.color.colorRed);
                circle_image_3.setBorderColorResource(R.color.colorRed);
                circle_image_4.setBorderColorResource(R.color.colorRed);
                line_1.setBackgroundResource(R.color.colorRed);
                line_2.setBackgroundResource(R.color.colorRed);
                line_3.setBackgroundResource(R.color.colorRed);
                line_4.setBackgroundResource(R.color.colorRed);
                line_5.setBackgroundResource(R.color.colorRed);
                tv_result_end.setText(Html.fromHtml("<b>เก็บเกี่ยว : </b>"  + "<b>" + selectedOption.getSelectedDateEnd() + "</b>" + "<br>"
                        + "<b>คำแนะนำ : </b>" + "<font color='#F44336'>" + "<b>" + EventName[0] + "</b>" + "</font>" + "<br>"
                        + "<b>รายละเอียด : </b>" + EventDesc[0]));
            } else if (EventName[0].equals("แนะนำให้ปลูก")) {
                circle_image_4.setImageResource(R.drawable.twoleaf);
                ll_up_field.setBackgroundResource(R.drawable.background_rec_gadiant_green_green);
                tv_result_end.setBackgroundResource(R.drawable.text_box_green);
                tv_result_start.setBackgroundResource(R.drawable.text_box_green);
                circle_image_1.setBorderColorResource(R.color.colorGreen);
                circle_image_2.setBorderColorResource(R.color.colorGreen);
                circle_image_3.setBorderColorResource(R.color.colorGreen);
                circle_image_4.setBorderColorResource(R.color.colorGreen);
                line_1.setBackgroundResource(R.color.colorGreen);
                line_2.setBackgroundResource(R.color.colorGreen);
                line_3.setBackgroundResource(R.color.colorGreen);
                line_4.setBackgroundResource(R.color.colorGreen);
                line_5.setBackgroundResource(R.color.colorGreen);
                tv_result_end.setText(Html.fromHtml("<b>เก็บเกี่ยว : </b>"  + "<b>" + selectedOption.getSelectedDateEnd() + "</b>" + "<br>"
                        + "<b>คำแนะนำ : </b>" + "<font color='#8BC34A'>" + "<b>" + EventName[0] + "</b>" + "</font>" + "<br>"
                        + "<b>รายละเอียด : </b>" + EventDesc[0]));
            } else {
                circle_image_4.setImageResource(R.drawable.oneleaf);
                ll_up_field.setBackgroundResource(R.drawable.background_rec_gadiant_yellow_orange);
                tv_result_end.setBackgroundResource(R.drawable.text_box_yellow);
                tv_result_start.setBackgroundResource(R.drawable.text_box_yellow);
                circle_image_1.setBorderColorResource(R.color.colorYellow);
                circle_image_2.setBorderColorResource(R.color.colorYellow);
                circle_image_3.setBorderColorResource(R.color.colorYellow);
                circle_image_4.setBorderColorResource(R.color.colorYellow);
                line_1.setBackgroundResource(R.color.colorYellow);
                line_2.setBackgroundResource(R.color.colorYellow);
                line_3.setBackgroundResource(R.color.colorYellow);
                line_4.setBackgroundResource(R.color.colorYellow);
                line_5.setBackgroundResource(R.color.colorYellow);
                tv_result_end.setText(Html.fromHtml("<b>เก็บเกี่ยว : </b>"  + "<b>" + selectedOption.getSelectedDateEnd() + "</b>" + "<br>"
                        + "<b>คำแนะนำ : </b>" + "<font color='#F9A825'>" +"<b>" + EventName[0] + "</b>" + "</font>" + "<br>"
                        + "<b>รายละเอียด : </b>" + EventDesc[0]));
            }
        } else{
            Log.e("dateEndInt", selectedOption.getSelectedDateEndInteger() + "");
        }
    }

    private void configSelected() {
        List<Event> listEvent = databaseHelper.getEvent(selectedOption.getSelectedDateEndInteger(), selectedOption.getSelectedPlantId());
        EventName = new String[listEvent.size()];
        EventDesc = new String[listEvent.size()];
        for (int i = 0; i < listEvent.size(); i++) {
            EventName[i] = listEvent.get(i).getE_name();
            EventDesc[i] = listEvent.get(i).getE_desc();
            Log.i("EventName", EventName[i] + "");
            Log.i("EventDesc", EventDesc[i] + "");
            Log.i("i", i + "");
        }
    }

    private void setCircleImage() {
        if (selectedOption.getSelectedSeekMonth() >= 2 && selectedOption.getSelectedSeekMonth() <= 5) {
            if (check == 2) {

            } else {
                slide_out.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        slide_in.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                profile_image_weather.setImageResource(R.drawable.summer_weather);
                                profile_image.setImageResource(R.drawable.summer);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                            }
                        });

                        profile_image_weather.startAnimation(slide_in);
                    }
                });
                profile_image_weather.startAnimation(slide_out);
                check = 2;
            }
        } else if (selectedOption.getSelectedSeekMonth() >= 6 && selectedOption.getSelectedSeekMonth() <= 9) {
            if (check == 6) {

            } else {
                slide_out.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        slide_in.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                profile_image_weather.setImageResource(R.drawable.rainy_weather);
                                profile_image.setImageResource(R.drawable.rainy);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                            }
                        });

                        profile_image_weather.startAnimation(slide_in);
                    }
                });
                profile_image_weather.startAnimation(slide_out);
                check = 6;
            }
        } else if ((selectedOption.getSelectedSeekMonth() >= 10 && selectedOption.getSelectedSeekMonth() <= 12) || (selectedOption.getSelectedSeekMonth() == 1)) {
            if (check == 10) {

            } else {
                slide_out.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        slide_in.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                profile_image_weather.setImageResource(R.drawable.winter_weather);
                                profile_image.setImageResource(R.drawable.winter);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                            }
                        });

                        profile_image_weather.startAnimation(slide_in);
                    }
                });
                profile_image_weather.startAnimation(slide_out);
                check = 10;
            }
        }
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

    /************************
     * LISTENER
     ***********************/
    OnSwipeTouchListener swipeRight = new OnSwipeTouchListener(getContext()) {
        public void onSwipeRight() {
            TimelineFragmentListener timelineFragmentListener = (TimelineFragmentListener) getActivity();
            timelineFragmentListener.returnHome();
        }
    };
}
