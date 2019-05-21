package com.mindyourlovedone.healthcare.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mindyourlovedone.healthcare.DashBoard.DropboxLoginActivity;
import com.mindyourlovedone.healthcare.HomeActivity.BaseActivity;
import com.mindyourlovedone.healthcare.HomeActivity.BaseNewActivity;
import com.mindyourlovedone.healthcare.HomeActivity.R;
import com.mindyourlovedone.healthcare.InsuranceHealthCare.ContactUsAdapter;
import com.mindyourlovedone.healthcare.InsuranceHealthCare.SettingAdapter;
import com.mindyourlovedone.healthcare.model.Setting;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;

public class FragmentContactUs extends Fragment {
    View rootView;
    ArrayList<Setting> contactList;
    ListView lvContactUs;
    ImageView imgHelp;
    TextView txtTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_contact_us, container, false);
        initUi();
        getData();
        setData();

        return rootView;
    }

    private void setData() {
        ContactUsAdapter sd = new ContactUsAdapter(getActivity(), contactList);
        lvContactUs.setAdapter(sd);
    }

    private void getData() {
        contactList = new ArrayList<>();

      /*  Setting s1 = new Setting();
        s1.setName("Support - FAQs");
        s1.setResImage(R.drawable.medical_one);*/

        Setting s2 = new Setting();
        s2.setName("MYLO Email");
        s2.setResImage(R.drawable.insu_one);

        Setting s3 = new Setting();
        s3.setName("MYLO Website");
        s3.setResImage(R.drawable.drawer_videos);

       // contactList.add(s1);
        contactList.add(s2);
        contactList.add(s3);
    }

    private void initUi() {
        txtTitle = getActivity().findViewById(R.id.txtTitle);
        txtTitle.setText("Contact Us");
        lvContactUs = rootView.findViewById(R.id.lvContactUs);
        imgHelp = getActivity().findViewById(R.id.imgRight);
        imgHelp.setVisibility(View.GONE);

        lvContactUs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0://Mylo Email
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("message/rfc822");
                        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"http://customersupport@mindyour-lovedones.com"});
                        i.putExtra(Intent.EXTRA_SUBJECT, "");
                        i.putExtra(Intent.EXTRA_TEXT   , "");
                        try {
                            startActivity(Intent.createChooser(i, "Send mail..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 1://Mylo Website
                     //   String formatD = "https://drive.google.com/viewerng/viewer?embedded=true&url=%s";
                       // String fullPathD = String.format(Locale.ENGLISH, formatD, "http://mindyour-lovedones.com");
                        Intent browserIntentD = new Intent(Intent.ACTION_VIEW, Uri.parse("http://mindyour-lovedones.com"));
                        startActivity(browserIntentD);

                        break;

                }
            }
        });

    }


}

