package com.bigdipper.chj.bigdipperv1.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bigdipper.chj.bigdipperv1.MainActivity;
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

    private InterestModel interestModel;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private ImageView buttonRegist, buttonConfirm, buttonComment, buttonPhoto, buttonShare;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interest_detail,container,false);

        setHasOptionsMenu(true);

        toolbar = (Toolbar)view.findViewById(R.id.toolbar_interest_detail);
        viewPager = (ViewPager)view.findViewById(R.id.fragment_interest_detail_viewpager);
        buttonRegist = view.findViewById(R.id.fragment_interest_detail_registbutton);
        buttonConfirm = view.findViewById(R.id.fragment_interest_detail_confirmbutton);
        buttonComment = view.findViewById(R.id.fragment_interest_detail_commentbutton);
        buttonPhoto = view.findViewById(R.id.fragment_interest_detail_photobutton);
        buttonShare = view.findViewById(R.id.fragment_interest_detail_sharebutton);

        final MainActivity activity = (MainActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        String interestUid = getArguments().getString("interestUid");
        final String interestName = getArguments().getString("interestName");
        FirebaseDatabase.getInstance().getReference().child("interest").child(interestUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                interestModel = dataSnapshot.getValue(InterestModel.class);
                activity.setTitle(interestModel.interestName);
                viewPager.setAdapter(new InterestViewPagerAdapter(getFragmentManager()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        buttonRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fragmentTag = view.getClass().getSimpleName();
                getFragmentManager().popBackStack(fragmentTag,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout,new InterestDetailRegistFragment().newInstance(interestName)).addToBackStack(null).commit();
            }
        });



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

    public static  InterestDetailFragment newInstance(String interestUid, String interestName){
        InterestDetailFragment fragment = new InterestDetailFragment();
        Bundle args = new Bundle();
//        args.putSerializable("interestUid",param);
//        args.putParcelable("interestUid",param);
        args.putString("interestUid",interestUid);
        args.putString("interestName",interestName);
        fragment.setArguments(args);


        return fragment;
    }

    class InterestViewPagerAdapter extends FragmentStatePagerAdapter{

        int image[] = {R.drawable.empire_building,R.drawable.profile};

        public InterestViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new InterestDetailImageFragment().newInstance(image[position]);
        }

        @Override
        public int getCount() {
            return image.length;
        }
    }


}
