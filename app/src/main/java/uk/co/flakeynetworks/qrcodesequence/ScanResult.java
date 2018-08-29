package uk.co.flakeynetworks.qrcodesequence;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Richard Stokes on 8/29/2018.
 */

public class ScanResult implements Parcelable {

    private final String message;
    private final long scanTime;
    private final long scanDuration;
    private final int numberOfPayloads;


    public ScanResult(String message, long scanTime, long scanDuration, int numberOfPayloads) {

        this.message = message;
        this.scanTime = scanTime;
        this.scanDuration = scanDuration;
        this.numberOfPayloads = numberOfPayloads;
    } // end of constructor


    public ScanResult(Parcel parcel) {

        this.message = parcel.readString();
        this.scanTime = parcel.readLong();
        this.scanDuration = parcel.readLong();
        this.numberOfPayloads = parcel.readInt();
    } // end of constructor


    public static final Parcelable.Creator<ScanResult> CREATOR = new Parcelable.Creator<ScanResult>(){

        @Override
        public ScanResult createFromParcel(Parcel parcel) { return new ScanResult(parcel); } // end of createFromParcel

        @Override
        public ScanResult[] newArray(int size) { return new ScanResult[0]; } // end of newArray
    };



    @Override
    public int describeContents() { return hashCode(); } // end of describeContent


    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(message);
        dest.writeLong(scanTime);
        dest.writeLong(scanDuration);
        dest.writeInt(numberOfPayloads);
    } // end of writeToParcel


    public String getMessage() { return message; } // end of getMessage

    public long getScanTime() { return scanTime; } // end of getScanTime

    public long getScanDuration() { return scanDuration; } // end of getScanDuration

    public int getNumberOfPayloads() { return numberOfPayloads; } // end of getNumberOfPayloads

    public boolean save(Context context) {

        File directory = new File(context.getFilesDir(), "history");
        if(!directory.exists())
            directory.mkdirs();

        File file = new File(directory, String.format("%d%n", scanTime));

        FileWriter writer = null;

        try {

            writer = new FileWriter(file);
            writer.write(toJSON());
            writer.flush();
        } catch (IOException e) {
            return false;
        } finally {

            try {
                if (writer != null)
                    writer.close();
            } catch (IOException e) { } // end of catch
        } // end of finally

        return true;
    } // end of save


    public String toJSON() {

        Gson gson = new Gson();
        try {
            return gson.toJson(this);
        } catch(Exception e) {
            return null;
        } // end of catch
    } // end of toJSON


    public static ScanResult fromJSON(String fileContents) {

        if(fileContents == null || fileContents.isEmpty()) return null;

        Gson gson = new Gson();
        return gson.fromJson(fileContents, ScanResult.class);
    } // end of fromJSON
} // end of ScanResult
