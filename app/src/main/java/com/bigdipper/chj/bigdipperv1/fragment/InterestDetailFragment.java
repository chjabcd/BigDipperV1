package com.bigdipper.chj.bigdipperv1.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bigdipper.chj.bigdipperv1.R;
import com.bigdipper.chj.bigdipperv1.model.InterestModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by chj on 2018-01-24.
 */

public class InterestDetailFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interest_detail,container,false);

        String interestUid = getArguments().getString("interestUid");
        FirebaseDatabase.getInstance().getReference().child("interest").child(interestUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                InterestModel interestModel = dataSnapshot.getValue(InterestModel.class);
                System.out.println(interestModel.toString()+"  0");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    public static  InterestDetailFragment newInstance(InterestModel param){
        InterestDetailFragment fragment = new InterestDetailFragment();
        Bundle args = new Bundle();
        InterestModel interestModel = new InterestModel();
        interestModel = param;
        args.putSerializable("interestUid",param);
        args.putParcelable("interestUid",param);
        fragment.setArguments(args);


        return fragment;
    }

}
