package com.bigdipper.chj.bigdipperv1.infomation;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigdipper.chj.bigdipperv1.R;

/**
 * Created by chj on 2018-01-18.
 */

public class InfomationEditFragment extends Fragment {

    private TextView complete;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_infomation_edit,container,false);

        complete = (TextView) view.findViewById(R.id.fragment_infomation_edit_complete);

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout,new InfomationFragment()).commit();
            }
        });

        return view;
    }
}
