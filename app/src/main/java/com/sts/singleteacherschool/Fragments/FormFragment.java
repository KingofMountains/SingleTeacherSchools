package com.sts.singleteacherschool.Fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.sts.singleteacherschool.Data.Acharya;
import com.sts.singleteacherschool.Data.DatabaseHelper;
import com.sts.singleteacherschool.Data.Sanchayat;
import com.sts.singleteacherschool.Data.Village;
import com.sts.singleteacherschool.Listeners.OnFragmentInteractionListener;
import com.sts.singleteacherschool.R;
import com.sts.singleteacherschool.Utilities.Preferences;
import com.sts.singleteacherschool.Utilities.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.sts.singleteacherschool.MainActivity.data;

public class FormFragment extends Fragment {

    List<Sanchayat> sanchayatArrayList = new ArrayList<>();
    List<Village> villageArrayList = new ArrayList<>();
    List<Acharya> acharyaArrayList = new ArrayList<>();
    List<String> sanchayatArray = new ArrayList<>();
    List<String> villageArray = new ArrayList<>();
    List<String> acharyaArray = new ArrayList<>();
    ArrayAdapter adapterSanchayat, adapterVillage, adapterAcharya, adapterUniform;
    Spinner spinnerSanchayat, spinnerVillage, spinnerAcharya, spinnerUniform, spinnerBlackboard, spinnerCorporate, spinnerMats, spinnerSolorLamp, spinnerCharts, spinnerSyllabus, spinnerLibraryBooks, spinnerMedicine;

    Activity thisActivity;

    View v;

    private OnFragmentInteractionListener mListener;

    EditText txtDescription, txtBoysActual, txtGirlsActual, txtBoysAttendance, txtGirlsAttendance, txtLoggedinTime, txtAdvisorName, txtTotalActual, txtTotalAttendance, txtAdvisorLastVisited, txtAdvisorLastVisitedTime;

    DatabaseHelper dbHelper;
    SQLiteDatabase db;

    public FormFragment() {
        // Required empty public constructor
    }

    public static FormFragment newInstance() {
        FormFragment fragment = new FormFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (v == null) {
            v = inflater.inflate(R.layout.fragment_form, container, false);
            init();
        }
        return v;
    }

