package th.or.nectec.zoning.topfarm.datatype;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by topyttac on 9/26/2016 AD.
 */
public class Plant implements Parcelable{
    private int p_id;
    private String p_name;
    private String p_desc;
    private String p_picture;

    public Plant(){
        super();
    }

    public Plant(int p_id, String p_name, String p_desc, String p_picture) {
        this.p_id = p_id;
        this.p_name = p_name;
        this.p_desc = p_desc;
        this.p_picture = p_picture;
    }

    protected Plant(Parcel in) {
        p_id = in.readInt();
        p_name = in.readString();
        p_desc = in.readString();
        p_picture = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(p_id);
        dest.writeString(p_name);
        dest.writeString(p_desc);
        dest.writeString(p_picture);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Plant> CREATOR = new Creator<Plant>() {
        @Override
        public Plant createFromParcel(Parcel in) {
            return new Plant(in);
        }

        @Override
        public Plant[] newArray(int size) {
            return new Plant[size];
        }
    };

    public int getP_id() {
        return p_id;
    }

    public void setP_id(int p_id) {
        this.p_id = p_id;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getP_desc() {
        return p_desc;
    }

    public void setP_desc(String p_desc) {
        this.p_desc = p_desc;
    }

    public String getP_picture() {
        return p_picture;
    }

    public void setP_picture(String p_picture) {
        this.p_picture = p_picture;
    }
}
