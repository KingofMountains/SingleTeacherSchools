package com.sts.singleteacherschool;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class FormFragment extends Fragment {

    List<String> sArray = new ArrayList<>();
    ArrayAdapter adapterSanchayat,adapterVillage,adapterAcharya;
    Spinner spinnerSanchayat,spinnerVillage,spinnerAcharya;

    Activity thisActivity;

    View v;
    private OnFragmentInteractionListener mListener;

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

        initializeSpinners();

        v.findViewById(R.id.btnContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onFragmentInteraction("continue");
                }
            }
        });

    }

    private void initializeSpinners() {

        spinnerSanchayat = (Spinner) v.findViewById(R.id.spinnerSanchayat);
        spinnerVillage = (Spinner) v.findViewById(R.id.spinnerVillage);
        spinnerAcharya = (Spinner) v.findViewById(R.id.spinnerAcharya);

        adapterSanchayat = new ArrayAdapter(thisActivity,android.R.layout.simple_list_item_1,sArray);
        adapterSanchayat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSanchayat.setAdapter(adapterSanchayat);

        adapterVillage = new ArrayAdapter(thisActivity,android.R.layout.simple_list_item_1,sArray);
        adapterVillage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVillage.setAdapter(adapterSanchayat);

        adapterAcharya = new ArrayAdapter(thisActivity, android.R.layout.simple_list_item_1, sArray);
        adapterAcharya.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAcharya.setAdapter(adapterSanchayat);
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
}
