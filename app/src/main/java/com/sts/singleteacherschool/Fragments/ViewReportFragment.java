package com.sts.singleteacherschool.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sts.singleteacherschool.Data.Report;
import com.sts.singleteacherschool.R;

public class ViewReportFragment extends Fragment {

    Report data;
    View v;
    TextView lblSanchName, lblVillageName, lblAcharyaName, lblUniform, lblBlackboard, lblCorporate, lblMats, lblSolarLamp, lblCharts,
            lblSyllabus, lblLibraryBooks, lblMedicine, lblDescription, lblBoysActual, lblGirlsActual, lblBoysAttendance,
            lblGirlsAttendance, lblAdvisorName, lblTotalActual, lblTotalAttendance, lblAdvisorLastVisited,
            lblAdvisorLastVisitedTime;

    public ViewReportFragment() {
        // Required empty public constructor
    }

    public static ViewReportFragment newInstance(Report data) {
        ViewReportFragment fragment = new ViewReportFragment();
        Bundle args = new Bundle();
        args.putParcelable("report", data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_view_report, container, false);
        init();
        return v;
    }

    private void init() {

        if (getArguments() != null) {
            data = getArguments().getParcelable("report");
        }

        lblAdvisorName = (TextView) v.findViewById(R.id.lblAdvisorName);
        lblSanchName= (TextView) v.findViewById(R.id.lblSanchName);
        lblVillageName = (TextView) v.findViewById(R.id.lblVillageName);
        lblAcharyaName = (TextView) v.findViewById(R.id.lblNameAcharya);
        lblBoysActual = (TextView) v.findViewById(R.id.lblActualStrengthBoys);
        lblGirlsActual = (TextView) v.findViewById(R.id.lblActualStrengthGirls);
        lblTotalActual = (TextView) v.findViewById(R.id.lblActualStrengthTotal);
        lblBoysAttendance = (TextView) v.findViewById(R.id.lblAttendanceBoys);
        lblGirlsAttendance = (TextView) v.findViewById(R.id.lblAttendanceGirls);
        lblTotalAttendance = (TextView) v.findViewById(R.id.lblAttendanceToys);
        lblUniform = (TextView) v.findViewById(R.id.lblUniform);
        lblBlackboard = (TextView) v.findViewById(R.id.lblBlackboard);
        lblCorporate = (TextView) v.findViewById(R.id.lblCorporate);
        lblMats = (TextView) v.findViewById(R.id.lblMats);
        lblSolarLamp = (TextView) v.findViewById(R.id.lblSolarLamp);
        lblSyllabus = (TextView) v.findViewById(R.id.lblSyllabus);
        lblCharts = (TextView) v.findViewById(R.id.lblCharts);
        lblLibraryBooks = (TextView) v.findViewById(R.id.lblLibraryBooks);
        lblMedicine = (TextView) v.findViewById(R.id.lblMedicine);
        lblDescription = (TextView) v.findViewById(R.id.lblDetails);
        lblAdvisorLastVisitedTime = (TextView) v.findViewById(R.id.lblLastVisitedDate);
        lblAdvisorLastVisited = (TextView) v.findViewById(R.id.lblLastVisitAdvisor);

        lblAdvisorName.setText(data.advisorName);
        lblSanchName.setText(data.sanchayatName);
        lblVillageName.setText(data.villageName);
        lblAcharyaName.setText(data.acharyaName);
        lblBoysActual.setText(data.boysActualStrength);
        lblGirlsActual.setText(data.girlsActualStrength);
        lblTotalActual.setText(data.totalActualStrength);
        lblBoysAttendance.setText(data.boysAttendanceStrength);
        lblGirlsAttendance.setText(data.girlsAttendanceStrength);
        lblTotalAttendance.setText(data.totalAttendanceStrength);
        lblUniform.setText(data.uniform);
        lblBlackboard.setText(data.blackboard);
        lblCorporate.setText(data.corporate);
        lblMats.setText(data.mats);
        lblSolarLamp.setText(data.solarlamp);
        lblSyllabus.setText(data.syllabus);
        lblCharts.setText(data.charts);
        lblLibraryBooks.setText(data.library);
        lblMedicine.setText(data.medicine);
        lblDescription.setText(data.description);
        lblAdvisorLastVisitedTime.setText(data.advisorLastVisitDate);
        lblAdvisorLastVisited.setText(data.lastVisitAdvisorName);

    }
}
