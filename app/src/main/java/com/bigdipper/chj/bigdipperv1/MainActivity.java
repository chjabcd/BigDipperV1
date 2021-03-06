package com.bigdipper.chj.bigdipperv1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.bigdipper.chj.bigdipperv1.chat.activity.MessageActivity;
import com.bigdipper.chj.bigdipperv1.infomation.InfomationFragment;
import com.bigdipper.chj.bigdipperv1.chat.fragment.ChatFragment;
import com.bigdipper.chj.bigdipperv1.interest.InterestFragment;
import com.bigdipper.chj.bigdipperv1.people.PeopleFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private NavigationView navigationView;
    private BottomNavigationViewEx bottomNavigationViewEx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout,new PeopleFragment()).commit();

        Window window = getWindow();
        View view = getWindow().getDecorView();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.parseColor("#000000"));//F9FBFA

        }
        //view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        ActionBar actionBar = getSupportActionBar();

//        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//
//        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawerlayout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,0,0);
//        drawerLayout.setDrawerListener(toggle);
//        toggle.syncState();

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(android.Manifest.permission.READ_CONTACTS, android.Manifest.permission.ACCESS_FINE_LOCATION)
                .check();



        //bottomNavigationView = (BottomNavigationView) findViewById (R.id.mainactivity_bottomnavigationview);
        //BottomNavigationViewHelper.disableShiftMode (bottomNavigationView);
        bottomNavigationViewEx = (BottomNavigationViewEx)findViewById(R.id.mainactivity_bottomnavigationview);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_people:
                        getFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout,new PeopleFragment()).commit();


                        return true;
                    case R.id.action_chat:
                        getFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout,new ChatFragment()).commit();

                        return true;
                    case R.id.action_info:
                        getFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout,new InfomationFragment()).commit();

                        return true;
                    case R.id.action_interest:
                        getFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout,new InterestFragment()).commit();

                        return true;
                    case R.id.action_bigdipper:
                        Intent intent = new Intent(MainActivity.this, MessageActivity.class);
                        startActivity(intent);
                        //getFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout,new InfomationFragment()).commit();

                        return true;
                }
                return false;
            }
        });
        passPushTokenToServer();
    }

    void passPushTokenToServer(){

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String token = FirebaseInstanceId.getInstance().getToken();
        Map<String,Object> map = new HashMap<>();
        map.put("pushToken",token);

        FirebaseDatabase.getInstance().getReference().child("users").child(uid).updateChildren(map);
    }

    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        if(count == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("정말 종료하시겠습니까?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.show();
        }else{
            super.onBackPressed();
        }
    }
}