    private void init() {

        initializeEditTextViews();

        initializeSpinners();

        dbHelper = new DatabaseHelper(thisActivity);
        db = dbHelper.getWritableDatabase();

        getSanchayat();

        v.findViewById(R.id.btnContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    data.description = txtDescription.getText().toString().trim();
                    data.boysActualStrength = txtBoysActual.getText().toString().trim();
                    data.girlsActualStrength = txtGirlsActual.getText().toString().trim();
                    data.totalActualStrength = txtTotalActual.getText().toString().trim();
                    data.boysAttendanceStrength = txtBoysAttendance.getText().toString().trim();
                    data.girlsAttendanceStrength = txtGirlsAttendance.getText().toString().trim();
                    data.totalAttendanceStrength = txtTotalAttendance.getText().toString().trim();

                    if (allValuesEntered())
                        mListener.onFragmentInteraction("continue");
                }
            }
        });

    }

    private boolean allValuesEntered() {

        if (data.sanchayatName.equalsIgnoreCase("")) {
            Utils.showAlert(thisActivity, "Please select Sanch Name");
            return false;
        } else if (data.villageName.equalsIgnoreCase("")) {
            Utils.showAlert(thisActivity, "Please select Village Name");
            return false;
        } else if (data.acharyaName.equalsIgnoreCase("")) {
            Utils.showAlert(thisActivity, "Please select Acharya Name");
            return false;
        } else if (data.boysActualStrength.equalsIgnoreCase("")) {
            Utils.showAlert(thisActivity, "Please enter boys actual strength");
            return false;
        } else if (data.girlsActualStrength.equalsIgnoreCase("")) {
            Utils.showAlert(thisActivity, "Please enter girls actual strength");
            return false;
        } else if (data.boysAttendanceStrength.equalsIgnoreCase("")) {
            Utils.showAlert(thisActivity, "Please enter boys attendance strength");
            return false;
        } else if (data.girlsAttendanceStrength.equalsIgnoreCase("")) {
            Utils.showAlert(thisActivity, "Please enter girls attendance strength");
            return false;
        } else if (data.uniform.equalsIgnoreCase("")) {
            Utils.showAlert(thisActivity, "Please select uniform details");
            return false;
        } else if (data.blackboard.equalsIgnoreCase("")) {
            Utils.showAlert(thisActivity, "Please select blackboard details");
            return false;
        } else if (data.corporate.equalsIgnoreCase("")) {
            Utils.showAlert(thisActivity, "Please select corporate details");
            return false;
        } else if (data.mats.equalsIgnoreCase("")) {
            Utils.showAlert(thisActivity, "Please select mats details");
            return false;
        } else if (data.solarlamp.equalsIgnoreCase("")) {
            Utils.showAlert(thisActivity, "Please select solar lamp details");
            return false;
        } else if (data.charts.equalsIgnoreCase("")) {
            Utils.showAlert(thisActivity, "Please select charts details");
            return false;
        } else if (data.syllabus.equalsIgnoreCase("")) {
            Utils.showAlert(thisActivity, "Please select syllabus details");
            return false;
        } else if (data.library.equalsIgnoreCase("")) {
            Utils.showAlert(thisActivity, "Please select library books details");
            return false;
        } else if (data.medicine.equalsIgnoreCase("")) {
            Utils.showAlert(thisActivity, "Please select medicine details");
            return false;
        } else if (data.description.equalsIgnoreCase("")) {
            Utils.showAlert(thisActivity, "Please enter details");
            txtDescription.setText("");
            return false;
        }
        return true;
    }

    private void getSanchayat() {

        Cursor cursor = db.rawQuery("select * from sanch where id = " + Preferences.geAdvisorSanchayatID(thisActivity) + " and live_id = 1 ", null);

        sanchayatArray.clear();
        sanchayatArray.add("Name of Sanch");

        while (cursor.moveToNext()) {

            Sanchayat data = new Sanchayat();
            data.id = cursor.getString(cursor.getColumnIndex("id"));
            data.sanch_name = cursor.getString(cursor.getColumnIndex("sanch_name"));
            sanchayatArrayList.add(data);
            sanchayatArray.add(data.sanch_name);
        }
    }


    private void getVillage(String sanchID) {

        Cursor cursor = db.rawQuery("select * from village where sanch_id = " + sanchID + " and live_id = 1 ", null);

        villageArray.clear();
        villageArray.add("Name of Village");

        while (cursor.moveToNext()) {

            Village data = new Village();
            data.id = cursor.getString(cursor.getColumnIndex("id"));
            data.village_name = cursor.getString(cursor.getColumnIndex("village_name"));
            villageArrayList.add(data);
            villageArray.add(data.village_name);
        }

    }


    private void getAcharya(String villageID) {

        Cursor cursor = db.rawQuery("select * from acharya where village_id = " + villageID + " and live_id = 1 ", null);

        acharyaArray.clear();
        acharyaArray.add("Name of Acharya");

        while (cursor.moveToNext()) {

            Acharya data = new Acharya();
            data.id = cursor.getString(cursor.getColumnIndex("id"));
            data.acharya_name = cursor.getString(cursor.getColumnIndex("acharya_name"));
            acharyaArrayList.add(data);
            acharyaArray.add(data.acharya_name);
        }

    }

    private void initializeEditTextViews() {
        txtLoggedinTime = (EditText) v.findViewById(R.id.txtLoginTime);
        txtAdvisorName = (EditText) v.findViewById(R.id.txtAdvisorName);
        txtBoysActual = (EditText) v.findViewById(R.id.txtStrengthBoys);
        txtGirlsActual = (EditText) v.findViewById(R.id.txtStrengthGirls);
        txtTotalActual = (EditText) v.findViewById(R.id.txtStrengthTotal);
        txtBoysAttendance = (EditText) v.findViewById(R.id.txtAttendanceBoys);
        txtGirlsAttendance = (EditText) v.findViewById(R.id.txtAttendanceGirls);
        txtTotalAttendance = (EditText) v.findViewById(R.id.txtAttendanceTotal);
        txtDescription = (EditText) v.findViewById(R.id.txtDetails);
        txtAdvisorLastVisitedTime = (EditText) v.findViewById(R.id.txtAdvisorLastVisit);
        txtAdvisorLastVisited = (EditText) v.findViewById(R.id.txtLastVisitAdvisor);

        txtBoysActual.addTextChangedListener(new StudentCountWatcher());
        txtGirlsActual.addTextChangedListener(new StudentCountWatcher());
        txtBoysAttendance.addTextChangedListener(new StudentCountWatcher());
        txtGirlsAttendance.addTextChangedListener(new StudentCountWatcher());

        txtAdvisorName.setText(Preferences.getAdvisorName(thisActivity));
        String date = Utils.getDate(System.currentTimeMillis());
        txtLoggedinTime.setText(date);
        data.loggedInTime = date;

    }

    private void initializeSpinners() {

        spinnerSanchayat = (Spinner) v.findViewById(R.id.spinnerSanchayat);
        spinnerVillage = (Spinner) v.findViewById(R.id.spinnerVillage);
        spinnerAcharya = (Spinner) v.findViewById(R.id.spinnerAcharya);
        spinnerUniform = (Spinner) v.findViewById(R.id.spinnerUniform);
        spinnerBlackboard = (Spinner) v.findViewById(R.id.spinnerBlackBoard);
        spinnerCorporate = (Spinner) v.findViewById(R.id.spinnerCorporate);
        spinnerMats = (Spinner) v.findViewById(R.id.spinnerMats);
        spinnerSolorLamp = (Spinner) v.findViewById(R.id.spinnerSolarLamp);
        spinnerCharts = (Spinner) v.findViewById(R.id.spinnerCharts);
        spinnerSyllabus = (Spinner) v.findViewById(R.id.spinnerSyllabus);
        spinnerLibraryBooks = (Spinner) v.findViewById(R.id.spinnerLibrary);
        spinnerMedicine = (Spinner) v.findViewById(R.id.spinnerMedicine);

        spinnerSanchayat.setOnItemSelectedListener(new OnSpinnerItemSelected());
        spinnerVillage.setOnItemSelectedListener(new OnSpinnerItemSelected());
        spinnerAcharya.setOnItemSelectedListener(new OnSpinnerItemSelected());
        spinnerUniform.setOnItemSelectedListener(new OnSpinnerItemSelected());
        spinnerBlackboard.setOnItemSelectedListener(new OnSpinnerItemSelected());
        spinnerCorporate.setOnItemSelectedListener(new OnSpinnerItemSelected());
        spinnerMats.setOnItemSelectedListener(new OnSpinnerItemSelected());
        spinnerSolorLamp.setOnItemSelectedListener(new OnSpinnerItemSelected());
        spinnerCharts.setOnItemSelectedListener(new OnSpinnerItemSelected());
        spinnerSyllabus.setOnItemSelectedListener(new OnSpinnerItemSelected());
        spinnerLibraryBooks.setOnItemSelectedListener(new OnSpinnerItemSelected());
        spinnerMedicine.setOnItemSelectedListener(new OnSpinnerItemSelected());

        sanchayatArray.add("Name of Sanch");
        villageArray.add("Name of Village");
        acharyaArray.add("Name of Acharya");

        adapterSanchayat = new ArrayAdapter(thisActivity, android.R.layout.simple_list_item_1, sanchayatArray);
        adapterSanchayat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSanchayat.setAdapter(adapterSanchayat);

        adapterVillage = new ArrayAdapter(thisActivity, android.R.layout.simple_list_item_1, villageArray);
        adapterVillage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVillage.setAdapter(adapterVillage);

        adapterAcharya = new ArrayAdapter(thisActivity, android.R.layout.simple_list_item_1, acharyaArray);
        adapterAcharya.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAcharya.setAdapter(adapterAcharya);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
        thisActivity = getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private class OnSpinnerItemSelected implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            String selectedItem = parent.getItemAtPosition(position).toString();

            switch (parent.getId()) {
                case R.id.spinnerAcharya:
                    if (!selectedItem.equalsIgnoreCase("Name of Acharya")) {
                        data.acharyaName = selectedItem;
                    } else {
                        data.acharyaName = "";
                    }
                    break;
                case R.id.spinnerVillage:
                    if (!selectedItem.equalsIgnoreCase("Name of Village")) {
                        String villageID = villageArrayList.get(position - 1).id;
                        data.villageName = selectedItem;
                        setLastReportDetails(data.villageName);
                        getAcharya(villageID);
                    } else {
                        data.villageName = "";
                    }
                    break;
                case R.id.spinnerSanchayat:
                    if (!selectedItem.equalsIgnoreCase("Name of Sanch")) {
                        String sanchID = sanchayatArrayList.get(position - 1).id;
                        data.sanchayatName = selectedItem;
                        getVillage(sanchID);
                    } else {
                        data.sanchayatName = "";
                    }
                    break;
                case R.id.spinnerUniform:
                    if (!selectedItem.equalsIgnoreCase("Acharya in Uniform")) {
                        data.uniform = selectedItem;
                    } else {
                        data.uniform = "";
                    }
                    break;
                case R.id.spinnerBlackBoard:
                    if (!selectedItem.equalsIgnoreCase("Black board")) {
                        data.blackboard = selectedItem;
                    } else {
                        data.blackboard = "";
                    }
                    break;
                case R.id.spinnerCorporate:
                    if (!selectedItem.equalsIgnoreCase("Corporate Name Board")) {
                        data.corporate = selectedItem;
                    } else {
                        data.corporate = "";
                    }
                    break;
                case R.id.spinnerMats:
                    if (!selectedItem.equalsIgnoreCase("Mats")) {
                        data.mats = selectedItem;
                    } else {
                        data.mats = "";
                    }
                    break;
                case R.id.spinnerSolarLamp:
                    if (!selectedItem.equalsIgnoreCase("Solar Lamp")) {
                        data.solarlamp = selectedItem;
                    } else {
                        data.solarlamp = "";
                    }
                    break;
                case R.id.spinnerCharts:
                    if (!selectedItem.equalsIgnoreCase("Charts")) {
                        data.charts = selectedItem;
                    } else {
                        data.charts = "";
                    }
                    break;
                case R.id.spinnerSyllabus:
                    if (!selectedItem.equalsIgnoreCase("Syllabus")) {
                        data.syllabus = selectedItem;
                    } else {
                        data.syllabus = "";
                    }
                    break;
                case R.id.spinnerLibrary:
                    if (!selectedItem.equalsIgnoreCase("Library Books")) {
                        data.library = selectedItem;
                    } else {
                        data.library = "";
                    }
                    break;
                case R.id.spinnerMedicine:
                    if (!selectedItem.equalsIgnoreCase("Medicine")) {
                        data.medicine = selectedItem;
                    } else {
                        data.medicine = "";
                    }
                    break;
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private void setLastReportDetails(String villageName) {
        Cursor cursor = db.rawQuery("select * from advisor_report where village = '" + villageName + "' ORDER BY id DESC LIMIT 1", null);
        while (cursor.moveToNext()) {
            data.lastVisitAdvisorName = cursor.getString(cursor.getColumnIndex("advisor_username"));
            data.advisorLastVisitDate = cursor.getString(cursor.getColumnIndex("date"));
            txtAdvisorLastVisited.setText(data.lastVisitAdvisorName);
            txtAdvisorLastVisitedTime.setText(data.advisorLastVisitDate);
        }
    }

    private class StudentCountWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            if (s.hashCode() == txtBoysActual.getText().hashCode() || s.hashCode() == txtGirlsActual.getText().hashCode()) {
                setTotalActual();
            } else if (s.hashCode() == txtBoysAttendance.getText().hashCode() || s.hashCode() == txtGirlsAttendance.getText().hashCode()) {
                setTotalAttendance();
            }

        }
    }

    private void setTotalAttendance() {
        int total = 0;
        if (!txtBoysAttendance.getText().toString().equalsIgnoreCase("")) {
            total += Integer.valueOf(txtBoysAttendance.getText().toString());
        }
        if (!txtGirlsAttendance.getText().toString().equalsIgnoreCase("")) {
            total += Integer.valueOf(txtGirlsAttendance.getText().toString());
        }
        if (total > 0) {
            txtTotalAttendance.setText(String.valueOf(total));
        } else {
            txtTotalAttendance.setText(String.valueOf(total));
        }
    }

    private void setTotalActual() {
        int total = 0;
        if (!txtBoysActual.getText().toString().equalsIgnoreCase("")) {
            total += Integer.valueOf(txtBoysActual.getText().toString());
        }
        if (!txtGirlsActual.getText().toString().equalsIgnoreCase("")) {
            total += Integer.valueOf(txtGirlsActual.getText().toString());
        }
        if (total > 0) {
            txtTotalActual.setText(String.valueOf(total));
        } else {
            txtTotalActual.setText(String.valueOf(total));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
        dbHelper.close();
    }
}
