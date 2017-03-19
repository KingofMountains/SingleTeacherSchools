package com.sts.singleteacherschool.Data;

import android.os.Parcel;
import android.os.Parcelable;

public class Report implements Parcelable{
    public String advisorName = "";
    public String loggedInTime = "";
    public String sanchayatName = "";
    public String villageName = "";
    public String acharyaName = "";
    public String boysActualStrength = "";
    public String girlsActualStrength = "";
    public String totalActualStrength = "";
    public String boysAttendanceStrength = "";
    public String girlsAttendanceStrength = "";
    public String totalAttendanceStrength = "";
    public String uniform = "";
    public String blackboard= "";
    public String corporate = "";
    public String mats = "";
    public String solarlamp = "";
    public String charts = "";
    public String syllabus = "";
    public String library = "";
    public String medicine = "";
    public String description = "";
    public String advisorLastVisitDate = "";
    public String lastVisitAdvisorName = "";
    public String imageone = "";
    public String imagetwo = "";
    public String imagethree = "";
    public String imagefour = "";
    public String loggedOutTime = "";
    public int id;
    public String advisor_userid="";

    public Report(Parcel in) {
        advisorName = in.readString();
        loggedInTime = in.readString();
        sanchayatName = in.readString();
        villageName = in.readString();
        acharyaName = in.readString();
        boysActualStrength = in.readString();
        girlsActualStrength = in.readString();
        totalActualStrength = in.readString();
        boysAttendanceStrength = in.readString();
        girlsAttendanceStrength = in.readString();
        totalAttendanceStrength = in.readString();
        uniform = in.readString();
        blackboard = in.readString();
        corporate = in.readString();
        mats = in.readString();
        solarlamp = in.readString();
        charts = in.readString();
        syllabus = in.readString();
        library = in.readString();
        medicine = in.readString();
        description = in.readString();
        advisorLastVisitDate = in.readString();
        lastVisitAdvisorName = in.readString();
        imageone = in.readString();
        imagetwo = in.readString();
        imagethree = in.readString();
        imagefour = in.readString();
        loggedOutTime = in.readString();
        id = in.readInt();
        advisor_userid = in.readString();
    }

    public static final Creator<Report> CREATOR = new Creator<Report>() {
        @Override
        public Report createFromParcel(Parcel in) {
            return new Report(in);
        }

        @Override
        public Report[] newArray(int size) {
            return new Report[size];
        }
    };

    public Report() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(advisorName);
        dest.writeString(loggedInTime);
        dest.writeString(sanchayatName);
        dest.writeString(villageName);
        dest.writeString(acharyaName);
        dest.writeString(boysActualStrength);
        dest.writeString(girlsActualStrength);
        dest.writeString(totalActualStrength);
        dest.writeString(boysAttendanceStrength);
        dest.writeString(girlsAttendanceStrength);
        dest.writeString(totalAttendanceStrength);
        dest.writeString(uniform);
        dest.writeString(blackboard);
        dest.writeString(corporate);
        dest.writeString(mats);
        dest.writeString(solarlamp);
        dest.writeString(charts);
        dest.writeString(syllabus);
        dest.writeString(library);
        dest.writeString(medicine);
        dest.writeString(description);
        dest.writeString(advisorLastVisitDate);
        dest.writeString(lastVisitAdvisorName);
        dest.writeString(imageone);
        dest.writeString(imagetwo);
        dest.writeString(imagethree);
        dest.writeString(imagefour);
        dest.writeString(loggedOutTime);
        dest.writeInt(id);
        dest.writeString(advisor_userid);
    }
}
