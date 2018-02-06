package com.bigdipper.chj.bigdipperv1.chat.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigdipper.chj.bigdipperv1.R;
import com.bigdipper.chj.bigdipperv1.model.chatModel.ChatModel;
import com.bigdipper.chj.bigdipperv1.model.peopleModel.HeaderModel;
import com.bigdipper.chj.bigdipperv1.model.peopleModel.ListModel;
import com.bigdipper.chj.bigdipperv1.model.peopleModel.UserModel;
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

public class SelectPeopleActivity extends AppCompatActivity {
    ChatModel chatModel = new ChatModel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_people);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.select_people_activity_recyclerview);
        recyclerView.setAdapter(new SelectPeopleRecyclerViewAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button button = (Button)findViewById(R.id.selectFriendActivity_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                chatModel.users.put(myUid,true);

                FirebaseDatabase.getInstance().getReference().child("chatrooms").push().setValue(chatModel);
            }
        });

    }
    class SelectPeopleRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        List<UserModel> userModels;

        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;

        List<ListModel> list;

        public SelectPeopleRecyclerViewAdapter(final String s) {
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

        public SelectPeopleRecyclerViewAdapter() {
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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_select, parent, false);
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
                final UserModel item = (UserModel)list.get(position);
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
                ((CustomViewHolder)holder).checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        //체크 된 상태
                        if(b){
                            chatModel.users.put(item.uid,true);
                        }//체크 취소 상태
                        else{
                            chatModel.users.remove(item);
                        }
                    }
                });
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
            public CheckBox checkBox;

            public CustomViewHolder(View view) {
                super(view);
                imageView = (ImageView) view.findViewById(R.id.frienditem_imageview);
                textViewId = (TextView) view.findViewById(R.id.frienditem_id);
                textViewPhone = (TextView) view.findViewById(R.id.frienditem_phone);
                textView_comment = (TextView) view.findViewById(R.id.frienditem_textview_comment);
                checkBox = (CheckBox)view.findViewById(R.id.frienditem_checkbox);
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
