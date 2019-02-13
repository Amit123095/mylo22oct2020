package com.mindyourlovedone.healthcare.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mindyourlovedone.healthcare.HomeActivity.R;
import com.mindyourlovedone.healthcare.InsuranceHealthCare.SettingAdapter;
import com.mindyourlovedone.healthcare.model.Setting;

import java.util.ArrayList;

public class FragmentSponsor extends Fragment {
    View rootView;
    TextView txtName;
    ImageView imgHelp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_sponsor, container, false);
        initUi();

        return rootView;
    }


    private void initUi() {
        txtName = getActivity().findViewById(R.id.txtName);
        txtName.setText("Sponsor");
        imgHelp = getActivity().findViewById(R.id.imgHelp);
        imgHelp.setVisibility(View.GONE);
    }
}

