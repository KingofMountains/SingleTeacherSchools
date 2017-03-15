package com.sts.singleteacherschool;

import android.app.Activity;
import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;
import static com.sts.singleteacherschool.MainActivity.data;
public class FormFragment extends Fragment {

    List<String> sanchayatArray = new ArrayList<>();
    List<String> villageArray = new ArrayList<>();
    List<String> acharyaArray = new ArrayList<>();
    ArrayAdapter adapterSanchayat, adapterVillage, adapterAcharya, adapterUniform;
    Spinner spinnerSanchayat, spinnerVillage, spinnerAcharya, spinnerUniform, spinnerBlackboard, spinnerCorporate, spinnerMats, spinnerSolorLamp, spinnerCharts, spinnerSyllabus, spinnerLibraryBooks, spinnerMedicine;

    Activity thisActivity;

    View v;

    private OnFragmentInteractionListener mListener;

    EditText txtDescription, txtBoysActual, txtGirlsActual, txtBoysAttendance, txtGirlsAttendance, txtLoggedinTime, txtAdvisorName, txtTotalActual, txtTotalAttendance, txtAdvisorLastVisited, getTxtAdvisorLastVisitedTime;

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

        getValuesFromDB();

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
                    mListener.onFragmentInteraction("continue");
                }
            }
        });

    }

    private void getValuesFromDB() {

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
        getTxtAdvisorLastVisitedTime = (EditText) v.findViewById(R.id.txtAdvisorLastVisit);
        txtAdvisorLastVisited = (EditText) v.findViewById(R.id.txtLastVisitAdvisor);

        txtBoysActual.addTextChangedListener(new StudentCountWatcher());
        txtGirlsActual.addTextChangedListener(new StudentCountWatcher());
        txtBoysAttendance.addTextChangedListener(new StudentCountWatcher());
        txtGirlsAttendance.addTextChangedListener(new StudentCountWatcher());

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

            System.out.println("view --------- " + view.toString());
            System.out.println("position --------- " + position);
            System.out.println("id --------- " + id);

            String selectedItem = parent.getItemAtPosition(position).toString();

            switch (parent.getId()) {
                case R.id.spinnerAcharya:
                    if (!selectedItem.equalsIgnoreCase(""))
                        data.acharyaName = selectedItem;
                    break;
                case R.id.spinnerVillage:
                    if (!selectedItem.equalsIgnoreCase(""))
                        data.villageName = selectedItem;
                    break;
                case R.id.spinnerSanchayat:
                    if (!selectedItem.equalsIgnoreCase(""))
                        data.sanchayatName = selectedItem;
                    break;
                case R.id.spinnerUniform:
                    if (!selectedItem.equalsIgnoreCase(""))
                        data.uniform = selectedItem;
                    break;
                case R.id.spinnerBlackBoard:
                    if (!selectedItem.equalsIgnoreCase(""))
                        data.blackboard = selectedItem;
                    break;
                case R.id.spinnerCorporate:
                    if (!selectedItem.equalsIgnoreCase(""))
                        data.corporate = selectedItem;
                    break;
                case R.id.spinnerMats:
                    if (!selectedItem.equalsIgnoreCase(""))
                        data.mats = selectedItem;
                    break;
                case R.id.spinnerSolarLamp:
                    if (!selectedItem.equalsIgnoreCase(""))
                        data.solarlamp = selectedItem;
                    break;
                case R.id.spinnerCharts:
                    if (!selectedItem.equalsIgnoreCase(""))
                        data.charts = selectedItem;
                    break;
                case R.id.spinnerSyllabus:
                    if (!selectedItem.equalsIgnoreCase(""))
                        data.syllabus = selectedItem;
                    break;
                case R.id.spinnerLibrary:
                    if (!selectedItem.equalsIgnoreCase(""))
                        data.library = selectedItem;
                    break;
                case R.id.spinnerMedicine:
                    if (!selectedItem.equalsIgnoreCase(""))
                        data.medicine = selectedItem;
                    break;
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

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
}
