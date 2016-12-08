package th.or.nectec.zoning.topfarm.fragment;

import android.annotation.TargetApi;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crystal.crystalpreloaders.widgets.CrystalPreloader;
import com.lukedeighton.wheelview.WheelView;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import th.or.nectec.zoning.topfarm.R;
import th.or.nectec.zoning.topfarm.database.DatabaseHelper;
import th.or.nectec.zoning.topfarm.datatype.Event;
import th.or.nectec.zoning.topfarm.datatype.Processes;
import th.or.nectec.zoning.topfarm.datatype.SubPlant;
import th.or.nectec.zoning.topfarm.manager.OnSwipeTouchListener;
import th.or.nectec.zoning.topfarm.manager.SelectedOption;

/**
 * Created by nuuneoi on 11/16/2014.
 */
@SuppressWarnings("unused")
public class CircularFragment extends Fragment {

    public interface CircularFragmentListener {
        void returnHome();
    }

    /************************
     * VARIABLE
     ***********************/
    private String[] processesName, subPlantName;
    private int[] processesDuration, processedId, subPlantDuration, subPlantId;
    private DatabaseHelper databaseHelper;
    private SelectedOption selectedOption;
    private TextView tv_selected_type, tv_selected_solution, tv_result, tv_selected_date, tv_confirm, tv_result_zoom;
    private ImageView iv_circular, iv_zoom, iv_redo, iv_zoom_out, iv_exit_zoom, iv_exit_wheel;
    private String[] EventName, EventDesc;
    private LinearLayout ll_up_field;
    private int dayEnd = 0, monthEnd = 0;
    private RelativeLayout ll_content_circular, fragment_circular;
    private LinearLayout ll_set_solution, ll_set_type, ll_set_date;
    private CrystalPreloader circular_progress_bar;
    private WheelView wheel_view;
    private int monthOfYear, dayOfMonth, current_date, fixedDate;
    private Animation bounce;
    private int year;
    private Calendar calendar;
    private CircleImageView profile_image, profile_image_weather, profile_image_plant;
    private Animation slide_in, slide_out;
    private int check = 0;

    public CircularFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static CircularFragment newInstance() {
        CircularFragment fragment = new CircularFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_circular, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
        selectedOption = SelectedOption.getInstance();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        circular_progress_bar = (CrystalPreloader) rootView.findViewById(R.id.circular_progress_bar);
        ll_up_field = (LinearLayout) rootView.findViewById(R.id.ll_up_field);
        fragment_circular = (RelativeLayout) rootView.findViewById(R.id.fragment_circular);
        iv_circular = (ImageView) rootView.findViewById(R.id.iv_circular);
        ll_set_solution = (LinearLayout) rootView.findViewById(R.id.ll_set_solution);
        ll_set_type = (LinearLayout) rootView.findViewById(R.id.ll_set_type);
        ll_set_date = (LinearLayout) rootView.findViewById(R.id.ll_set_date);
        wheel_view = (WheelView) rootView.findViewById(R.id.wheel_view);
        tv_confirm = (TextView) rootView.findViewById(R.id.tv_confirm);
        ll_content_circular = (RelativeLayout) rootView.findViewById(R.id.ll_content_circular);
        tv_selected_type = (TextView) rootView.findViewById(R.id.tv_selected_type);
        tv_selected_solution = (TextView) rootView.findViewById(R.id.tv_selected_solution);
        tv_selected_date = (TextView) rootView.findViewById(R.id.tv_selected_date);
        tv_result = (TextView) rootView.findViewById(R.id.tv_result);
        fragment_circular = (RelativeLayout) rootView.findViewById(R.id.fragment_circular);
        iv_zoom = (ImageView) rootView.findViewById(R.id.iv_zoom);
        iv_redo = (ImageView) rootView.findViewById(R.id.iv_redo);
        tv_result_zoom = (TextView) rootView.findViewById(R.id.tv_result_zoom);
        iv_zoom_out = (ImageView) rootView.findViewById(R.id.iv_zoom_out);
        profile_image = (CircleImageView) rootView.findViewById(R.id.profile_image);
        profile_image_weather = (CircleImageView) rootView.findViewById(R.id.profile_image_weather);
        profile_image_plant = (CircleImageView) rootView.findViewById(R.id.profile_image_plant);
        iv_exit_zoom = (ImageView) rootView.findViewById(R.id.iv_exit_zoom);
        iv_exit_wheel = (ImageView) rootView.findViewById(R.id.iv_exit_wheel);

        wheel_view.setVisibility(View.INVISIBLE);
        tv_confirm.setVisibility(View.INVISIBLE);
        ll_content_circular.setVisibility(View.INVISIBLE);
        tv_result_zoom.setVisibility(View.INVISIBLE);
        iv_zoom_out.setVisibility(View.INVISIBLE);
        iv_exit_zoom.setVisibility(View.INVISIBLE);
        iv_exit_wheel.setVisibility(View.INVISIBLE);

        Typeface mitr_light = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Mitr-Light.ttf");
        Typeface mitr_regular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Mitr-Regular.ttf");
        tv_confirm.setTypeface(mitr_regular);
        tv_selected_type.setTypeface(mitr_regular);
        tv_selected_solution.setTypeface(mitr_regular);
        tv_selected_date.setTypeface(mitr_regular);
        tv_result.setTypeface(mitr_light);
        tv_result_zoom.setTypeface(mitr_light);

        slide_in = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_right_season);
        slide_out = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_right_season);

        bounce = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
        BounceInterpolator interpolator = new BounceInterpolator();
        bounce.setInterpolator(interpolator);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

