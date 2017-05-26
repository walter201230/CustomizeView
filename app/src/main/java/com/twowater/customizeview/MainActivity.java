package com.twowater.customizeview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.twowater.myview.MyView.MyView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyView mView= new MyView(this);
    }
}
