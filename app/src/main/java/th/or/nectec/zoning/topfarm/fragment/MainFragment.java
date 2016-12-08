package th.or.nectec.zoning.topfarm.fragment;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.feeeei.circleseekbar.CircleSeekBar;
import th.or.nectec.zoning.topfarm.R;
import th.or.nectec.zoning.topfarm.database.DatabaseHelper;
import th.or.nectec.zoning.topfarm.datatype.Plant;
import th.or.nectec.zoning.topfarm.datatype.Processes;
import th.or.nectec.zoning.topfarm.datatype.SubPlant;
import th.or.nectec.zoning.topfarm.manager.SelectedOption;

/**
 * Created by nuuneoi on 11/16/2014.
 */
@SuppressWarnings("unused")
public class MainFragment extends Fragment {

    public interface MainFragmentListener {
        void showCircular();

        void showTimeline();
    }

    /************************
     * VARIABLE
     ***********************/
    private String[] processesName, subPlantName, plantName, plantPicture;
    private int[] processesDuration, processedId, subPlantDuration, subPlantId, plantId;
    private DatabaseHelper databaseHelper;
    private TextView tv_setting, tv_separate_date, tv_selectedDay, tv_selectedMonth, tv_rice_type, tv_solution_type;
    private CircleSeekBar seekDay, seekMonth;
    private ImageView iv_rice_icon, iv_solution_icon, iv_show_timeline, iv_show_circular;
    private CircleImageView profile_image, profile_image_weather, profile_image_plant;
    private SelectedOption selectedOption;
    private Calendar calendar;
    private int month, day, year;
    private LinearLayout ll_selected_date, ll_setting_plant, fragment_main;
    private FrameLayout fl_show_timeline, fl_show_circular;
    private Animation slide_in, slide_out;
    private int check = 0;

    public MainFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
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
        Log.i("selectSeekDay", selectedOption.getSelectedSeekDay() + "");
        Log.i("selectSeekMonth", selectedOption.getSelectedSeekMonth() + "");
        Log.i("selectDate", selectedOption.getSelectedDate() + "");
        ll_setting_plant = (LinearLayout) rootView.findViewById(R.id.ll_setting_plant);
        fragment_main = (LinearLayout) rootView.findViewById(R.id.fragment_main);
        profile_image = (CircleImageView) rootView.findViewById(R.id.profile_image);
        profile_image_weather = (CircleImageView) rootView.findViewById(R.id.profile_image_weather);
        profile_image_plant = (CircleImageView) rootView.findViewById(R.id.profile_image_plant);
        tv_setting = (TextView) rootView.findViewById(R.id.tv_setting);
        tv_solution_type = (TextView) rootView.findViewById(R.id.tv_solution_type);
        tv_rice_type = (TextView) rootView.findViewById(R.id.tv_rice_type);
        fl_show_circular = (FrameLayout) rootView.findViewById(R.id.fl_show_circular);
        iv_show_circular = (ImageView) rootView.findViewById(R.id.iv_show_circular);
        fl_show_timeline = (FrameLayout) rootView.findViewById(R.id.fl_show_timeline);
        iv_show_timeline = (ImageView) rootView.findViewById(R.id.iv_show_timeline);
        tv_separate_date = (TextView) rootView.findViewById(R.id.tv_separate_date);
        tv_selectedDay = (TextView) rootView.findViewById(R.id.tv_selectedDay);
        tv_selectedMonth = (TextView) rootView.findViewById(R.id.tv_selectedMonth);
        iv_rice_icon = (ImageView) rootView.findViewById(R.id.iv_rice_icon);
        iv_solution_icon = (ImageView) rootView.findViewById(R.id.iv_solution_icon);
        ll_selected_date = (LinearLayout) rootView.findViewById(R.id.ll_selected_date);
        seekDay = (CircleSeekBar) rootView.findViewById(R.id.seekDay);
        seekMonth = (CircleSeekBar) rootView.findViewById(R.id.seekMonth);