//        ll_set_solution.setOnClickListener(showDialogSolution);
//        ll_set_type.setOnClickListener(showDialogType);
//        ll_set_date.setOnClickListener(ll_set_dateListener);
        iv_redo.setOnClickListener(iv_redoListener);
        tv_confirm.setOnClickListener(tv_confirmListener);
        iv_zoom.setOnClickListener(iv_zoomListener);
        wheel_view.setOnWheelAngleChangeListener(wheel_viewListener);
        iv_zoom_out.setOnClickListener(iv_zoom_outListener);
        fragment_circular.setOnTouchListener(swipeLeft);
        iv_exit_zoom.setOnClickListener(iv_exit_zoomListener);
        iv_exit_wheel.setOnClickListener(iv_exit_wheelListener);

        configSelected();

        tv_selected_type.setText(selectedOption.getSelectedType());
        tv_selected_solution.setText(selectedOption.getSelectedSolution());
        tv_selected_date.setText(selectedOption.getSelectedDate() + "");
        wheel_view.setWheelDrawable(R.drawable.wheel);
        wheel_view.setAngle(360 - getCurrentDate());
        iv_circular.setRotation(360 - getCurrentDate());

        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                ll_content_circular.setVisibility(View.VISIBLE);
                circular_progress_bar.setVisibility(View.INVISIBLE);
                Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.up_from_buttom);
                ll_content_circular.startAnimation(anim);
            }
        }, secondsDelayed * 500);

        setCircleImage();
        if (selectedOption.getSelectedPlantId() == 1) {
            profile_image_plant.setImageResource(R.drawable.rice);
        } else if (selectedOption.getSelectedPlantId() == 2) {
            profile_image_plant.setImageResource(R.drawable.corn);
        }
        showResult();
    }

    /************************
     * ADD-ON FUNCTION
     ***********************/
    private void showResult() {
        if (EventName.length > 0) {
            if (selectedOption.getSelectedSolutionDuration() == 20 && selectedOption.getSelectedPlantId() == 1) {
                if (EventName[0].equals("ไม่แนะนำให้ปลูก")) {
                    ll_up_field.setBackgroundResource(R.drawable.background_rec_gadiant_red_orange);
                    tv_result.setBackgroundResource(R.drawable.text_box_red_circular);
                    tv_result_zoom.setBackgroundResource(R.drawable.box_red);
                    tv_result.setText(Html.fromHtml("<b>เก็บเกี่ยว : </b>" + "<b>" + selectedOption.getSelectedDateEnd() + "</b>" + " "
                            + "<b>ระยะเวลาปลูก : </b>" + "<b>" + (selectedOption.getSelectedSolutionDuration() + selectedOption.getSelectedTypeDuration()) + " วัน" + "</b>" + "<br>"
                            + "<b>คำแนะนำ : </b>" + "<font color='#F44336'>" + "<b>" + EventName[0] + "</b>" + "</font>" + "<br>"
                            + "<b>รายละเอียด : </b>" + EventDesc[0] + "<br>"
                            + "<b>หมายเหตุ : </b>ปลูกข้าวในช่วงฤดูหนาว 20 กันยายน - 1 ธันวาคม ทำให้ใช้ระยะเวลาปลูกมากขึ้น"));
                    tv_result_zoom.setText(Html.fromHtml("<b>เก็บเกี่ยว : </b>" + "<b>" + selectedOption.getSelectedDateEnd() + "</b>" + "<br>"
                            + "<b>ระยะเวลาปลูก : </b>" + "<b>" + (selectedOption.getSelectedSolutionDuration() + selectedOption.getSelectedTypeDuration()) + " วัน" + "</b>" + "<br>"
                            + "<b>คำแนะนำ : </b>" + "<font color='#F44336'>" + "<b>" + EventName[0] + "</b>" + "</font>" + "<br>"
                            + "<b>รายละเอียด : </b>" + EventDesc[0] + "<br>"
                            + "<b>หมายเหตุ : </b>ปลูกข้าวในช่วงฤดูหนาว 20 กันยายน - 1 ธันวาคม ทำให้ใช้ระยะเวลาปลูกมากขึ้น"));
                } else if (EventName[0].equals("แนะนำให้ปลูก")) {
                    ll_up_field.setBackgroundResource(R.drawable.background_rec_gadiant_green_green);
                    tv_result.setBackgroundResource(R.drawable.text_box_green_circular);
                    tv_result_zoom.setBackgroundResource(R.drawable.box_green);
                    tv_result.setText(Html.fromHtml("<b>เก็บเกี่ยว : </b>" + "<b>" + selectedOption.getSelectedDateEnd() + "</b>" + " "
                            + "<b>ระยะเวลาปลูก : </b>" + "<b>" + (selectedOption.getSelectedSolutionDuration() + selectedOption.getSelectedTypeDuration()) + " วัน" + "</b>" + "<br>"
                            + "<b>คำแนะนำ : </b>" + "<font color='#8BC34A'>" + "<b>" + EventName[0] + "</b>" + "</font>" + "<br>"
                            + "<b>รายละเอียด : </b>" + EventDesc[0] + "<br>"
                            + "<b>หมายเหตุ : </b>ปลูกข้าวในช่วงฤดูหนาว 20 กันยายน - 1 ธันวาคม ทำให้ใช้ระยะเวลาปลูกมากขึ้น"));
                    tv_result_zoom.setText(Html.fromHtml("เก็บเกี่ยว : " + "<b>" + selectedOption.getSelectedDateEnd() + "</b>" + "<br>"
                            + "<b>ระยะเวลาปลูก : </b>" + "<b>" + (selectedOption.getSelectedSolutionDuration() + selectedOption.getSelectedTypeDuration()) + " วัน" + "</b>" + "<br>"
                            + "<b>คำแนะนำ : </b>" + "<font color='#8BC34A'>" + "<b>" + EventName[0] + "</b>" + "</font>" + "<br>"
                            + "<b>รายละเอียด : </b>" + EventDesc[0] + "<br>"
                            + "<b>หมายเหตุ : </b>ปลูกข้าวในช่วงฤดูหนาว 20 กันยายน - 1 ธันวาคม ทำให้ใช้ระยะเวลาปลูกมากขึ้น"));
                } else {
                    ll_up_field.setBackgroundResource(R.drawable.background_rec_gadiant_yellow_orange);
                    tv_result.setBackgroundResource(R.drawable.text_box_yellow_circular);
                    tv_result_zoom.setBackgroundResource(R.drawable.box_yellow);
                    tv_result.setText(Html.fromHtml("<b>เก็บเกี่ยว : </b>" + "<b>" + selectedOption.getSelectedDateEnd() + "</b>" + " "
                            + "<b>ระยะเวลาปลูก : </b>" + "<b>" + (selectedOption.getSelectedSolutionDuration() + selectedOption.getSelectedTypeDuration()) + " วัน" + "</b>" + "<br>"
                            + "<b>คำแนะนำ : </b>" + "<font color='#F9A825'>" + "<b>" + EventName[0] + "</b>" + "</font>" + "<br>"
                            + "<b>รายละเอียด : </b>" + EventDesc[0] + "<br>"
                            + "<b>หมายเหตุ : </b>ปลูกข้าวในช่วงฤดูหนาว 20 กันยายน - 1 ธันวาคม ทำให้ใช้ระยะเวลาปลูกมากขึ้น"));
                    tv_result_zoom.setText(Html.fromHtml("<b>เก็บเกี่ยว : </b>" + "<b>" + selectedOption.getSelectedDateEnd() + "</b>" + "<br>"
                            + "<b>ระยะเวลาปลูก : </b>" + "<b>" + (selectedOption.getSelectedSolutionDuration() + selectedOption.getSelectedTypeDuration()) + " วัน" + "</b>" + "<br>"
                            + "<b>คำแนะนำ : </b>" + "<font color='#F9A825'>" + "<b>" + EventName[0] + "</b>" + "</font>" + "<br>"
                            + "<b>รายละเอียด : </b>" + EventDesc[0] + "<br>"
                            + "<b>หมายเหตุ : </b>ปลูกข้าวในช่วงฤดูหนาว 20 กันยายน - 1 ธันวาคม ทำให้ใช้ระยะเวลาปลูกมากขึ้น"));
                }
            } else {
                if (EventName[0].equals("ไม่แนะนำให้ปลูก")) {
                    ll_up_field.setBackgroundResource(R.drawable.background_rec_gadiant_red_orange);
                    tv_result.setBackgroundResource(R.drawable.text_box_red_circular);
                    tv_result_zoom.setBackgroundResource(R.drawable.box_red);
                    tv_result.setText(Html.fromHtml("<b>เก็บเกี่ยว : </b>" + "<b>" + selectedOption.getSelectedDateEnd() + "</b>" + " "
                            + "<b>ระยะเวลาปลูก : </b>" + "<b>" + (selectedOption.getSelectedSolutionDuration() + selectedOption.getSelectedTypeDuration()) + " วัน" + "</b>" + "<br>"
                            + "<b>คำแนะนำ : </b>" + "<font color='#F44336'>" + "<b>" + EventName[0] + "</b>" + "</font>" + "<br>"
                            + "<b>รายละเอียด : </b>" + EventDesc[0]));
                    tv_result_zoom.setText(Html.fromHtml("<b>เก็บเกี่ยว : </b>" + "<b>" + selectedOption.getSelectedDateEnd() + "</b>" + "<br>"
                            + "<b>ระยะเวลาปลูก : </b>" + "<b>" + (selectedOption.getSelectedSolutionDuration() + selectedOption.getSelectedTypeDuration()) + " วัน" + "</b>" + "<br>"
                            + "<b>คำแนะนำ : </b>" + "<font color='#F44336'>" + "<b>" + EventName[0] + "</b>" + "</font>" + "<br>"
                            + "<b>รายละเอียด : </b>" + EventDesc[0]));
                } else if (EventName[0].equals("แนะนำให้ปลูก")) {
                    ll_up_field.setBackgroundResource(R.drawable.background_rec_gadiant_green_green);
                    tv_result.setBackgroundResource(R.drawable.text_box_green_circular);
                    tv_result_zoom.setBackgroundResource(R.drawable.box_green);
                    tv_result.setText(Html.fromHtml("<b>เก็บเกี่ยว : </b>" + "<b>" + selectedOption.getSelectedDateEnd() + "</b>" + " "
                            + "<b>ระยะเวลาปลูก : </b>" + "<b>" + (selectedOption.getSelectedSolutionDuration() + selectedOption.getSelectedTypeDuration()) + " วัน" + "</b>" + "<br>"
                            + "<b>คำแนะนำ : </b>" + "<font color='#8BC34A'>" + "<b>" + EventName[0] + "</b>" + "</font>" + "<br>"
                            + "<b>รายละเอียด : </b>" + EventDesc[0]));
                    tv_result_zoom.setText(Html.fromHtml("<b>เก็บเกี่ยว : </b>" + "<b>" + selectedOption.getSelectedDateEnd() + "</b>" + "<br>"
                            + "<b>ระยะเวลาปลูก : </b>" + "<b>" + (selectedOption.getSelectedSolutionDuration() + selectedOption.getSelectedTypeDuration()) + " วัน" + "</b>" + "<br>"
                            + "<b>คำแนะนำ : </b>" + "<font color='#8BC34A'>" + "<b>" + EventName[0] + "</b>" + "</font>" + "<br>"
                            + "<b>รายละเอียด : </b>" + EventDesc[0]));
                } else {
                    ll_up_field.setBackgroundResource(R.drawable.background_rec_gadiant_yellow_orange);
                    tv_result.setBackgroundResource(R.drawable.text_box_yellow_circular);
                    tv_result_zoom.setBackgroundResource(R.drawable.box_yellow);
                    tv_result.setText(Html.fromHtml("<b>เก็บเกี่ยว : </b>" + "<b>" + selectedOption.getSelectedDateEnd() + "</b>" + " "
                            + "<b>ระยะเวลาปลูก : </b>" + "<b>" + (selectedOption.getSelectedSolutionDuration() + selectedOption.getSelectedTypeDuration()) + " วัน" + "</b>" + "<br>"
                            + "<b>คำแนะนำ : </b>" + "<font color='#F9A825'>" + "<b>" + EventName[0] + "</b>" + "</font>" + "<br>"
                            + "<b>รายละเอียด : </b>" + EventDesc[0]));
                    tv_result_zoom.setText(Html.fromHtml("<b>เก็บเกี่ยว : </b>" + "<b>" + selectedOption.getSelectedDateEnd() + "</b>" + "<br>"
                            + "<b>ระยะเวลาปลูก : </b>" + "<b>" + (selectedOption.getSelectedSolutionDuration() + selectedOption.getSelectedTypeDuration()) + " วัน" + "</b>" + "<br>"
                            + "<b>คำแนะนำ : </b>" + "<font color='#F9A825'>" + "<b>" + EventName[0] + "</b>" + "</font>" + "<br>"
                            + "<b>รายละเอียด : </b>" + EventDesc[0]));
                }
            }
        }
    }

    private void onConfigMonth(int position, int day) {
        if (position + 1 == 1) {
            tv_selected_date.setText(day + " " + "มกราคม");
        } else if (position + 1 == 2) {
            tv_selected_date.setText(day + " " + "กุมภาพันธ์");
        } else if (position + 1 == 3) {
            tv_selected_date.setText(day + " " + "มีนาคม");
        } else if (position + 1 == 4) {
            tv_selected_date.setText(day + " " + "เมษายน");
        } else if (position + 1 == 5) {
            tv_selected_date.setText(day + " " + "พฤษภาคม");
        } else if (position + 1 == 6) {
            tv_selected_date.setText(day + " " + "มิถุนายน");
        } else if (position + 1 == 7) {
            tv_selected_date.setText(day + " " + "กรกฎาคม");
        } else if (position + 1 == 8) {
            tv_selected_date.setText(day + " " + "สิงหาคม");
        } else if (position + 1 == 9) {
            tv_selected_date.setText(day + " " + "กันยายน");
        } else if (position + 1 == 10) {
            tv_selected_date.setText(day + " " + "ตุลาคม");
        } else if (position + 1 == 11) {
            tv_selected_date.setText(day + " " + "พฤศจิกายน");
        } else if (position + 1 == 12) {
            tv_selected_date.setText(day + " " + "ธันวาคม");
        } else {
            tv_selected_date.setText("OutOfMemory!");
        }
    }

    private void configSelected() {
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

        List<Event> listEvent = databaseHelper.getEvent(selectedOption.getSelectedDateEndInteger(), selectedOption.getSelectedPlantId());
        EventName = new String[listEvent.size()];
        EventDesc = new String[listEvent.size()];
        for (int i = 0; i < listEvent.size(); i++) {
            EventName[i] = listEvent.get(i).getE_name();
            EventDesc[i] = listEvent.get(i).getE_desc();
        }
    }

    private int getCurrentDate() {
        if (selectedOption.getSelectedDateEndInteger() != null) {

            monthOfYear = (Integer.parseInt(selectedOption.getSelectedDateEndInteger().substring(5, 7).toString())) - 1;
            dayOfMonth = Integer.parseInt(selectedOption.getSelectedDateEndInteger().substring(8, 10).toString());

            if (monthOfYear + 1 <= 2) {
                if (dayOfMonth == 31)
                    dayOfMonth = 30;
                current_date = ((monthOfYear * 30) + dayOfMonth);
            } else if (monthOfYear + 1 <= 7) {
                if (dayOfMonth == 31)
                    dayOfMonth = 30;
                current_date = ((monthOfYear * 30) + dayOfMonth - 2);
            } else if (monthOfYear + 1 == 8) {
                current_date = ((monthOfYear * 30) + dayOfMonth - 2);
            } else if (monthOfYear + 1 <= 11) {
                if (dayOfMonth == 31)
                    dayOfMonth = 30;
                current_date = ((monthOfYear * 30) + dayOfMonth - 1);
            } else if (monthOfYear + 1 == 12) {
                current_date = ((monthOfYear * 30) + dayOfMonth - 1);
            }

            return current_date;
        }
        return 0;
    }

    private void processWheel(int duration) {
        if (duration >= 0 && duration <= 29) {
            tv_confirm.setText("วันที่เก็บเกี่ยว : " + (duration + 1) + " มกราคม");
            selectedOption.setSelectedDateEnd((duration + 1) + " มกราคม");
            if (duration + 1 < 10) {
                selectedOption.setSelectedDateEndInteger("0000-01" + "-0" + duration + 1);
            } else {
                selectedOption.setSelectedDateEndInteger("0000-01" + "-" + duration + 1);
            }
        } else if (duration >= 30 && duration <= 57) {
            int date = duration - 29;
            tv_confirm.setText("วันที่เก็บเกี่ยว : " + (date) + " กุมภาพันธ์");
            selectedOption.setSelectedDateEnd((date) + " กุมภาพันธ์");
            if (date < 10) {
                selectedOption.setSelectedDateEndInteger("0000-02" + "-0" + date);
            } else {
                selectedOption.setSelectedDateEndInteger("0000-02" + "-" + date);
            }
        } else if (duration >= 58 && duration <= 87) {
            int date = duration - 57;
            tv_confirm.setText("วันที่เก็บเกี่ยว : " + (date) + " มีนาคม");
            selectedOption.setSelectedDateEnd((date) + " มีนาคม");
            if (date < 10) {
                selectedOption.setSelectedDateEndInteger("0000-03" + "-0" + date);
            } else {
                selectedOption.setSelectedDateEndInteger("0000-03" + "-" + date);
            }
        } else if (duration >= 88 && duration <= 117) {
            int date = duration - 87;
            tv_confirm.setText("วันที่เก็บเกี่ยว : " + (date) + " เมษายน");
            selectedOption.setSelectedDateEnd((date) + " เมษายน");
            if (date < 10) {
                selectedOption.setSelectedDateEndInteger("0000-04" + "-0" + date);
            } else {
                selectedOption.setSelectedDateEndInteger("0000-04" + "-" + date);
            }
        } else if (duration >= 118 && duration <= 147) {
            int date = duration - 117;
            tv_confirm.setText("วันที่เก็บเกี่ยว : " + (date) + " พฤษภาคม");
            selectedOption.setSelectedDateEnd((date) + " พฤษภาคม");
            if (date < 10) {
                selectedOption.setSelectedDateEndInteger("0000-05" + "-0" + date);
            } else {
                selectedOption.setSelectedDateEndInteger("0000-05" + "-" + date);
            }
        } else if (duration >= 148 && duration <= 177) {
            int date = duration - 147;
            tv_confirm.setText("วันที่เก็บเกี่ยว : " + (date) + " มิถุนายน");
            selectedOption.setSelectedDateEnd((date) + " มิถุนายน");
            if (date < 10) {
                selectedOption.setSelectedDateEndInteger("0000-06" + "-0" + date);
            } else {
                selectedOption.setSelectedDateEndInteger("0000-06" + "-" + date);
            }
        } else if (duration >= 178 && duration <= 207) {
            int date = duration - 177;
            tv_confirm.setText("วันที่เก็บเกี่ยว : " + (date) + " กรกฎาคม");
            selectedOption.setSelectedDateEnd((date) + " กรกฎาคม");
            if (date < 10) {
                selectedOption.setSelectedDateEndInteger("0000-07" + "-0" + date);
            } else {
                selectedOption.setSelectedDateEndInteger("0000-07" + "-" + date);
            }
        } else if (duration >= 208 && duration <= 238) {
            int date = duration - 207;
            tv_confirm.setText("วันที่เก็บเกี่ยว : " + (date) + " สิงหาคม");
            selectedOption.setSelectedDateEnd((date) + " สิงหาคม");
            if (date < 10) {
                selectedOption.setSelectedDateEndInteger("0000-08" + "-0" + date);
            } else {
                selectedOption.setSelectedDateEndInteger("0000-08" + "-" + date);
            }
        } else if (duration >= 239 && duration <= 268) {
            int date = duration - 238;
            tv_confirm.setText("วันที่เก็บเกี่ยว : " + (date) + " กันยายน");
            selectedOption.setSelectedDateEnd((date) + " กันยายน");
            if (date < 10) {
                selectedOption.setSelectedDateEndInteger("0000-09" + "-0" + date);
            } else {
                selectedOption.setSelectedDateEndInteger("0000-09" + "-" + date);
            }
        } else if (duration >= 269 && duration <= 298) {
            int date = duration - 268;
            tv_confirm.setText("วันที่เก็บเกี่ยว : " + (date) + " ตุลาคม");
            selectedOption.setSelectedDateEnd((date) + " ตุลาคม");
            if (date < 10) {
                selectedOption.setSelectedDateEndInteger("0000-10" + "-0" + date);
            } else {
                selectedOption.setSelectedDateEndInteger("0000-10" + "-" + date);
            }
        } else if (duration >= 299 && duration <= 328) {
            int date = duration - 298;
            tv_confirm.setText("วันที่เก็บเกี่ยว : " + (date) + " พฤศจิกายน");
            selectedOption.setSelectedDateEnd((date) + " พฤศจิกายน");
            if (date < 10) {
                selectedOption.setSelectedDateEndInteger("0000-11" + "-0" + date);
            } else {
                selectedOption.setSelectedDateEndInteger("0000-11" + "-" + date);
            }
        } else if (duration >= 329 && duration <= 360) {
            int date = duration - 328;
            tv_confirm.setText("วันที่เก็บเกี่ยว : " + (date) + " ธันวาคม");
            selectedOption.setSelectedDateEnd((date) + " ธันวาคม");
            if (date < 10) {
                selectedOption.setSelectedDateEndInteger("0000-12" + "-0" + date);
            } else {
                selectedOption.setSelectedDateEndInteger("0000-12" + "-" + date);
            }
        }
    }

    private void setSelected(int duration) {
        duration = duration - selectedOption.getSelectedSolutionDuration() - selectedOption.getSelectedTypeDuration() + 2;
        if (duration <= -122 && duration >= -152) {
            int date = duration + 121;
            tv_selected_date.setText((32 + date) + " สิงหาคม");
            selectedOption.setSelectedDate((32 + date) + " สิงหาคม");
            selectedOption.setSelectedSeekDay(32 + date);
            selectedOption.setSelectedSeekMonth(8);
        } else if (duration <= -92 && duration >= -121) {
            int date = duration + 91;
            tv_selected_date.setText((31 + date) + " กันยายน");
            selectedOption.setSelectedDate((31 + date) + " กันยายน");
            selectedOption.setSelectedSeekDay(31 + date);
            selectedOption.setSelectedSeekMonth(9);
        } else if (duration <= -62 && duration >= -91) {
            int date = duration + 61;
            tv_selected_date.setText((31 + date) + " ตุลาคม");
            selectedOption.setSelectedDate((31 + date) + " ตุลาคม");
            selectedOption.setSelectedSeekDay(31 + date);
            selectedOption.setSelectedSeekMonth(10);
        } else if (duration <= -32 && duration >= -61) {
            int date = duration + 31;
            tv_selected_date.setText((31 + date) + " พฤศจิกายน");
            selectedOption.setSelectedDate((31 + date) + " พฤศจิกายน");
            selectedOption.setSelectedSeekDay(31 + date);
            selectedOption.setSelectedSeekMonth(11);
        } else if (duration <= -1 && duration >= -31) {
            tv_selected_date.setText((32 + duration) + " ธันวาคม");
            selectedOption.setSelectedDate((32 + duration) + " ธันวาคม");
            selectedOption.setSelectedSeekDay(32 + duration);
            selectedOption.setSelectedSeekMonth(12);
        } else if (duration >= 0 && duration <= 29) {
            tv_selected_date.setText((duration) + " มกราคม");
            selectedOption.setSelectedDate(duration + " มกราคม");
            selectedOption.setSelectedSeekDay(duration);
            selectedOption.setSelectedSeekMonth(1);
        } else if (duration >= 30 && duration <= 57) {
            int date = duration - 29;
            tv_selected_date.setText((date) + " กุมภาพันธ์");
            selectedOption.setSelectedDate(date + " กุมภาพันธุ์");
            selectedOption.setSelectedSeekDay(date);
            selectedOption.setSelectedSeekMonth(2);
        } else if (duration >= 58 && duration <= 87) {
            int date = duration - 57;
            tv_selected_date.setText((date) + " มีนาคม");
            selectedOption.setSelectedDate(date + " มีนาคม");
            selectedOption.setSelectedSeekDay(date);
            selectedOption.setSelectedSeekMonth(3);
        } else if (duration >= 88 && duration <= 117) {
            int date = duration - 87;
            tv_selected_date.setText((date) + " เมษายน");
            selectedOption.setSelectedDate(date + " เมษายน");
            selectedOption.setSelectedSeekDay(date);
            selectedOption.setSelectedSeekMonth(4);
        } else if (duration >= 118 && duration <= 147) {
            int date = duration - 117;
            tv_selected_date.setText((date) + " พฤษภาคม");
            selectedOption.setSelectedDate(date + " พฤษภาคม");
            selectedOption.setSelectedSeekDay(date);
            selectedOption.setSelectedSeekMonth(5);
        } else if (duration >= 148 && duration <= 177) {
            int date = duration - 147;
            tv_selected_date.setText((date) + " มิถุนายน");
            selectedOption.setSelectedDate(date + " มิถุนายน");
            selectedOption.setSelectedSeekDay(date);
            selectedOption.setSelectedSeekMonth(6);
        } else if (duration >= 178 && duration <= 207) {
            int date = duration - 177;
            tv_selected_date.setText((date) + " กรกฎาคม");
            selectedOption.setSelectedDate(date + " กรกฎาคม");
            selectedOption.setSelectedSeekDay(date);
            selectedOption.setSelectedSeekMonth(7);
        } else if (duration >= 208 && duration <= 238) {
            int date = duration - 207;
            tv_selected_date.setText((date) + " สิงหาคม");
            selectedOption.setSelectedDate(date + " สิงหาคม");
            selectedOption.setSelectedSeekDay(date);
            selectedOption.setSelectedSeekMonth(8);
        } else if (duration >= 239 && duration <= 268) {
            int date = duration - 238;
            tv_selected_date.setText((date) + " กันยายน");
            selectedOption.setSelectedDate(date + " กันยายน");
            selectedOption.setSelectedSeekDay(date);
            selectedOption.setSelectedSeekMonth(9);
        } else if (duration >= 269 && duration <= 298) {
            int date = duration - 268;
            tv_selected_date.setText((date) + " ตุลาคม");
            selectedOption.setSelectedDate(date + " ตุลาคม");
            selectedOption.setSelectedSeekDay(date);
            selectedOption.setSelectedSeekMonth(10);
        } else if (duration >= 299 && duration <= 328) {
            int date = duration - 298;
            tv_selected_date.setText((date) + " พฤศจิกายน");
            selectedOption.setSelectedDate(date + " พฤศจิกายน");
            selectedOption.setSelectedSeekDay(date);
            selectedOption.setSelectedSeekMonth(11);
        } else if (duration >= 329 && duration <= 359) {
            int date = duration - 328;
            tv_selected_date.setText((date) + " ธันวาคม");
            selectedOption.setSelectedDate(date + " ธันวาคม");
            selectedOption.setSelectedSeekDay(date);
            selectedOption.setSelectedSeekMonth(12);
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

    private void setProcessesDuration() {
        if (selectedOption.getSelectedSeekMonth() == 9 && selectedOption.getSelectedSeekDay() >= 20) {
            selectedOption.setSelectedSolutionDuration(20);
        } else if (selectedOption.getSelectedSeekMonth() == 10 && selectedOption.getSelectedSeekDay() >= 1) {
            selectedOption.setSelectedSolutionDuration(20);
        } else if (selectedOption.getSelectedSeekMonth() == 11 && selectedOption.getSelectedSeekDay() >= 1) {
            selectedOption.setSelectedSolutionDuration(20);
        } else if (selectedOption.getSelectedSeekMonth() == 12 && selectedOption.getSelectedSeekDay() == 1) {
            selectedOption.setSelectedSolutionDuration(20);
        } else {
            selectedOption.setSelectedSolutionDuration(processesDuration[selectedOption.getSelectedSolutionId() - 1]);
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
//    View.OnClickListener showDialogSolution = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            ll_set_solution.startAnimation(bounce);
//            MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext());
//            builder
//                    .title("เลือกวิธีปลูก")
//                    .items(processesName)
//                    .typeface("Mitr-Regular.ttf", "Mitr-Regular.ttf")
//                    .itemsCallbackSingleChoice(selectedOption.getSelectedSolutionId() - 1, new MaterialDialog.ListCallbackSingleChoice() {
//                        @Override
//                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
//                            /**
//                             * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
//                             * returning false here won't allow the newly selected radio button to actually be selected.
//                             **/
//                            selectedOption.setSelectedSolution(processesName[which]);
//                            selectedOption.setSelectedSolutionDuration(processesDuration[which]);
//                            selectedOption.setSelectedSolutionId(processedId[which]);
//                            tv_selected_solution.setText(text);
//                            showResult();
//                            return true;
//                        }
//                    })
//                    .negativeText("ยกเลิก")
//                    .show();
//        }
//    };
//
//
//    View.OnClickListener showDialogType = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            ll_set_type.startAnimation(bounce);
//            new MaterialDialog.Builder(getContext())
//                    .title("เลือกสายพันธุ์")
//                    .items(subPlantName)
//                    .typeface("Mitr-Regular.ttf", "Mitr-Regular.ttf")
//                    .itemsCallbackSingleChoice(selectedOption.getSelectedTypeId() - 1, new MaterialDialog.ListCallbackSingleChoice() {
//                        @Override
//                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
//                            /**
//                             * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
//                             * returning false here won't allow the newly selected radio button to actually be selected.
//                             **/
//                            selectedOption.setSelectedType(subPlantName[which]);
//                            selectedOption.setSelectedTypeDuration(subPlantDuration[which]);
//                            selectedOption.setSelectedTypeId(subPlantId[which]);
//                            tv_selected_type.setText(text);
//                            showResult();
//                            return true;
//                        }
//                    })
//                    .negativeText("ยกเลิก")
//                    .show();
//        }
//    };

    View.OnClickListener iv_zoomListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            iv_circular.setVisibility(View.INVISIBLE);
            tv_result.setVisibility(View.INVISIBLE);
            iv_zoom.setVisibility(View.INVISIBLE);
            iv_redo.setVisibility(View.INVISIBLE);
            tv_result_zoom.setVisibility(View.VISIBLE);
            iv_zoom_out.setVisibility(View.VISIBLE);
            iv_exit_zoom.setVisibility(View.VISIBLE);
            Animation zoom_out = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_in);
            tv_result_zoom.startAnimation(zoom_out);
        }
    };

    WheelView.OnWheelAngleChangeListener wheel_viewListener = new WheelView.OnWheelAngleChangeListener() {
        @Override
        public void onWheelAngleChange(float angle) {
            int i = (int) angle;
            if (angle < 0) {
                fixedDate = (Math.abs(Integer.parseInt(i % 360 + "")));
                processWheel(fixedDate - 1);
            } else if (angle > 0) {
                fixedDate = 360 - (Math.abs(Integer.parseInt(i % 360 + "")));
                processWheel(fixedDate - 1);
            }
            setSelected(fixedDate);
        }
    };

    View.OnClickListener iv_redoListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            iv_circular.setVisibility(View.INVISIBLE);
            tv_result.setVisibility(View.INVISIBLE);
            iv_zoom.setVisibility(View.INVISIBLE);
            iv_redo.setVisibility(View.INVISIBLE);
            wheel_view.setVisibility(View.VISIBLE);
            iv_exit_wheel.setVisibility(View.VISIBLE);
            Animation zoom_out = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_in);
            wheel_view.startAnimation(zoom_out);
            tv_confirm.setVisibility(View.VISIBLE);
        }
    };

    View.OnClickListener tv_confirmListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            tv_confirm.setVisibility(View.INVISIBLE);
            wheel_view.setVisibility(View.INVISIBLE);
            iv_exit_wheel.setVisibility(View.INVISIBLE);
            iv_circular.setVisibility(View.VISIBLE);
            iv_redo.setVisibility(View.VISIBLE);
            tv_result.setVisibility(View.VISIBLE);
            iv_zoom.setVisibility(View.VISIBLE);
            iv_circular.setRotation(360 - fixedDate);
            setCircleImage();
            configSelected();
            setProcessesDuration();
            showResult();
            Animation zoom_out = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_in);
            ll_content_circular.startAnimation(zoom_out);
        }
    };

