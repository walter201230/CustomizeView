package com.twowater.customizeview.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.twowater.beziercurve.BezierCurve;
import com.twowater.customizeview.R;

public class BezierCurveActivity extends AppCompatActivity implements
        CompoundButton.OnCheckedChangeListener {

    private BezierCurve mBezierCurve;
    private TextView mTextView;
    private Switch mLoop, mTangent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier_curve);
        initView();
    }

    private void initView() {
        mBezierCurve = (BezierCurve) findViewById(R.id.bezier);
        mTextView = (TextView) findViewById(R.id.textview);
        mLoop = (Switch) findViewById(R.id.loop);
        mTangent = (Switch) findViewById(R.id.tangent);
        mTextView.setText(mBezierCurve.getOrderStr() + "阶贝塞尔曲线");
        mLoop.setChecked(false);
        mTangent.setChecked(true);
    }

    public void start(View view) {
        mBezierCurve.start();
    }

    public void stop(View view) {
        mBezierCurve.stop();
    }

    public void add(View view) {
        if (mBezierCurve.addPoint()) {
            mTextView.setText(mBezierCurve.getOrderStr() + "阶贝塞尔曲线");
        } else {
            Toast.makeText(this, "添加失败", Toast.LENGTH_SHORT).show();
        }
    }

    public void del(View view) {
        if (mBezierCurve.delPoint()) {
            mTextView.setText(mBezierCurve.getOrderStr() + "阶贝塞尔曲线");
        } else {
            Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()) {
            case R.id.loop:
                mBezierCurve.setLoop(isChecked);
                break;
            case R.id.tangent:
                mBezierCurve.setTangent(isChecked);
                break;
        }
    }
}
