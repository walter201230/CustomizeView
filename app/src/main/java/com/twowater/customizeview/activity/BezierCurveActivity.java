package com.twowater.customizeview.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.twowater.beziercurve.BezierCurve;
import com.twowater.customizeview.R;

public class BezierCurveActivity extends AppCompatActivity {

    private BezierCurve mBezierCurve;

    private SeekBar mSeekBar;

    private TextView mTextView;

    private Switch mLoop, mTangent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier_curve);
        mBezierCurve = (BezierCurve) findViewById(R.id.bezier);
        mTextView = (TextView) findViewById(R.id.textview);
        mSeekBar = (SeekBar) findViewById(R.id.seekbar);
        mLoop = (Switch) findViewById(R.id.loop);
        mTangent = (Switch) findViewById(R.id.tangent);

        mTextView.setText(mBezierCurve.getOrderStr() + "阶贝塞尔曲线");

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) {
                    progress = 1;
                }
                mBezierCurve.setRate(progress * 2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mLoop.setChecked(false);
        mTangent.setChecked(true);
        mLoop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mBezierCurve.setLoop(isChecked);
            }
        });
        mTangent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mBezierCurve.setTangent(isChecked);
            }
        });
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
}