        Typeface mitr_light = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Mitr-Light.ttf");
        Typeface mitr_regular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Mitr-Regular.ttf");
        tv_setting.setTypeface(mitr_regular);
        tv_solution_type.setTypeface(mitr_light);
        tv_rice_type.setTypeface(mitr_light);
        tv_separate_date.setTypeface(mitr_light);
        tv_selectedDay.setTypeface(mitr_light);
        tv_selectedMonth.setTypeface(mitr_light);

        slide_in = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_right_season);
        slide_out = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_right_season);

        calendar = Calendar.getInstance();
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        year = calendar.get(Calendar.YEAR);

        fl_show_circular.setOnClickListener(fl_show_circularListener);
        fl_show_timeline.setOnClickListener(fl_show_timelineListener);
        iv_rice_icon.setOnClickListener(showDialogType);
        iv_solution_icon.setOnClickListener(showDialogSolution);
        ll_selected_date.setOnClickListener(ll_selected_dateListener);
        seekDay.setOnSeekBarChangeListener(seekDayListener);
        seekMonth.setOnSeekBarChangeListener(seekMonthListener);
//        fragment_main.setOnTouchListener(fragment_mainListener);

        tv_solution_type.setTextColor(Color.parseColor("#757575"));
        tv_rice_type.setTextColor(Color.parseColor("#757575"));
        tv_selectedDay.setTextColor(Color.parseColor("#9C27B0"));
        tv_selectedMonth.setTextColor(Color.parseColor("#9C27B0"));
        seekDay.setMaxProcess(31);
        seekMonth.setMaxProcess(11);
        configSelected();

        if (selectedOption.getSelectedSolution() != null) {
            Log.i("type", selectedOption.getSelectedType() + "");
            tv_solution_type.setText(selectedOption.getSelectedSolution() + "");
            tv_solution_type.setTextColor(Color.parseColor("#9C27B0"));
        }

        if (selectedOption.getSelectedType() != null) {
            tv_rice_type.setText(selectedOption.getSelectedType());
            tv_rice_type.setTextColor(Color.parseColor("#9C27B0"));
        }

        if (tv_solution_type.getText().toString().equals("1) กรุณาเลือกวิธีปลูก ") || tv_rice_type.getText().toString().equals("2) กรุณาเลือกสายพันธุ์")) {
            fl_show_circular.setBackgroundResource(R.drawable.button_rec_tran_gray);
            iv_show_circular.setImageResource(R.drawable.circular_gray);
        }

        if (tv_solution_type.getText().toString().equals("1) กรุณาเลือกวิธีปลูก ") || tv_rice_type.getText().toString().equals("2) กรุณาเลือกสายพันธุ์")) {
            fl_show_timeline.setBackgroundResource(R.drawable.button_rec_tran_gray);
            iv_show_timeline.setImageResource(R.drawable.timeline_gray);
        }

        if (plantId.length > 0) {
            if (selectedOption.getSelectedPlantName() != null) {
                if (plantPicture[selectedOption.getSelectedPlantId() - 1].toString().equals("rice")) {
                    profile_image_plant.setImageResource(R.drawable.rice);
                } else if (plantPicture[selectedOption.getSelectedPlantId() - 1].toString().equals("corn")) {
                    profile_image_plant.setImageResource(R.drawable.corn);
                }
                tv_setting.setText("ปลูก" + selectedOption.getSelectedPlantName());
                configSelected();
            } else {
                tv_setting.setText("ปลูกข้าว");
                selectedOption.setSelectedPlantName(plantName[0]);
                selectedOption.setSelectedPlantId(plantId[0]);
                profile_image_plant.setImageResource(R.drawable.rice);
                configSelected();
            }
            ll_setting_plant.setOnClickListener(ll_settingListener);
        }

        if (selectedOption.getSeparateDate() != null) {
            tv_separate_date.setText(selectedOption.getSeparateDate());
        }

        if (selectedOption.getSelectedSeekDay() != 0) {
            seekDay.setCurProcess(selectedOption.getSelectedSeekDay() - 1);
            tv_selectedDay.setText(seekDay.getCurProcess() + 1 + "");
        } else {
            tv_selectedDay.setText(day + "");
            selectedOption.setSelectedSeekDay(day);
            tv_selectedDay.setTextColor(Color.parseColor("#9C27B0"));
            seekDay.setCurProcess(day - 1);
        }

        if (selectedOption.getSelectedSeekMonth() != 0) {
            Log.i("month", selectedOption.getSelectedSeekMonth() + "");
            seekMonth.setCurProcess(selectedOption.getSelectedSeekMonth() - 1);
            onConfigMonth(selectedOption.getSelectedSeekMonth() - 1);
            setCircleImage();
        } else {
            onConfigMonth(month);
            selectedOption.setSelectedSeekMonth(month);
            tv_selectedMonth.setTextColor(Color.parseColor("#9C27B0"));
            seekMonth.setCurProcess(month);
            setCircleImage();
        }
    }

    /************************
     * ADD-ON FUNCTION
     ***********************/
    private void configSelected() {
        List<Plant> listPlant = databaseHelper.getAllPlant();
        plantName = new String[listPlant.size()];
        plantPicture = new String[listPlant.size()];
        plantId = new int[listPlant.size()];
        for (int i = 0; i < listPlant.size(); i++) {
            plantName[i] = listPlant.get(i).getP_name();
            plantPicture[i] = listPlant.get(i).getP_picture();
            plantId[i] = listPlant.get(i).getP_id();
        }

        List<Processes> listProcesses = databaseHelper.getAllProcesses(selectedOption.getSelectedPlantId());
        processesName = new String[listProcesses.size()];
        processesDuration = new int[listProcesses.size()];
        processedId = new int[listProcesses.size()];
        for (int i = 0; i < listProcesses.size(); i++) {
            processesName[i] = listProcesses.get(i).getPc_name();
            processesDuration[i] = listProcesses.get(i).getPc_duration();
            processedId[i] = i + 1;
        }

        List<SubPlant> listSubPlant = databaseHelper.getAllSubPlant(selectedOption.getSelectedPlantId());
        subPlantName = new String[listSubPlant.size()];
        subPlantDuration = new int[listSubPlant.size()];
        subPlantId = new int[listSubPlant.size()];
        for (int i = 0; i < listSubPlant.size(); i++) {
            subPlantName[i] = listSubPlant.get(i).getSp_name();
            subPlantDuration[i] = listSubPlant.get(i).getSp_duration();
            subPlantId[i] = i + 1;
        }
    }

    private void alertToast() {
        String validate = "";
        if (tv_solution_type.getText().toString().equals("1) กรุณาเลือกวิธีปลูก ")) {
            validate = validate + " 1)วิธีปลูก";
            Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.bounce_long);
            BounceInterpolator interpolator = new BounceInterpolator();
            anim.setInterpolator(interpolator);
            iv_solution_icon.startAnimation(anim);
        }
        if (tv_rice_type.getText().toString().equals("2) กรุณาเลือกสายพันธุ์")) {
            validate = validate + " 2)สายพันธุ์";
            Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.bounce_long);
            BounceInterpolator interpolator = new BounceInterpolator();
            anim.setInterpolator(interpolator);
            iv_rice_icon.startAnimation(anim);
        }
        Toast.makeText(getContext(), "กรุณาเลือก" + validate, Toast.LENGTH_LONG).show();
    }

    private void clearView() {
        tv_rice_type.setText("2) กรุณาเลือกสายพันธุ์");
        tv_rice_type.setTextColor(Color.parseColor("#757575"));

        tv_solution_type.setText("1) กรุณาเลือกวิธีปลูก ");
        tv_solution_type.setTextColor(Color.parseColor("#757575"));

        selectedOption.setSelectedTypeId(-1);
        selectedOption.setSelectedSolutionId(-1);

        disableButton();
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

    private void setProcessesDuration() {
        if (seekMonth.getCurProcess() == 8 && seekDay.getCurProcess() >= 19) {
            selectedOption.setSelectedSolutionDuration(20);
        } else if (seekMonth.getCurProcess() == 9 && seekDay.getCurProcess() >= 0) {
            selectedOption.setSelectedSolutionDuration(20);
        } else if (seekMonth.getCurProcess() == 10 && seekDay.getCurProcess() >= 0) {
            selectedOption.setSelectedSolutionDuration(20);
        } else if (seekMonth.getCurProcess() == 11 && seekDay.getCurProcess() == 0) {
            selectedOption.setSelectedSolutionDuration(20);
        } else {
            selectedOption.setSelectedSolutionDuration(processesDuration[selectedOption.getSelectedSolutionId() - 1]);
        }
    }

    private void checkValidate() {
        if (tv_solution_type.getText().toString().equals("1) กรุณาเลือกวิธีปลูก ") || tv_rice_type.getText().toString().equals("2) กรุณาเลือกสายพันธุ์")) {

        } else {
            fl_show_timeline.setEnabled(true);
            fl_show_timeline.setBackgroundResource(R.drawable.button_rec_tran_purple);
            iv_show_timeline.setImageResource(R.drawable.timeline_purple);
            fl_show_circular.setEnabled(true);
            fl_show_circular.setBackgroundResource(R.drawable.button_rec_tran_purple);
            iv_show_circular.setImageResource(R.drawable.circular_purple);
        }
    }

    private void disableButton() {
        fl_show_circular.setBackgroundResource(R.drawable.button_rec_tran_gray);
        iv_show_circular.setImageResource(R.drawable.circular_gray);

        fl_show_timeline.setBackgroundResource(R.drawable.button_rec_tran_gray);
        iv_show_timeline.setImageResource(R.drawable.timeline_gray);
    }

    private void setDate() {
        int day = seekDay.getCurProcess() + 1;
        int month = seekMonth.getCurProcess() + 1;
        int duration = 0;
        String date_end;
        selectedOption.setSelectedDate(tv_selectedDay.getText().toString() + " " + tv_selectedMonth.getText().toString());
        if (month == 1) {
            duration = day;
        } else if (month == 2) {
            duration = 31 + day;
        } else if (month == 3) {
            duration = 59 + day;
        } else if (month == 4) {
            duration = 90 + day;
        } else if (month == 5) {
            duration = 120 + day;
        } else if (month == 6) {
            duration = 151 + day;
        } else if (month == 7) {
            duration = 181 + day;
        } else if (month == 8) {
            duration = 212 + day;
        } else if (month == 9) {
            duration = 243 + day;
        } else if (month == 10) {
            duration = 273 + day;
        } else if (month == 11) {
            duration = 304 + day;
        } else if (month == 12) {
            duration = 334 + day;
        }

        duration = duration + selectedOption.getSelectedTypeDuration() + selectedOption.getSelectedSolutionDuration() - 2;

        Log.i("duration", duration + "");
        if (duration >= 0 && duration <= 30) {
            selectedOption.setSelectedDateEnd(day + " มกราคม");
            if (day < 10) {
                selectedOption.setSelectedDateEndInteger("0000-01" + "-0" + day);
            } else {
                selectedOption.setSelectedDateEndInteger("0000-01" + "-" + day);
            }
        } else if (duration >= 31 && duration <= 58) {
            int date = duration - 30;
            selectedOption.setSelectedDateEnd((date) + " กุมภาพันธ์");
            if (date < 10) {
                selectedOption.setSelectedDateEndInteger("0000-02" + "-0" + date);
            } else {
                selectedOption.setSelectedDateEndInteger("0000-02" + "-" + date);
            }
        } else if (duration >= 59 && duration <= 89) {
            int date = duration - 58;
            selectedOption.setSelectedDateEnd((date) + " มีนาคม");
            if (date < 10) {
                selectedOption.setSelectedDateEndInteger("0000-03" + "-0" + date);
            } else {
                selectedOption.setSelectedDateEndInteger("0000-03" + "-" + date);
            }
        } else if (duration >= 90 && duration <= 119) {
            int date = duration - 89;
            selectedOption.setSelectedDateEnd((date) + " เมษายน");
            if (date < 10) {
                selectedOption.setSelectedDateEndInteger("0000-04" + "-0" + date);
            } else {
                selectedOption.setSelectedDateEndInteger("0000-04" + "-" + date);
            }
        } else if (duration >= 120 && duration <= 150) {
            int date = duration - 119;
            selectedOption.setSelectedDateEnd((date) + " พฤษภาคม");
            if (date < 10) {
                selectedOption.setSelectedDateEndInteger("0000-05" + "-0" + date);
            } else {
                selectedOption.setSelectedDateEndInteger("0000-05" + "-" + date);
            }
        } else if (duration >= 151 && duration <= 180) {
            int date = duration - 150;
            selectedOption.setSelectedDateEnd((date) + " มิถุนายน");
            if (date < 10) {
                selectedOption.setSelectedDateEndInteger("0000-06" + "-0" + date);
            } else {
                selectedOption.setSelectedDateEndInteger("0000-06" + "-" + date);
            }
        } else if (duration >= 181 && duration <= 211) {
            int date = duration - 180;
            selectedOption.setSelectedDateEnd((date) + " กรกฎาคม");
            if (date < 10) {
                selectedOption.setSelectedDateEndInteger("0000-07" + "-0" + date);
            } else {
                selectedOption.setSelectedDateEndInteger("0000-07" + "-" + date);
            }
        } else if (duration >= 212 && duration <= 242) {
            int date = duration - 211;
            selectedOption.setSelectedDateEnd((date) + " สิงหาคม");
            if (date < 10) {
                selectedOption.setSelectedDateEndInteger("0000-08" + "-0" + date);
            } else {
                selectedOption.setSelectedDateEndInteger("0000-08" + "-" + date);
            }
        } else if (duration >= 243 && duration <= 272) {
            int date = duration - 242;
            selectedOption.setSelectedDateEnd((date) + " กันยายน");
            if (date < 10) {
                selectedOption.setSelectedDateEndInteger("0000-09" + "-0" + date);
            } else {
                selectedOption.setSelectedDateEndInteger("0000-09" + "-" + date);
            }
        } else if (duration >= 273 && duration <= 303) {
            int date = duration - 272;
            selectedOption.setSelectedDateEnd((date) + " ตุลาคม");
            if (date < 10) {
                selectedOption.setSelectedDateEndInteger("0000-10" + "-0" + date);
            } else {
                selectedOption.setSelectedDateEndInteger("0000-10" + "-" + date);
            }
        } else if (duration >= 304 && duration <= 333) {
            int date = duration - 303;
            selectedOption.setSelectedDateEnd((date) + " พฤศจิกายน");
            if (date < 10) {
                selectedOption.setSelectedDateEndInteger("0000-11" + "-0" + date);
            } else {
                selectedOption.setSelectedDateEndInteger("0000-11" + "-" + date);
            }
        } else if (duration >= 334 && duration <= 364) {
            int date = duration - 333;
            selectedOption.setSelectedDateEnd((date) + " ธันวาคม");
            if (date < 10) {
                selectedOption.setSelectedDateEndInteger("0000-12" + "-0" + date);
            } else {
                selectedOption.setSelectedDateEndInteger("0000-12" + "-" + date);
            }
        } else if (duration >= 365 && duration <= 395) {
            int date = duration - 364;
            selectedOption.setSelectedDateEnd((date) + " มกราคม");
            if (date < 10) {
                selectedOption.setSelectedDateEndInteger("0000-01" + "-0" + date);
            } else {
                selectedOption.setSelectedDateEndInteger("0000-01" + "-" + date);
            }
        } else if (duration >= 396 && duration <= 423) {
            int date = duration - 395;
            selectedOption.setSelectedDateEnd((date) + " กุมภาพันธ์");
            if (date < 10) {
                selectedOption.setSelectedDateEndInteger("0000-02" + "-0" + date);
            } else {
                selectedOption.setSelectedDateEndInteger("0000-02" + "-" + date);
            }
        } else if (duration >= 424 && duration <= 454) {
            int date = duration - 423;
            selectedOption.setSelectedDateEnd((date) + " มีนาคม");
            if (date < 10) {
                selectedOption.setSelectedDateEndInteger("0000-03" + "-0" + date);
            } else {
                selectedOption.setSelectedDateEndInteger("0000-03" + "-" + date);
            }
        } else if (duration >= 455 && duration <= 484) {
            int date = duration - 454;
            selectedOption.setSelectedDateEnd((date) + " เมษายน");
            if (date < 10) {
                selectedOption.setSelectedDateEndInteger("0000-04" + "-0" + date);
            } else {
                selectedOption.setSelectedDateEndInteger("0000-04" + "-" + date);
            }
        } else if (duration >= 485 && duration <= 516) {
            int date = duration - 484;
            selectedOption.setSelectedDateEnd((date) + " พฤษภาคม");
            if (date < 10) {
                selectedOption.setSelectedDateEndInteger("0000-05" + "-0" + date);
            } else {
                selectedOption.setSelectedDateEndInteger("0000-05" + "-" + date);
            }
        }
    }

    private void onConfigMonth(int position) {
        if (position + 1 == 1 || position + 1 == 3 || position + 1 == 5 || position + 1 == 7 || position + 1 == 8 || position + 1 == 10 || position + 1 == 12) {
            seekDay.setMaxProcess(30);
            seekDay.setCurProcess(seekDay.getCurProcess());
        } else if (position + 1 == 2) {
            seekDay.setMaxProcess(27);
            seekDay.setCurProcess(seekDay.getCurProcess());
        } else {
            seekDay.setMaxProcess(29);
            seekDay.setCurProcess(seekDay.getCurProcess());
        }
        if (position + 1 == 1) {
            tv_selectedMonth.setText("มกราคม");
        } else if (position + 1 == 2) {
            tv_selectedMonth.setText("กุมภาพันธ์");
        } else if (position + 1 == 3) {
            tv_selectedMonth.setText("มีนาคม");
        } else if (position + 1 == 4) {
            tv_selectedMonth.setText("เมษายน");
        } else if (position + 1 == 5) {
            tv_selectedMonth.setText("พฤษภาคม");
        } else if (position + 1 == 6) {
            tv_selectedMonth.setText("มิถุนายน");
        } else if (position + 1 == 7) {
            tv_selectedMonth.setText("กรกฎาคม");
        } else if (position + 1 == 8) {
            tv_selectedMonth.setText("สิงหาคม");
        } else if (position + 1 == 9) {
            tv_selectedMonth.setText("กันยายน");
        } else if (position + 1 == 10) {
            tv_selectedMonth.setText("ตุลาคม");
        } else if (position + 1 == 11) {
            tv_selectedMonth.setText("พฤศจิกายน");
        } else if (position + 1 == 12) {
            tv_selectedMonth.setText("ธันวาคม");
        } else {
            tv_selectedMonth.setText("OutOfMemory!");
        }
    }

    public void updateView() {
        tv_rice_type.setText(selectedOption.getSelectedType() + "");
        tv_solution_type.setText(selectedOption.getSelectedSolution() + "");
        seekDay.setCurProcess(selectedOption.getSelectedSeekDay() - 1);
        tv_selectedDay.setText(seekDay.getCurProcess() + 1 + "");
        seekMonth.setCurProcess(selectedOption.getSelectedSeekMonth() - 1);
        onConfigMonth(selectedOption.getSelectedSeekMonth() - 1);
        setCircleImage();
    }

    public void selectPlant() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext());
        builder
                .title("เลือกพีชที่ต้องการปลูก")
                .items(plantName)
                .typeface("Mitr-Regular.ttf", "Mitr-Regular.ttf")
                .itemsCallbackSingleChoice(selectedOption.getSelectedPlantName() != null ? selectedOption.getSelectedPlantId() - 1 : 0, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        /**
                         * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                         * returning false here won't allow the newly selected radio button to actually be selected.
                         **/
                        selectedOption.setSelectedPlantName(plantName[which]);
                        selectedOption.setSelectedPlantId(plantId[which]);
                        tv_setting.setText("ปลูก" + plantName[which]);
                        Log.i("which", which + "");
                        Log.i("plantPicture", plantPicture[which] + "");
                        if (plantPicture[which].toString().equals("rice")) {
                            profile_image_plant.setImageResource(R.drawable.rice);
                        } else if (plantPicture[which].toString().equals("corn")) {
                            profile_image_plant.setImageResource(R.drawable.corn);
                        }
                        configSelected();
                        clearView();
                        return true;
                    }
                })
                .negativeText("ยกเลิก")
                .show();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /*
         * Save Instance State Here
         */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
        outState.putBundle("selectedOption", selectedOption.onSaveInstanceState());
    }

    /*
     * Restore Instance State Here
     */
    @SuppressWarnings("UnusedParameters")
    private void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore Instance State here
        selectedOption.onRestoreInstanceState(savedInstanceState.getBundle("selectedOption"));
    }

    View.OnClickListener ll_settingListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
            BounceInterpolator interpolator = new BounceInterpolator();
            anim.setInterpolator(interpolator);
            ll_setting_plant.startAnimation(anim);
            MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext());
            builder
                    .title("เลือกพีชที่ต้องการปลูก")
                    .items(plantName)
                    .typeface("Mitr-Regular.ttf", "Mitr-Regular.ttf")
                    .itemsCallbackSingleChoice(selectedOption.getSelectedPlantName() != null ? selectedOption.getSelectedPlantId() - 1 : 0, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            /**
                             * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                             * returning false here won't allow the newly selected radio button to actually be selected.
                             **/
                            selectedOption.setSelectedPlantName(plantName[which]);
                            selectedOption.setSelectedPlantId(plantId[which]);
                            tv_setting.setText("ปลูก" + plantName[which]);
                            Log.i("which", which + "");
                            Log.i("plantPicture", plantPicture[which] + "");
                            if (plantPicture[which].toString().equals("rice")) {
                                profile_image_plant.setImageResource(R.drawable.rice);
                            } else if (plantPicture[which].toString().equals("corn")) {
                                profile_image_plant.setImageResource(R.drawable.corn);
                            }
                            configSelected();
                            clearView();
                            return true;
                        }
                    })
                    .negativeText("ยกเลิก")
                    .show();
        }
    };

    View.OnClickListener fl_show_circularListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (tv_solution_type.getText().toString().equals("1) กรุณาเลือกวิธีปลูก ") || tv_rice_type.getText().toString().equals("2) กรุณาเลือกสายพันธุ์")) {
                alertToast();
            } else {
                Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
                BounceInterpolator interpolator = new BounceInterpolator();
                anim.setInterpolator(interpolator);
                fl_show_circular.startAnimation(anim);

                setProcessesDuration();
                setDate();

                MainFragmentListener mainFragmentListener = (MainFragmentListener) getActivity();
                mainFragmentListener.showCircular();
            }
        }
    };

    View.OnClickListener fl_show_timelineListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (tv_solution_type.getText().toString().equals("1) กรุณาเลือกวิธีปลูก ") || tv_rice_type.getText().toString().equals("2) กรุณาเลือกสายพันธุ์")) {
                alertToast();
            } else {
                Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
                BounceInterpolator interpolator = new BounceInterpolator();
                anim.setInterpolator(interpolator);
                fl_show_timeline.startAnimation(anim);

                setProcessesDuration();
                setDate();

                MainFragmentListener mainFragmentListener = (MainFragmentListener) getActivity();
                mainFragmentListener.showTimeline();
            }
        }
    };

    View.OnClickListener showDialogType = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
            BounceInterpolator interpolator = new BounceInterpolator();
            anim.setInterpolator(interpolator);
            iv_rice_icon.startAnimation(anim);
            new MaterialDialog.Builder(getContext())
                    .title("เลือกสายพันธุ์")
                    .items(subPlantName)
                    .typeface("Mitr-Regular.ttf", "Mitr-Regular.ttf")
                    .itemsCallbackSingleChoice(selectedOption.getSelectedTypeId() - 1, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            /**
                             * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                             * returning false here won't allow the newly selected radio button to actually be selected.
                             **/
                            selectedOption.setSelectedType(subPlantName[which]);
                            selectedOption.setSelectedTypeDuration(subPlantDuration[which]);
                            selectedOption.setSelectedTypeId(subPlantId[which]);
                            tv_rice_type.setText(text);
                            tv_rice_type.setTextColor(Color.parseColor("#9C27B0"));
                            checkValidate();
                            Log.i("typeId", selectedOption.getSelectedTypeId() + "");
                            return true;
                        }
                    })
                    .negativeText("ยกเลิก")
                    .show();
        }
    };

    View.OnClickListener showDialogSolution = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
            BounceInterpolator interpolator = new BounceInterpolator();
            anim.setInterpolator(interpolator);
            iv_solution_icon.startAnimation(anim);
            MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext());
            builder
                    .title("เลือกวิธีปลูก")
                    .items(processesName)
                    .typeface("Mitr-Regular.ttf", "Mitr-Regular.ttf")
                    .itemsCallbackSingleChoice(selectedOption.getSelectedSolutionId() - 1, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            /**
                             * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                             * returning false here won't allow the newly selected radio button to actually be selected.
                             **/
                            selectedOption.setSelectedSolution(processesName[which]);
                            selectedOption.setSelectedSolutionDuration(processesDuration[which]);
                            selectedOption.setSelectedSolutionId(processedId[which]);
                            tv_solution_type.setText(text);
                            tv_solution_type.setTextColor(Color.parseColor("#9C27B0"));
                            checkValidate();
                            return true;
                        }
                    })
                    .negativeText("ยกเลิก")
                    .show();
        }
    };

    View.OnClickListener ll_selected_dateListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
            BounceInterpolator interpolator = new BounceInterpolator();
            anim.setInterpolator(interpolator);
            ll_selected_date.startAnimation(anim);
            if (selectedOption.getSelectedSeekDay() == day && selectedOption.getSelectedSeekMonth() == month) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                tv_selectedDay.setText(day + "");
                                selectedOption.setSelectedSeekDay(day);
                                seekDay.setCurProcess(day - 1);
                                onConfigMonth(month);
                                selectedOption.setSelectedSeekMonth(month);
                                seekMonth.setCurProcess(month);
                                setCircleImage();
                            }
                        }, year, month, day);
                datePickerDialog.show();
            } else {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                tv_selectedDay.setText(day + "");
                                selectedOption.setSelectedSeekDay(day);
                                seekDay.setCurProcess(day - 1);
                                onConfigMonth(month);
                                selectedOption.setSelectedSeekMonth(month);
                                seekMonth.setCurProcess(month);
                                setCircleImage();
                            }
                        }, year, selectedOption.getSelectedSeekMonth() - 1, selectedOption.getSelectedSeekDay());
                datePickerDialog.show();
            }
        }
    };

    CircleSeekBar.OnSeekBarChangeListener seekDayListener = new CircleSeekBar.OnSeekBarChangeListener() {
        @Override
        public void onChanged(CircleSeekBar circleSeekBar, int position) {
            tv_selectedDay.setText(position + 1 + "");
            selectedOption.setSelectedSeekDay(position + 1);
            tv_selectedDay.setTextColor(Color.parseColor("#9C27B0"));
            checkValidate();
        }
    };

    CircleSeekBar.OnSeekBarChangeListener seekMonthListener = new CircleSeekBar.OnSeekBarChangeListener() {
        @Override
        public void onChanged(CircleSeekBar circleSeekBar, int position) {
            onConfigMonth(position);
            tv_selectedMonth.setTextColor(Color.parseColor("#9C27B0"));
            selectedOption.setSelectedSeekMonth(position + 1);
            checkValidate();
            setCircleImage();
        }
    };

//    OnSwipeTouchListener fragment_mainListener = new OnSwipeTouchListener(getContext()) {
//        public void onSwipeLeft() {
//            if (tv_solution_type.getText().toString().equals("1) กรุณาเลือกวิธีปลูก ") || tv_rice_type.getText().toString().equals("2) กรุณาเลือกสายพันธุ์")) {
//                alertToast();
//            } else {
//                setProcessesDuration();
//                setDate();
//
//                MainFragmentListener mainFragmentListener = (MainFragmentListener) getActivity();
//                mainFragmentListener.showTimeline();
//            }
//        }
//
//        public void onSwipeRight() {
//            if (tv_solution_type.getText().toString().equals("1) กรุณาเลือกวิธีปลูก ") || tv_rice_type.getText().toString().equals("2) กรุณาเลือกสายพันธุ์")) {
//                alertToast();
//            } else {
//                setProcessesDuration();
//                setDate();
//
//                MainFragmentListener mainFragmentListener = (MainFragmentListener) getActivity();
//                mainFragmentListener.showCircular();
//            }
//        }
//    };
}
