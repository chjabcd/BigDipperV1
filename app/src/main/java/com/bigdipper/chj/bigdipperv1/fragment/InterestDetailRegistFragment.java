package com.bigdipper.chj.bigdipperv1.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bigdipper.chj.bigdipperv1.MainActivity;
import com.bigdipper.chj.bigdipperv1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

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
        activity.setTitle(interestName+" 등록하기");
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Toast.makeText(view.getContext(), interestName, Toast.LENGTH_SHORT).show();

        RadioButton button1 = (RadioButton)view.findViewById(R.id.fragment_interest_detail_regist_radiobutton1);
        RadioButton button2 = (RadioButton)view.findViewById(R.id.fragment_interest_detail_regist_radiobutton2);
        RadioButton button3 = (RadioButton)view.findViewById(R.id.fragment_interest_detail_regist_radiobutton3);
        RadioButton button4 = (RadioButton)view.findViewById(R.id.fragment_interest_detail_regist_radiobutton4);
        RadioButton button5 = (RadioButton)view.findViewById(R.id.fragment_interest_detail_regist_radiobutton5);
        RadioButton button6 = (RadioButton)view.findViewById(R.id.fragment_interest_detail_regist_radiobutton6);

        button3.setOnClickListener(button3OnClickListener);
        button4.setOnClickListener(button4OnClickListener);
        button5.setOnClickListener(button5OnClickListener);

        return view;
    }

    RadioButton.OnClickListener button3OnClickListener
            = new RadioButton.OnClickListener(){
        public void onClick(View view){
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            LayoutInflater layoutInflater = getActivity().getLayoutInflater();
            View button1View = layoutInflater.inflate(R.layout.dialog_interest_regist_group,null);
            builder.setView(button1View)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.show();

        }
    };
    RadioButton.OnClickListener button4OnClickListener
            = new RadioButton.OnClickListener(){
        public void onClick(View view){
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            LayoutInflater layoutInflater = getActivity().getLayoutInflater();
            View button1View = layoutInflater.inflate(R.layout.dialog_interest_regist_intimacy,null);
            builder.setView(button1View)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.show();

        }
    };
    RadioButton.OnClickListener button5OnClickListener
            = new RadioButton.OnClickListener(){
        public void onClick(View view){
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            LayoutInflater layoutInflater = getActivity().getLayoutInflater();
            View button1View = layoutInflater.inflate(R.layout.activity_select_people,null);
            builder.setView(button1View)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.show();

        }
    };

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
