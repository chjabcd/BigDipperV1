package com.bigdipper.chj.bigdipperv1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class SplashActivity extends AppCompatActivity {

    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        linearLayout = (LinearLayout)findViewById(R.id.splashactivity_linearlayout);
    }
}
