package th.or.nectec.zoning.topfarm.datatype;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by topyttac on 9/26/2016 AD.
 */
public class Event implements Parcelable{
    private int e_id;
    private String e_name;
    private String e_desc;
    private String e_start;
    private String e_end;
    private int p_id;

    public Event(){
        super();
    }

    public Event(int e_id, String e_name, String e_desc, String e_start, String e_end, int p_id) {
        this.e_id = e_id;
        this.e_name = e_name;
        this.e_desc = e_desc;
        this.e_start = e_start;
        this.e_end = e_end;
        this.p_id = p_id;
    }

    protected Event(Parcel in) {
        e_id = in.readInt();
        e_name = in.readString();
        e_desc = in.readString();
        e_start = in.readString();
        e_end = in.readString();
        p_id = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(e_id);
        dest.writeString(e_name);
        dest.writeString(e_desc);
        dest.writeString(e_start);
        dest.writeString(e_end);
        dest.writeInt(p_id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public int getE_id() {
        return e_id;
    }

    public void setE_id(int e_id) {
        this.e_id = e_id;
    }

    public String getE_name() {
        return e_name;
    }

    public void setE_name(String e_name) {
        this.e_name = e_name;
    }

    public String getE_desc() {
        return e_desc;
    }

    public void setE_desc(String e_desc) {
        this.e_desc = e_desc;
    }

    public String getE_start() {
        return e_start;
    }

    public void setE_start(String e_start) {
        this.e_start = e_start;
    }

    public String getE_end() {
        return e_end;
    }

    public void setE_end(String e_end) {
        this.e_end = e_end;
    }

    public int getP_id() {
        return p_id;
    }

    public void setP_id(int p_id) {
        this.p_id = p_id;
    }
}
