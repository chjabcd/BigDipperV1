package com.bigdipper.chj.bigdipperv1.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bigdipper.chj.bigdipperv1.R;

/**
 * Created by chj on 2018-01-03.
 */

public class InfoFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_info,container,false);
        view.bringToFront();
        return view;
    }
}