//    View.OnClickListener ll_set_dateListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            ll_set_date.startAnimation(bounce);
//            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
//                    new DatePickerDialog.OnDateSetListener() {
//                        @Override
//                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//                            selectedOption.setSelectedSeekDay(day);
//                            onConfigMonth(month, day);
//                            selectedOption.setSelectedSeekMonth(month + 1);
//                        }
//                    }, year, selectedOption.getSelectedSeekMonth() - 1, selectedOption.getSelectedSeekDay());
//            datePickerDialog.show();
//        }
//    };

    OnSwipeTouchListener swipeLeft = new OnSwipeTouchListener(getContext()) {
        public void onSwipeLeft() {
            CircularFragmentListener circularFragmentListener = (CircularFragmentListener) getActivity();
            circularFragmentListener.returnHome();
        }
    };

    View.OnClickListener iv_zoom_outListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            tv_result_zoom.setVisibility(View.INVISIBLE);
            iv_zoom_out.setVisibility(View.INVISIBLE);
            iv_exit_zoom.setVisibility(View.INVISIBLE);
            iv_circular.setVisibility(View.VISIBLE);
            tv_result.setVisibility(View.VISIBLE);
            iv_zoom.setVisibility(View.VISIBLE);
            iv_redo.setVisibility(View.VISIBLE);
            Animation zoom_out = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_in);
            ll_content_circular.startAnimation(zoom_out);
        }
    };

    View.OnClickListener iv_exit_wheelListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            tv_confirm.setVisibility(View.INVISIBLE);
            wheel_view.setVisibility(View.INVISIBLE);
            iv_exit_wheel.setVisibility(View.INVISIBLE);
            iv_circular.setVisibility(View.VISIBLE);
            iv_redo.setVisibility(View.VISIBLE);
            tv_result.setVisibility(View.VISIBLE);
            iv_zoom.setVisibility(View.VISIBLE);
            iv_circular.setRotation(360 - fixedDate);
            setCircleImage();
            configSelected();
            setProcessesDuration();
            showResult();
            Animation zoom_out = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_in);
            ll_content_circular.startAnimation(zoom_out);
        }
    };

    View.OnClickListener iv_exit_zoomListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            tv_result_zoom.setVisibility(View.INVISIBLE);
            iv_zoom_out.setVisibility(View.INVISIBLE);
            iv_exit_zoom.setVisibility(View.INVISIBLE);
            iv_circular.setVisibility(View.VISIBLE);
            tv_result.setVisibility(View.VISIBLE);
            iv_zoom.setVisibility(View.VISIBLE);
            iv_redo.setVisibility(View.VISIBLE);
            Animation zoom_out = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_in);
            ll_content_circular.startAnimation(zoom_out);
        }
    };
}
