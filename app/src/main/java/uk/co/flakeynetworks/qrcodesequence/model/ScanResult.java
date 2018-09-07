package uk.co.flakeynetworks.qrcodesequence.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import uk.co.flakeynetworks.qrcodesequence.R;

/**
 * Created by Richard Stokes on 8/29/2018.
 */

public class ScanResult implements Parcelable, Comparable<ScanResult> {

    private final String message;
    private final long scanTime;
    private final long scanDuration;
    private final int numberOfPayloads;
    private File file;


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

        File directory = new File(context.getFilesDir(), context.getResources().getString(R.string.historyFolder));
        if(!directory.exists())
            directory.mkdirs();

        File file = new File(directory, String.format("%d%n.json", scanTime));

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


    @Override
    public int compareTo(@NonNull ScanResult o) {

        if(scanTime == o.scanTime) return 0;
        if(scanTime < o.scanTime) return -1;
        return 1;
    } // end of compareTo


    public String getDateAndTimeTaken() {

        Date date = new Date(scanTime);
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

        return df.format(date);
    } // end of getDateAndTimeTaken


    public void setFile(File file) {

        this.file = file;
    } // end of setFile


    public boolean deleteFile() {

        if(file == null) return false;

        return file.delete();
    } // end of deleteFile


    public String getScanTimeFormatted() {

        long scanDuration = this.scanDuration;

        // Get number of minutes
        int minutes = (int) (scanDuration / (1000 * 60));
        scanDuration -= minutes * (1000 * 60);

        // Get number of seconds
        int seconds = (int) (scanDuration / 1000);
        scanDuration -= seconds * 1000;

        if(scanDuration > 0)
            seconds++;

        StringBuilder readable = new StringBuilder();
        if(minutes > 0)
            readable.append(minutes).append(" minutes ");

        if(seconds > 0)
            readable.append(seconds).append(" seconds");

        return readable.toString().trim();
    } // end of getScanTimeReadable


    public int getMessageLength() {

        if(message == null) return 0;

        return message.length();
    } // end of getMessageLength
} // end of ScanResult
