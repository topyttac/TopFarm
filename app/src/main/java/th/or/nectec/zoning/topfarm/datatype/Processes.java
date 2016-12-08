package th.or.nectec.zoning.topfarm.datatype;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by topyttac on 9/26/2016 AD.
 */
public class Processes implements Parcelable{
    private int pc_id;
    private String pc_name;
    private int pc_duration;
    private int p_id;

    public Processes(){
        super();
    }

    public Processes(int pc_id, String pc_name, int pc_duration, int p_id) {
        this.pc_id = pc_id;
        this.pc_name = pc_name;
        this.pc_duration = pc_duration;
        this.p_id = p_id;
    }

    protected Processes(Parcel in) {
        pc_id = in.readInt();
        pc_name = in.readString();
        pc_duration = in.readInt();
        p_id = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(pc_id);
        dest.writeString(pc_name);
        dest.writeInt(pc_duration);
        dest.writeInt(p_id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Processes> CREATOR = new Creator<Processes>() {
        @Override
        public Processes createFromParcel(Parcel in) {
            return new Processes(in);
        }

        @Override
        public Processes[] newArray(int size) {
            return new Processes[size];
        }
    };

    public int getPc_id() {
        return pc_id;
    }

    public void setPc_id(int pc_id) {
        this.pc_id = pc_id;
    }

    public String getPc_name() {
        return pc_name;
    }

    public void setPc_name(String pc_name) {
        this.pc_name = pc_name;
    }

    public int getPc_duration() {
        return pc_duration;
    }

    public void setPc_duration(int pc_duration) {
        this.pc_duration = pc_duration;
    }

    public int getP_id() {
        return p_id;
    }

    public void setP_id(int p_id) {
        this.p_id = p_id;
    }
}
