package assignment2.bootcamp.com.nytimessearch.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by baphna on 10/23/2016.
 */

public class SelectedFilters implements Parcelable {
    private String date;
    private String sortOrder;
    private boolean arts;
    private boolean fashion;
    private boolean sports;


    public SelectedFilters(String date, String sortOrder, boolean arts, boolean fashion, boolean sports) {
        this.date = date;
        this.sortOrder = sortOrder;
        this.arts = arts;
        this.fashion = fashion;
        this.sports = sports;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getDate() {
        return date;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public boolean isArts() {
        return arts;
    }

    public boolean isFashion() {
        return fashion;
    }

    public boolean isSports() {
        return sports;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.date);
        dest.writeString(this.sortOrder);
        dest.writeByte(this.arts ? (byte) 1 : (byte) 0);
        dest.writeByte(this.fashion ? (byte) 1 : (byte) 0);
        dest.writeByte(this.sports ? (byte) 1 : (byte) 0);
    }

    public SelectedFilters() {
    }

    protected SelectedFilters(Parcel in) {
        this.date = in.readString();
        this.sortOrder = in.readString();
        this.arts = in.readByte() != 0;
        this.fashion = in.readByte() != 0;
        this.sports = in.readByte() != 0;
    }

    public static final Parcelable.Creator<SelectedFilters> CREATOR = new Parcelable.Creator<SelectedFilters>() {
        @Override
        public SelectedFilters createFromParcel(Parcel source) {
            return new SelectedFilters(source);
        }

        @Override
        public SelectedFilters[] newArray(int size) {
            return new SelectedFilters[size];
        }
    };
}
