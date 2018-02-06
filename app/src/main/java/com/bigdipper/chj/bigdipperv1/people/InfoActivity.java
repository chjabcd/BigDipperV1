package com.bigdipper.chj.bigdipperv1.people;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigdipper.chj.bigdipperv1.R;
import com.bigdipper.chj.bigdipperv1.chat.activity.MessageActivity;
import com.bigdipper.chj.bigdipperv1.model.peopleModel.PhoneBookModel;
import com.bigdipper.chj.bigdipperv1.model.peopleModel.UserModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InfoActivity extends AppCompatActivity {

    private String destinationUid;
    private String friendUid;
    private UserModel userModel;
    private PhoneBookModel.Id phoneBook;
    private FirebaseDatabase firebaseDatabase;
    private TextView name;
    private TextView phone;
    private TextView address;
    private ImageView profileImage;
    private String myUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_info);

        firebaseDatabase = FirebaseDatabase.getInstance();
        destinationUid = getIntent().getStringExtra("destinationUid");
        friendUid = getIntent().getStringExtra("friendUid");
        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        name = (TextView) findViewById(R.id.activity_people_info_edittext_name);
        phone = (TextView)findViewById(R.id.activity_people_info_edittext_phone);
        address = (TextView)findViewById(R.id.activity_people_info_edittext_address);
        profileImage = (ImageView)findViewById(R.id.activity_people_info_profile);

        userModel = new UserModel();

        if(destinationUid == null){
            getFriendUid(friendUid);
        }else{
            getDestinationUid(destinationUid);
        }



        Button button = (Button)findViewById(R.id.infoActivity_button_message);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MessageActivity.class);
                intent.putExtra("destinationUid",userModel.uid);

                ActivityOptions activityOptions = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    activityOptions = ActivityOptions.makeCustomAnimation(view.getContext(), R.anim.fromright, R.anim.toleft);
                    startActivity(intent,activityOptions.toBundle());
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.fromstop,R.anim.totop);
    }

    public void getDestinationUid(String uid){
        firebaseDatabase.getReference().child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userModel = dataSnapshot.getValue(UserModel.class);
                Glide.with(profileImage.getContext())
                        .load(userModel.profileImageUrl)
                        .apply(new RequestOptions().circleCrop())
                        .into(profileImage);
                name.setText(userModel.userName.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void getFriendUid(String uid){
        firebaseDatabase.getReference().child("users").child(myUid).child("phonebook").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                phoneBook = dataSnapshot.getValue(PhoneBookModel.Id.class);
                Glide.with(profileImage.getContext())
                        .load(R.drawable.profile)
                        .apply(new RequestOptions().circleCrop())
                        .into(profileImage);
                name.setText(phoneBook.name.toString());
                phone.setText(phoneBook.phoneNumber.toString());
                if(phoneBook.adrCountry == null){
                    phoneBook.adrCountry = "";
                }
                if(phoneBook.adrRegion == null){
                    phoneBook.adrRegion = "";
                }
                if(phoneBook.adrStreet == null){
                    phoneBook.adrStreet = "";
                }
                if(phoneBook.adrCity == null){
                    phoneBook.adrCity = "";
                }
                if(phoneBook.adrPoBox == null){
                    phoneBook.adrPoBox = "";
                }
                address.setText(phoneBook.adrCountry.toString()+" "+phoneBook.adrRegion.toString()+" "+phoneBook.adrCity.toString()+" "+phoneBook.adrStreet.toString()+" "
                        +phoneBook.adrPoBox.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
