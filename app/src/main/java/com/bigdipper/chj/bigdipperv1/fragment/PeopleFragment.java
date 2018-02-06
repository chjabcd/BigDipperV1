package com.bigdipper.chj.bigdipperv1.fragment;

import android.app.ActivityOptions;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import com.bigdipper.chj.bigdipperv1.model.HeaderModel;
import com.bigdipper.chj.bigdipperv1.model.ListModel;
import com.bigdipper.chj.bigdipperv1.model.UserModel;
import com.bigdipper.chj.bigdipperv1.people.InfoActivity;
import com.bigdipper.chj.bigdipperv1.service.SoundSearcher;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chj on 2017-12-27.
 */

public class PeopleFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_people, container, false);
        adapter = new PeopleFragmentRecyclerViewAdapter();
        recyclerView = (RecyclerView) view.findViewById(R.id.peoplefragment_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        setHasOptionsMenu(true);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar_people);

        MainActivity activity = (MainActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.setTitle("People");

        FloatingActionButton floatingActionButton = (FloatingActionButton)view.findViewById(R.id.peoplefragment_floatingButtom);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(),SelectPeopleActivity.class));
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_people, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_people_search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                recyclerView.setAdapter(new PeopleFragmentRecyclerViewAdapter(s));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                recyclerView.setAdapter(new PeopleFragmentRecyclerViewAdapter(s));
                return true;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                recyclerView.setAdapter(new PeopleFragmentRecyclerViewAdapter());

                return false;
            }
        });
    }


    class PeopleFragmentRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        List<UserModel> userModels;

        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;

        List<ListModel> list;

        public PeopleFragmentRecyclerViewAdapter(final String s) {
            userModels = new ArrayList<>();
            list = new ArrayList<>();
            final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userModels.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        UserModel userModel = snapshot.getValue(UserModel.class);
                        if (userModel.uid.equals(myUid)) {
                            continue;
                        }
                        if (SoundSearcher.matchStringAll(userModel.userName, s)) {
                            list.add(userModel);
                        }
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        public PeopleFragmentRecyclerViewAdapter() {
            userModels = new ArrayList<>();
            list = new ArrayList<>();
            final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userModels.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        UserModel userModel = snapshot.getValue(UserModel.class);
                        if (userModel.uid.equals(myUid)) {
                            continue;
                        }
                        userModels.add(userModel);
                    }
                    HeaderModel header = new HeaderModel();
                    header.setHeader("친구");
                    list.add(header);
                    for(int i=0;i<userModels.size();i++){
                        list.add(userModels.get(i));
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType == TYPE_ITEM) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
                return new CustomViewHolder(view);
            }else if(viewType == TYPE_HEADER){
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_header, parent, false);
                return new CustomHeaderViewHolder(view);
            }
            throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            if(holder instanceof CustomViewHolder){
                UserModel item = (UserModel)list.get(position);
                Glide.with
                        (holder.itemView.getContext())
                        .load(item.getProfileImageUrl())
                        .apply(new RequestOptions().circleCrop())
                        .into(((CustomViewHolder) holder).imageView);
                ((CustomViewHolder) holder).textViewId.setText(item.getUserName());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), InfoActivity.class);
                        intent.putExtra("destinationUid", userModels.get(position).uid);
                        ActivityOptions activityOptions = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            activityOptions = ActivityOptions.makeCustomAnimation(view.getContext(), R.anim.fromtop, R.anim.tostop);
                            startActivity(intent, activityOptions.toBundle());
                        }
                    }
                });
                if (item.getComment() != null) {
                    ((CustomViewHolder) holder).textView_comment.setText(item.getComment());
                }
            }else if(holder instanceof CustomHeaderViewHolder){
                HeaderModel item = (HeaderModel)list.get(position);
                ((CustomHeaderViewHolder)holder).title.setText(item.getHeader());
                ((CustomHeaderViewHolder)holder).titleCount.setText("( "+userModels.size()+" )");
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
            return list.get(position) instanceof HeaderModel;
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageView;
            public TextView textViewId;
            public TextView textViewPhone;
            public TextView textView_comment;
            public ImageView imageView_hello, imageView_chat, imageView_call, imageView_sns, imageView_hide, imageView_info;

            public CustomViewHolder(View view) {
                super(view);
                imageView = (ImageView) view.findViewById(R.id.frienditem_imageview);
                textViewId = (TextView) view.findViewById(R.id.frienditem_id);
                textViewPhone = (TextView) view.findViewById(R.id.frienditem_phone);
                textView_comment = (TextView) view.findViewById(R.id.frienditem_textview_comment);
            }
        }
        private class CustomHeaderViewHolder extends RecyclerView.ViewHolder {
            public TextView title;
            public TextView titleCount;

            public CustomHeaderViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.frienditem_header_id);
                titleCount = (TextView) view.findViewById(R.id.frienditem_header_count);
            }
        }
    }
}
