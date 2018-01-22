package com.bigdipper.chj.bigdipperv1.fragment;

import android.app.ActivityOptions;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigdipper.chj.bigdipperv1.MainActivity;
import com.bigdipper.chj.bigdipperv1.R;
import com.bigdipper.chj.bigdipperv1.model.PhoneBookModel;
import com.bigdipper.chj.bigdipperv1.model.UserModel;
import com.bigdipper.chj.bigdipperv1.people.InfoActivity;
import com.bigdipper.chj.bigdipperv1.service.SoundSearcher;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.daimajia.swipe.SwipeLayout;
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

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_people, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
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
        List<PhoneBookModel.Id> phoneBooks;
        List<PhoneBookModel.Id> phoneBooksFilter;

        public PeopleFragmentRecyclerViewAdapter(final String s) {
            userModels = new ArrayList<>();
            final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userModels.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        UserModel userModel = snapshot.getValue(UserModel.class);
                        if (SoundSearcher.matchStringAll(userModel.userName, s)) {
                            userModels.add(userModel);
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
            phoneBooks = new ArrayList<>();
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
                        if (userModel.phonebook != null) {

                        }
                        userModels.add(userModel);
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);

            SwipeLayout item = (SwipeLayout) view.findViewById(R.id.frienditem_swipe);
            item.setShowMode(SwipeLayout.ShowMode.PullOut);
            item.addDrag(SwipeLayout.DragEdge.Left, item.findViewById(R.id.bottom_wrapper));
            item.addDrag(SwipeLayout.DragEdge.Right, item.findViewById(R.id.bottom_wrapper_2));

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            Glide.with
                    (holder.itemView.getContext())
                    .load(userModels.get(position).profileImageUrl)
                    .apply(new RequestOptions().circleCrop())
                    .into(((CustomViewHolder) holder).imageView);
            ((CustomViewHolder) holder).textViewId.setText(userModels.get(position).userName);

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
            if (userModels.get(position).comment != null) {
                ((CustomViewHolder) holder).textView_comment.setText(userModels.get(position).comment);
            }
            ((CustomViewHolder) holder).imageView_hello.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "hello", Toast.LENGTH_SHORT).show();
                }
            });
            ((CustomViewHolder) holder).imageView_chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "chat", Toast.LENGTH_SHORT).show();
                }
            });
            ((CustomViewHolder) holder).imageView_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "call", Toast.LENGTH_SHORT).show();
                }
            });
            ((CustomViewHolder) holder).imageView_sns.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "sns", Toast.LENGTH_SHORT).show();
                }
            });
            ((CustomViewHolder) holder).imageView_chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "chat", Toast.LENGTH_SHORT).show();
                }
            });
            ((CustomViewHolder) holder).imageView_hide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "hide", Toast.LENGTH_SHORT).show();
                }
            });
            ((CustomViewHolder) holder).imageView_info.setOnClickListener(new View.OnClickListener() {
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


        }

        @Override
        public int getItemCount() {
            return userModels.size();
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
                imageView_hello = (ImageView) view.findViewById(R.id.frienditem_hello);
                imageView_chat = (ImageView) view.findViewById(R.id.frienditem_chat);
                imageView_call = (ImageView) view.findViewById(R.id.frienditem_call);
                imageView_sns = (ImageView) view.findViewById(R.id.frienditem_sns);
                imageView_hide = (ImageView) view.findViewById(R.id.frienditem_hide);
                imageView_info = (ImageView) view.findViewById(R.id.frienditem_infomation);
            }
        }

    }
}
