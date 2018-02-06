package com.bigdipper.chj.bigdipperv1.interest;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigdipper.chj.bigdipperv1.MainActivity;
import com.bigdipper.chj.bigdipperv1.R;
import com.bigdipper.chj.bigdipperv1.model.interestModel.InterestHeaderModel;
import com.bigdipper.chj.bigdipperv1.model.interestModel.InterestListModel;
import com.bigdipper.chj.bigdipperv1.model.interestModel.InterestModel;
import com.bigdipper.chj.bigdipperv1.service.SoundSearcher;
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

        setHasOptionsMenu(true);

        toolbar = (Toolbar)view.findViewById(R.id.toolbar_interest);

        MainActivity activity = (MainActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        activity.setTitle("관심사");

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_interest, menu);
        SearchView searchView = (SearchView)menu.findItem(R.id.menu_interest_search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                recyclerView.setAdapter(new InterestFragmentRecyclerViewAdapter(query));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerView.setAdapter(new InterestFragmentRecyclerViewAdapter(newText));
                return true;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                recyclerView.setAdapter(new InterestFragmentRecyclerViewAdapter());
                return false;
            }
        });
    }

    class InterestFragmentRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;

        List<InterestModel> interestModels;
        List<InterestListModel> list;

        public InterestFragmentRecyclerViewAdapter(final String s) {
            interestModels = new ArrayList<>();
            list = new ArrayList<>();
            FirebaseDatabase.getInstance().getReference().child("interest").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    interestModels.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        InterestModel interestModel = snapshot.getValue(InterestModel.class);
                        if(SoundSearcher.matchStringAll(interestModel.interestName, s)){
                            interestModels.add(interestModel);
                            list.add(interestModel);
                        }
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            System.out.println(interestModels.size()+"");

        }
        public InterestFragmentRecyclerViewAdapter() {
            interestModels = new ArrayList<>();
            list = new ArrayList<>();
            FirebaseDatabase.getInstance().getReference().child("interest").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    interestModels.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        InterestModel interestModel = snapshot.getValue(InterestModel.class);
                        interestModel.interestUid = snapshot.getKey();
                        interestModels.add(interestModel);
                    }
                    InterestHeaderModel header = new InterestHeaderModel();
                    header.setHeader("최신 관심사");
                    list.add(header);
                    for(int i=0;i<interestModels.size();i++){
                        list.add(interestModels.get(i));
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
            if(viewType == TYPE_ITEM) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_interest, parent, false);
                return new CustomViewHolder(view);
            }else if(viewType == TYPE_HEADER){
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_interest_header, parent, false);
                return new CustomViewHeaderHolder(view);
            }
            throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof CustomViewHolder) {
                final InterestModel item = (InterestModel) list.get(position);
                CustomViewHolder viewHolder = (CustomViewHolder)holder;
                Glide.with
                        (holder.itemView.getContext())
                        .load(R.drawable.empire_building)
                        .apply(new RequestOptions().circleCrop())
                        .into(viewHolder.imageView);
                viewHolder.textViewName.setText(item.interestName);
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout, InterestDetailFragment.newInstance(item.interestUid,item.interestName)).addToBackStack(null).commit();
                    }
                });
            }else if(holder instanceof CustomViewHeaderHolder){
                InterestHeaderModel item = (InterestHeaderModel)list.get(position);
                CustomViewHeaderHolder headerHolder = (CustomViewHeaderHolder)holder;
                headerHolder.textViewTitie.setText(item.getHeader());

            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public int getItemViewType(int position) {
            if(isPositionHeader(position)){
                return TYPE_HEADER;
            }
            return TYPE_ITEM;
        }

        private boolean isPositionHeader(int position){
            return list.get(position) instanceof InterestHeaderModel;
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
        private class CustomViewHeaderHolder extends RecyclerView.ViewHolder{
            public TextView textViewTitie;
            public TextView textViewCount;

            public CustomViewHeaderHolder(View view) {
                super(view);
                textViewTitie = (TextView)view.findViewById(R.id.interestitem_header_id);
                textViewCount = (TextView)view.findViewById(R.id.interestitem_header_count);
            }
        }
    }



}
