package com.bigdipper.chj.bigdipperv1.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigdipper.chj.bigdipperv1.MainActivity;
import com.bigdipper.chj.bigdipperv1.R;
import com.bigdipper.chj.bigdipperv1.model.InterestModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chj on 2018-01-18.
 */

public class InterestFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interest,container,false);
        adapter = new InterestFragmentRecyclerViewAdapter();
        recyclerView = (RecyclerView)view.findViewById(R.id.fragment_interest_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(new InterestFragmentRecyclerViewAdapter());
        recyclerView.setHasFixedSize(true);

        toolbar = (Toolbar)view.findViewById(R.id.toolbar_interest);

        MainActivity activity = (MainActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        activity.setTitle("관심사");

        return view;
    }

    class InterestFragmentRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        List<InterestModel> interestModels;

        public InterestFragmentRecyclerViewAdapter() {
            interestModels = new ArrayList<>();

            FirebaseDatabase.getInstance().getReference().child("interest").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    interestModels.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        InterestModel interestModel = snapshot.getValue(InterestModel.class);
                        interestModels.add(interestModel);
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            System.out.println(interestModels.size()+"");

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_interest, parent, false);

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            InterestModel item = (InterestModel)interestModels.get(position);
            Glide.with
                    (holder.itemView.getContext())
                    .load(R.drawable.empire_building)
                    .apply(new RequestOptions().circleCrop())
                    .into(((CustomViewHolder)holder).imageView);
            ((CustomViewHolder)holder).textViewName.setText(item.interestName);
        }

        @Override
        public int getItemCount() {
            return interestModels.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder{
            public ImageView imageView;
            public TextView textViewName;

            public CustomViewHolder(View view) {
                super(view);
                imageView = (ImageView)view.findViewById(R.id.item_interest_imageview);
                textViewName = (TextView)view.findViewById(R.id.item_interest_textview_name);
            }
        }
    }


}
