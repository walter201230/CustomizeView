package com.twowater.customizeview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.twowater.customizeview.activity.BezierCurveActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnShowBezier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mBtnShowBezier = (Button) findViewById(R.id.show_beziercurve);
        mBtnShowBezier.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.show_beziercurve:
                intent = new Intent(this, BezierCurveActivity.class);
                startActivity(intent);
                break;
        }
    }
}
