package th.or.nectec.zoning.topfarm.datatype;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by topyttac on 9/26/2016 AD.
 */
public class SubPlant implements Parcelable{
    private int sp_id;
    private String sp_name;
    private int sp_duration;
    private int p_id;

    public SubPlant(){
        super();
    }

    public SubPlant(int sp_id, String sp_name, int sp_duration, int p_id) {
        this.sp_id = sp_id;
        this.sp_name = sp_name;
        this.sp_duration = sp_duration;
        this.p_id = p_id;
    }

    protected SubPlant(Parcel in) {
        sp_id = in.readInt();
        sp_name = in.readString();
        sp_duration = in.readInt();
        p_id = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(sp_id);
        dest.writeString(sp_name);
        dest.writeInt(sp_duration);
        dest.writeInt(p_id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SubPlant> CREATOR = new Creator<SubPlant>() {
        @Override
        public SubPlant createFromParcel(Parcel in) {
            return new SubPlant(in);
        }

        @Override
        public SubPlant[] newArray(int size) {
            return new SubPlant[size];
        }
    };

    public int getSp_id() {
        return sp_id;
    }

    public void setSp_id(int sp_id) {
        this.sp_id = sp_id;
    }

    public String getSp_name() {
        return sp_name;
    }

    public void setSp_name(String sp_name) {
        this.sp_name = sp_name;
    }

    public int getSp_duration() {
        return sp_duration;
    }

    public void setSp_duration(int sp_duration) {
        this.sp_duration = sp_duration;
    }

    public int getP_id() {
        return p_id;
    }

    public void setP_id(int p_id) {
        this.p_id = p_id;
    }
}
