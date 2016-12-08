package th.or.nectec.zoning.topfarm.manager;

import android.content.Context;
import android.os.Bundle;

import com.inthecheesefactory.thecheeselibrary.manager.Contextor;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class SelectedOption {

    private int selectedPlantId;
    private int selectedSeekDay;
    private int selectedSeekMonth;
    private int selectedTypeDuration;
    private int selectedSolutionDuration;
    private int selectedTypeId;
    private int selectedSolutionId;
    private String selectedPlantName;
    private String selectedType;
    private String selectedSolution;
    private String selectedDate;
    private String selectedDateEnd;
    private String separateDate;
    private String selectedDateEndInteger;

    private static SelectedOption instance;

    public static SelectedOption getInstance() {
        if (instance == null)
            instance = new SelectedOption();
        return instance;
    }

    private Context mContext;

    private SelectedOption() {
        mContext = Contextor.getInstance().getContext();
    }

    public int getSelectedTypeDuration() {
        return selectedTypeDuration;
    }

    public void setSelectedTypeDuration(int selectedTypeDuration) {
        this.selectedTypeDuration = selectedTypeDuration;
    }

    public int getSelectedSolutionDuration() {
        return selectedSolutionDuration;
    }

    public void setSelectedSolutionDuration(int selectedSolutionDuration) {
        this.selectedSolutionDuration = selectedSolutionDuration;
    }

    public int getSelectedTypeId() {
        return selectedTypeId;
    }

    public void setSelectedTypeId(int selectedTypeId) {
        this.selectedTypeId = selectedTypeId;
    }

    public int getSelectedSolutionId() {
        return selectedSolutionId;
    }

    public void setSelectedSolutionId(int selectedSolutionId) {
        this.selectedSolutionId = selectedSolutionId;
    }

    public String getSelectedType() {
        return selectedType;
    }

    public void setSelectedType(String selectedType) {
        this.selectedType = selectedType;
    }

    public String getSelectedSolution() {
        return selectedSolution;
    }

    public void setSelectedSolution(String selectedSolution) {
        this.selectedSolution = selectedSolution;
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    public String getSelectedDateEnd() {
        return selectedDateEnd;
    }

    public void setSelectedDateEnd(String selectedDateEnd) {
        this.selectedDateEnd = selectedDateEnd;
    }

    public int getSelectedSeekDay() {
        return selectedSeekDay;
    }

    public void setSelectedSeekDay(int selectedSeekDay) {
        this.selectedSeekDay = selectedSeekDay;
    }

    public int getSelectedSeekMonth() {
        return selectedSeekMonth;
    }

    public void setSelectedSeekMonth(int selectedSeekMonth) {
        this.selectedSeekMonth = selectedSeekMonth;
    }

    public String getSeparateDate() {
        return separateDate;
    }

    public void setSeparateDate(String separateDate) {
        this.separateDate = separateDate;
    }

    public String getSelectedDateEndInteger() {
        return selectedDateEndInteger;
    }

    public void setSelectedDateEndInteger(String selectedDateEndInteger) {
        this.selectedDateEndInteger = selectedDateEndInteger;
    }

    public int getSelectedPlantId() {
        return selectedPlantId;
    }

    public void setSelectedPlantId(int selectedPlantId) {
        this.selectedPlantId = selectedPlantId;
    }

    public String getSelectedPlantName() {
        return selectedPlantName;
    }

    public void setSelectedPlantName(String selectedPlantName) {
        this.selectedPlantName = selectedPlantName;
    }

    public Bundle onSaveInstanceState(){
        Bundle bundle = new Bundle();
        bundle.putInt("selectedPlantId", selectedPlantId);
        bundle.putInt("selectedSeekDay", selectedSeekDay);
        bundle.putInt("selectedSeekMonth", selectedSeekMonth);
        bundle.putInt("selectedTypeDuration", selectedTypeDuration);
        bundle.putInt("selectedSolutionDuration", selectedSolutionDuration);
        bundle.putInt("selectedTypeId", selectedTypeId);
        bundle.putInt("selectedSolutionId", selectedSolutionId);
        bundle.putString("selectedPlantName", selectedPlantName);
        bundle.putString("selectedType", selectedType);
        bundle.putString("selectedSolution", selectedSolution);
        bundle.putString("selectedDate", selectedDate);
        bundle.putString("selectedDateEnd", selectedDateEnd);
        bundle.putString("separateDate", separateDate);
        bundle.putString("selectedDateEndInteger", selectedDateEndInteger);
        return bundle;
    }

    public void onRestoreInstanceState(Bundle saveInstanceState){
        saveInstanceState.getInt("selectedPlantId");
        saveInstanceState.getInt("selectedSeekDay");
        saveInstanceState.getInt("selectedSeekMonth");
        saveInstanceState.getInt("selectedTypeDuration");
        saveInstanceState.getInt("selectedSolutionDuration");
        saveInstanceState.getInt("selectedTypeId");
        saveInstanceState.getInt("selectedSolutionId");
        saveInstanceState.getString("selectedPlantName");
        saveInstanceState.getString("selectedType");
        saveInstanceState.getString("selectedSolution");
        saveInstanceState.getString("selectedDate");
        saveInstanceState.getString("selectedDateEnd");
        saveInstanceState.getString("separateDate");
        saveInstanceState.getString("selectedDateEndInteger");
    }
}
