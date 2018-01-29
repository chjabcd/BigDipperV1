package com.bigdipper.chj.bigdipperv1.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bigdipper.chj.bigdipperv1.MainActivity;
import com.bigdipper.chj.bigdipperv1.R;

/**
 * Created by chj on 2018-01-26.
 */

public class InterestDetailRegistFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interest_detail_regist,container,false);


        String interestName = getArguments().getString("interestName");

        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar_interest_detail_regist);
        MainActivity activity = (MainActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        activity.setTitle(interestName);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Toast.makeText(view.getContext(), interestName, Toast.LENGTH_SHORT).show();

        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_interest_detail_regist,menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                getFragmentManager().popBackStack();

        }
        return super.onOptionsItemSelected(item);
    }

    public static InterestDetailRegistFragment newInstance(String interestName) {

        Bundle args = new Bundle();
        args.putString("interestName",interestName);
        InterestDetailRegistFragment fragment = new InterestDetailRegistFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
