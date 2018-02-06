package com.bigdipper.chj.bigdipperv1.interest;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bigdipper.chj.bigdipperv1.MainActivity;
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
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by chj on 2018-01-26.
 */

public class InterestDetailRegistFragment extends Fragment{
    private static final int PICK_FROM_ALBUM = 10;
    private View view;
    private ImageView photoRegist;
    private TextView startDate, endDate;
    private TextView selectFriend;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
    ChatModel chatModel = new ChatModel();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_interest_detail_regist,container,false);


        String interestName = getArguments().getString("interestName");

        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar_interest_detail_regist);
        MainActivity activity = (MainActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        activity.setTitle(interestName+" 등록하기");
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RadioButton button1 = (RadioButton)view.findViewById(R.id.fragment_interest_detail_regist_radiobutton1);
        RadioButton button2 = (RadioButton)view.findViewById(R.id.fragment_interest_detail_regist_radiobutton2);
        RadioButton button3 = (RadioButton)view.findViewById(R.id.fragment_interest_detail_regist_radiobutton3);
        RadioButton button4 = (RadioButton)view.findViewById(R.id.fragment_interest_detail_regist_radiobutton4);
        RadioButton button5 = (RadioButton)view.findViewById(R.id.fragment_interest_detail_regist_radiobutton5);
        RadioButton button6 = (RadioButton)view.findViewById(R.id.fragment_interest_detail_regist_radiobutton6);

        button3.setOnClickListener(button3OnClickListener);
        button4.setOnClickListener(button4OnClickListener);
        button5.setOnClickListener(button5OnClickListener);

        photoRegist = (ImageView) view.findViewById(R.id.fragment_interest_detail_regist_photoregistbutton);
        photoRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent,PICK_FROM_ALBUM);
            }
        });
        startDate = (TextView)view.findViewById(R.id.fragment_interest_detail_regist_startdate);
        endDate = (TextView)view.findViewById(R.id.fragment_interest_detail_regist_enddate);

        String timeNow = simpleDateFormat.format(new Date(System.currentTimeMillis()));

        startDate.setText(timeNow);
        endDate.setText(timeNow);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(view.getContext(),"시작날짜");
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(view.getContext(),"종료날짜");
            }
        });
        Button addFriendButton = (Button)view.findViewById(R.id.fragment_interest_detail_regist_addfriendbutton);
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(view.getContext(),RegistSelectPeopleActivity.class));
                showSelectFriendDialog(view.getContext());
            }
        });
        selectFriend = (TextView)view.findViewById(R.id.fragment_interest_detail_regist_addfriendtext);


        return view;
    }

    int sDay,sMonth,sYear,sHour,sMin,eDay,eMonth,eYear,eHour,eMin;
    //상태메세지
    void showDialog(final Context context, String title){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_datepicker,null);
        TextView titleTextview = (TextView)view.findViewById(R.id.dialog_datepicker_title);
        final DatePicker datePicker = (DatePicker)view.findViewById(R.id.dialog_datepicker_date);
        final TimePicker timePicker = (TimePicker)view.findViewById(R.id.dialog_datepicker_time);
        titleTextview.setText(title);
        if(title.equals("시작날짜")){
            builder.setView(view)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            sDay = datePicker.getDayOfMonth();
                            sMonth = datePicker.getMonth()+1;
                            sYear = datePicker.getYear();
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                sHour = timePicker.getHour();
                                sMin = timePicker.getMinute();
                            }else{
                                sHour = timePicker.getCurrentHour();
                                sMin = timePicker.getCurrentMinute();
                            }
                            startDate.setText(sYear+"."+sMonth+"."+sDay+" "+sHour+":"+sMin);
                        }
                    }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
        }else{
            builder.setView(view)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            eDay = datePicker.getDayOfMonth();
                            eMonth = datePicker.getMonth()+1;
                            eYear = datePicker.getYear();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                eHour = timePicker.getHour();
                                eMin = timePicker.getMinute();
                            }else{
                                eHour = timePicker.getCurrentHour();
                                eMin = timePicker.getCurrentMinute();
                            }
                            if(sYear<=eYear && sMonth<=eMonth && sDay<=eDay && sHour<=eHour && sMin<=eMin){
                                endDate.setText(eYear+"."+eMonth+"."+eDay+" "+eHour+":"+eMin);
                            }else{
                                Toast.makeText(context, "시작시간보다 나중 시간을 입력하세요.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
        }

        //AlertDialog dialog = builder.create();
        //dialog.getWindow().getAttributes().windowAnimations = R.style.CustomAnim;

        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK){
            photoRegist.setImageURI(data.getData()); //가운데 뷰를 바꿈
            //imageUri = data.getData(); //이미지 경로 원본
        }
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

    void showSelectFriendDialog(final Context context){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_interest_regist_addfriend,null);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.activity_interest_regist_recyclerview);
        recyclerView.setAdapter(new InterestRegistSelectPeopleRecyclerViewAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        builder.setView(view)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //selectFriend.setText((String)chatModel.users.get(0).toString());
                        Toast.makeText(context, chatModel.user.get(0).toString(), Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });


        builder.show();
    }

    class InterestRegistSelectPeopleRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        List<UserModel> userModels;

        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;

        List<ListModel> list;

        public InterestRegistSelectPeopleRecyclerViewAdapter(final String s) {
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

        public InterestRegistSelectPeopleRecyclerViewAdapter() {
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
                            chatModel.user.add(item.uid);
                        }//체크 취소 상태
                        else{
                            chatModel.users.remove(item);
                            chatModel.user.remove(item);
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
